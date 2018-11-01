package com.proper.enterprise.platform.announcement.repository;

import com.proper.enterprise.platform.announcement.entity.AnnouncementEntity;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnnouncementRepository extends BaseRepository<AnnouncementEntity, String> {

    /**
     * 按公告通知类型，获得当前有效的,状态为true的公告信息列表，按生效日期由近至远排序
     *
     * @param  infoType 公告通知类型编码
     * @param  nowTime  当前时间
     * @return 公告信息实体集合
     */
    @Query(value = "select t "
            + "from AnnouncementEntity t "
            + "where t.infoType = ?1 "
            + "and t.beginTime <= ?2 "
            + "and (t.endTime >= ?2 or t.endTime = '' or t.endTime is null) "
            + "and t.infoStatus = true "
            + "order by t.beginTime desc")
    List<AnnouncementEntity> findLatestValidNoticesByInfoType(String infoType, String nowTime);

    /**
     * 通过公告通知类型查询并对标题信息和开始时间进行模糊查询，按照开始时间的倒序排序
     * @param infoType 公告通知类型编码
     * @param title 标题信息
     * @return 公告信息的集合
     */
    List<AnnouncementEntity> findByInfoTypeAndTitleLikeOrderByBeginTimeDesc(String infoType, String title);

    /**
     * 通过公告通知类型查询并对标题信息和开始时间进行模糊查询，按照开始时间的倒序排序
     * @param infoType 公告通知类型编码
     * @param title 标题信息
     * @param pageable 分页
     * @return 公告信息集合的分页
     */
    Page<AnnouncementEntity> findByInfoTypeAndTitleLikeOrderByBeginTimeDesc(String infoType, String title, Pageable pageable);
}