package com.testapi.api.entity;

/**
 * 用于pdf签名
 *
 * @author yuanmomo
 * @create 2020-03-30 16:02
 */
public class GpPdfSginBO {

  private static final long serialVersionUID = 8319156432067765415L;

  /**
   * 证书的密码（默认123456）
   */
  private String certPwd = "123456";

  /**
   * 证书的路径
   */
  private String certPath;

  /**
   * 要签名pdf的路径
   */
  private String pdfReaderPath;

  /**
   * 签完名pdf的输出路径
   */
  private String pdfOutputPath;

  public String getCertPwd() {
    return certPwd;
  }

  public void setCertPwd(String certPwd) {
    this.certPwd = certPwd;
  }

  public String getCertPath() {
    return certPath;
  }

  public void setCertPath(String certPath) {
    this.certPath = certPath;
  }

  public String getPdfReaderPath() {
    return pdfReaderPath;
  }

  public void setPdfReaderPath(String pdfReaderPath) {
    this.pdfReaderPath = pdfReaderPath;
  }

  public String getPdfOutputPath() {
    return pdfOutputPath;
  }

  public void setPdfOutputPath(String pdfOutputPath) {
    this.pdfOutputPath = pdfOutputPath;
  }
}
