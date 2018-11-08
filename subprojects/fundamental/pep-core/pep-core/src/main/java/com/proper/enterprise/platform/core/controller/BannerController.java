package com.proper.enterprise.platform.core.controller;

import com.proper.enterprise.platform.core.PEPConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@Api(tags = "/banner")
@RequestMapping("/banner")
public class BannerController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);

    @Value(value = "classpath:banner.txt")
    private Resource resource;

    @GetMapping
    @ApiOperation("‍欢迎页展示 banner 图案")
    public String readTxt() {
        String line;
        StringBuilder str = new StringBuilder();

        try (
            InputStream is = resource.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, PEPConstants.DEFAULT_CHARSET);
            BufferedReader br = new BufferedReader(isr)
        ) {
            while ((line = br.readLine()) != null) {
                str.append(line).append("\r\n");
            }
        } catch (IOException e) {
            LOGGER.error("Error occurs when reading banner resource!", e);
        }

        return str.toString();
    }

}
