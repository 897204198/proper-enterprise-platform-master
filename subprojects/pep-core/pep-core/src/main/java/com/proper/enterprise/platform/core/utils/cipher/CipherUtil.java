package com.proper.enterprise.platform.core.utils.cipher;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class CipherUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtil.class);

    private String algorithm;
    private String signAlgorithm;
    private String mode;
    private String secretKey;
    private String padding;

	private CipherUtil() {
	}

    /**
     * 获得对称加密算法实例
     *
     * @param algorithm 算法
     * @param mode      模式
     * @param padding   填充
     * @param secretKey 秘钥
     * @return 对称加密算法实例
     */
    protected static CipherUtil getInstance(String algorithm, String mode, String padding, String secretKey) {
        CipherUtil instance = new CipherUtil();
        instance.algorithm = algorithm;
        instance.mode = mode;
        instance.secretKey = secretKey;
        instance.padding = padding;
        return instance;
    }

    /**
     * 获得非对称加密算法实例
     *
     * @param algorithm     算法
     * @param signAlgorithm 签名算法
     * @return 非对称加密算法实例
     */
    protected static CipherUtil getInstance(String algorithm, String signAlgorithm) {
        CipherUtil instance = new CipherUtil();
        instance.algorithm = algorithm;
        instance.signAlgorithm = signAlgorithm;
        return instance;
    }

    @Deprecated
    protected String encrypt(String content) throws Exception {
		return new String(execute(content.getBytes(PEPConstants.DEFAULT_CHARSET), getSecretKey(), true), PEPConstants.DEFAULT_CHARSET);
	}

    protected byte[] encrypt(byte[] data) throws Exception {
        return execute(data, getSecretKey(), true);
    }

    private byte[] execute(byte[] data, Key key, boolean encrypt) throws Exception {
        LOGGER.trace("{} data with {}({})",
            encrypt ? "Encrypt" : "Decrypt",
            new String(key.getEncoded(), PEPConstants.DEFAULT_CHARSET), amp(algorithm, mode, padding));

        byte[] result;
        Cipher cipher = Cipher.getInstance(amp(algorithm, mode, padding));

        if (encrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            result = Base64.encodeBase64(cipher.doFinal(data));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key);
            result = cipher.doFinal(Base64.decodeBase64(data));
        }
        return result;
    }

    private Key getSecretKey() {
        return new SecretKeySpec(secretKey.getBytes(PEPConstants.DEFAULT_CHARSET), algorithm);
    }

    @Deprecated
    protected String decrypt(String content) throws Exception  {
		return new String(execute(content.getBytes(PEPConstants.DEFAULT_CHARSET), getSecretKey(), false), PEPConstants.DEFAULT_CHARSET);
	}

    protected byte[] decrypt(byte[] data) throws Exception {
        return execute(data, getSecretKey(), false);
    }

    private String amp(String algorithm, String mode, String padding) {
        return StringUtil.join(new String[]{algorithm, mode, padding}, "/");
    }

    protected Map<String, String> generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        SecureRandom sr = new SecureRandom();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        kpg.initialize(keySize, sr);
        KeyPair kp = kpg.generateKeyPair();

        Map<String, String> map = new HashMap<>(2);
        Key publicKey = kp.getPublic();
        map.put("publicKey", Base64.encodeBase64String(publicKey.getEncoded()));
        map.put("privateKey", Base64.encodeBase64String(kp.getPrivate().getEncoded()));

        return map;
    }

    @Deprecated
    protected String encrypt(String content, String publicKey) throws Exception {
        return new String(execute(content.getBytes(PEPConstants.DEFAULT_CHARSET), getPublicKey(publicKey), true), PEPConstants.DEFAULT_CHARSET);
    }

    protected byte[] encrypt(byte[] data, String publicKey) throws Exception {
        return execute(data, getPublicKey(publicKey), true);
    }

    protected PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
    }

    @Deprecated
    protected String decrypt(String content, String privateKey) throws Exception {
        return new String(execute(content.getBytes(PEPConstants.DEFAULT_CHARSET), getPrivateKey(privateKey), false), PEPConstants.DEFAULT_CHARSET);
    }

    protected byte[] decrypt(byte[] data, String privateKey) throws Exception {
        return execute(data, getPrivateKey(privateKey), false);
    }

    protected PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
    }

    protected String sign(String content, String privateKey) throws Exception {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initSign(getPrivateKey(privateKey));
        signature.update(content.getBytes(PEPConstants.DEFAULT_CHARSET));
        return Base64.encodeBase64String(signature.sign());
    }

    protected boolean verifySign(String content, String sign, String publicKey) throws Exception {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initVerify(getPublicKey(publicKey));
        signature.update(content.getBytes(PEPConstants.DEFAULT_CHARSET));
        return signature.verify(Base64.decodeBase64(sign));
    }

}
