package com.proper.enterprise.platform.core.banner;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/banner")
public class BannerController extends BaseController {

    @Value(value = "classpath:banner.txt")
    private Resource resource;

    @GetMapping
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
            e.printStackTrace();
        }

        return str.toString();
    }

}