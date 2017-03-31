package org.fwoxford.web.rest.util;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by gengluying on 2017/3/31.
 */
public class BankUtil {
    public static String getCurrentTime() {
        String time = "";
        try {
            NTPUDPClient client = new NTPUDPClient();
            client.setDefaultTimeout(1000);//设置超时
            String timeServerUrl = "ntp5.aliyun.com";
            InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);
            TimeInfo timeInfo = client.getTime(timeServerAddress);
            TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();
            Long a = timeStamp.getTime();
            String b = timeStamp.toUTCString();
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
            System.out.println(dateFormat.format(timeStamp.getDate()));
            time = dateFormat.format(timeStamp.getDate()).toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time;
    }
}
