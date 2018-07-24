package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.PEPConstants
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.LocalDate
import spock.lang.Specification


class DateUtilSpec extends Specification {

    def "Check utils in DateUtil"() {
        given:
        Date d = new DateTime(2016, 6, 15, 15, 31, 24).toDate()
        String ds = '2016-06-15'
        String ts = '15:31:24'

        expect:
        DateUtil.getCurrentYear() == new Date().format('YYYY').toInteger()
        DateUtil.getTimestamp().substring(0, 13) == new Date().format(PEPConstants.DEFAULT_DATETIME_FORMAT).substring(0, 13)
        DateUtil.toDate(ds) == new LocalDate(2016, 6, 15).toDate()
        DateUtil.toDateTime("$ds $ts") == new DateTime(2016, 6, 15, 15, 31, 24).toDate()
        DateUtil.toDateString(d) == ds
        DateUtil.toTimestamp(d) == "$ds $ts".toString()
        DateUtil.safeClone(d) == d
        DateUtil.safeClone(null) == null
    }

    def "To date"() {
        expect:
        DateUtil.toDate('19870412', 'yyyyMMdd') == new Date(87, 3, 12)
        DateUtil.toDate('19870502', 'yyyyMMdd') == new Date(87, 4, 2)
    }

    // 更多对日期的操作可以直接使用 joda time 提供的方法

    def "Get #year year #month month 1st date: #result"() {
        expect:
        result.equals new LocalDate(year, month, 1).toString(PEPConstants.DEFAULT_DATE_FORMAT)

        where:
        year | month | result
        2012 | 12    | '2012-12-01'
    }

    def "Get #year year #month month last date: #result"() {
        expect:
        result.equals new LocalDate(year, month, 1).dayOfMonth().withMaximumValue().toString(PEPConstants.DEFAULT_DATE_FORMAT)

        where:
        year | month | result
        2012 | 12    | '2012-12-31'
        2013 | 2     | '2013-02-28'
    }

    def "The margin between #start and #end is #result"() {
        expect:
        result == new Interval(DateUtil.toDate(start).getTime(), DateUtil.toDate(end).getTime()).toPeriod().years

        where:
        start        | end          | result
        '1993-10-01' | '2013-09-13' | 19
        '2010-01-01' | '2013-09-13' | 3
        '2010-01-15' | '2013-09-13' | 3
        '1993-10-15' | '2013-09-13' | 19
        '1993-10-15' | '2013-10-15' | 20
        '1993-10-15' | '2013-09-15' | 19
        '2013-9-15'  | '2013-10-15' | 0
    }

    def "Timestamp with millisecond is unique"() {
        given:
        def timestamp = DateUtil.getTimestamp(true)
        sleep(1)

        expect:
        timestamp != DateUtil.getTimestamp(true)
    }

    def "Check addDay in DateUtil"() {
        given:
        Date d = new DateTime(2016, 6, 15, 15, 31, 24).toDate()

        expect:
        DateUtil.addDay(DateUtil.addDay(d, 2), -2) == d
        DateUtil.addDay(DateUtil.addDay(d, 2), -1) != d
        DateUtil.addDay(d, 0) == d
        DateUtil.addDay(null, 0) == null
        DateUtil.addDay(d, 5) == new DateTime(2016, 6, 20, 15, 31, 24).toDate()
    }

    def "Check addMinute in DateUtil"() {
        given:
        Date d = new DateTime(2016, 6, 15, 15, 31, 24).toDate()

        expect:
        DateUtil.addMinute(DateUtil.addMinute(d, 100), -100) == d
        DateUtil.addMinute(DateUtil.addMinute(d, 100), -99) != d
        DateUtil.addMinute(d, 0) == d
        DateUtil.addMinute(null, 0) == null
        DateUtil.addMinute(d, 5) == new DateTime(2016, 6, 15, 15, 36, 24).toDate()
    }

    def "Check parseSpecial"() {
        expect:
        assert "2018-07-25 09:42:17" == DateUtil.toString(DateUtil.parseGMTSpecial("2018-07-25T01:42:17.582Z"), PEPConstants.DEFAULT_DATETIME_FORMAT)
    }

}
