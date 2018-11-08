package com.proper.enterprise.platform.file.controller

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.file.vo.FileVO
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class FileControllerTest extends AbstractJPATest {

    @Test
    public void uploadTest() {
        Resource[] resources = AntResourceUtil.getResources('classpath*:com/proper/enterprise/platform/file/test/upload/测试上传文件.png')
        String result = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "测试上传文件.png", ",multipart/form-data", resources[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        assert result != null
        FileVO fileVO = JSONUtil.parse(get("/file/" + result + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)
        assert fileVO.getFileName() == "测试上传文件.png"
    }

    @Test
    public void putTest() {
        //上传文件
        Resource[] resources = AntResourceUtil.getResources('classpath*:com/proper/enterprise/platform/file/test/upload/测试上传文件.png')
        String result = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "测试上传文件.png", ",multipart/form-data", resources[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        assert result != null
        FileVO fileVO = JSONUtil.parse(get("/file/" + result + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)
        assert fileVO.getFileName() == "测试上传文件.png"
        //修改文件
        Resource[] resourcePuts = AntResourceUtil.getResources('classpath*:com/proper/enterprise/platform/file/test/upload/测试修改文件.png')
        String resultPut = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "测试修改文件.png", ",multipart/form-data", resourcePuts[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        //修改文件验证
        FileVO filePutVO = JSONUtil.parse(get("/file/" + resultPut + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)
        assert filePutVO.getFileName() == "测试修改文件.png"
        assert "测试修改文件.png" == URLDecoder.decode(get("/file/" + resultPut, HttpStatus.OK).getResponse().getHeader("Content-disposition").replace("attachment;filename=", ""), PEPConstants.DEFAULT_CHARSET.name())

    }

    @Test
    public void downloadTest() {
        Resource[] resources = AntResourceUtil.getResources('classpath*:com/proper/enterprise/platform/file/test/upload/测试下载文件.png')
        String result = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file")
                .file(
                new MockMultipartFile("file", "测试下载文件.png", ",multipart/form-data", resources[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        assert "测试下载文件.png" == URLDecoder.decode(get("/file/" + result, HttpStatus.OK).getResponse().getHeader("Content-disposition").replace("attachment;filename=", ""), PEPConstants.DEFAULT_CHARSET.name())
    }
}
