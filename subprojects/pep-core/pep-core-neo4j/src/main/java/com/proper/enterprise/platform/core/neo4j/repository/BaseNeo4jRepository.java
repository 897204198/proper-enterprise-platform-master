package com.proper.enterprise.platform.core.neo4j.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


@NoRepositoryBean
public interface BaseNeo4jRepository<T, ID extends Serializable> extends Neo4jRepository<T, ID>, BaseRepository<T, ID> {

}
