package com.proper.enterprise.platform.auth.service;

import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Utility service for JSON Web Token (http://jwt.io/)
 * Only support JWT type and HS256 algorithm now
 * Use user id as key of apiSecret caches
 *
 * @author Hinex
 */
public class JWTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTService.class);

    private static final String AUTH_HEADER = "Authorization";
    protected static final String TOKEN_FLAG = "X-PEP-TOKEN";

    private APISecret secret;

    public void setSecret(APISecret secret) {
        this.secret = secret;
    }

    public String getToken(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        if (StringUtil.isNull(token)) {
            token = getTokenFromCookie(request);
        }
        return token;
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER);
        if (StringUtil.isNull(token)) {
            token = request.getHeader(TOKEN_FLAG);
        }
        LOGGER.trace("Request {} token header!", StringUtil.isNotNull(token) ? "has" : "DOES NOT HAVE");
        if (StringUtil.isNotNull(token) && token.contains("Bearer")) {
            token = token.replace("Bearer", "").trim();
        }
        return token;
    }

    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (TOKEN_FLAG.equals(cookie.getName())) {
                LOGGER.trace("Found token in cookie! {}", cookie);
                return cookie.getValue();
            }
        }
        return null;
    }

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
        String result = Base64.encodeBase64URLSafeString(HmacUtils.hmacSha256(secret, message));
        LOGGER.debug("API secrect is {}, message is {} and result is {}", secret, message, result);
        return result;
    }

    private String base64(String str) {
        return Base64.encodeBase64URLSafeString(str.getBytes(PEPConstants.DEFAULT_CHARSET));
    }

    public boolean verify(String token) throws IOException {
        if (StringUtil.isNull(token) || !token.contains(".")) {
            LOGGER.debug("Token should NOT NULL!");
            return false;
        }

        String[] split = token.split("\\.");
        String headerBase64 = split[0];
        String payloadBase64 = split[1];
        String sign = split[2];

        JWTHeader header = getHeader(token);
        String apiSecret = secret.getAPISecret(header.getId());
        if (!sign.equals(hmacSha256Base64(apiSecret, headerBase64 + "." + payloadBase64))) {
            LOGGER.debug("Token is INVALID or EXPIRED! Sign is {}", sign);
            return false;
        }
        return true;
    }

    public JWTHeader getHeader(String token) throws IOException {
        Assert.notNull(token, "COULD NOT GET JWT HEADER FROM NULL TOKEN!");
        String[] split = token.split("\\.");
        String headerStr = split[0];
        Assert.notNull(headerStr, "COULD NOT GET JWT HEADER FROM TOKEN " + token);
        return JSONUtil.parse(Base64.decodeBase64(headerStr), JWTHeader.class);
    }

    public void clearToken(JWTHeader header) {
        secret.clearAPISecret(header.getId());
    }

}
