package org.fwoxford.web.rest.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by gengluying on 2017/7/21.
 */
public class GeocoderReaderUtils {
    public static JSONObject readJsonFromUrl(String url, String address) throws IOException, JSONException {
        JSONObject xmlJSONObj = new JSONObject();
        if(StringUtils.isEmpty(url) || StringUtils.isEmpty(address)){

        }else{
            InputStream is = new URL(url + address).openStream();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                xmlJSONObj = XML.toJSONObject(jsonText);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }finally {
                is.close();
            }
        }
        return xmlJSONObj;
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
