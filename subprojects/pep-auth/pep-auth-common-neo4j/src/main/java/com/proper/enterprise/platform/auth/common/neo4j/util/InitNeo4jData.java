package com.proper.enterprise.platform.auth.common.neo4j.util;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.utils.AntResourceUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 初始化Neo4j数据.
 */
@Component
public class InitNeo4jData {

    private static Logger logger = LoggerFactory.getLogger(InitNeo4jData.class);

    @Value("${auth.common.neo4j.cql.resource.path}")
    private String resourcePath;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    SessionFactory sessionFactory;

    @PostConstruct
    public void initNeo4j() throws Exception {
        logger.info("----------InitNeo4jData-----------");
        try {
            Collection<String> cqlList = new ArrayList<>();
            Resource[] resources = AntResourceUtil.getResources(resourcePath);
            for (Resource resource :  resources) {
                File file = resource.getFile();
                cqlList.addAll(FileUtils.readLines(file, PEPConstants.DEFAULT_CHARSET));
            }
            if (cqlList.size() > 0) {
                executeCQL(cqlList);
            }
            logger.info("init neo4j successfully finished!");
        } catch (Exception e) {
            logger.error("init neo4j data error occurred!", e);
        }
    }

    private void executeCQL(Collection<String> cqlList) {
        Session session = sessionFactory.openSession();
        for (String cql : cqlList) {
            try {
                if (StringUtil.isNotNull(cql) && !cql.startsWith("--")) {
                    session.query(cql, Collections.EMPTY_MAP, false);
                }
            } catch (Exception e) {
                logger.error("error occurred when execute cql:{}", cql);
            }
        }
    }
}
