package com.proper.enterprise.platform.oopsearch.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.api.document.OOPSearchDocument;
import com.proper.enterprise.platform.oopsearch.api.serivce.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;

@AuthcIgnore
@RestController
@RequestMapping("/search")
public class OopSearchController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(OopSearchController.class);

    @Autowired
    private SearchService searchService;

    @GetMapping("/inverse")
    public ResponseEntity<List<OOPSearchDocument>> searchInfo(@RequestParam String data, @RequestParam String moduleName) {
        ApplicationContext context = PEPApplicationContext.getApplicationContext();
        AbstractSearchConfigs searchConfig = (AbstractSearchConfigs) context.getBean(moduleName.replace("$", ""));
        List<OOPSearchDocument> docs = (List<OOPSearchDocument>) searchService.getSearchInfo(data, searchConfig);
        ResponseEntity<List<OOPSearchDocument>> result = responseOfGet(docs);
        return result;
    }

    @GetMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, String req, String moduleName) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (StringUtil.isEmpty(req)) {
            req = "[{}]";
        }
        try {
            req = URLDecoder.decode(req, PEPConstants.DEFAULT_CHARSET.toString());
            JsonNode jl = objectMapper.readValue(req, JsonNode.class);
            StringBuffer stringBuffer = new StringBuffer(moduleName.replace("$", "/"));
            for (int i = 0; i < jl.size(); i++) {
                JsonNode jn = jl.get(i);
                String key = jn.findValue("key").asText();
                String value = jn.findValue("value").asText();
                if (i == 0) {
                    stringBuffer.append("?" + key + "=" + value);
                } else {
                    stringBuffer.append("&" + key + "=" + value);
                }
            }
            request.getRequestDispatcher(stringBuffer.toString()).forward(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
