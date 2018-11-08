package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class DateUtilSpec extends Specification {

    def "Check utils in DateUtil"() {
        given:
        Date d = DateUtil.toDate(LocalDateTime.of(2016, 6, 15, 15, 31, 24))

        String ds = '2016-06-15'
        String ts = '15:31:24'

        expect:
        DateUtil.getCurrentYear() == new Date().format('YYYY').toInteger()
        DateUtil.getTimestamp().substring(0, 13) == new Date().format(PEPPropertiesLoader.load(CoreProperties).getDefaultDatetimeFormat()).substring(0, 13)
        DateUtil.toTimestamp(LocalDateTime.of(2016, 6, 15, 15, 31, 24), false) == '2016-06-15 15:31:24'
        DateUtil.toDate(ds).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()==LocalDate.of(2016, 6, 15)
        LocalDateTime.ofInstant(DateUtil.toDateTime("$ds $ts").toInstant(), ZoneId.systemDefault()) == LocalDateTime.of(2016, 6, 15, 15, 31, 24)
        DateUtil.toDateString(d) == ds
        DateUtil.toLocalDateString(LocalDateTime.of(2016, 6, 15, 15, 31, 24)) == ds
        DateUtil.toLocalDate(ds) == LocalDate.of(2016,6,15)
        DateUtil.toLocalDate('2016-06-15 15:31:24', PEPPropertiesLoader.load(CoreProperties).getDefaultDatetimeFormat()) == LocalDate.of(2016, 06,15)
        DateUtil.toTimestamp(d) == "$ds $ts".toString()
        DateUtil.toLocalDateTime(d) == LocalDateTime.of(2016, 6, 15, 15, 31, 24)
        DateUtil.toLocalDateTime('2016-06-15 15:31:24') == LocalDateTime.of(2016, 6, 15, 15, 31, 24)
        DateUtil.safeClone(d) == d
        DateUtil.safeClone(null) == null
    }

    def "To date"() {
        expect:
        DateUtil.toDate('19870412', 'yyyyMMdd') == new Date(87, 3, 12)
        DateUtil.toDate('19870502', 'yyyyMMdd') == new Date(87, 4, 2)
    }

    // 更多对日期的操作可以直接使用 java time 提供的方法

    def "Get #year year #month month 1st date: #result"() {
        expect:
        result.equals LocalDate.of(year, month, 1).toString()

        where:
        year | month | result
        2012 | 12    | '2012-12-01'
    }

    def "Get #year year #month month last date: #result"() {
        expect:
        result.equals LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth()).toString()

        where:
        year | month | result
        2012 | 12    | '2012-12-31'
        2013 | 2     | '2013-02-28'
    }

    @Unroll
    def "The margin between #start and #end is #result"() {
        expect:
        result == Period.between(DateUtil.toDate(start).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                DateUtil.toDate(end).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears()

        where:
        start        | end          | result
        '1993-10-01' | '2013-09-13' | 19
        '2010-01-01' | '2013-09-13' | 3
        '2010-01-15' | '2013-09-13' | 3
        '1993-10-15' | '2013-09-13' | 19
        '1993-10-15' | '2013-10-15' | 20
        '1993-10-15' | '2013-09-15' | 19
        '2013-09-15'  | '2013-10-15' | 0
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
        Date d = DateUtil.toDate(LocalDateTime.of(2016, 6, 15, 15, 31, 24))
        LocalDateTime localDateTime = LocalDateTime.of(2016, 6, 15, 15, 31, 24)

        expect:
        DateUtil.addDay(DateUtil.addDay(d, 2), -2) == d
        DateUtil.addDay(DateUtil.addDay(d, 2), -1) != d
        DateUtil.addDay(d, 0) == d

        DateUtil.addDay(d, 5) == Date.from(LocalDateTime.of(2016, 6, 20, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.addDay(localDateTime, 0) == localDateTime
    }

    def "Check addMinute in DateUtil"() {
        given:
        Date d = DateUtil.toDate(LocalDateTime.of(2016, 6, 15, 15, 31, 24))
        LocalDateTime localDateTime = LocalDateTime.of(2016, 6, 15, 15, 31, 24)

        expect:
        DateUtil.addMinute(DateUtil.addMinute(d, 100), -100) == d
        DateUtil.addMinute(DateUtil.addMinute(d, 100), -99) != d
        DateUtil.addMinute(d, 0) == d
        DateUtil.addMinute(d, 5) == Date.from(LocalDateTime.of(2016, 6, 15, 15, 36, 24).atZone(ZoneId.systemDefault()).toInstant())

        DateUtil.addMinute(localDateTime, 0) == localDateTime
    }

    def "Check parseSpecial"() {
        expect:
        assert TimeZone.getDefault() == "GMT" || "2018-07-25 01:42:17" != DateUtil.toString(DateUtil.parseGMTSpecial("2018-07-25T01:42:17.582Z"),
                PEPPropertiesLoader.load(CoreProperties).getDefaultDatetimeFormat())
        assert "2018-07-25 01:42:17" == LocalDateTime.ofInstant(DateUtil.parseGMTSpecial("2018-07-25T01:42:17.582Z").toInstant(), ZoneId.of("GMT")).format(DateTimeFormatter.ofPattern(PEPPropertiesLoader.load(CoreProperties).getDefaultDatetimeFormat())).toString()

        assert "2018-07-25 01:42:17" == DateUtil.parseGMTSpecialToLocalDateTime("2018-07-25T01:42:17.582Z").format(DateTimeFormatter.ofPattern
                (PEPPropertiesLoader.load(CoreProperties).getDefaultDatetimeFormat()))
    }

    def "Check addWeek in DateUtil"() {
        given:
        Date d = DateUtil.toDate(LocalDateTime.of(2018, 7, 19, 15, 31, 24))
        LocalDateTime localDateTime = LocalDateTime.of(2018, 7, 19, 15, 31, 24)

        expect:
        DateUtil.addWeek(DateUtil.addWeek(d, 2), -2) == d
        DateUtil.addWeek(DateUtil.addWeek(d, 2), -1) != d
        DateUtil.addWeek(d, 0) == d
        DateUtil.addWeek(d, 1) == Date.from(LocalDateTime.of(2018, 7, 26, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.addWeek(localDateTime, 0) == localDateTime
    }

    def "Check addMonth in DateUtil"() {
        given:
        Date d = DateUtil.toDate(LocalDateTime.of(2018, 7, 19, 15, 31, 24))
        LocalDateTime localDateTime = LocalDateTime.of(2018, 7, 19, 15, 31, 24)

        expect:
        DateUtil.addMonth(DateUtil.addMonth(d, 2), -2) == d
        DateUtil.addMonth(DateUtil.addMonth(d, 2), -1) != d
        DateUtil.addMonth(d, 0) == d
        DateUtil.addMonth(localDateTime, 0) == localDateTime
    }

    def "Check getDayOfWeek in DateUtil"() {
        given:
        Date dateOfSundy = Date.from(LocalDateTime.of(2018, 7, 15, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        Date dateNotSundy = Date.from(LocalDateTime.of(2018, 7, 5, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        LocalDateTime localDateTime = LocalDateTime.of(2018, 7, 15, 15, 31, 24)

        expect:
        DateUtil.getDayOfWeek(dateOfSundy, 1) == Date.from(LocalDateTime.of(2018, 7, 9, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateOfSundy, 2) == Date.from(LocalDateTime.of(2018, 7, 10, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateOfSundy, 3) == Date.from(LocalDateTime.of(2018, 7, 11, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateOfSundy, 4) == Date.from(LocalDateTime.of(2018, 7, 12, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateOfSundy, 5) == Date.from(LocalDateTime.of(2018, 7, 13, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateOfSundy, 6) == Date.from(LocalDateTime.of(2018, 7, 14, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateOfSundy, 7) == dateOfSundy

        DateUtil.getDayOfWeek(dateNotSundy, 1) == Date.from(LocalDateTime.of(2018, 7, 2, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateNotSundy, 2) == Date.from(LocalDateTime.of(2018, 7, 3, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateNotSundy, 3) == Date.from(LocalDateTime.of(2018, 7, 4, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateNotSundy, 4) == Date.from(LocalDateTime.of(2018, 7, 5, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateNotSundy, 5) == Date.from(LocalDateTime.of(2018, 7, 6, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateNotSundy, 6) == Date.from(LocalDateTime.of(2018, 7, 7, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getDayOfWeek(dateNotSundy, 7) == Date.from(LocalDateTime.of(2018, 7, 8, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())

        DateUtil.getDayOfWeek(localDateTime, 7) == LocalDateTime.of(2018, 7, 15, 15, 31, 24)
    }


    def "Check getBeginningOfYear in DateUtil"() {
        given:
        Date d = DateUtil.toDate(LocalDateTime.of(2018, 7, 15, 15, 31, 24))
        LocalDateTime localDateTime = LocalDateTime.of(2018, 7, 15, 15, 31, 24)
        expect:
        DateUtil.getBeginningOfYear(d) == Date.from(LocalDateTime.of(2018, 1, 1, 15, 31, 24).atZone(ZoneId.systemDefault()).toInstant())
        DateUtil.getBeginningOfYear(d) != d

        DateUtil.getBeginningOfYear(localDateTime) == LocalDateTime.of(2018, 1, 1, 15, 31, 24)
    }
}
