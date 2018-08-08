package com.proper.enterprise.platform.core.jpa.curd.a.service;

import com.proper.enterprise.platform.core.jpa.curd.a.api.A;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;

import java.util.Collection;

public interface AService extends BaseJpaService<A, String> {

    /**
     * 保存
     *
     * @param a a
     * @return A
     */
    A save(A a);

    /**
     * 添加
     *
     * @param aid aid
     * @param bid bid
     * @return A
     */
    A addB(String aid, String bid);

    /**
     * 添加
     *
     * @param aid aid
     * @param cid cid
     * @return A
     */
    A addC(String aid, String cid);

    /**
     * 用B查找所有
     *
     * @return 返回集合
     */
    Collection<A> findAllWithB();

    /**
     * 测试只读事务套可写事务
     * @param b B
     * @return B
     */

    B txReadOnly(B b);


}
