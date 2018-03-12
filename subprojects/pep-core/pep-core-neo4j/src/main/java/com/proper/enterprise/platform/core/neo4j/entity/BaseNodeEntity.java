package com.proper.enterprise.platform.core.neo4j.entity;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.utils.DateUtil;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.Labels;

import java.util.*;

public abstract class BaseNodeEntity implements IBase {

    @GraphId(name = "graphId")
    protected Long graphId;
    @Index(unique = true, primary = true)
    protected String id = UUID.randomUUID().toString();
    @Labels
    protected List<String> labels = new ArrayList<>();
    protected String createUserId;
    protected String createTime = DateUtil.getTimestamp(true);
    protected String lastModifyUserId;
    protected String lastModifyTime = DateUtil.getTimestamp(true);
    protected boolean valid = true;

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void addLabel(String label) {
        this.getLabels().add(label);
    }

    public void deleteLabel(String label) {
        if (this.getLabels().contains(label)) {
            this.getLabels().remove(label);
        }
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
