package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.PEPConstants
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
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
        assert DateTimeZone.getDefault() == "GMT" || "2018-07-25 01:42:17" != DateUtil.toString(DateUtil.parseGMTSpecial("2018-07-25T01:42:17.582Z"), PEPConstants.DEFAULT_DATETIME_FORMAT)
        assert "2018-07-25 01:42:17" == new DateTime(DateUtil.parseGMTSpecial("2018-07-25T01:42:17.582Z")
            .getTime(), DateTimeZone.forID("GMT"))
            .toString(PEPConstants.DEFAULT_DATETIME_FORMAT)
    }

    def "Check addWeek in DateUtil"() {
        given:
        Date d = new DateTime(2018, 7, 19, 15, 31, 24).toDate()

        expect:
        DateUtil.addWeek(DateUtil.addWeek(d, 2), -2) == d
        DateUtil.addWeek(DateUtil.addWeek(d, 2), -1) != d
        DateUtil.addWeek(d, 0) == d
        DateUtil.addWeek(null, 0) == null
        DateUtil.addWeek(d, 1) == new DateTime(2018, 7, 26, 15, 31, 24).toDate()
    }

    def "Check getDayOfWeek in DateUtil"() {
        given:
        Date dateOfSundy = new DateTime(2018, 7, 15, 15, 31, 24).toDate()
        Date dateNotSundy = new DateTime(2018, 7, 5, 15, 31, 24).toDate()

        expect:
        DateUtil.getDayOfWeek(dateOfSundy, 1) == new DateTime(2018, 7, 9, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateOfSundy, 2) == new DateTime(2018, 7, 10, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateOfSundy, 3) == new DateTime(2018, 7, 11, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateOfSundy, 4) == new DateTime(2018, 7, 12, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateOfSundy, 5) == new DateTime(2018, 7, 13, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateOfSundy, 6) == new DateTime(2018, 7, 14, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateOfSundy, 7) == dateOfSundy

        DateUtil.getDayOfWeek(dateNotSundy, 1) == new DateTime(2018, 7, 2, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateNotSundy, 2) == new DateTime(2018, 7, 3, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateNotSundy, 3) == new DateTime(2018, 7, 4, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateNotSundy, 4) == new DateTime(2018, 7, 5, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateNotSundy, 5) == new DateTime(2018, 7, 6, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateNotSundy, 6) == new DateTime(2018, 7, 7, 15, 31, 24).toDate()
        DateUtil.getDayOfWeek(dateNotSundy, 7) == new DateTime(2018, 7, 8, 15, 31, 24).toDate()

        DateUtil.getDayOfWeek(null, 1) == null
    }


    def "Check getBeginningOfYear in DateUtil"() {
        given:
        Date d = new DateTime(2018, 7, 15, 15, 31, 24).toDate()

        expect:
        DateUtil.getBeginningOfYear(d) == new DateTime(2018, 1, 1, 15, 31, 24).toDate()
        DateUtil.getBeginningOfYear(d) != d
        DateUtil.getBeginningOfYear(null) == null
    }
}
