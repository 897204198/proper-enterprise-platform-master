package com.proper.enterprise.platform.image.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController extends BaseController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping(path = "/{id}/thumbnail")
    public void downloadThumbnail(@PathVariable String id, @RequestParam Integer width, @RequestParam Integer height,
                                  HttpServletRequest request, HttpServletResponse response) throws IOException {
        imageService.downloadThumbnail(id, width, height, request, response);
    }
}
