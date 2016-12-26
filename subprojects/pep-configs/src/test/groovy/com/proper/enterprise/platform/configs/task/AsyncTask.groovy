package com.proper.enterprise.platform.configs.task

import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.stereotype.Component

import java.util.concurrent.Future

@Component
class AsyncTask {

    @Async
    public Future<Integer> addOne(int i) {
        new AsyncResult<Integer>(++i)
    }

}
