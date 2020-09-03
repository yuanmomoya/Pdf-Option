/*
 * $Id:$
 * Copyright 2017 sinounited.com.cn All rights reserved.
 *//*


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

*/
/**
 * ServiceImpl
 *
 * @author yuanmomo
 * @since 2020-07-08 15:14:20
 *//*

@Service
public class TestHtmlCharToPdf1 extends BaseService implements BbRecordFileService {

	@Autowired
	private BbRecordFileManager bbRecordFileManager;

	@Autowired
	private ApCodeGeneratorService apCodeGeneratorServiceImpl;

	*/
/* (non-Javadoc)
	 * @see com.sino.ap.param.api.BbRecordFileService#pageFindAll(com.sino.ap.param.qo.BbRecordFilePageQO)
	 *//*

	@Override
	public ResultDTO<List<BbRecordFileQueryBO>> pageFindAll(BbRecordFilePageQO pageQO) {
		return bbRecordFileManager.pageFindAll(pageQO);
	}

	*/
/* (non-Javadoc)
	 * @see com.sino.ap.param.api.BbRecordFileService#save(com.sino.ap.param.dto.BbRecordFileInsertDTO)
	 *//*

	@Override
	@Transactional(readOnly = false)
	public ResultDTO<Object> save(BbRecordFileInsertDTO insertDTO) {
		bbRecordFileManager.save(insertDTO);
		ResultDTO<Object> rtDTO = new ResultDTO<Object>();
		rtDTO.setSuccess(true);
		return rtDTO;
	}

	*/
/* (non-Javadoc)
	 * @see com.sino.ap.param.api.BbRecordFileService#edit(com.sino.ap.param.dto.BbRecordFileUpdateDTO)
	 *//*

	@Override
	@Transactional(readOnly = false)
	public ResultDTO<Object> edit(BbRecordFileUpdateDTO updateDTO) {
		bbRecordFileManager.edit(updateDTO);
		ResultDTO<Object> rtDTO = new ResultDTO<Object>();
		rtDTO.setSuccess(true);
		return rtDTO;
	}

	*/
/* (non-Javadoc)
	 * @see com.sino.ap.param.api.BbRecordFileService#remove(java.lang.Long)
	 *//*

	@Override
	@Transactional(readOnly = false)
	public ResultDTO<Object> remove(Long anId) {
		bbRecordFileManager.remove(anId);
		ResultDTO<Object> rtDTO = new ResultDTO<Object>();
		rtDTO.setSuccess(true);
		return rtDTO;
	}

	*/
/* (non-Javadoc)
	 * @see com.sino.ap.param.api.BbRecordFileService#getById(java.lang.Long)
	 *//*

	@Override
	public ResultDTO<BbRecordFileGetBO> getById(Long id) {
		BbRecordFileGetBO entity = bbRecordFileManager.getById(id);
		ResultDTO<BbRecordFileGetBO> rtDTO = new ResultDTO<BbRecordFileGetBO>();
		rtDTO.setData(entity);
		rtDTO.setSuccess(true);
		return rtDTO;
	}

	*/
/**
	 * 根据html字符串生成pdf并保存文件信息
	 *
	 * @param bbRecordFilePOJO
	 * @param user
	 * @return com.sino.framework.model.ResultDTO<java.lang.Object>
	 * @author yuanmomo
	 * @date 2020/7/8 15:48
	 *//*

	@Override
	public ResultDTO<Long> genPdfFileAndSaveInfo(BbRecordFilePOJO bbRecordFilePOJO,
			UserContext user) {
		SimpleDateFormat aDate = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String fileName = sdf.format(new Date()) + ".pdf";
		String fileUrl = "upload" + "\\"
				+  aDate.format(new Date()) + "\\" + sdf.format(new Date()) + ".pdf";
		String htmlFileUrl = "upload" + "\\"
				+  aDate.format(new Date()) + "\\" + sdf.format(new Date()) + ".html";
		try {
			htmlToPdf(bbRecordFilePOJO.getHtmlContent(), fileUrl);
		} catch (Exception e) {
			logger.info("---------------{html转pdf失败-尝试使用文字内容生成pdf}----------------");
			try {
				// 有的html字符串生成pdf存在问题(暂时使用html文件)
				//contentToPdf(bbRecordFilePOJO.getHtmlContent(), fileUrl);
				contentToHtml(bbRecordFilePOJO.getHtmlContent(), htmlFileUrl);
			} catch (Exception ioException) {
				//logger.info("---------------{文字内容生成pdf失败}----------------");
				logger.info("---------------{文字内容生成html失败}----------------");
			}
		}
		Long fileInfoId = bbRecordFileManager.saveFileInfo(bbRecordFilePOJO, user, fileUrl, fileName);
		ResultDTO<Long> rtDTO = new ResultDTO<Long>();
		rtDTO.setSuccess(true);
		rtDTO.setData(fileInfoId);
		return rtDTO;
	}

	*/
