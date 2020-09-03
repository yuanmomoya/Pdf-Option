package com.testapi.api.controller;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.testapi.api.entity.GpPdfSginBO;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanmomo
 * @create 2019-11-05 15:20
 */

@RestController
public class PdfSignController {

  private static final Logger logger = LoggerFactory.getLogger(PdfSignController.class);

  @RequestMapping("/signPdf")
  public Boolean saveUser(@RequestBody GpPdfSginBO pdfSginBO) {
    logger.info("-----传过来的签名参数-----");
    logger.info(JSON.toJSONString(pdfSginBO));
    logger.info("-----传过来的签名参数-----");
    Boolean sginFlag = true;
    try {
      sign(pdfSginBO);
    } catch (Exception e) {
      sginFlag = false;
      e.printStackTrace();
    }
    logger.info("done!!!-----转换完成！！！");
    return sginFlag;
  }

  public void sign(GpPdfSginBO pdfSginBO) throws Exception {
    BouncyCastleProvider provider = new BouncyCastleProvider();
    Security.addProvider(provider);
    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(new FileInputStream(pdfSginBO.getCertPath()), pdfSginBO.getCertPwd().toCharArray());
    String alias = (String) ks.aliases().nextElement();
    PrivateKey pk = GetPvkformPfx(pdfSginBO.getCertPath(), pdfSginBO.getCertPwd());
    Certificate[] chain = ks.getCertificateChain(alias);
    PdfReader reader = new PdfReader(pdfSginBO.getPdfReaderPath());
    FileOutputStream os = new FileOutputStream(pdfSginBO.getPdfOutputPath());
    PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
    PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
    ExternalSignature pks = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, provider.getName());
    ExternalDigest digest = new BouncyCastleDigest();
    MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0, CryptoStandard.CMS);
  }
  public PrivateKey GetPvkformPfx(String strPfx, String strPassword) {
    try {
      BouncyCastleProvider provider = new BouncyCastleProvider();
      Security.addProvider(provider);
      KeyStore ks = KeyStore.getInstance("PKCS12");
      FileInputStream fis = new FileInputStream(strPfx);
      char[] nPassword = null;
      if ((strPassword == null) || strPassword.trim().equals("")) {
        nPassword = null;
      } else {
        nPassword = strPassword.toCharArray();
      }
      ks.load(fis, nPassword);
      fis.close();
      logger.info("keystore type=" + ks.getType());
      Enumeration enumas = ks.aliases();
      String keyAlias = null;
      if (enumas.hasMoreElements())
      {
        keyAlias = (String) enumas.nextElement();
        System.out.println("alias=[" + keyAlias + "]");
      }
      logger.info("is key entry=" + ks.isKeyEntry(keyAlias));
      PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
      Certificate[] cert = ks.getCertificateChain(keyAlias);
      System.out.println("chain size " + cert.length);
      PublicKey pubkey = cert[0].getPublicKey();
      logger.info("cert class = " + cert.getClass().getName());
      logger.info("cert = " + cert);
      logger.info("public key = " + pubkey);
      logger.info("private key = " + prikey);
      return prikey;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
