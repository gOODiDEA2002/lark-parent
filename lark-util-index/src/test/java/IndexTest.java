
import lark.util.index.config.ElasticsearchConfig;
import lark.util.index.IndexService;
import lark.util.index.user.UserEntity;
import lark.util.index.user.UserIndexService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class IndexTest {

    ElasticsearchConfig config;
    IndexService indexService;
    UserIndexService userIndexService;
    String indexName = "lark-util-index.test1";
    String docId = "123456";

    @Before
    public void before() {
        config = new ElasticsearchConfig( "es-00-qa.sanqlt.com", 9200,3000 );
        indexService = new IndexService( config.elasticsearchClient() );
        userIndexService = new UserIndexService( indexService );
    }

    private static final int ID = 1;

    private static final String USER_NAME = "star";

    private UserEntity mockUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(ID);
        userEntity.setName(USER_NAME);
        userEntity.setEmail("test@new.com");
        userEntity.setAge(24);

        return userEntity;
    }

    @Test
    public void test1SaveUser() {
        UserEntity userEntity = this.mockUser();
        // 保存数据
        userIndexService.saveUser(userEntity);
        UserEntity newUserEntity = userIndexService.getUser(userEntity.getId().toString());
        // 验证结果
        org.junit.Assert.assertEquals(userEntity.getId(), newUserEntity.getId());
        org.junit.Assert.assertEquals(userEntity.getName(), newUserEntity.getName());
        org.junit.Assert.assertEquals(userEntity.getEmail(), newUserEntity.getEmail());
        org.junit.Assert.assertEquals(userEntity.getAge(), newUserEntity.getAge());
    }

    @Test
    public void test2SearchUserByName() {
        List<UserEntity> userEntities = userIndexService.searchUserByName(USER_NAME);
        // 验证结果
        org.junit.Assert.assertNotNull(userEntities);
        org.junit.Assert.assertTrue(userEntities.size() > 0);
    }

    @Test
    public void test3DeleteUser() {
        String id = String.valueOf(ID);

        // 删除数据
        userIndexService.deleteUser(id);

        UserEntity newUserEntity = userIndexService.getUser(id);
        // 验证结果
        Assert.assertNull(newUserEntity);
    }

}
