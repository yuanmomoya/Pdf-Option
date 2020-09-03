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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Enumeration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 * 用于测试pdf签名
 */
public class TestPdfSgin {
  public TestPdfSgin() {
  }

  //转换成十六进制字符串
  public static String Byte2String(byte[] b) {
    String hs = "";
    String stmp = "";

    for (int n = 0; n < b.length; n++) {
      stmp = (Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else
        hs = hs + stmp;
      //if (n<b.length-1)  hs=hs+":";
    }
    return hs.toUpperCase();
  }

  public static byte[] StringToByte(int number) {
    int temp = number;
    byte[] b = new byte[4];
    for (int i = b.length - 1; i > -1; i--) {
      b[i] = new Integer(temp & 0xff).byteValue();//将最高位保存在最低位
      temp = temp >> 8; //向右移8位
    }
    return b;
  }

  public PrivateKey GetPvkformPfx(String strPfx, String strPassword) {
    try {
      BouncyCastleProvider provider = new BouncyCastleProvider();
      Security.addProvider(provider);
      KeyStore ks = KeyStore.getInstance("PKCS12");
      FileInputStream fis = new FileInputStream(strPfx);
      // If the keystore password is empty(""), then we have to set
      // to null, otherwise it won't work!!!
      char[] nPassword = null;
      if ((strPassword == null) || strPassword.trim().equals("")) {
        nPassword = null;
      } else {
        nPassword = strPassword.toCharArray();
      }
      ks.load(fis, nPassword);
      fis.close();
      System.out.println("keystore type=" + ks.getType());
      // Now we loop all the aliases, we need the alias to get keys.
      // It seems that this value is the "Friendly name" field in the
      // detals tab <-- Certificate window <-- view <-- Certificate
      // Button <-- Content tab <-- Internet Options <-- Tools menu
      // In MS IE 6.
      Enumeration enumas = ks.aliases();
      String keyAlias = null;
      if (enumas.hasMoreElements())// we are readin just one certificate.
      {
        keyAlias = (String) enumas.nextElement();
        System.out.println("alias=[" + keyAlias + "]");
      }
      // Now once we know the alias, we could get the keys.
      System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
      PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
      Certificate[] cert = ks.getCertificateChain(keyAlias);
      System.out.println("chain size " + cert.length);
      PublicKey pubkey = cert[0].getPublicKey();
      System.out.println("cert class = " + cert.getClass().getName());
      System.out.println("cert = " + cert);
      System.out.println("public key = " + pubkey);
      System.out.println("private key = " + prikey);

      //			PdfReader reader = new PdfReader("/Users/yeyanxin/downloads/lib/test.pdf");
      //			FileOutputStream os = new FileOutputStream("/Users/yeyanxin/downloads/lib/pkxsign.pdf");
      //			PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
      //			// Creating the appearance
      //			PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
      //			appearance.setReason("digtal signature");
      //			appearance.setLocation("FuZhou");
      //			appearance.setVisibleSignature(new Rectangle(300, 840 - 200, 200, 600), 1, "sig");
      //			// Creating the signature
      //			ExternalSignature pks = new PrivateKeySignature(prikey, DigestAlgorithms.SHA256, provider.getName());
      //			ExternalDigest digest = new BouncyCastleDigest();
      //			MakeSignature.signDetached(appearance, digest, pks, cert, null, null, null, 0, CryptoStandard.CMS);
      return prikey;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // 证书密码
  private static final String cert_pwd = "123456";
  //利用keytool生成数字证书
  //keytool -genkey -alias ctidcert -keystore c:/证书名称.keystore -storepass "888" -keypass "888" -keyalg "RSA" -dname "CN=www.123.cn,OU=简称,O=公司名称,L=北京,ST=北京,C=中国"
  private static final String cert_path = "E:/ssl/cstbr3.pfx";
  //"D:\\dev\\eclipse-jee-juno-SR2-win32\\eclipse\\workspace\\sign\\src\\machunlin_1.pfx";

  public static void sign() throws Exception {
    BouncyCastleProvider provider = new BouncyCastleProvider();
    Security.addProvider(provider);
    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(new FileInputStream(cert_path), cert_pwd.toCharArray());
    String alias = (String) ks.aliases().nextElement();
    //		PrivateKey pk = (PrivateKey) ks.getKey(alias, cert_pwd.toCharArray());
    //		KeyStore ks = KeyStore.getInstance("PKCS12");
    //		ks.load(new FileInputStream(cert_path), cert_pwd.toCharArray());
    TestPdfSgin sign = new TestPdfSgin();
    PrivateKey pk = sign.GetPvkformPfx(cert_path, cert_pwd);
    Certificate[] chain = ks.getCertificateChain(alias);

    // 要签名的pdf 输出签名的pdf
    PdfReader reader = new PdfReader("E:/20200330_sign.pdf");
    FileOutputStream os = new FileOutputStream("E:/20200330_sign_sign.pdf");
    PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
    // Creating the appearance
    PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
    //appearance.setReason("digtal signature");
    //appearance.setLocation("FuZhou");
    //appearance.setVisibleSignature(new Rectangle(400, 840 - 200, 200, 700), 1, "sig");
    // Creating the signature
    ExternalSignature pks = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, provider.getName());
    ExternalDigest digest = new BouncyCastleDigest();
    MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0, CryptoStandard.CMS);
  }

  public static void main(String[] args) {
    try {
      sign();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("done!!!");
  }
}