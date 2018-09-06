package com.proper.enterprise.platform.sequence.dictionary;

import com.proper.enterprise.platform.sys.datadic.AbstractConcreteDataDic;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import org.springframework.stereotype.Service;

@Service
public class ClearType extends AbstractConcreteDataDic {

    @Override
    public String getCatalog() {
        return "CLEAR_TYPE";
    }

    public DataDic yearClear() {
        return get("YEAR_CLEAR");
    }

    public DataDic monthClear() {
        return get("MONTH_CLEAR");
    }

    public DataDic dayClear() {
        return get("DAY_CLEAR");
    }

    public DataDic noClear() {
        return get("NO_CLEAR");
    }

}
