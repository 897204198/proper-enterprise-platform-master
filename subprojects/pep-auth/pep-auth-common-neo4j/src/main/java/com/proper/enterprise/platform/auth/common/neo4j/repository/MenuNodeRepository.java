package com.proper.enterprise.platform.auth.common.neo4j.repository;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.auth.common.neo4j.entity.MenuNodeEntity;
import com.proper.enterprise.platform.core.neo4j.repository.BaseNeo4jRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.Set;

public interface MenuNodeRepository extends BaseNeo4jRepository<MenuNodeEntity, String> {

    Collection<? extends MenuNodeEntity> findAllByIdIn(Specification specification, Sort orders);

    Collection<? extends Menu> save(Set<MenuNodeEntity> menuList);

    Collection<? extends Menu> save(Collection<MenuNodeEntity> menus);

    MenuNodeEntity findByIdAndValid(String id, boolean valid);

    MenuNodeEntity findByIdAndValidAndEnable(String id, boolean valid, boolean enable);

}
