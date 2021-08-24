package lark.db.jsd;

import com.esotericsoftware.reflectasm.MethodAccess;
import lark.core.util.Beans;
import lark.core.util.Strings;
import lark.db.jsd.annotation.JsdConverter;
import lark.db.jsd.annotation.JsdTable;
import lark.db.jsd.converter.Converter;
import lark.db.jsd.converter.ConverterManager;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象映射辅助类
 * Created by guohua.cui on 15/5/22.
 */
public final class Mapper {
    private static Map<String, EntityInfo> infos = new ConcurrentHashMap<>();

    public static EntityInfo getEntityInfo(Class<?> clazz) {
        String key = clazz.getTypeName();
        EntityInfo info = infos.get(key);
        if (info != null) return info;

        info = new EntityInfo(clazz);
        if (info.fields.size() == 0) throw new JsdException("clazz is not a valid entity");

        infos.put(key, info);
        return info;
    }

    public static class EntityInfo {
        private MethodAccess method;
        // 实体对应的表名
        String table;
        // 有效字段(有对应的 get/set 方法)
        Map<String, FieldInfo> fields = new HashMap<>();
        // 删除,更新条件字段(带 Id 注解的)
        String[] idColumns;
        // 插入字段(不带 GeneratedValue 注解的)
        String[] insertColumns;
        // 更新字段(不带 Id 注解的)
        String[] updateColumns;
        // 表分片字段
        String[] shardKeys;

        Map<String, String> fieldToColumnMap = new HashMap<>();

        EntityInfo(Class<?> clazz) {
//        Entity anno = clazz.getAnnotation(Entity.class);
//        if (anno == null) throw new JsdException("clazz doesn't has Entity annotation");
            method = MethodAccess.get(clazz);

            JsdTable jsdTable = clazz.getAnnotation(JsdTable.class);
            NameStyle nameStyle;
            if (jsdTable == null) {
                nameStyle = NameStyle.LOWER;
            } else {
                nameStyle = jsdTable.nameStyle();
                this.shardKeys = jsdTable.shardKeys();
            }

            javax.persistence.Table tableAnno = clazz.getAnnotation(Table.class);
            if (tableAnno == null || Strings.isEmpty(tableAnno.name()))
                this.table = NameStyle.transform(clazz.getSimpleName(), nameStyle);
            else this.table = tableAnno.name();

            initField(clazz, nameStyle);
        }

        private void initField(Class<?> clazz, NameStyle nameStyle) {
            Field[] declaredFields = clazz.getDeclaredFields();
            List<String> tempIdColumns = new ArrayList<>();
            for (Field f : declaredFields) {
                FieldInfo fi = getFieldInfo(f);
                if (fi == null) {
                    continue;
                }

                String column = resolveColumn(f, f.getName(), nameStyle);
                Id idAnno = f.getAnnotation(Id.class);
                if (idAnno != null) {
                    fi.key = true;
                    tempIdColumns.add(column);
                }

                GeneratedValue gvAnno = f.getAnnotation(GeneratedValue.class);
                if (gvAnno != null) {
                    fi.auto = true;
                }

                this.fields.put(column, fi);
                this.fieldToColumnMap.put(f.getName(), column);
            }

            if (!tempIdColumns.isEmpty()) {
                this.idColumns = tempIdColumns.toArray(new String[0]);
            }

            /**
             * 如果有继承关系，需要增加对父类的处理，这里是属于嵌套
             */
            if (clazz.getSuperclass() != Object.class) {
                initField(clazz.getSuperclass(), nameStyle);
            }
        }

