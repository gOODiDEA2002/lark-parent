package lark.util.oss;

import lark.core.lang.FatalException;
import lark.util.oss.util.OssUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
/**
 *
 * @author Andy Yuan
 * @date 2020/9/29
 */
public class OssService {

  ObjectStorageService objectStorageService;

  public OssService( ObjectStorageService objectStorageService ) {
    this.objectStorageService = objectStorageService;
  }

  /**
   * 上传本地文件到对象存储服务
   *
   * @param bucketName  OSS上的桶名称
   * @param filename    文件名，如："xxx.jpg"
   * @return objectName 对象名，如：/2021/01/08/xyz.jpg
   */
  public String upload( String bucketName, String filename, byte[] fileData ) {
    String objectName = OssUtil.getObjectName( filename );
    String contentType = OssUtil.getContentType( filename );
    int fileSize = fileData.length;
    try (InputStream input = new ByteArrayInputStream( fileData )) {
      objectStorageService.putObject( bucketName, objectName, input, fileSize, contentType );
      return objectName;
    } catch ( Exception e ) {
      throw new FatalException( e );
    }
  }

  public byte[] download( String bucketName, String objectName ) {
    return objectStorageService.getObject( bucketName, objectName );
  }

  public String getObjectUrl( String bucketName, String objectName ) {
    return objectStorageService.getObjectUrl( bucketName, objectName );
  }
}
