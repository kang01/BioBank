package org.fwoxford.web.rest.util;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
}
