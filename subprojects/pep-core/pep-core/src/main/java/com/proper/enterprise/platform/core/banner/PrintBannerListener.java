package com.proper.enterprise.platform.core.banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
class PrintBannerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    BannerController bannerController;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            System.out.println("------------------Propersoft Enterprise Platform started successfully!----------------------");
            String str = bannerController.readTxt();
            System.out.println(str);
        }
    }

}
