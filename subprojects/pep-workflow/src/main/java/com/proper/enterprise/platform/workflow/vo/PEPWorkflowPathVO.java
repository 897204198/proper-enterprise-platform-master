package com.proper.enterprise.platform.workflow.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;

public class PEPWorkflowPathVO {

    private PEPStartVO start;

    private List<PEPTaskVO> currentTasks;

    private List<PEPTaskVO> hisTasks;

    public PEPStartVO getStart() {
        return start;
    }

    public void setStart(PEPStartVO start) {
        this.start = start;
    }

    public List<PEPTaskVO> getCurrentTasks() {
        return currentTasks;
    }

    public void setCurrentTasks(List<PEPTaskVO> currentTasks) {
        this.currentTasks = currentTasks;
    }

    public List<PEPTaskVO> getHisTasks() {
        return hisTasks;
    }

    public void setHisTasks(List<PEPTaskVO> hisTasks) {
        this.hisTasks = hisTasks;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
