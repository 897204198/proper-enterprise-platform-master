package com.proper.enterprise.platform.core.utils.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.ConnectException;

public class HttpClientInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientInterceptor.class);

    private int retryNum = 1;

    /**
     * 最大重试次数
     */
    private int executionCount;

    /**
     * 重试间隔(默认第一次5s间隔,第二次5*2s,第三次5*3s...)
     */
    @Value("${pep.core.http-retry-interval}")
    private long retryInterval;

    public int getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(int executionCount) {
        this.executionCount = executionCount;
    }

    public long getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        LOGGER.debug("start intercept http client");
        Request request = chain.request();
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (ConnectException e) {
            while ((response == null || !response.isSuccessful()) && retryNum <= executionCount) {
                LOGGER.debug("start intercept http retry {} time", retryNum);
                try {
                    threadSleep();
                } catch (InterruptedException e1) {
                    LOGGER.debug("Thread throws an interrupted exception {}", e1.getMessage());
                    Thread.currentThread().interrupt();
                    throw e;
                }
                retryNum++;
                // retry the request
                response = intercept(chain);
            }
            throw e;
        }
        return response;
    }

    /**
     * 重试间隔
     *
     * @throws InterruptedException 中止状态改变异常
     */
    private void threadSleep() throws InterruptedException {
        final long nextInterval = getRetryInterval();
        Thread.sleep(nextInterval * retryNum);
    }
}
