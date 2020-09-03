import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

/**
 * 测试生成一个pdf文件
 *
 * @author yuanmomo
 * @create 2020-07-09 21:47
 */
public class TestPdfGen {

  public static void main(String[] args) throws Exception {
    // 1.新建document对象
    Document document = new Document();
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("d:\\test1.pdf"));
    //中文字体,解决中文不能显示问题
    BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    //字体
    Font blackFont = new Font(bfChinese);
    blackFont.setColor(BaseColor.BLACK);
    // 3.打开文档
    document.open();
    // 4.添加一个内容段落
    String str = "sd添加一个内容段落添加一个内容"
        + "添加一个内容段落添加一个内容段落"
        + "添加一个内容段落"
        + "添加一个内容段落"
        + "添加一个内容段落"
        + "添加一个内容段落"
        + "添加一个内容段落添加一个内容段落"
        + "添加一个内容段落"
        + "添加一个内容段落添加一个内容段落"
        + "添加一个内容段落"
        + "添加一个内容段落"
        + ""
        + ""
        + ""
        + ""
        + ""
        + "段落添加一个内容段落";
    document.add(new Paragraph(str,blackFont));
    // 5.关闭文档
    document.close();
    writer.close();
  }
}
