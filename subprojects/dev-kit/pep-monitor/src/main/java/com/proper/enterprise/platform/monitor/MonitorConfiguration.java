package com.proper.enterprise.platform.monitor;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@PropertySource("classpath:/application-monitor.properties")
public class MonitorConfiguration {

    @Bean
    public List<String> ignorePatternsListMonitor() {
        return Arrays.asList("GET:/**/druid/**", "POST:/**/druid/**", "GET:/actuator/**");
    }

    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatView() {
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setServlet(new StatViewServlet());
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/druid/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatFilter() {
        FilterRegistrationBean<WebStatFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new WebStatFilter());
        // 排除一些不必要的url
        registrationBean.setInitParameters(Collections.singletonMap("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"));
        return registrationBean;
    }

}
