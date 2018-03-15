package com.proper.enterprise.platform.core.utils.gzip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

public class FinishOnCompleteListener implements AsyncListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FinishOnCompleteListener.class);

    private CompressedResponseWrapper wrappedResponse;

    public FinishOnCompleteListener(CompressedResponseWrapper wrappedResponse)
    {
        this.wrappedResponse = wrappedResponse;
    }

    @Override
    public void onComplete(AsyncEvent event) throws IOException
    {
        try
        {
            wrappedResponse.finish();
        }
        catch (IOException e)
        {
            LOGGER.warn(""+e);
        }
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException
    {
    }

    @Override
    public void onError(AsyncEvent event) throws IOException
    {
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException
    {
    }
}
