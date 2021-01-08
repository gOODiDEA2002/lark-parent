package lark.util.oss.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lark.core.lang.FatalException;
import lark.util.oss.ObjectStorageService;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 *
 * @author Andy Yuan
 * @date 2020/9/29
 */
public class MinioOssService implements ObjectStorageService {

  private static final int BUFFER_SIZE = 256;
  private final MinioClient client;

  public MinioOssService(MinioClient client ) {
    this.client = client;
  }

  /** 保存文件到OSS
   * @param objectName 上传对象名，全路径，如：x/y/z.jpg
   * @param objectStream 对象内容流，全路径，如：x/y/z.jpg
   * @param objectSize 对象内容大小
   * @param contentType 对象内容类型
   * @return
   */
  @Override
  public String putObject( String bucketName, String objectName, InputStream objectStream, long objectSize, String contentType ) {
    PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
        objectStream, objectSize, -1)
        .contentType(contentType)
        .build();
    try {
      client.putObject( objectArgs );
      return client.getObjectUrl( bucketName, objectName );
    } catch ( Exception e ) {
      throw new FatalException( e );
    }
  }

  @Override
  public String getObjectUrl(String bucketName, String objectName) {
    try {
      return client.getObjectUrl( bucketName, objectName );
    } catch ( Exception e ) {
      throw new FatalException( e );
    }
  }

  @Override
  public byte[] getObject(String bucketName, String objectName)  {
    GetObjectArgs objectArgs = GetObjectArgs.builder().bucket( bucketName ).object( objectName ).build();
    try ( InputStream in = client.getObject(objectArgs ) ) {
      try (ByteArrayOutputStream out = new ByteArrayOutputStream( in.available() ) ) {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while((bytesRead = in.read(buffer, 0, buffer.length)) > 0) {
          out.write( buffer, 0, bytesRead);
        }
        return out.toByteArray();
      }
    } catch ( Exception e ) {
      throw new FatalException( e );
    }
  }
}
