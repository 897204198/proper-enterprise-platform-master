package com.proper.enterprise.platform.core.utils.gzip;

import java.io.IOException;
import java.util.EventListener;

/**
 *
 * Callback notification mechanism that signals to the developer it's possible
 * to write content without blocking.
 *
 * @since Servlet 3.1
 */
public interface WriteListener extends EventListener {

    public void onWritePossible() throws IOException;

    public void onError(final Throwable t);

}
