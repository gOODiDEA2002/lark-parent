import lark.util.oss.OssService;
import lark.util.oss.minio.MinioConfig;
import lark.util.oss.minio.MinioOssService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OssTest {
    MinioConfig config;
    MinioOssService minioOssService;
    OssService ossService;
    private final String BUCKET_NAME = "techwis";
    private String fileRoot = "/Users/andy/Downloads/";
    @Before
    public void before() {
        config = new MinioConfig( "minio-qa.sanqlt.com", "minio", "1O4iE2BGre7R" );
        minioOssService = new MinioOssService( config.minioClient() );
        ossService = new OssService( minioOssService );
    }

    @Test
    public void test() throws IOException {
        File file = new File( fileRoot + "1.jpg");
        byte[] fileData = Files.readAllBytes(file.toPath());
        String objectName = ossService.upload( BUCKET_NAME, file.getName(), fileData );
        System.out.println( objectName );
        String objectUrl = ossService.getObjectUrl( BUCKET_NAME, objectName );
        System.out.println( objectUrl );
        fileData = ossService.download( BUCKET_NAME, objectName );
        File newfile = new File( fileRoot + "2.jpg");
        Files.write( newfile.toPath(), fileData );
    }
}
