package com.proper.enterprise.platform.test.integration

import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SqlWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlWorker)

    Sql db
    def sqls

    public SqlWorker() {
        Properties properties = new Properties()
        InputStream is = getClass().getResourceAsStream('/conf/webapp/database/webapp-database-test.properties')
        properties.load(is)
        is.close()

        def url = properties.getProperty('datasource.url')
        def user = properties.getProperty('datasource.username')
        def pwd = properties.getProperty('datasource.password')
        def driver = properties.getProperty('datasource.driverClassName')

        db = Sql.newInstance(url, user, pwd, driver)
        sqls = []
    }

    public void push(sqls) {
        this.sqls << sqls
        this.sqls = this.sqls.flatten()
    }

    public void work(repeatable=false) {
        sqls.each { sql ->
            LOGGER.trace(sql)
            db.execute(sql)
            if (!repeatable) {
                sqls -= sql
            }
        }
    }

}
