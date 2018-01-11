package com.proper.enterprise.platform.page.custom.grid.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.page.custom.grid.document.CustomGridDocument;
import com.proper.enterprise.platform.page.custom.grid.repository.CustomGridRepository;
import com.proper.enterprise.platform.page.custom.grid.service.CustomGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomGridServiceImpl implements CustomGridService {

    @Autowired
    CustomGridRepository customGridRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public DataTrunk<CustomGridDocument> getCustomGridForPage(String title, int pageNo, int pageSize) {
        Query query = new Query();
        if (StringUtil.isNotEmpty(title)) {
            query.addCriteria(Criteria.where("title").regex(title, "i"));
        }
        query.with(new Sort(Sort.Direction.ASC, "code"));
        long count = this.mongoTemplate.count(query, CustomGridDocument.class);
        query.skip((pageNo - 1) * pageSize);
        query.limit(pageSize);
        List<CustomGridDocument> list = mongoTemplate.find(query, CustomGridDocument.class);
        DataTrunk<CustomGridDocument> page = new DataTrunk<CustomGridDocument>();
        page.setCount(count);
        page.setData(list);
        return page;
    }

    @Override
    public CustomGridDocument getCustomGridByCode(String code) {
        return customGridRepository.getByCode(code);
    }

    @Override
    public CustomGridDocument getCustomGridById(String id) {
        return customGridRepository.getById(id);
    }

    @Override
    public void saveOrUpdateCustomGrid(CustomGridDocument customGridDocument) {
        customGridRepository.save(customGridDocument);
    }

    @Override
    public void deleteCustomGridByIds(String ids) {
        for (String id : ids.split(",")) {
            customGridRepository.delete(id);
        }
    }
}
