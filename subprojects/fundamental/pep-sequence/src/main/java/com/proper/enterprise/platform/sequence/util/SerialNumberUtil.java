package com.proper.enterprise.platform.sequence.util;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.TmplUtil;
import com.proper.enterprise.platform.sequence.dictionary.ClearType;
import com.proper.enterprise.platform.sequence.handler.SerialNumberHandler;
import com.proper.enterprise.platform.sequence.service.SequenceService;
import com.proper.enterprise.platform.sequence.vo.SequenceVO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生成流水号工具类
 */
public class SerialNumberUtil {

    private SerialNumberUtil() {

    }

    public static String generate(String sequenceCode) {
        SequenceService sequenceService = PEPApplicationContext.getBean(SequenceService.class);
        SequenceVO sequenceVO = sequenceService.findBySequenceCode(sequenceCode);
        Map<String, Object> formulaMap = handleFormula(sequenceVO.getFormula());
        String atomicName = handleAtomicName(sequenceVO);

        SerialNumberHandler serialNumberHandler = PEPApplicationContext.getBean(SerialNumberHandler.class);
        // 获取下一个Id
        Long sequence = serialNumberHandler.getNextID(atomicName);
        String str = String.valueOf(sequence);
        // 根据需要生成的序列号长度补齐0
        int len = str.length();
        int rest = Integer.parseInt(formulaMap.get("LENGTH").toString()) - len;
        if (rest < 0) {
            throw new ErrMsgException("serial number length is greater than " + Integer.parseInt(formulaMap.get("LENGTH").toString()));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rest; i++) {
            sb.append('0');
        }
        sb.append(str);
        formulaMap.put("SERIAL_NUMBER", sb.toString());
        return TmplUtil.resolveTmpl((String) formulaMap.get("FORMULA"), formulaMap);
    }

    public static String getCurrentSerialNumber(String sequenceCode) {
        SequenceService sequenceService = PEPApplicationContext.getBean(SequenceService.class);
        SequenceVO sequenceVO = sequenceService.findBySequenceCode(sequenceCode);
        Map<String, Object> formulaMap = handleFormula(sequenceVO.getFormula());
        String atomicName = handleAtomicName(sequenceVO);

        SerialNumberHandler serialNumberHandler = PEPApplicationContext.getBean(SerialNumberHandler.class);
        // 获取当前Id
        Long sequence = serialNumberHandler.getCurrentID(atomicName);
        String str = String.valueOf(sequence);
        // 根据需要生成的序列号长度补齐0
        int len = str.length();
        int rest = Integer.parseInt(formulaMap.get("LENGTH").toString()) - len;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rest; i++) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

    public static void setCurrentSerialNumber(String sequenceCode, String newValue) {
        SequenceService sequenceService = PEPApplicationContext.getBean(SequenceService.class);
        SequenceVO sequenceVO = sequenceService.findBySequenceCode(sequenceCode);
        String atomicName = handleAtomicName(sequenceVO);

        SerialNumberHandler serialNumberHandler = PEPApplicationContext.getBean(SerialNumberHandler.class);
        // 获取当前Id
        serialNumberHandler.setCurrentId(atomicName, Long.parseLong(newValue));
    }

    /**
     * 规则解析方法
     * 如：HT${Date:yyyyMM}${length:8} 解析后为 ["FORMULA":"HT${DATE}${SERIAL_NUMBER}","DATE","201808","LENGTH","8"]
     *
     * @param formula 规则(,分割)
     * @return 解析后Map
     */
    private static Map<String, Object> handleFormula(String formula) {
        String re = "(?<=\\$\\{).*?(?=\\})";
        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(formula);
        Map<String, Object> formulaMap = new HashMap<>(3);
        while (m.find()) {
            String context = m.group();
            if (StringUtil.isEmpty(context)) {
                continue;
            }
            String[] subContext = context.split(":");
            switch (subContext[0].toUpperCase()) {
                case "DATE" :
                    formulaMap.put("DATE", DateUtil.toString(LocalDateTime.now(), subContext[1]));
                    formula = formula.replaceAll("\\$\\{" + context + "\\}", StringUtil.isEmpty(subContext[1]) ? "" : "\\$\\{DATE\\}");
                    break;
                case "LENGTH" :
                    formulaMap.put("LENGTH", subContext[1]);
                    formula = formula.replaceAll("\\$\\{" + context + "\\}", StringUtil.isEmpty(subContext[1]) ? "" : "\\$\\{SERIAL_NUMBER\\}");
                    break;
                default:
                    formulaMap.put("FORMULA", formula);
            }
        }
        formulaMap.put("FORMULA", formula);
        return formulaMap;
    }

    /**
     * 根据流水规则信息生成流水key
     *
     * @param sequenceVO 流水规则信息
     * @return 流水key
     */
    private static String handleAtomicName(SequenceVO sequenceVO) {
        String atomicName = sequenceVO.getSequenceCode();
        String clearTypeCode = sequenceVO.getClearType().getCode();
        // 根据清零方式生成cache key, 实现清零操作
        ClearType clearType = PEPApplicationContext.getBean(ClearType.class);
        if (clearType.yearClear().getCode().equals(clearTypeCode)) {
            atomicName += DateUtil.toString(LocalDateTime.now(), PEPPropertiesLoader.load(CoreProperties.class).getDefaultYearFormat());
        } else if (clearType.monthClear().getCode().equals(clearTypeCode)) {
            atomicName += DateUtil.toString(LocalDateTime.now(), PEPPropertiesLoader.load(CoreProperties.class).getDefaultMonthFormat());
        } else if (clearType.dayClear().getCode().equals(clearTypeCode)) {
            atomicName += DateUtil.toString(LocalDateTime.now(), PEPPropertiesLoader.load(CoreProperties.class).getDefaultDateFormat());
        } else if (clearType.noClear().getCode().equals(clearTypeCode)) {
            atomicName += "";
        }
        return atomicName;
    }
}
