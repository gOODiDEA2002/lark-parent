package lark.util.index.user;

import lark.core.lang.BusinessException;
import lark.util.index.object.IndexDocument;
import lark.util.index.IndexService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * lark.util.index.user.UserService
 *
 * @author star
 */
@Service
public class UserIndexService {

    private static final String INDEX_NAME = "lark.util.index.test";

    private IndexService indexService;

    public UserIndexService( IndexService indexService ) {
        this.indexService = indexService;
    }

    /**
     * 保存用户信息
     *
     * @param userEntity 用户信息
     */
    public void saveUser(UserEntity userEntity) {
        IndexDocument<UserEntity> document = new IndexDocument<>();
        document.setId(userEntity.getId().toString());
        document.setData(userEntity);
        try {
            indexService.save(INDEX_NAME, document);
        } catch (IOException e) {
            throw new BusinessException( 0, "Failed to save user, id: " + userEntity.getId(), e);
        }

    }

    /**
     * 批量保存用户
     *
     * @param userEntityList 用户集合
     */
    public void saveAllUser(List<UserEntity> userEntityList) {
        if (CollectionUtils.isEmpty(userEntityList)) {
            return;
        }
        List<IndexDocument<UserEntity>> documentList = new ArrayList<>(userEntityList.size());
        IndexDocument<UserEntity> document = null;
        for (UserEntity userEntity : userEntityList) {
            document = new IndexDocument<>();
            document.setId(String.valueOf(userEntity.getId()));
            document.setData(userEntity);

            documentList.add(document);
        }

        try {
            indexService.saveAll(INDEX_NAME, documentList);
        } catch (IOException e) {
            throw new BusinessException( 0, "Failed to batch save users", e);
        }
    }

    /**
     * 根据用户 ID 获取用户信息
     *
     * @param id 用户 ID
     * @return 用户对象
     */
    public UserEntity getUser(String id) {
        try {
            UserEntity userEntity = indexService.get(INDEX_NAME, id, UserEntity.class);

            return userEntity;
        } catch (IOException e) {
            throw new BusinessException( 0, "Failed to get user, id: " + id, e);
        }
    }

    /**
     * 根据用户姓名查询
     *
     * @param name 用户姓名
     * @return 用户集合
     */
    public List<UserEntity> searchUserByName(String name) {
        // 构建查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchPhraseQuery("name", name));
        // 构建查询生成器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder);
        try {
            List<UserEntity> userEntityList = indexService.searchByQuery(INDEX_NAME, sourceBuilder, UserEntity.class);

            return userEntityList;
        } catch (IOException e) {
            throw new BusinessException( 0, "Failed to search user by name: " + name, e);
        }
    }

    /**
     * 根据用户 ID 删除用户信息
     *
     * @param id 用户 ID
     */
    public void deleteUser(String id) {
        try {
            indexService.delete(INDEX_NAME, id);
        } catch (IOException e) {
            throw new BusinessException( 0, "Failed to delete user by id: " + id, e);
        }
    }

    /**
     * 根据用户姓名删除
     *
     * @param name 用户姓名
     */
    public void deleteUserByName(String name) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchPhraseQuery("name", name));

        try {
            indexService.deleteByQuery(INDEX_NAME, boolQueryBuilder);
        } catch (IOException e) {
            throw new BusinessException( 0, "Failed to delete user by name: " + name, e);
        }
    }


}
