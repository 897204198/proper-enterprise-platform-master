package com.proper.enterprise.platform.file.controller

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.domain.PEPOrder
import com.proper.enterprise.platform.core.utils.AntResourceUtil
import com.proper.enterprise.platform.file.vo.FileVO
import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.core.io.Resource
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class FileControllerTest extends AbstractTest {

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

    @Test
    void uploadDirTest() {
        // com 下新建一个 upload 文件夹
        def data = [:]
        assert I18NUtil.getMessage("pep.file.filDir.isEmpty") == post("/file/dir", JSONUtil.toJSON(data), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        data['path'] = 'com/'
        assert I18NUtil.getMessage("pep.file.folderName.isEmpty") == post("/file/dir", JSONUtil.toJSON(data), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        data['fileName'] = 'upload'
        String result = post("/file/dir", JSONUtil.toJSON(data), HttpStatus.CREATED).getResponse().getContentAsString()
        assert result != null

        // upload 下新建一个 subUpload 文件夹
        def data2 = [:]
        data2['fileName'] = 'subUpload'
        data2['path'] = 'com/upload/'
        String result2 = post("/file/dir", JSONUtil.toJSON(data2), HttpStatus.CREATED).getResponse().getContentAsString()
        assert result2 != null

        // 创建的文件架构为：
        // com-|
        //     upload-|
        //            subUpload
        def fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/", PEPConstants.DEFAULT_CHARSET.toString()), HttpStatus.OK)
            .getResponse().getContentAsString(), Collection.class)
        assert 1 == fileVOs.size()
        fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/upload/", PEPConstants.DEFAULT_CHARSET.toString()), HttpStatus.OK)
            .getResponse().getContentAsString(), Collection.class)
        assert 1 == fileVOs.size()

        def data3 = [:]
        data3['fileName'] = 'subUpload'
        data3['path'] = 'com/upload/'
        assert I18NUtil.getMessage("pep.file.folder.isExist") == post("/file/dir", JSONUtil.toJSON(data3), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        // 上传文件
        Resource[] resources = AntResourceUtil.getResources('classpath*:com/proper/enterprise/platform/file/test/upload/测试上传文件.png')
        String filResult = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file?path=" + URLEncoder.encode("com/", PEPConstants.DEFAULT_CHARSET.toString()))
                .file(
                new MockMultipartFile("file", "测试上传文件.png", ",multipart/form-data", resources[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        assert filResult != null
        FileVO fileVO = JSONUtil.parse(get("/file/" + filResult + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)
        assert fileVO.getFileName() == "测试上传文件.png"

        // 创建的文件架构为：
        // com-|
        //              upload-|
        //     测试上传文件.png-|
        //                      subUpload

        // 默认排序 文件夹优先于文件 名称排序
        fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/", PEPConstants.DEFAULT_CHARSET.toString()), HttpStatus.OK).getResponse().getContentAsString(), Collection.class)
        assert 2 == fileVOs.size()
        assert fileVOs[0].dir

        fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/", PEPConstants.DEFAULT_CHARSET.toString()) + "&fileName=测试上", HttpStatus.OK).getResponse().getContentAsString(), Collection.class)
        assert 1 == fileVOs.size()

        List<PEPOrder> orders = new ArrayList<>()
        orders.add(new PEPOrder(Sort.Direction.DESC, "fileSize"))
        fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/", PEPConstants.DEFAULT_CHARSET.toString()) + "&orders=" + URLEncoder.encode(JSONUtil.toJSON(orders)), HttpStatus.OK).getResponse().getContentAsString(), Collection.class)
        assert 2 == fileVOs.size()
        assert fileVOs[0].fileName == "测试上传文件.png"
    }

    @Test
    void putDirTest() {
        // 创建的文件架构为：
        // com-|
        //     upload-|
        //            subUpload
        //            测试上传文件.png
        def data = [:]
        data['path'] = 'com/'
        data['fileName'] = 'upload'
        String result = post("/file/dir", JSONUtil.toJSON(data), HttpStatus.CREATED).getResponse().getContentAsString()
        assert result != null

        def data2 = [:]
        data2['fileName'] = 'subUpload'
        data2['path'] = 'com/upload/'
        String result2 = post("/file/dir", JSONUtil.toJSON(data2), HttpStatus.CREATED).getResponse().getContentAsString()
        assert result2 != null

        Resource[] resources = AntResourceUtil.getResources('classpath*:com/proper/enterprise/platform/file/test/upload/测试上传文件.png')
        String filResult = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file?path=com/upload/")
                .file(
                new MockMultipartFile("file", "测试上传文件.png", ",multipart/form-data", resources[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        assert filResult != null

        // 创建的文件架构为：
        // com-|
        //     upload-|
        //    upload2-|
        //            subUpload
        //            测试上传文件.png
        def data3 = [:]
        data3['fileName'] = 'upload2'
        data3['path'] = 'com/'
        String result3 = post("/file/dir", JSONUtil.toJSON(data3), HttpStatus.CREATED).getResponse().getContentAsString()
        assert result3 != null

        // 修改文件夹名称 upload => uploadupload
        FileVO fileVO = JSONUtil.parse(get("/file/" + result + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)
        assert fileVO.getFileName() == "upload"

        fileVO.setFileName("uploadupload")
        put("/file/dir/" + result, JSONUtil.toJSON(fileVO), HttpStatus.OK)

        def fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/upload/", PEPConstants.DEFAULT_CHARSET.toString()), HttpStatus.OK).getResponse().getContentAsString(), Collection.class)
        assert 0 == fileVOs.size()

        fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/uploadupload/", PEPConstants.DEFAULT_CHARSET.toString()), HttpStatus.OK).getResponse().getContentAsString(), Collection.class)
        assert 2 == fileVOs.size()

        // 修改文件夹名称 uploadupload => upload2
        fileVO.setFileName("upload2")
        assert I18NUtil.getMessage("pep.file.folder.isExist") == put("/file/dir/" + result, JSONUtil.toJSON(fileVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        // 移动文件夹 uploadupload 到 子目录中
        fileVO.setVirPath("com/uploadupload/subUpload/")
        assert I18NUtil.getMessage("pep.file.folder.move.error") == put("/file/dir/" + result, JSONUtil.toJSON(fileVO), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()

        // 移动文件夹 subUpload 到 父目录中
        FileVO fileVO2 = JSONUtil.parse(get("/file/" + result2 + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)
        assert fileVO2.getFileName() == "subUpload"
        fileVO2.setVirPath("com/")
        put("/file/dir/" + result2, JSONUtil.toJSON(fileVO2), HttpStatus.OK)

        fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/", PEPConstants.DEFAULT_CHARSET.toString()), HttpStatus.OK).getResponse().getContentAsString(), Collection.class)
        assert 3 == fileVOs.size()
    }

    @Test
    void deleteDirTest() {
        def data = [:]
        data['path'] = 'com/'
        data['fileName'] = 'upload'
        String result = post("/file/dir", JSONUtil.toJSON(data), HttpStatus.CREATED).getResponse().getContentAsString()
        assert result != null

        Resource[] resources = AntResourceUtil.getResources('classpath*:com/proper/enterprise/platform/file/test/upload/测试上传文件.png')
        String filResult = mockMvc.perform(
            MockMvcRequestBuilders
                .fileUpload("/file?path=" + URLEncoder.encode("com/upload/", PEPConstants.DEFAULT_CHARSET.toString()))
                .file(
                new MockMultipartFile("file", "测试上传文件.png", ",multipart/form-data", resources[0].inputStream)
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString()
        assert filResult != null

        def data2 = [:]
        data2['fileName'] = 'subUpload'
        data2['path'] = 'com/upload/'
        String result2 = post("/file/dir", JSONUtil.toJSON(data2), HttpStatus.CREATED).getResponse().getContentAsString()
        assert result2 != null

        FileVO fileVO = JSONUtil.parse(get("/file/" + result + "/meta", HttpStatus.OK).getResponse().getContentAsString(), FileVO.class)
        assert fileVO.getFileName() == "upload"

        delete("/file/dir?ids=" + result, HttpStatus.NO_CONTENT)

        def fileVOs = JSONUtil.parse(get("/file/dir?path=" + URLEncoder.encode("com/upload/", PEPConstants.DEFAULT_CHARSET.toString()), HttpStatus.OK).getResponse().getContentAsString(), Collection.class)
        assert 0 == fileVOs.size()

        assert '' == get("/file/" + filResult + "/meta", HttpStatus.OK).getResponse().getContentAsString()
    }
}
