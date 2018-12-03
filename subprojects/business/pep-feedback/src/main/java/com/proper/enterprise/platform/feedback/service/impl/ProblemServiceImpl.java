package com.proper.enterprise.platform.feedback.service.impl;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
import com.proper.enterprise.platform.feedback.entity.RecordEntity;
import com.proper.enterprise.platform.feedback.repository.ProblemRepository;
import com.proper.enterprise.platform.feedback.repository.RecordRepository;
import com.proper.enterprise.platform.feedback.service.ProblemService;
import com.proper.enterprise.platform.feedback.vo.ProblemVo;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {

    private static final String PROBLEM_ACCESS = "ihos_problem_access";
    private static final String ACCESS_YES = "1";
    private static final String ACCESS_NO = "2";


    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    RecordRepository recordRepository;

    @Override
    public List<ProblemEntity> findByCategoryId(String categoryId) {
        List<ProblemEntity> list;
        if (StringUtil.isEmpty(categoryId)) {
            list = problemRepository.findByEnableOrderByCreateTimeDesc(true);
        } else {
            list = problemRepository.findByEnableAndCategoryIdOrderByCreateTimeDesc(true, categoryId);
        }

        return list;
    }

    @Override
    public void updateProblem(String name, String answer, String id) {
        ProblemEntity problem = findProblemById(id);
        problem.setName(name);
        problem.setAnswer(answer);
        problemRepository.save(problem);
    }

    @Override
    public void addProblem(String name, String answer, String categoryId) {
        ProblemEntity problemEntity = new ProblemEntity();
        problemEntity.setAnswer(answer);
        problemEntity.setCategoryId(categoryId);
        problemEntity.setName(name);
        problemEntity.setAwesome(0);
        problemEntity.setTread(0);
        problemEntity.setViews(0);
        problemRepository.save(problemEntity);
    }

    @Override
    public void delProblem(String problemId) {
        problemRepository.deleteProblem(problemId);
    }

    @Override
    public void delAllProblems(String problemIds) {
        String[] ids = problemIds.split("\\,");
        for (int i = 0; i < ids.length; i++) {
            problemRepository.deleteProblem(ids[i]);
        }
    }

    @Override
    public List<ProblemEntity> getPopular(PageRequest pageRequest) {
        Page<ProblemEntity> pages = problemRepository.findPopulars(pageRequest);
        return pages.getContent();
    }

    @Override
    public ProblemVo saveProblemInfo(String problemId, String deviceId) {
        ProblemEntity problemEntity = findProblemById(problemId);
        ProblemVo problemVo = new ProblemVo();
        BeanUtil.copyProperties(problemEntity, problemVo);
        problemVo.setAwesome(String.valueOf(problemEntity.getAwesome()));
        problemVo.setTread(String.valueOf(problemEntity.getTread()));
        problemVo.setViews(String.valueOf(problemEntity.getViews()));
        RecordEntity recordEntity = recordRepository.findByProblemIdAndDeviceId(problemId, deviceId);

        if (recordEntity != null) {
            problemVo.setAssess(recordEntity.getAssess());

        }

        int count = problemEntity.getViews() + 1;
        problemEntity.setViews(count);
        problemRepository.save(problemEntity);
        return problemVo;
    }



    @Override
    public ProblemVo saveAccessProblem(String problemId, String deviceId, String code) {

        RecordEntity record = recordRepository.findByProblemIdAndDeviceId(problemId, deviceId);
        ProblemEntity problem = findProblemById(problemId);
        int count = problem.getAwesome();
        int reCount = problem.getTread();
        //当前设备没有对问题评价过
        if (record == null) {
            RecordEntity recordEntity = new RecordEntity();
            //记录当前设备对问题的评价
            recordEntity.setDeviceId(deviceId);
            recordEntity.setProblemId(problemId);
            recordEntity.setAssess(code);
            recordRepository.save(recordEntity);

            //赞
            if (DataDicUtil.get(PROBLEM_ACCESS, ACCESS_YES).getCode().equals(code)) {
                count += 1;
            }
            //踩
            if (DataDicUtil.get(PROBLEM_ACCESS, ACCESS_NO).getCode().equals(code)) {
                reCount += 1;
            }


        } else {
            if (DataDicUtil.get(PROBLEM_ACCESS, ACCESS_YES).getCode().equals(code)) {

                if (!record.getAssess().equals(code)) {
                    count += 1;
                    reCount -= 1;
                    //更新对问题的评价
                    record.setAssess(code);
                    recordRepository.save(record);
                } else {
                    //取消赞
                    count -= 1;
                    recordRepository.deleteById(record.getId());
                }
            } else {
                if (!record.getAssess().equals(code)) {
                    reCount += 1;
                    count -= 1;
                    //更新对问题的评价
                    record.setAssess(code);
                    recordRepository.save(record);
                } else {
                    //取消踩
                    reCount -= 1;
                    recordRepository.deleteById(record.getId());
                }
            }


        }

        problem.setAwesome(count);
        problem.setTread(reCount);
        problemRepository.save(problem);

        ProblemVo problemVo = new ProblemVo();
        BeanUtil.copyProperties(problem, problemVo);
        problemVo.setAwesome(String.valueOf(problem.getAwesome()));
        problemVo.setTread(String.valueOf(problem.getTread()));
        problemVo.setViews(String.valueOf(problem.getViews()));
        RecordEntity recordEntity = recordRepository.findByProblemIdAndDeviceId(problemId, deviceId);
        if (recordEntity != null) {
            problemVo.setAssess(recordEntity.getAssess());

        } else {
            problemVo.setAssess("");
        }
        return problemVo;
    }

    @Override
    public ProblemEntity findProblemById(String problemId) {
        return problemRepository.findById(problemId)
                                .orElseThrow(() -> new ErrMsgException("Could NOT find entity with id " + problemId));
    }

}
