package com.proper.enterprise.platform.workflow.model;

import java.util.ArrayList;
import java.util.List;

public class PEPTaskCandidate {

    public PEPTaskCandidate(String type, String name) {
        this.type = type;
        this.name = name;
        this.data = new ArrayList<>();
    }

    /**
     * 候选类型
     */
    private String type;

    /**
     * 候选名称
     */
    private String name;

    /**
     * 候选集合
     */
    private List<PEPCandidateModel> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PEPCandidateModel> getData() {
        return data;
    }

    public void setData(List<PEPCandidateModel> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
