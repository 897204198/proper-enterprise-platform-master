package com.proper.enterprise.platform.core.utils

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class DateUtilSpec extends Specification {
    
    def "Get #year year #month month 1st date: #result"() {
        expect:
        result.equals DateUtil.toDateString(DateUtil.getYearMonth1stDate(year, month))
        
        where:
        year    | month | result
        '2012'  | '12'  | '2012-12-01'
    }
    
    def "Get #year year #month month last date: #result"() {
        expect:
        result.equals DateUtil.toDateString(DateUtil.getYearMonthLastDate(year, month))
        
        where:
        year    | month | result
        '2012'  | '12'  | '2012-12-31'
        '2013'  | '2'   | '2013-02-28'
    }
    
    def "The margin between #start and #end is #result"() {
        expect:
        result == DateUtil.getYearMargin(DateUtil.toDate(start), DateUtil.toDate(end))
        
        where:
        start           | end           | result
        '1993-10-01'    | '2013-09-13'  | 19.92
        '2010-01-01'    | '2013-09-13'  | 3.67
        '2010-01-15'    | '2013-09-13'  | 3.58
        '1993-10-15'    | '2013-09-13'  | 19.83
        '1993-10-15'    | '2013-10-15'  | 20.00
        '1993-10-15'    | '2013-09-15'  | 19.92
        '2013-9-15'     | '2013-10-15'  | 0.08
    }

}
