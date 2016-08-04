package com.proper.enterprise.platform.core.utils;

import com.proper.enterprise.platform.core.PEPConstants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtil.class);

    private String algorithm;
    private String mode;
    private int keySize;
    private String key;
    private String padding;

	private CipherUtil() {
	}

    public static CipherUtil getInstance(String algorithm, String mode, String padding, String key, int keySize) {
        CipherUtil instance = new CipherUtil();
        instance.algorithm = algorithm;
        instance.mode = mode;
        instance.keySize = keySize;
        instance.key = key;
        instance.padding = padding;
        return instance;
    }

	public String encrypt(String content) throws Exception {
		return execute(content, true);
	}

	public String decrypt(String content) throws Exception  {
		return execute(content, false);
	}

	private String execute(String content, boolean encrypt) throws Exception {
        LOGGER.debug("{} some content: {} with {}/{}/{}, keySize: {}, key: {}",
            encrypt ? "Encrypt" : "Decrypt",
            StringUtil.abbreviate(content, 30),
            algorithm, mode, padding, keySize, key);

		String result;
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(PEPConstants.DEFAULT_CHARSET), algorithm);
        Cipher cipher = Cipher.getInstance(StringUtil.join(new String[]{algorithm, mode, padding}, "/"));

        if (encrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            result = Base64.encodeBase64String(cipher.doFinal(content.getBytes(PEPConstants.DEFAULT_CHARSET)));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            result = new String(cipher.doFinal(Base64.decodeBase64(content)), PEPConstants.DEFAULT_CHARSET);
        }
        LOGGER.debug("After {}: {}", encrypt ? "Encrypt" : "Decrypt", result);
		return result;
	}


}
