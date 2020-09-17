import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * pdf转换html
 *
 * @author yuanmomo
 * @create 2020-09-17 8:52
 */
public class PdfToHtmlUtil {

  public final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * pdf转换html
   *
   * @param outputPath html文件的输出路径
   * @param inputfilePath 要读取的pdf文件路径
   * @return void
   * @author yuanmomo
   * @date 2020/9/17 8:55
   */
  public void pdfToHtml(String outputPath, String inputfilePath) {
    byte[] bytes = getBytes(inputfilePath);
    // try() 写在()里面会自动关闭流
    try (
        BufferedWriter out = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(new File(outputPath)), "UTF-8"));) {
      //加载PDF文档
      PDDocument document = PDDocument.load(bytes);
      PDFDomTree pdfDomTree = new PDFDomTree();
      pdfDomTree.writeText(document, out);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 将文件转换为byte数组
   *
   * @param filePath 文件路径
   * @return byte[]
   * @author yuanmomo
   * @date 2020/9/17 8:54
   */
  private byte[] getBytes(String filePath) {
    byte[] buffer = null;
    try {
      File file = new File(filePath);
      FileInputStream fis = new FileInputStream(file);
      ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
      byte[] b = new byte[1000];
      int n;
      while ((n = fis.read(b)) != -1) {
        bos.write(b, 0, n);
      }
      fis.close();
      bos.close();
      buffer = bos.toByteArray();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return buffer;
  }

  /**
   * 将文本转为String返回
   *
   * @param inputFilePath 要读取的文件的路径
   * @return java.lang.String
   * @author yuanmomo
   * @date 2020/9/17 9:35
   */
  public String textString(String inputFilePath){
    FileInputStream file = null;
    BufferedReader reader = null;
    InputStreamReader inputFileReader = null;
    String content = "";
    String tempString = null;
    try {
      file = new FileInputStream(inputFilePath);
      inputFileReader = new InputStreamReader(file, "utf-8");
      reader = new BufferedReader(inputFileReader);
      // 一次读入一行，直到读入null为文件结束
      while ((tempString = reader.readLine()) != null) {
        content += tempString;
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    return content;
  }
}