/**
	 * 根据文字生成html
	 *
	 * @param htmlContent
	 * @param htmlFileUrl
	 * @return void
	 * @author yuanmomo
	 * @date 2020/7/9 23:37
	 *//*

	private void contentToHtml(String htmlContent, String htmlFileUrl) throws FileNotFoundException {
		logger.info("---------------{根据文字生成html}----------------");
		String rootPath = "";
		try {
			Properties property = new Properties();
			InputStream in = getClass().getClassLoader().getResourceAsStream("sinoupload.properties");
			property.load(in);
			rootPath = property.getProperty("sino.file.localpath");
		} catch (Exception e) {
			throw new ServiceException("获取上传根路径异常", e);
		}
		// 生成pdf文件的保存目录
		String pdfGenUrl = rootPath + "\\" + htmlFileUrl;
		//在磁盘下创建多级目录,并且在目录下创建文件
		File fileDemo1=new File(pdfGenUrl);
		//System.out.println(fileDemo1.getParentFile());
		if(!(fileDemo1.getParentFile().exists())){
			fileDemo1.getParentFile().mkdirs();
		}
		//用于存储html字符串
		StringBuilder stringHtml = new StringBuilder();
		PrintStream printStream = new PrintStream(new FileOutputStream(new File(pdfGenUrl)));
		stringHtml.append(htmlContent);
		printStream.println(stringHtml.toString());
		printStream.close();
	}

	*/
/**
	 * 根据文字生成pdf
	 *
	 * @param htmlContent
	 * @param fileUrl
	 * @return void
	 * @author yuanmomo
	 * @date 2020/7/9 21:33
	 *//*

	private void contentToPdf(String htmlContent, String fileUrl) throws IOException, DocumentException {
		logger.info("---------------{开始根据文字生成pdf}----------------");
		String rootPath = "";
		try {
			Properties property = new Properties();
			InputStream in = getClass().getClassLoader().getResourceAsStream("sinoupload.properties");
			property.load(in);
			rootPath = property.getProperty("sino.file.localpath");
		} catch (Exception e) {
			throw new ServiceException("获取上传根路径异常", e);
		}
		// 生成pdf文件的保存目录
		String pdfGenUrl = rootPath + "\\" + fileUrl;
		// 1.新建document对象
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfGenUrl));
		//中文字体,解决中文不能显示问题
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		//字体
		Font blackFont = new Font(bfChinese);
		blackFont.setColor(BaseColor.BLACK);
		// 3.打开文档
		document.open();
		// 4.添加一个内容段落
		document.add(new Paragraph(htmlContent,blackFont));
		// 5.关闭文档
		document.close();
		writer.close();
	}

	*/
/**
	 * 根据html字符串生成pdf
	 *
	 * @param htmlContent
	 *//*

	private void htmlToPdf(String htmlContent, String fileUrl)
			throws IOException, DocumentException {
		logger.info("---------------{开始根据html字符串生成pdf}----------------");
		String rootPath = "";
		try {
			Properties property = new Properties();
			InputStream in = getClass().getClassLoader().getResourceAsStream("sinoupload.properties");
			property.load(in);
			rootPath = property.getProperty("sino.file.localpath");
		} catch (Exception e) {
			throw new ServiceException("获取上传根路径异常", e);
		}
		// 生成pdf文件的保存目录
		String pdfGenUrl = rootPath + "\\" + fileUrl;
		// 默认页面大小是A4
		Document document = new Document(PageSize.A2);
		//建立一个书写器
		PdfWriter writer = PdfWriter
				.getInstance(document, new FileOutputStream(pdfGenUrl));
		//打开文件
		document.open();
		document.add(new Paragraph());
		//创建章节
		XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
		ByteArrayInputStream complainContentHtml = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
		worker.parseXHtml(writer, document, complainContentHtml, Charset.forName("UTF-8"));
		//关闭文档
		document.close();
		//关闭书写器
		writer.close();
	}
}*/
