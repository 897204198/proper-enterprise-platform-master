package com.proper.enterprise.platform.schedule.impl.jdbcjobstore;

import com.proper.enterprise.platform.core.utils.SerializationUtil;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;

import java.io.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A special JDBC delegate by PEP to solve the class load problem when using with spring-loaded.
 * Could get issue details on https://github.com/spring-projects/spring-loaded/issues/107
 * <p>
 * When the getObjectFromBlob method in the StdJDBCDelegate provided by quartz gets the class loader,
 * the latestUserDefinedLoader is used by default.
 * Sometimes the latestUserDefinedLoader system default loader is A.
 * But the class loader used by the current thread t is B.
 * <p>
 * The solution
 * Use SerializationUtil to deserialize object which fixed the problem.
 *
 * @author zhangjl
 */
public class PEPStdJDBCDelegate extends StdJDBCDelegate {

    @Override
    protected Object getObjectFromBlob(ResultSet rs, String colName) throws SQLException, IOException {
        Object obj = null;
        Blob blobLocator = rs.getBlob(colName);
        if (blobLocator != null && blobLocator.length() != 0) {
            InputStream binaryInput = blobLocator.getBinaryStream();
            obj = SerializationUtil.deserializeObject(binaryInput);
        }
        return obj;
    }

}
