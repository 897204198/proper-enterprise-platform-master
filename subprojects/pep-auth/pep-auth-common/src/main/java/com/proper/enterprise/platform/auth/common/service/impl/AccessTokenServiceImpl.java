package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.dao.AccessTokenDAO;
import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.api.auth.service.AccessTokenService;
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenServiceImpl.class);

    /**
     * 此依赖不通过构造函数方式注入，以便于继承此类扩展 AccessTokenService
     * 作为可选依赖，在使用前需进行校验
     */
    @Autowired(required = false)
    private AccessTokenDAO dao;

    @Override
    public Optional<String> getToken(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        if (token == null) {
            token = getTokenFromCookie(request);
        }
        if (token == null) {
            token = getTokenFromReqParameter(request);
        }
        return StringUtil.isNotBlank(token) ? Optional.of(unwrapBearer(token)) : Optional.empty();
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_FLAG_HEADER);
        if (StringUtil.isBlank(token)) {
            token = request.getHeader(TOKEN_FLAG_HEADER_COMPATIBILITY);
        }
        if (StringUtil.isNotBlank(token)) {
            LOGGER.trace("Found token in request header!");
            return token;
        }
        return null;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (TOKEN_FLAG_COOKIE.equals(cookie.getName())) {
                String token = cookie.getValue();
                if (StringUtil.isNotBlank(token)) {
                    LOGGER.trace("Found token in cookie!");
                    return token;
                }
            }
        }
        return null;
    }

    private String getTokenFromReqParameter(HttpServletRequest request) {
        String token = request.getParameter(TOKEN_FLAG_URI);
        if (StringUtil.isNotBlank(token)) {
            LOGGER.trace("Found token in URI!");
            return token;
        }
        return null;
    }

    private String unwrapBearer(String token) {
        return token.contains(TOKEN_FLAG_HEADER_BEARER)
                ? token.replace(TOKEN_FLAG_HEADER_BEARER, "").trim() : token;
    }

    @Override
    public boolean verify(String token) {
        if (dao == null) {
            return false;
        }
        Optional<AccessToken> tokenModel = dao.getByToken(token);
        return tokenModel.isPresent();
    }

    @Override
    public Optional<String> getUserId(String token) {
        Optional<AccessToken> tokenModel = getByToken(token);
        return tokenModel.map(AccessToken::getUserId);
    }

    private Optional<AccessToken> getByToken(String token) {
        if (dao == null) {
            return Optional.empty();
        }
        return dao.getByToken(token).map(this::convertToVO);
    }

    private AccessToken convertToVO(AccessToken accessToken) {
        AccessTokenVO vo = new AccessTokenVO();
        BeanUtils.copyProperties(accessToken, vo);
        return vo;
    }

    @Override
    public Optional<AccessToken> getByUserId(String userId) {
        if (dao == null) {
            return Optional.empty();
        }
        return dao.getByUserId(userId).map(this::convertToVO);
    }

    @Override
    public String generate() {
        return HmacUtils.hmacSha1Hex("PEP access T0ken", UUID.randomUUID().toString());
    }

    @Override
    public AccessToken saveOrUpdate(AccessToken accessToken) {
        if (dao == null) {
            return accessToken;
        }
        return convertToVO(dao.saveOrUpdate(accessToken));
    }

    @Override
    public void deleteByToken(String token) {
        if (dao == null) {
            return;
        }
        dao.deleteByToken(token);
    }

}
