import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author yuanmomo
 * @create 2020-07-09 19:53
 */
public class TestHtmlCharToPdf {

  public static void main(String[] args) throws IOException, DocumentException {
    // step 1
    Document document = new Document();
    // step 2
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("d://test4.pdf"));
    // step 3
    document.open();
    // step 4
    //显示中文必须设置font-family。这里为宋体(simsun)。中文可为汉字，也可为汉字的unicode
    String str = "<html><head>\n"
        + "</head>\n"
        + "<body>\n"
        + "<tr bgcolor=\"#ffd700\" align=\"center\" height=\"45\">\n"
        + "  <td>\n"
        + "    <a href=\"\">首页</a>\n"
        + "  </td>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "  <br>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "  <br>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "  <br>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "  <br>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "  <br>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "  <br>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "  <br>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "  <br>\n"
        + "  <td>\n"
        + "    测试html转pdf\n"
        + "  </td>\n"
        + "</tr>\n"
        + "\n"
        + "</body>\n"
        + "</html>\n";

    XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
    ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
    worker.parseXHtml(writer, document, is, Charset.forName("UTF-8"));
    // step 5
    document.close();
  }
}
