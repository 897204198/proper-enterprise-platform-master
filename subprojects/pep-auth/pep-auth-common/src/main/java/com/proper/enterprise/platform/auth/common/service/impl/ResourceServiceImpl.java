package com.proper.enterprise.platform.auth.common.service.impl;

import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.service.ResourceService;
import com.proper.enterprise.platform.auth.common.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.repository.ResourceRepository;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.Iterator;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save((ResourceEntity) resource);
    }

    @Override
    public Resource get(String id) {
        return resourceRepository.findOne(id);
    }

    @Override
    public Resource get(String url, RequestMethod method) {
        StringBuffer strbuf = new StringBuffer();
        strbuf = strbuf.append(method.toString()).append(":").append(url);
        return getBestMatch(find(), strbuf.toString());
    }

    @Override
    public void delete(Resource resource) {
        resourceRepository.delete((ResourceEntity) resource);
    }

    @Override
    public Collection<Resource> find() {
        return CollectionUtil.convert(resourceRepository.findAll());
    }

    /**
     * 思路：传参数signature与所有resources.method+resources.url
     * 字符进行 ANT 风格的路径匹配，匹配原则：符合条件的如果完全匹配直接return,无完全匹配的比较*号位置，*号越靠后越符合匹配规则，当*
     * 号位置相同时再比较整个字符长度，长度长的更符合条件。
     *
     * @param resources 资源集合
     * @param signature resources.method+":"+resources.url
     * @return 最佳匹配资源
     */
    public Resource getBestMatch(Collection<Resource> resources, String signature) {

        Iterator<Resource> it = resources.iterator();
        Resource returnres = null;
        AntPathMatcher matcher = new AntPathMatcher();

        while (it.hasNext()) {
            Resource resource = it.next();
            StringBuffer strbuf = new StringBuffer();
            strbuf = strbuf.append(resource.getMethod().toString()).append(":").append(resource.getURL());

            if (matcher.match(strbuf.toString(), signature)) {
                // 如果直接可以匹配上（无*） 直接返回
                if (strbuf.indexOf("*") < 0) {
                    return resource;
                }
                // 把符合条件的第一条记录赋值给返回值
                if (returnres == null) {
                    returnres = resource;
                } else {

                    StringBuffer oldstrbuf = new StringBuffer();
                    oldstrbuf = oldstrbuf.append(resource.getMethod().toString()).append(":")
                            .append(returnres.getURL());
                    // 比较本次值与返回值*的位置 *位置越靠后，越符合匹配值
                    if (strbuf.indexOf("*") >= oldstrbuf.indexOf("*")) {
                        // 特殊情况 如果* 位置相同 把长度更长的赋值给返回值
                        if (strbuf.indexOf("*") == oldstrbuf.indexOf("*")) {
                            if (strbuf.length() > oldstrbuf.length()) {
                                returnres = resource;
                            }
                        } else {
                            returnres = resource;
                        }
                    }
                }
            }
        }

        return returnres;
    }


}
