package com.proper.enterprise.platform.oopsearch.sync.mysql.cluster

import org.springframework.stereotype.Service

@Service
class BusinessService {

    int count = 0

    void firstBlood() {
        count ++
    }

    void doubleKill() {
        count += 2
    }

    void tripleKill() {
        count += 3
    }

    def getCount() {
        count
    }

}
