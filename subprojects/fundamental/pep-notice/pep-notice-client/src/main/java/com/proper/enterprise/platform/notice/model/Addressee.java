package com.proper.enterprise.platform.notice.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Addressee {

    public Addressee() {
        addressee = new HashMap<>();
    }

    Map<String, Set<String>> addressee;

    public void add(String noticeChannel, String target) {
        Set<String> set = addressee.get(noticeChannel);
        if (set == null) {
            set = new HashSet<>(0);
        }
        set.add(target);
        addressee.put(noticeChannel, set);
    }

    public Set<String> get(String noticeChannel) {
        return addressee.get(noticeChannel);
    }
}
