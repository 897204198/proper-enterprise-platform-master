package com.proper.enterprise.platform.image.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.image.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/image")
@Api(tags = "/image")
public class ImageController extends BaseController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping(path = "/{id}/thumbnail")
    @ApiOperation("‍下载缩略图")
    public void downloadThumbnail(@ApiParam(value = "‍文件id", required = true) @PathVariable String id,
                                  @ApiParam(value = "‍宽度‍", required = true) @RequestParam Integer width,
                                  @ApiParam(value = "‍高度‍", required = true) @RequestParam Integer height,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        imageService.downloadThumbnail(id, width, height, request, response);
    }
}
