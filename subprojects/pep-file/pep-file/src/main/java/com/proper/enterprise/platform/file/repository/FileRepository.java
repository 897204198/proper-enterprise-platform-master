package com.proper.enterprise.platform.file.repository;

import com.proper.enterprise.platform.file.entity.FileEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface FileRepository extends BaseJpaRepository<FileEntity, String> {

    /**
     * 通过虚拟路径匹配该路径下的所有文件以及文件夹
     * 匹配 a/b/% 下的所有文件以及文件夹
     *
     * @param virPath 虚拟路径
     * @return 文件列表
     */
    List<FileEntity> findAllByVirPathStartingWith(String virPath);

    /**
     * 通过虚拟路径匹配该路径下的所有文件以及文件夹
     * 匹配 a/b/ 下的所有文件以及文件夹
     *
     * @param virPath 虚拟路径
     * @param sort    排序
     * @return 文件列表
     */
    List<FileEntity> findAllByVirPath(String virPath, Sort sort);

    /**
     * 通过虚拟路径,文件名匹配该路径下的文件或者文件夹
     * 匹配 a/b/ 下的文件名包含 c 的文件或者文件夹
     *
     * @param virPath  虚拟路径
     * @param fileName 文件名
     * @param sort     排序
     * @return 文件列表
     */
    List<FileEntity> findAllByVirPathAndFileNameContaining(String virPath, String fileName, Sort sort);

    /**
     * 通过虚拟路径,文件名匹配该路径下的文件或者文件夹
     * 匹配 a/b/ 下的文件名为 c 的文件或者文件夹, 以文件名重复序数倒序
     *
     * @param virPath  虚拟路径
     * @param fileName 文件名
     * @return 文件列表
     */
    List<FileEntity> findAllByVirPathAndFileNameLikeOrderByFileCountDesc(String virPath, String fileName);

    /**
     * 通过虚拟路径和文件名匹配文件或文件夹
     * 匹配 a/b/ 下文件名为 c 的文件或文件夹
     *
     * @param virPath  虚拟路径
     * @param fileName 文件名
     * @return 文件列表
     */
    FileEntity findOneByVirPathAndFileName(String virPath, String fileName);
}
