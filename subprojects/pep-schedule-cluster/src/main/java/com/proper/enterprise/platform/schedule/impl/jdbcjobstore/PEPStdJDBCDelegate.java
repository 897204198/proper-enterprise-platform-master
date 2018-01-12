package com.proper.enterprise.platform.schedule.impl.jdbcjobstore;

import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;

import java.io.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A special JDBC delegate by PEP
 * <p>
 * Solve the problem of using tomcat in gradle to start quartz.
 * When the getObjectFromBlob method in the StdJDBCDelegate provided by quartz gets the class loader,
 * the latestUserDefinedLoader is used by default.
 * Sometimes the latestUserDefinedLoader system default loader is A.
 * But the class loader used by the current thread t is B.
 * <p>
 * The solution
 * Override the resolveClass method of ObjectInputStream, first get the class loader from the
 * current thread, and then go to the default loader if it fails.
 *
 * @author zhangjl
 */
public class PEPStdJDBCDelegate extends StdJDBCDelegate {

    private static final String ERROR_TITLE = "====PEPDelegate ResolveClass Exception====";

    @Override
    protected Object getObjectFromBlob(ResultSet rs, String colName) throws SQLException, IOException {
        Object obj = null;
        ObjectInputStream in = null;
        Blob blobLocator = rs.getBlob(colName);
        if (blobLocator != null && blobLocator.length() != 0) {
            InputStream binaryInput = blobLocator.getBinaryStream();
            if (null != binaryInput) {
                try {
                    if (!(binaryInput instanceof ByteArrayInputStream && ((ByteArrayInputStream) binaryInput).available() == 0)) {
                        in = new ObjectInputStream(binaryInput) {
                            @Override
                            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                                String name = desc.getName();
                                try {
                                    return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                                } catch (ClassNotFoundException ex) {
                                    return super.resolveClass(desc);
                                }
                            }
                        };
                        obj = in.readObject();
                    }
                } catch (Exception e) {
                    logger.error(ERROR_TITLE, e);
                } finally {
                    if (null != in) {
                        in.close();
                    }
                }
            }
        }
        return obj;
    }
}
