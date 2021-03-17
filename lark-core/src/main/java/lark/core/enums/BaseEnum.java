package lark.core.enums;

public interface BaseEnum {

    Integer getCode();

    String getMsg();

    public enum DefaultEnum implements BaseEnum {


        SUCCESS(1000, "SUCCESS"),

        ERROR(9999, "服务器开小差了");

        private Integer code;
        private String msg;

        @Override
        public Integer getCode() {
            return code;
        }

        @Override
        public String getMsg() {
            return msg;
        }

        private DefaultEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

}
