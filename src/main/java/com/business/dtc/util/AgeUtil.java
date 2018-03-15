package com.business.dtc.util;

import net.sf.rose.util.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: xudy
 * @date: 2018/03/15 14:35
 * @description:
 */
public class AgeUtil {

    public static boolean checkAgeWithBirthday(int age, Date birthday){
        if(age==AgeUtil.getAgeBirthday(birthday)){
            return true;
        }
        return false;
    }

    public static int getAgeBirthday(Date birthday){
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 根据身份证获取年龄
     * @param idcard
     * @return
     */
    public static int getAgeCredit(String idcard){
        // 获取出生年月日
        String birthday = idcard.substring(6, 14);
        Date birthdate = null;
        try {
            birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
            return getAgeBirthday(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,1991);
        calendar.set(Calendar.MONTH,11);
        calendar.set(Calendar.DAY_OF_MONTH,6);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        System.out.println(DateFormat.format(calendar.getTime(),DateFormat.DATE_FORMAT_3));
        System.out.println(AgeUtil.getAgeBirthday(calendar.getTime()));
        System.out.println(AgeUtil.getAgeCredit("320623199112068417"));
    }
}