package com.proper.enterprise.platform.announcement.service;

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnouncementService {
    /**
     * 通过ID查找公告信息.
     *
     * @param id 公告信息ID.
     * @return 公告信息.
     */
    AnnouncementEntity getById(String id);

    /**
     * 保存NoticeInfo.
     *
     * @param noticeInfo 公告信息.
     * @return 保存后的公告信息.
     */
    AnnouncementEntity save(AnnouncementEntity noticeInfo);

    /**
     * 添加公告信息
     * @param noticeInfo 公告信息
     * @return 添加的公告信息
     * @throws Exception 抛出异常
     */
    AnnouncementEntity addNoticeinfo(AnnouncementEntity noticeInfo) throws Exception;

    /**
     * 通过id列表查找全部公告信息.
     * @return 公告信息的列表
     */
    List<AnnouncementEntity> findAll();

    /**
     * 通过id列表查找公告信息列表.
     * @param idList id列表.
     * @return 公告信息的列表
     */
    List<AnnouncementEntity> findAllByIdList(List<String> idList);

    /**
     * 删除公告信息列表.
     *
     * @param noticeInfoList 公告信息列表.
     */
    void delete(List<AnnouncementEntity> noticeInfoList);

    /**
     * 根据ID，删除对应的信息
     * @param id id
     */
    void deleteById(String id);

    /**
     * 按公告通知类型，获得当前有效的公告信息列表，按生效日期由近至远排序
     * @param  infoType 公告通知类型编码
     * @return 公告信息实体集合
     */
    List<AnnouncementEntity> findLatestValidNoticesByInfoType(String infoType);

    /**
     * 按公告通知类型，及公告标题内容，获得所有公告信息分页列表，按生效日期由近至远排序
     *
     * @param infoType 公告信息类型.
     * @param title    公告标题
     * @param pageable 分页信息
     * @return 公告信息.
     */
    DataTrunk<AnnouncementEntity> findLatestNoticesPage(String infoType, String title, Pageable pageable);

    /**
     * 按公告通知类型，及公告标题内容，获得所有公告信息列表，按生效日期由近至远排序
     * @param infoType 公告信息类型
     * @param title 公告标题
     * @return 公告通知的集合
     */
    List<AnnouncementEntity> findLatestNotices(String infoType, String title);

    /**
     * 根据ID，修改对应的实体信息
     * @param id id
     * @param noticeInfoEntity  公告信息的entity
     * @return 公告通知
     */
    AnnouncementEntity updateNoticeInfoById(String id, AnnouncementEntity noticeInfoEntity);
}