package com.proper.enterprise.platform.image.service.impl;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.image.service.ImageService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    private FileService fileService;

    private DFSService dsfService;

    @Autowired
    public ImageServiceImpl(FileService fileService, DFSService dsfService) {
        this.fileService = fileService;
        this.dsfService = dsfService;
    }

    @Override
    public void downloadThumbnail(String id, int width, int height, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = fileService.findById(id);
        if (null == file) {
            throw new ErrMsgException("The downloaded resource was not found");
        }
        Map<String, String> imgExtMsg = file.getFileExtMsgMap();
        InputStream inputStream = dsfService.getFile(file.getFilePath());
        Integer imgWidth = null == imgExtMsg || null == imgExtMsg.get("img_width")
            ? null
            : Integer.valueOf(imgExtMsg.get("img_width"));
        Integer imgHeight = null == imgExtMsg || null == imgExtMsg.get("img_height")
            ? null
            : Integer.valueOf(imgExtMsg.get("img_height"));
        if (null != imgWidth && imgWidth <= width) {
            fileService.download(id, request, response);
            return;
        }
        if (null != imgHeight && imgHeight <= height) {
            fileService.download(id, request, response);
            return;
        }
        if (inputStream == null) {
            throw new ErrMsgException("The downloaded resource was not found");
        }
        OutputStream outputStream = response.getOutputStream();
        Thumbnails.of(inputStream).size(width, height).toOutputStream(outputStream);
        inputStream.close();
        outputStream.close();
    }

}
