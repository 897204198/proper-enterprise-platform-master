
package com.proper.enterprise.platform.oopsearch.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proper.enterprise.platform.api.auth.service.AuthzService;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.document.OOPSearchDocument;
import com.proper.enterprise.platform.oopsearch.service.SearchService;
import com.proper.enterprise.platform.oopsearch.config.service.SearchConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Api(tags = "/search")
@RequestMapping("/search")
public class OopSearchController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(OopSearchController.class);

    @Autowired
    private SearchService searchService;

    @Autowired
    private SearchConfigService searchConfigService;

    @Autowired
    private AuthzService authzService;

    @GetMapping("/inverse")
    @ApiOperation("???????????????????????????????????????")
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<OOPSearchDocument>> searchInfo(@ApiParam(value = "??????????????????") @RequestParam String data,
                                                              @ApiParam(value = "???????????????", required = true) @RequestParam String moduleName) {
        List<OOPSearchDocument> docs = (List<OOPSearchDocument>) searchService.getSearchInfo(data, moduleName);
        return responseOfGet(docs);
    }

    @GetMapping("/query")
    @ApiOperation("???????????????????????????????????????")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam(value = "???rest??????", required = true) String restPath,
                      @ApiParam(value = "???????????????", required = true) String req,
                      @ApiParam(value = "???????????????", required = true) String moduleName) {
        String url = searchConfigService.getURL(moduleName.replace("$", "/"));
        if (StringUtil.isNull(url)) {
            return;
        }
        try {
            url = handleRestUrl(url, restPath, response);
        } catch (HttpRequestMethodNotSupportedException e) {
            return;
        }
        if (!accessible(url, request, response)) {
            return;
        }
        url = handleOOPSearchParam(url, req);
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException | IOException e) {
            logger.error("oopSearch forward error", e);
            throw new ErrMsgException("oopSearch forward error");
        }
    }

    private String handleOOPSearchParam(String url, String req) {
        if (StringUtil.isEmpty(req)) {
            return url;
        }
        try {
            req = URLDecoder.decode(req, PEPPropertiesLoader.load(CoreProperties.class).getCharset());
        } catch (UnsupportedEncodingException e) {
            logger.error("oopSearch decode req error,req:{}", req, e);
            throw new ErrMsgException("oopSearch decode req error");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jl;
        try {
            jl = objectMapper.readValue(req, JsonNode.class);
        } catch (IOException e) {
            logger.error("oopSearch config req error,req:{}", req, e);
            throw new ErrMsgException("oopSearch config req error");
        }
        Assert.notNull(url, "CAN NOT GET URL WITH THE MODULENAME");
        StringBuffer stringBuffer = new StringBuffer(url);
        for (int i = 0; i < jl.size(); i++) {
            JsonNode jn = jl.get(i);
            String key = jn.findValue("key").asText();
            String value = jn.findValue("value").asText();
            if (i == 0) {
                stringBuffer.append("?").append(key).append("=").append(value);
            } else {
                stringBuffer.append("&").append(key).append("=").append(value);
            }
        }
        return stringBuffer.toString();
    }

    @SuppressWarnings("unchecked")
    private String handleRestUrl(String url, String restPath, HttpServletResponse response) throws HttpRequestMethodNotSupportedException {
        Map<String, String> restPathMap = new HashMap<>(16);
        if (StringUtil.isNotEmpty(restPath)) {
            try {
                restPathMap = JSONUtil.parse(URLDecoder.decode(restPath,
                    PEPPropertiesLoader.load(CoreProperties.class).getCharset()), Map.class);
            } catch (IOException e) {
                logger.error("oopSearch parse restPath error,restPath:{}", restPath, e);
                throw new ErrMsgException("oopSearch parse restPath error");
            }
        }
        String re = "(\\{.+?\\})";
        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(url);
        while (m.find()) {
            String key = m.group();
            if (StringUtil.isEmpty(key)) {
                continue;
            }
            String realKey = key.replaceAll("\\{", "").replaceAll("\\}", "");
            String value = restPathMap.get(realKey);
            if (null == value) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                throw new HttpRequestMethodNotSupportedException("oopsearch config rest error,path:{} value is null", realKey);
            }
            url = url.replaceAll(realKey, value);
        }
        return url.replaceAll("\\{", "").replaceAll("\\}", "");
    }

    private boolean accessible(String url, HttpServletRequest request, HttpServletResponse response) {
        String resDescriptor = request.getMethod() + ":" + url;
        if (!authzService.accessible(resDescriptor, Authentication.getCurrentUserId())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setHeader("WWW-Authenticate",
                "Bearer realm=\"pep\", "
                    + "error=\"invalid_auth\", "
                    + "error_description=\"COULD NOT ACCESS THIS API WITHOUT A PROPER AUTHORIZATION\"");
            return false;
        }
        return true;
    }
}
