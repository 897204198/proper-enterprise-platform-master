package com.proper.enterprise.platform.core.listener;

import com.proper.enterprise.platform.core.PEPVersion;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component
class SuccessfulStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        PrintStream ps = System.out;
        ps.println();
        ps.println("Propersoft Enterprise Platform (v" + PEPVersion.getVersion() + ") Started Successfully!");
        ps.println();
    }

}
