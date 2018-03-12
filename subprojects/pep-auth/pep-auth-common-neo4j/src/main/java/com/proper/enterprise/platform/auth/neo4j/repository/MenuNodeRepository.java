package com.proper.enterprise.platform.auth.neo4j.repository;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.auth.neo4j.entity.MenuNodeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Collection;
import java.util.Set;

public interface MenuNodeRepository extends Neo4jRepository<MenuNodeEntity, String> {

    Collection<? extends MenuNodeEntity> findAllByIdIn(Specification specification, Sort orders);

    Collection<? extends Menu> save(Set<MenuNodeEntity> menuList);

    Collection<? extends Menu> save(Collection<MenuNodeEntity> menus);
}
