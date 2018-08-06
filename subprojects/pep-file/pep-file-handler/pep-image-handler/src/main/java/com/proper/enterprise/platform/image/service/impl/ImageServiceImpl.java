package com.proper.enterprise.platform.image.service.impl;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.dfs.api.service.DFSService;
import com.proper.enterprise.platform.file.api.File;
import com.proper.enterprise.platform.file.service.FileService;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proper.enterprise.platform.image.service.ImageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        File file = fileService.findOne(id);
        if (null == file) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.download.not.find"));
        }
        InputStream inputStream = dsfService.getFile(file.getFilePath());
        if (inputStream == null) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.file.download.not.find"));
        }
        OutputStream outputStream = response.getOutputStream();
        Thumbnails.of(inputStream).size(width, height).toOutputStream(outputStream);
        inputStream.close();
        outputStream.close();
    }
}
