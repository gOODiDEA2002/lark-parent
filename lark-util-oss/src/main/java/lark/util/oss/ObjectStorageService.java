package lark.util.oss;

import java.io.InputStream;

/**
 *
 * @author Andy Yuan
 * @date 2020/9/29
 */
public interface ObjectStorageService {


  /** 保存文件到OSS
   * @param objectName 对象名，全路径，如：x/y/z.jpg
   * @param objectStream 对象内容流，全路径，如：x/y/z.jpg
   * @param objectSize 对象内容大小
   * @param contentType 对象内容类型
   * @return
   */
  String putObject( String bucketName, String objectName, InputStream objectStream, long objectSize, String contentType );

  String getObjectUrl( String bucketName, String objectName );

  byte[] getObject( String bucketName, String objectName );
}
