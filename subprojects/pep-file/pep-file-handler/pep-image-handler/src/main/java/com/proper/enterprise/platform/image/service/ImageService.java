package com.proper.enterprise.platform.image.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ImageService {

    /**
     * 下载缩略图
     *
     * @param id       文件id
     * @param width    宽度
     * @param height   高度
     * @param request  请求
     * @param response 响应
     * @throws IOException io异常
     */
    void downloadThumbnail(String id, int width, int height, HttpServletRequest request, HttpServletResponse response) throws IOException;

}
