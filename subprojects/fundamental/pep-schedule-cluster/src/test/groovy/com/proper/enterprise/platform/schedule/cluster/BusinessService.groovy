package com.proper.enterprise.platform.schedule.cluster

import org.springframework.stereotype.Service

@Service
class BusinessService {

    int count = 0

    void firstBlood() {
        println 'First blood'
        count ++
    }

    void doubleKill() {
        println 'Double kill'
        count += 2
    }

    void tripleKill() {
        println 'Triple kill'
        count += 3
    }

    def getCount() {
        count
    }

}