        private FieldInfo getFieldInfo(Field f) {
            Transient transientAnno = f.getAnnotation(Transient.class);
            if (transientAnno != null) {
                return null;
            }

            int getIndex = this.getMethodIndex(Beans.getGetterName(f));
            if (getIndex == -1) {
                return null;
            }

            int setIndex = this.getMethodIndex(Beans.getSetterName(f));
            if (setIndex == -1) {
                return null;
            }

            Converter converter;
            JsdConverter jsdConverter = f.getAnnotation(JsdConverter.class);
            if (jsdConverter != null) {
                try {
                    converter = jsdConverter.value().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                converter = ConverterManager.get(f.getType());
            }

            return new FieldInfo(getIndex, setIndex, f.getType(), converter);
        }

        private static String resolveColumn(Field f, String pascalName, NameStyle nameStyle) {
            String name;

            javax.persistence.Column columnAnnotation = f.getAnnotation(javax.persistence.Column.class);
            if (columnAnnotation == null || Strings.isEmpty(columnAnnotation.name())) {
                name = NameStyle.transform(pascalName, nameStyle);
            } else {
                name = columnAnnotation.name();
            }

            return name;
        }

        public String[] getShardKeys() {
            return this.shardKeys;
        }

        public Object[] getShardValues(Object obj) {
            if (this.shardKeys.length == 0) {
                throw new JsdException("shard keys aren't specified for class: " + obj.getClass().getName());
            }

            Object[] values = new Object[shardKeys.length];
            for (int i = 0; i < shardKeys.length; i++) {
                values[i] = getValue(obj, shardKeys[i]);
            }
            return values;
        }

        public String getColumn(String fieldName) {
            return this.fieldToColumnMap.get(fieldName);
        }

        public String[] getInsertColumns() {
            if (this.insertColumns == null) {
                List<String> columns = new ArrayList<>(fields.size());
                Iterator<Map.Entry<String, FieldInfo>> iter = fields.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Mapper.EntityInfo.FieldInfo> fi = iter.next();
                    if (!fi.getValue().auto) {
                        columns.add(fi.getKey());
                    }
                }
                this.insertColumns = columns.toArray(new String[0]);
            }
            return this.insertColumns;
        }

        public Object[] getInsertValues(Object obj) {
            String[] columns = this.getInsertColumns();
            Object[] values = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                values[i] = getValue(obj, columns[i]);
            }
            return values;
        }

        public String[] getUpdateColumns() {
            if (this.updateColumns == null) {
                List<String> columns = new ArrayList<>(fields.size());
                Iterator<Map.Entry<String, FieldInfo>> iter = fields.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Mapper.EntityInfo.FieldInfo> fi = iter.next();
                    if (!fi.getValue().key) {
                        columns.add(fi.getKey());
                    }
                }
                this.updateColumns = columns.toArray(new String[0]);
            }
            return this.updateColumns;
        }

        public Object[] getUpdateValues(Object obj) {
            String[] columns = this.getUpdateColumns();
            Object[] values = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                values[i] = getValue(obj, columns[i]);
            }
            return values;
        }

        public String[] getIdColumns() {
            return this.idColumns;
        }

        public String getTable() {
            return this.table;
        }

        public Object[] getIdValues(Object obj) {
            String[] columns = this.getIdColumns();
            Object[] values = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                values[i] = getValue(obj, columns[i]);
            }
            return values;
        }

        public Object getValue(Object obj, String field) {
            FieldInfo fi = fields.get(field);
            if (fi == null) return null;

            return fi.getDbValue(method.invoke(obj, fi.getIndex));
        }

        /**
         * 设置对象的值
         *
         * @param obj
         * @param field 字段名
         * @param value 值
         */
        public void setValue(Object obj, String field, Object value) {
            if (value == null) {
                return;
            }

            FieldInfo fi = fields.get(field);
            if (fi == null) return;

            method.invoke(obj, fi.setIndex, fi.getJavaValue(value));
        }

        private int getMethodIndex(String name) {
            String[] names = method.getMethodNames();
            for (int i = 0; i < names.length; ++i) {
                if (name.equals(names[i])) {
                    return i;
                }
            }
            return -1;
        }

        static class FieldInfo {
            Converter converter;
            int getIndex;
            int setIndex;
            boolean key;
            boolean auto;
            Class<?> type;

            FieldInfo(int getIndex, int setIndex, Class<?> type, Converter converter) {
                this.getIndex = getIndex;
                this.setIndex = setIndex;
                this.type = type;
                this.converter = converter;
            }

            @SuppressWarnings("unchecked")
            Object getDbValue(Object javaValue) {
                return converter.toDbValue(javaValue);
            }

            Object getJavaValue(Object dbValue) {
                return converter.toJavaValue(dbValue);
            }
        }
    }

}
