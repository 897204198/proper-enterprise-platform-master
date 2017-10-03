package com.proper.enterprise.platform.webapp;

import com.proper.enterprise.platform.api.auth.annotation.AuthcIgnore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebAppController {

    @AuthcIgnore
    @GetMapping("/")
    public String helloWorld() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proper Enterprise Platform is running ....<br>")
          .append("Propersoft Present<br>")
          .append("Powered by Oopstorm");
        return sb.toString();
    }

}
