package lark.util.oss.util;

import javax.activation.MimetypesFileTypeMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 *
 * @author Andy Yuan
 * @date 2020/9/29
 */
public class OssUtil {

  private static final String EMPTY = "";
  private static final String SLASH = "/";
  private static final String DOT = ".";
  private static final String DASHED = "-";
  private static final MimetypesFileTypeMap MIME_MAP = new MimetypesFileTypeMap();

  /**
   * 获取带路径的对象名
   *
   * @param filename 初始的文件名
   * @return 返回加uuid后的文件名
   */
  public static String getObjectName(String filename ) {
    return getPath() + getUniqueId() + extName( filename );
  }

  /**
   * 获取文件的路径，路径最后带/
   * @return eg: /2021/01/08/
   */
  private static String getPath() {
    LocalDate now = LocalDate.now();
    return now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
  }

  /**
   * 获取文件扩展名，扩展名带.
   * @param filename zzz.jpg
   * @return eg: .jpg
   */
  private static String extName( String filename ) {
    int begin = filename.lastIndexOf(DOT);
    if (begin == -1 ) {
      return EMPTY;
    }
    return filename.substring(begin);
  }

  /**
   * 获取一个随机的唯一ID
   * @return
   */
  private static String getUniqueId() {
    return UUID.randomUUID().toString().replace(DASHED, EMPTY);
  }

  /**
   * 获取文件的contentType
   * @param filename
   * @return eg:image/png
   */
  public static String getContentType( String filename ) {
    return MIME_MAP.getContentType( filename );
  }
}
