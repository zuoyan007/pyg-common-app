package pyg.daheng.common.model.vo.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TongTechEncode {

	/**
	 * @Title: getSin
	 * @Description: TODO
	 * @param appkey 授权码
	 * @param rid 请求者身份标识
	 * @param sid 服务标识
	 * @return
	 * @return: String
	 * @throws Exception
	 */
	public static String getSercret(String sid, String rid, String rtime, String sign1,String signKeyUrl) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(signKeyUrl);// 创建httpPost
		String charSet = "UTF-8";
		String body = "";

		String params = "{'hnjhpt_rid':'"+rid+"','hnjhpt_sid':'"+sid+"','hnjhpt_rtime':'"+ rtime + "','hnjhpt_sign':'" + sign1 + "'}";
		HttpEntity httpEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
		httpPost.setEntity(httpEntity);
		// 执行请求操作，并拿到结果（同步阻塞）
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost, new BasicHttpContext());
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String jsonString = EntityUtils.toString(responseEntity);
				return jsonString;
			} else {
				System.out.println("yicang");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	/**
	 * * 加密 26. * 1.构造密钥生成器 27. * 2.根据ecnodeRules规则初始化密钥生成器 28. * 3.产生密钥 29. * 4.创建和初始化密码器 30. *
	 * 5.内容加密 31. * 6.返回字符串
	 * @Title: AESEncode
	 * @Description: TODO
	 * @param encodeRules
	 * @param content
	 * @return
	 * @return: String
	 */
	public static String AESEncode(String encodeRules, String content) {
		try {
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 新增下面两行，处理Linux操作系统下随机数生成不一致的问题
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(encodeRules.getBytes());
			// 生成一个128位的随机源,根据传入的字节数组
			// keygen.init(128, new SecureRandom(encodeRules.getBytes()));
			// 将原来的初始化方式，改为下面的方式
			keygen.init(128, secureRandom);
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
			byte[] byte_encode = content.getBytes("utf-8");
			// 9.根据密码器的初始化方式--加密：将数据加密
			byte[] byte_AES = cipher.doFinal(byte_encode);
			// 10.将加密后的数据转换为字符串
			// 这里用Base64Encoder中会找不到包
			// 解决办法：
			// 在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
			Base64 base64Encoder = new Base64();
			String AES_encode = new String(base64Encoder.encode(byte_AES));
			// 11.将字符串返回
			return AES_encode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 如果有错就返加nulll
		return null;
	}
	// 接口成功返回后，应使用授权码（appkey）对返回值进行解密（ASE算法），以获取签名密钥（参见附件3.
	// 服务密钥解密示例代码）。涉及String/byte[]互转时，字符集均使用UTF-8。
	/**
	 * 获取签名秘钥解密
	 * @Title: AESDncode
	 * @Description: TODO
	 * @param encodeRules
	 * @param content
	 * @return
	 * @return: String
	 */
	public static String AESDncode(String encodeRules, String content) {
		try {
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 新增下面两行，处理Linux操作系统下随机数生成不一致的问题
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(encodeRules.getBytes());
			// 生成一个128位的随机源,根据传入的字节数组
			// keygen.init(128, new SecureRandom(encodeRules.getBytes()));
			// 将原来的初始化方式，改为下面的方式
			keygen.init(128, secureRandom);
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 8.将加密并编码后的内容解码成字节数组
			Base64 base64Encoder = new Base64();
			byte[] byte_content = base64Encoder.decode(content);
			/*
			 * 解密
			 */
			byte[] byte_decode = cipher.doFinal(byte_content);
			String AES_decode = new String(byte_decode, "utf-8");
			return AES_decode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		// 如果有错就返加nulll
		return null;
	}
	// sign值由客户端使用授权码（appkey）对rid、sid和rtime进行HMAC-SHA256计算并进行base64转码产生（参见附件2. 服务调用签名示例代码）。
	public static String HmacSHA256(String sid, String rid, String appkey, String rtime) {
		/*
		 * String sid = sid; String rid = rid; String appkey =
		 * appkey;//"ef0167b25ea547cfb78f789915900033";//TestTongtec.appkey;
		 */
		//String rtime = "" + System.currentTimeMillis();
		String result = null;
		try {
			Mac hmacSha256 = Mac.getInstance("HmacSHA256");
			byte[] keyBytes = appkey.getBytes("UTF-8");
			hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
			String inputString = sid + rid + rtime;
			System.out.println("INPUT: " + inputString);
			byte[] hmacSha256Bytes = hmacSha256.doFinal(inputString.getBytes("UTF-8"));
			result = new String(Base64.encodeBase64(hmacSha256Bytes), "UTF-8");
			System.out.println("OUTPUT: " + result);
			System.out.println(rtime);
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
}
