package com.proper.enterprise.platform.cache.redis.entity

import com.proper.enterprise.platform.core.PEPVersion
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity
import org.hibernate.annotations.GenericGenerator

import javax.persistence.*

@Entity
@Table(name = "pep_test_a")
@CacheEntity
class AEntity {

    private static final long serialVersionUID = PEPVersion.VERSION

    AEntity() { }

    AEntity(String username, String password) {
        this.username = username
        this.password = password
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    String id

    @Column(unique = true, nullable = false)
    String username

    @Column(nullable = false)
    String password

    boolean valid = false

    String description

}
