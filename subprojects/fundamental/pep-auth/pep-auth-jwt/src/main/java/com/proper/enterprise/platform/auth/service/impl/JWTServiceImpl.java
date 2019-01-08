package com.proper.enterprise.platform.auth.service.impl;

import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.auth.common.service.impl.AccessTokenServiceImpl;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;
import com.proper.enterprise.platform.auth.service.APISecret;
import com.proper.enterprise.platform.auth.service.JWTService;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

@Service
@Primary
public class JWTServiceImpl extends AccessTokenServiceImpl implements JWTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    private static final String TOKEN_SEPARATOR = ".";

    private APISecret secret;

    private CoreProperties coreProperties;

    @Autowired
    public JWTServiceImpl(APISecret secret, CoreProperties coreProperties) {
        this.secret = secret;
        this.coreProperties = coreProperties;
    }

    @Override
    public String generateToken(JWTHeader header, JWTPayload payload) throws IOException {
        String apiSecret = secret.getAPISecret(header.getId());
        String headerStr = JSONUtil.toJSON(header);
        String payloadStr = JSONUtil.toJSON(payload);
        LOGGER.debug("apiSecret: {}, header: {}, payload: {}", apiSecret, headerStr, payloadStr);
        String headerBase64 = base64(headerStr);
        String payloadBase64 = base64(payloadStr);
        String sign = hmacSha256Base64(apiSecret, headerBase64 + "." + payloadBase64);
        return StringUtil.join(new String[]{headerBase64, payloadBase64, sign}, ".");
    }

    private String hmacSha256Base64(String secret, String message) {
        String result = Base64.encodeBase64URLSafeString(new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmac(message));
        LOGGER.debug("API secrect is {}, message is {} and result is {}", secret, message, result);
        return result;
    }

    private String base64(String str) {
        return Base64.encodeBase64URLSafeString(str.getBytes(Charset.forName(coreProperties.getCharset())));
    }

    @Override
    public boolean verify(String token) {
        if (StringUtil.isBlank(token)) {
            LOGGER.debug("Token IS NULL!");
            return false;
        }

        if (notJWTToken(token)) {
            LOGGER.debug("Token is NOT JWT type, delegate super class to verify!");
            return super.verify(token);
        }

        String[] split = token.split("\\" + TOKEN_SEPARATOR);
        String headerBase64 = split[0];
        String payloadBase64 = split[1];
        String sign = split[2];

        try {
            JWTHeader header = getHeader(token);
            String apiSecret = secret.getAPISecret(header.getId());
            if (!sign.equals(hmacSha256Base64(apiSecret, headerBase64 + TOKEN_SEPARATOR + payloadBase64))) {
                LOGGER.debug("Token is INVALID or EXPIRED! Sign is {}", sign);
                return false;
            }
            return true;
        } catch (IOException e) {
            LOGGER.debug("Error occurs when parsing token.", e);
            return false;
        }
    }

    private boolean notJWTToken(String token) {
        return !token.contains(TOKEN_SEPARATOR) || token.split("\\" + TOKEN_SEPARATOR).length != 3;
    }

    @Override
    public Optional<String> getUserId(String token) {
        if (notJWTToken(token)) {
            return super.getUserId(token);
        }
        try {
            JWTHeader header = getHeader(token);
            if (header != null) {
                return Optional.of(header.getId());
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            LOGGER.debug("Get JWT header error!", e);
            return Optional.empty();
        }
    }

    @Override
    public JWTHeader getHeader(String token) throws IOException {
        Assert.notNull(token, "COULD NOT GET JWT HEADER FROM NULL TOKEN!");
        String[] split = token.split("\\" + TOKEN_SEPARATOR);
        String headerStr = split[0];
        Assert.notNull(headerStr, "COULD NOT GET JWT HEADER FROM TOKEN " + token);
        return JSONUtil.parse(Base64.decodeBase64(headerStr), JWTHeader.class);
    }

    @Override
    public void clearToken(JWTHeader header) {
        secret.clearAPISecret(header.getId());
    }

    @Override
    public String generate() {
        throw new ErrMsgException("Generate AccessToken UnSupported!");
    }

    @Override
    public AccessToken saveOrUpdate(AccessToken accessToken) {
        throw new ErrMsgException("SaveOrUpdate AccessToken UnSupported!");
    }

    @Override
    public void deleteByToken(String token) {
        throw new ErrMsgException("DeleteByToken AccessToken UnSupported!");
    }

}
