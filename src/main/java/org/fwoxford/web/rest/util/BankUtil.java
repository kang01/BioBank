package org.fwoxford.web.rest.util;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gengluying on 2017/3/31.
 */
public class BankUtil {
    public static String getUniqueID() {
        String time = "";
        String timeServerUrl = "ntp5.aliyun.com";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        Date date = new Date();
        try {
            NTPUDPClient client = new NTPUDPClient();
            client.setDefaultTimeout(500);//设置超时
            InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);
            TimeInfo timeInfo = client.getTime(timeServerAddress);
            TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();
            date = timeStamp.getDate();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        time = dateFormat.format(date).toString();
        time = String.valueOf(date.getTime());
        return time;
    }

    //由出生日期获得年龄
    public static int getAge(ZonedDateTime birthDay) throws Exception {
        Date date = Date.from(birthDay.toInstant());
        Calendar cal = Calendar.getInstance();

        if (cal.before(date)) {
            throw new IllegalArgumentException(
                "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }
}
