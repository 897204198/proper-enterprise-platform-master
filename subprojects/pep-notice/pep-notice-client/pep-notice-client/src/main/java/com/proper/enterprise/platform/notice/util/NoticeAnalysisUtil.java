package com.proper.enterprise.platform.notice.util;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.notice.document.NoticeDocument;
import com.proper.enterprise.platform.notice.entity.PushDeviceEntity;
import com.proper.enterprise.platform.notice.enums.AnalysisResult;

import java.util.*;

public class NoticeAnalysisUtil {

    private static final String UNEXPECTED_URL = "unexpected url";

    public static boolean isNecessaryResult(NoticeDocument noticeDocument) {
        if (noticeDocument.getAnalysisResult() == null) {
            return false;
        }
        if (noticeDocument.getAnalysisResult().equals(AnalysisResult.UNNECESSARY)) {
            return true;
        }
        return false;
    }

    public static void isUsersExist(NoticeDocument noticeDocument) {
        if (noticeDocument.getUsers() == null || noticeDocument.getUsers().size() == 0) {
            noticeDocument.setAnalysisResult(AnalysisResult.UNNECESSARY);
            noticeDocument.setNotes("All users Entered is not exist.");
        }
    }

    public static void isThereATarget(NoticeDocument noticeDocument) {
        if (isNecessaryResult(noticeDocument)) {
            return;
        }
        if (noticeDocument.getTargets() == null || noticeDocument.getTargets().size() == 0) {
            noticeDocument.setAnalysisResult(AnalysisResult.UNNECESSARY);
            noticeDocument.setNotes("Can't find effective notice receivers.");
        }
    }

    public static void isUsersMoreThanTargets(NoticeDocument noticeDocument, Collection<? extends User> targets) {
        if (isNecessaryResult(noticeDocument)) {
            return;
        }
        if (noticeDocument.getUsers().size() > noticeDocument.getTargets().size()) {
            noticeDocument.setAnalysisResult(AnalysisResult.PARTLY);
            Map<String, User> userMap = convertToUserMap(targets);
            Set<String> users = noticeDocument.getUsers();
            Set<String> targetIds = userMap.keySet();
            Set<String> result = differenceSet(users, targetIds);
            for (String id : result) {
                User user = userMap.get(id);
                if (user == null) {
                    noticeDocument.setNotes("'%s' does not exist.", id);
                } else {
                    noticeDocument.setNotes("%s does not config %s notice.", user.getName(), noticeDocument.getNoticeType().name());
                }
            }
        }
    }

    public static boolean isDeviceInfoOk(NoticeDocument noticeDocument, User user, PushDeviceEntity pushDeviceEntity) {
        boolean result = true;
        if (pushDeviceEntity == null) {
            result = false;
            noticeDocument.setAnalysisResult(AnalysisResult.PARTLY);
            noticeDocument.setNotes("%s is missing device info, please re login to the app.", user.getName());
        } else {
            if (pushDeviceEntity.getPushToken() == null) {
                result = false;
                noticeDocument.setAnalysisResult(AnalysisResult.PARTLY);
                noticeDocument.setNotes("%s is missing push token, please re login to the app.", user.getName());
            }
            if (pushDeviceEntity.getDevicetype() == null) {
                result = false;
                noticeDocument.setAnalysisResult(AnalysisResult.PARTLY);
                noticeDocument.setNotes("%s is missing device type, please re login to the app.", user.getName());
            }
            if (pushDeviceEntity.getPushMode() == null) {
                result = false;
                noticeDocument.setAnalysisResult(AnalysisResult.PARTLY);
                noticeDocument.setNotes("%s is missing push mode, please re login to the app.", user.getName());
            }
        }
        return result;
    }

    public static void accessNoticeServer(NoticeDocument noticeDocument, String exception, String serverUrl) {
        noticeDocument.setException(exception);
        if (exception.contains(UNEXPECTED_URL)) {
            noticeDocument.setAnalysisResult(AnalysisResult.ERROR);
            noticeDocument.setNotes("The notice server url '%s' configuration error. ", serverUrl);
        }
    }

    private static Map<String, User> convertToUserMap(Collection<? extends User> users) {
        Map<String, User> result = new HashMap<>(0);
        for (User user : users) {
            result.put(user.getId(), user);
        }
        return result;
    }

    private static Set<String> differenceSet(Set<String> source, Set<String> target) {
        Set<String> result = new HashSet<>();
        result.addAll(source);
        result.removeAll(target);
        return result;
    }

}
