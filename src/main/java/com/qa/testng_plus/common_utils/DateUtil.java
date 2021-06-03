package com.qa.testng_plus.common_utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
    public static final long MILLIS_PER_HOUR = 3600000;
    public static final long MILLIS_PER_MINUTE = 60000;
    public static final String dtLongMill = "yyyy-MM-dd HH:mm:ss";
    public static final String hmsFormat = "HH:mm:ss";
    public static final String longFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String monthFormat = "yyyyMM";
    public static final String shortFormat = "yyyyMMdd";
    public static final String simple = "yyyy-MM-dd HH:mm:ss";
    public static final String webFormat = "yyyy-MM-dd";
    public static final String week = "EEEE";

    public static String getLongDateString() {
        return getNewDateFormat(longFormat).format(new Date());
    }

    public static Date addDays(Date date1, long days) {
        return addSeconds(date1, 86400 * days);
    }

    public static Date addHours(Date date, long hours) {
        return addMinutes(date, 60 * hours);
    }

    public static Date addMinutes(Date date, long minutes) {
        return addSeconds(date, 60 * minutes);
    }

    public static Date addSeconds(Date date1, long secs) {
        return new Date(date1.getTime() + (1000 * secs));
    }

    public static final int calculateDecreaseDate(String beforDate, String afterDate) throws ParseException {
        return (int) (((getDateBetween(getFormat(webFormat).parse(beforDate), getFormat(webFormat).parse(afterDate)) / 1000) / 3600) / 24);
    }

    public static boolean checkDays(Date start, Date end, int days) {
        return countDays(start, end) <= days;
    }

    public static boolean checkTime(String statTime) {
        if (statTime.length() > 8) {
            return false;
        }
        String[] timeArray = statTime.split(":");
        if (timeArray.length != 3) {
            return false;
        }
        int i = 0;
        while (i < timeArray.length) {
            try {
                Integer tmpInt = new Integer(timeArray[i]);
                if (i == 0) {
                    if (tmpInt.intValue() > 23 || tmpInt.intValue() < 0) {
                        return false;
                    }
                } else if (tmpInt.intValue() > 59 || tmpInt.intValue() < 0) {
                    return false;
                }
                i++;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static String convert(String dateString, DateFormat formatIn, DateFormat formatOut) {
        try {
            return formatOut.format(formatIn.parse(dateString));
        } catch (ParseException e) {
            System.out.println("convert() --- orign date error: " + dateString);
            return StringUtils.EMPTY_STRING;
        }
    }

    public static String convert2ChineseDtFormat(String dateString) {
        return convert(dateString, getNewDateFormat(shortFormat), getNewDateFormat("yyyy年MM月dd日"));
    }

    public static String convert2WebFormat(String dateString) {
        return convert(dateString, getNewDateFormat(shortFormat), getNewDateFormat(webFormat));
    }

    public static String convertFromWebFormat(String dateString) {
        return convert(dateString, getNewDateFormat(webFormat), getNewDateFormat(shortFormat));
    }

    public static int countDays(Date dateStart, Date dateEnd) {
        if (dateStart == null || dateEnd == null) {
            return -1;
        }
        return (int) ((dateEnd.getTime() - dateStart.getTime()) / 86400000);
    }

    public static long countDays(String startDate, String endDate) {
        try {
            return (string2Date(endDate).getTime() - string2Date(startDate).getTime()) / 86400000;
        } catch (ParseException var7) {
            var7.printStackTrace();
            return 0;
        }
    }

    public static boolean dateLessThanNowAddMin(Date date, long min) {
        return addMinutes(date, min).before(new Date());
    }

    public static boolean dateNotLessThan(String date1, String date2, DateFormat format) {
        try {
            if (!format.parse(date1).before(format.parse(date2))) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            return false;
        }
    }

    public static final Long dateToNumber(Date date) {
        String month;
        String day;
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(2) + 1 >= 10) {
            month = StringUtils.EMPTY_STRING + (c.get(2) + 1);
        } else {
            month = "0" + (c.get(2) + 1);
        }
        if (c.get(5) >= 10) {
            day = StringUtils.EMPTY_STRING + c.get(5);
        } else {
            day = "0" + c.get(5);
        }
        return new Long(c.get(1) + StringUtils.EMPTY_STRING + month + day);
    }

    public static final String dtFromShortToSimpleStr(String strDate) {
        Date date;
        if (strDate != null) {
            try {
                date = shortstring2Date(strDate);
            } catch (ParseException e) {
                date = null;
            }
            if (date != null) {
                return dtSimpleFormat(date);
            }
        }
        return StringUtils.EMPTY_STRING;
    }

    public static final String dtLongMillFormat(Date date) {
        return date == null ? StringUtils.EMPTY_STRING : getFormat(dtLongMill).format(date);
    }

    public static final String dtShortSimpleFormat(Date date) {
        return date == null ? StringUtils.EMPTY_STRING : getFormat(shortFormat).format(date);
    }

    public static final String dtSimpleChineseFormat(Date date) {
        return date == null ? StringUtils.EMPTY_STRING : getFormat("yyyy年MM月dd日").format(date);
    }

    public static final String dtSimpleChineseFormatStr(String date) throws ParseException {
        return date == null ? StringUtils.EMPTY_STRING : getFormat("yyyy年MM月dd日").format(string2Date(date));
    }

    public static final String dtSimpleFormat(Date date) {
        return date == null ? StringUtils.EMPTY_STRING : getFormat(webFormat).format(date);
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatMonth(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(monthFormat).format(date);
    }

    public static String formatTimeRange(Date startDate, Date endDate, String format) {
        if (endDate == null || startDate == null) {
            return null;
        }
        long range = endDate.getTime() - startDate.getTime();
        long day = range / 86400000;
        long hour = (range % 86400000) / MILLIS_PER_HOUR;
        long minute = (range % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
        if (range < 0) {
            day = 0;
            hour = 0;
            minute = 0;
        }
        return format.replaceAll("dd", String.valueOf(day)).replaceAll("hh", String.valueOf(hour)).replaceAll("mm", String.valueOf(minute));
    }

    public static Date getBeforeDate() {
        return new Date(new Date().getTime() - 86400000);
    }

    public static String getBeforeDay(Date date) throws ParseException {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(5, -1);
        return dtSimpleFormat(cad.getTime());
    }

    public static String getBeforeDay(String StringDate) throws ParseException {
        Date tempDate = string2Date(StringDate);
        Calendar cad = Calendar.getInstance();
        cad.setTime(tempDate);
        cad.add(5, -1);
        return dtSimpleFormat(cad.getTime());
    }

    public static String getBeforeDayString(int days) {
        return getDateString(new Date(System.currentTimeMillis() - (86400000 * ((long) days))), getNewDateFormat(shortFormat));
    }

    public static String getChineseDateString(Date date) {
        return getDateString(date, getNewDateFormat("yyyy年MM月dd日"));
    }

    public static final long getDateBetween(Date dBefor, Date dAfter) {
        return dAfter.getTime() - dBefor.getTime();
    }

    public static final int getDateBetweenNow(Date dateBefore) {
        if (dateBefore == null) {
            return 0;
        }
        return (int) ((getDateBetween(dateBefore, new Date()) / 1000) / 60);
    }

    public static String getDateString(Date date) {
        return getNewDateFormat(shortFormat).format(date);
    }

    public static String getDateString(Date date, DateFormat dateFormat) {
        if (date == null || dateFormat == null) {
            return null;
        }
        return dateFormat.format(date);
    }

    public static Date getDayBegin(Date date) {
        DateFormat df = new SimpleDateFormat(shortFormat);
        df.setLenient(false);
        try {
            return df.parse(df.format(date));
        } catch (ParseException e) {
            return date;
        }
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7);
    }

    public static final String getDiffDate(Date dt, int idiff) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(5, idiff);
        return dtSimpleFormat(c.getTime());
    }

    public static final String getDiffDate(int diff) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(5, diff);
        return dtSimpleFormat(c.getTime());
    }

    public static final String getDiffDate(String srcDate, String format, int diff) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            Date source = f.parse(srcDate);
            Calendar c = Calendar.getInstance();
            c.setTime(source);
            c.add(5, diff);
            return f.format(c.getTime());
        } catch (Exception e) {
            return srcDate;
        }
    }

    public static final String getDiffDateDtShort(Date dt, int idiff) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(5, idiff);
        return dtShortSimpleFormat(c.getTime());
    }

    public static final String getDiffDateMin(Date dt, int idiff) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(5, idiff);
        return simpleFormat(c.getTime());
    }

    public static final Date getDiffDateTime(int diff) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(5, diff);
        return c.getTime();
    }

    public static final String getDiffDateTime(int diff, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(5, diff);
        c.add(10, hours);
        return dtSimpleFormat(c.getTime());
    }

    public static long getDiffDays(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 86400000;
    }

    public static long getDiffMinutes(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / MILLIS_PER_MINUTE;
    }

    public static final String getDiffMon(Date dt, int idiff) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(2, idiff);
        return dtSimpleFormat(c.getTime());
    }

    public static long getDiffSeconds(Date one, Date two) {
        Calendar sysDate = new GregorianCalendar();
        sysDate.setTime(one);
        Calendar failDate = new GregorianCalendar();
        failDate.setTime(two);
        return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
    }

    public static String getDiffStringDate(Date dt, int diff) {
        Calendar ca = Calendar.getInstance();
        if (dt == null) {
            ca.setTime(new Date());
        } else {
            ca.setTime(dt);
        }
        ca.add(5, diff);
        return dtSimpleFormat(ca.getTime());
    }

    public static String getEmailDate(Date today) {
        return new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss").format(today);
    }

    public static final DateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static Map<String, String> getLastWeek(String StringDate, int interval) throws ParseException {
        Map lastWeek = new HashMap();
        Date tempDate = shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();
        cad.setTime(tempDate);
        cad.add(5, cad.getActualMaximum(5) - 1);
        lastWeek.put("endDate", shortDate(cad.getTime()));
        cad.add(5, interval);
        lastWeek.put("startDate", shortDate(cad.getTime()));
        return lastWeek;
    }

    public static String getLongDateString(Date date) {
        return getDateString(date, new SimpleDateFormat(longFormat));
    }

    public static DateFormat getNewDateFormat(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df;
    }

    public static String getNewFormatDateString(Date date) {
        return getDateString(date, new SimpleDateFormat(simple));
    }

    public static String getNextDay(Date date) throws ParseException {
        Calendar cad = Calendar.getInstance();
        cad.setTime(date);
        cad.add(5, 1);
        return dtSimpleFormat(cad.getTime());
    }

    public static String getNextDay(String StringDate) throws ParseException {
        Date tempDate = string2Date(StringDate);
        Calendar cad = Calendar.getInstance();
        cad.setTime(tempDate);
        cad.add(5, 1);
        return dtSimpleFormat(cad.getTime());
    }

    public static Date getNextDayDtShort(String StringDate) throws ParseException {
        Date tempDate = shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();
        cad.setTime(tempDate);
        cad.add(5, 1);
        return cad.getTime();
    }

    public static String getNextDayDtShortToShort(String StringDate) throws ParseException {
        Date tempDate = shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();
        cad.setTime(tempDate);
        cad.add(5, 1);
        return dtShortSimpleFormat(cad.getTime());
    }

    public static String getNextMon(String StringDate) throws ParseException {
        Date tempDate = shortstring2Date(StringDate);
        Calendar cad = Calendar.getInstance();
        cad.setTime(tempDate);
        cad.add(2, 1);
        return shortDate(cad.getTime());
    }

    public static final String getNowDateForPageSelectAhead() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(12) < 30) {
            cal.set(12, 0);
        } else {
            cal.set(12, 30);
        }
        return simpleDate(cal.getTime());
    }

    public static final String getNowDateForPageSelectBehind() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(12) < 30) {
            cal.set(12, 30);
        } else {
            cal.set(11, cal.get(11) + 1);
            cal.set(12, 0);
        }
        return simpleDate(cal.getTime());
    }

    public static String getShortFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(5, 1);
        return getNewDateFormat(shortFormat).format(cal.getTime());
    }

    public static String getSmsDate(Date today) {
        return new SimpleDateFormat("MM月dd日HH:mm").format(today);
    }

    public static String getTimeString(Date date) {
        return getDateString(date, getNewDateFormat(hmsFormat));
    }

    public static final String getTimeWithSSS() {
        return new SimpleDateFormat("yyyyMMddhhmmssSSS").format(Calendar.getInstance().getTime());
    }

    public static String getTodayString() {
        return getDateString(new Date(), getNewDateFormat(shortFormat));
    }

    public static String getWebDateString(Date date) {
        return getDateString(date, getNewDateFormat(webFormat));
    }

    public static String getWebFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(5, 1);
        return getNewDateFormat(webFormat).format(cal.getTime());
    }

    public static String getWebNextDayString() {
        Calendar cad = Calendar.getInstance();
        cad.setTime(new Date());
        cad.add(5, 1);
        return dtSimpleFormat(cad.getTime());
    }

    public static String getWebTodayString() {
        return getNewDateFormat(webFormat).format(new Date());
    }

    public static final String getWeekDay(Date date) {
        return getFormat(week).format(date);
    }

    public static final String hmsFormat(Date date) {
        return date == null ? StringUtils.EMPTY_STRING : getFormat(hmsFormat).format(date);
    }

    public static final Date increaseDate(Date aDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.add(5, days);
        return cal.getTime();
    }

    public static boolean isBeforeNow(Date date) {
        if (date != null && date.compareTo(new Date()) < 0) {
            return true;
        }
        return false;
    }

    public static final boolean isDefaultWorkingDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week2 = calendar.get(7);
        if (week2 == 7 || week2 == 1) {
            return false;
        }
        return true;
    }

    public static final boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static boolean isValidLongDateFormat(String strDate) {
        if (strDate.length() != longFormat.length()) {
            return false;
        }
        try {
            Long.parseLong(strDate);
            try {
                getNewDateFormat(longFormat).parse(strDate);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean isValidLongDateFormat(String strDate, String delimiter) {
        return isValidLongDateFormat(strDate.replaceAll(delimiter, StringUtils.EMPTY_STRING));
    }

    public static boolean isValidShortDateFormat(String strDate) {
        if (strDate.length() != shortFormat.length()) {
            return false;
        }
        try {
            Integer.parseInt(strDate);
            try {
                getNewDateFormat(shortFormat).parse(strDate);
                return true;
            } catch (ParseException e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean isValidShortDateFormat(String strDate, String delimiter) {
        return isValidShortDateFormat(strDate.replaceAll(delimiter, StringUtils.EMPTY_STRING));
    }

    public static final String longDate(Date Date) {
        if (Date == null) {
            return null;
        }
        return getFormat(longFormat).format(Date);
    }

    public static Date now() {
        return new Date();
    }

    public static Date parseDateLongFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(longFormat);
        Date d = null;
        if (sDate != null && sDate.length() == longFormat.length()) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException e) {
                return null;
            }
        }
        Date date = d;
        return d;
    }

    public static Date parseDateNewFormat(String sDate) {
        DateFormat dateFormat = new SimpleDateFormat(simple);
        Date d = null;
        if (sDate != null && sDate.length() == simple.length()) {
            try {
                d = dateFormat.parse(sDate);
            } catch (ParseException e) {
                return null;
            }
        }
        Date date = d;
        return d;
    }

    public static Date parseDateNoTimeWithDelimit(String sDate, String delimit) throws ParseException {
        String sDate2 = sDate.replaceAll(delimit, StringUtils.EMPTY_STRING);
        DateFormat dateFormat = new SimpleDateFormat(shortFormat);
        if (sDate2 != null && sDate2.length() == shortFormat.length()) {
            return dateFormat.parse(sDate2);
        }
        throw new ParseException("length not match", 0);
    }

    public static final String shortDate(Date Date) {
        if (Date == null) {
            return null;
        }
        return getFormat(shortFormat).format(Date);
    }

    public static final Date shortstring2Date(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }
        return getFormat(shortFormat).parse(stringDate);
    }

    public static final String shortString2SimpleString(String shortString) {
        if (shortString == null) {
            return null;
        }
        try {
            return getFormat(webFormat).format(shortstring2Date(shortString));
        } catch (Exception e) {
            return null;
        }
    }

    public static final String shortStringToString(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }
        return shortDate(strToDtSimpleFormat(stringDate));
    }

    public static final String simpleDate(Date date) {
        return date == null ? StringUtils.EMPTY_STRING : getFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static final String simpleFormat(Date date) {
        return date == null ? StringUtils.EMPTY_STRING : getFormat(simple).format(date);
    }

    public static final Date simpleFormatDate(String dateString) throws ParseException {
        if (dateString == null) {
            return null;
        }
        return getFormat("yyyy-MM-dd HH:mm").parse(dateString);
    }

    public static final Date string2Date(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }
        return getFormat(webFormat).parse(stringDate);
    }

    public static Date string2Date(String str, String format) {
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static final Long string2DateLong(String stringDate) throws ParseException {
        Date d = string2Date(stringDate);
        if (d == null) {
            return null;
        }
        return new Long(d.getTime());
    }

    public static final Date string2DateTime(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }
        return getFormat(simple).parse(stringDate);
    }

    public static final Date string2DateTimeBy23(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }
        if (stringDate.length() == 11) {
            stringDate = stringDate + "23:59:59";
        } else if (stringDate.length() == 13) {
            stringDate = stringDate + ":59:59";
        } else if (stringDate.length() == 16) {
            stringDate = stringDate + ":59";
        } else if (stringDate.length() == 10) {
            stringDate = stringDate + " 23:59:59";
        }
        return getFormat(simple).parse(stringDate);
    }

    public static final Date string2DateTimeByAutoZero(String stringDate) throws ParseException {
        if (stringDate == null) {
            return null;
        }
        if (stringDate.length() == 11) {
            stringDate = stringDate + "00:00:00";
        } else if (stringDate.length() == 13) {
            stringDate = stringDate + ":00:00";
        } else if (stringDate.length() == 16) {
            stringDate = stringDate + ":00";
        } else if (stringDate.length() == 10) {
            stringDate = stringDate + " 00:00:00";
        }
        return getFormat(simple).parse(stringDate);
    }

    public static final String StringToStringDate(String stringDate) {
        if (stringDate != null && stringDate.length() == 8) {
            return stringDate.substring(0, 4) + stringDate.substring(4, 6) + stringDate.substring(6, 8);
        }
        return null;
    }

    public static final Date strToDate(String strDate) {
        return strToSimpleFormat(strDate) != null ? strToSimpleFormat(strDate) : strToDtSimpleFormat(strDate);
    }

    public static final Date strToDtSimpleFormat(String strDate) {
        if (strDate == null) {
            return null;
        }
        try {
            return getFormat(webFormat).parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static final Date strToSimpleFormat(String strDate) {
        if (strDate == null) {
            return null;
        }
        try {
            return getFormat("yyyy-MM-dd HH:mm").parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean webDateNotLessThan(String date1, String date2) {
        return dateNotLessThan(date1, date2, getNewDateFormat(webFormat));
    }
}
