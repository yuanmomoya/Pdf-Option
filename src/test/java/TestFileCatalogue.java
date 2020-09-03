import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 测试文件目录，如果目录存在创建文件，
 * 如果目录不存在，创建目录再创建文件
 *
 * @author yuanmomo
 * @create 2020-07-09 23:48
 */
public class TestFileCatalogue {

  public static void main(String[] args) throws FileNotFoundException {
    //在磁盘下创建多级目录,并且在目录下创建文件
    File fileDemo1=new File("d:\\hello\\hello\\demo.txt");
    //System.out.println(fileDemo1.getParentFile());
    if(!(fileDemo1.getParentFile().exists())){
      fileDemo1.getParentFile().mkdirs();
      new FileOutputStream(new File("d:\\hello\\hello\\demo.txt"));
    }
  }
}
