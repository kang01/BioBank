package org.fwoxford.web.rest.util;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.SerialNo;
import org.fwoxford.domain.StockOutBoxPosition;
import org.fwoxford.repository.SerialNoRepository;
import org.fwoxford.service.impl.SerialNoServiceImpl;
import org.fwoxford.service.mapper.SerialNoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by gengluying on 2017/3/31.
 */
@Service
public class BankUtil{

    @Autowired
    private SerialNoRepository serialNoRepository;
    public static String getUniqueCODE() {
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

    public static String toPositionString(StockOutBoxPosition pos){
        if(pos ==null){
            return null;
        }
        ArrayList<String> positions = new ArrayList<>();
        if (pos.getEquipmentCode() != null && pos.getEquipmentCode().length() > 0){
            positions.add(pos.getEquipmentCode());
        }

        if (pos.getAreaCode() != null && pos.getAreaCode().length() > 0) {
            positions.add(pos.getAreaCode());
        }

        if (pos.getSupportRackCode() != null && pos.getSupportRackCode().length() > 0){
            positions.add(pos.getSupportRackCode());
        }

        if (pos.getRowsInShelf() != null && pos.getRowsInShelf().length() > 0 && pos.getColumnsInShelf() != null && pos.getColumnsInShelf().length() > 0){
            positions.add(pos.getColumnsInShelf()+pos.getRowsInShelf());
        }

        return String.join(".", positions);
    }

    public static  String getPositionString(FrozenBox frozenBox) {
        String position = "";
        if(frozenBox == null){
            return null;
        }
        ArrayList<String> positions = new ArrayList<>();
        if (frozenBox.getEquipmentCode() != null && frozenBox.getEquipmentCode().length() > 0){
            positions.add(frozenBox.getEquipmentCode());
        }

        if (frozenBox.getAreaCode() != null && frozenBox.getAreaCode().length() > 0) {
            positions.add(frozenBox.getAreaCode());
        }

        if (frozenBox.getSupportRackCode() != null && frozenBox.getSupportRackCode().length() > 0){
            positions.add(frozenBox.getSupportRackCode());
        }

        if (frozenBox.getRowsInShelf() != null && frozenBox.getRowsInShelf().length() > 0 && frozenBox.getColumnsInShelf() != null && frozenBox.getColumnsInShelf().length() > 0){
            positions.add(frozenBox.getColumnsInShelf()+frozenBox.getRowsInShelf());
        }

        return String.join(".", positions);
    }

    public static  String getPositionString(Object equipmentCode,Object areaCode,Object supportCode,Object columnsInshelf,Object rowsInshelf,Object tubeRows,Object tubeColumns) {
        String position = "";
        ArrayList<String> positions = new ArrayList<>();
        if (equipmentCode != null && equipmentCode.toString().length() > 0){
            positions.add(equipmentCode.toString());
        }

        if (areaCode != null && areaCode.toString().length() > 0) {
            positions.add(areaCode.toString());
        }

        if (supportCode != null && supportCode.toString().length() > 0){
            positions.add(supportCode.toString());
        }

        if (rowsInshelf != null && rowsInshelf.toString().length() > 0 && columnsInshelf != null && columnsInshelf.toString().length() > 0){
            positions.add(columnsInshelf.toString()+rowsInshelf.toString());
        }

        if (tubeRows != null && tubeRows.toString().toString().length() > 0 && tubeColumns != null && tubeColumns.toString().length() > 0){
            positions.add(tubeRows.toString()+tubeColumns.toString());
        }

        return String.join(".", positions);
    }

    public String getUniqueID(String flag) {
        String time = "";
        String timeServerUrl = "ntp5.aliyun.com";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
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
        String dateTime = dateFormat.format(date);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        String fourRandom = getSerialRandom(flag,localDate);
        if(flag.length()>1){
            dateTime = dateTime.substring(1,dateTime.length());
        }
        time = flag +dateTime+fourRandom;
        return time;
    }

    public  String getSerialRandom(String flag,LocalDate localDate) {
        String fourRandom = "";
        SerialNo serialNo = serialNoRepository.findlimitOneByMachineNoAndUsedDate(flag,localDate);
        int random = 0;
        if(serialNo != null){
            random = Integer.valueOf(serialNo.getSerialNo());
        }
        random = random >= 999 ? 1 : random + 1;
        String s = Integer.toString(random);
        String j = addLeftZero(s,3);
        SerialNo serial = new SerialNo().serialNo(String.valueOf(j)).machineNo(flag).usedDate(localDate).status(Constants.VALID);
        serialNoRepository.save(serial);
        return j;
    }
    public  static String addLeftZero(String s, int length) {
        int old = s.length();
        if (length > old) {
            char[] c = new char[length];
            char[] x = s.toCharArray();
            if (x.length > length) {
                throw new IllegalArgumentException(
                    "Numeric value is larger than intended length: " + s
                        + " LEN " + length);
            }
            int lim = c.length - x.length;
            for (int i = 0; i < lim; i++) {
                c[i] = '0';
            }
            System.arraycopy(x, 0, c, lim, x.length);
            return new String(c);
        }
        return s.substring(0, length);
    }

    public static String getFourRandom(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if(randLength<4){
            for(int i=1; i<=4-randLength; i++)
                fourRandom = "0" + fourRandom  ;
        }
        return fourRandom;
    }

    public String getUniqueIDByDate(String flag,Date date) {
        String time = "";
        String timeServerUrl = "ntp5.aliyun.com";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateTime = dateFormat.format(date);
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        String fourRandom = getSerialRandom(flag,localDate);
        time = flag +dateTime+fourRandom;
        return time;
    }

}
