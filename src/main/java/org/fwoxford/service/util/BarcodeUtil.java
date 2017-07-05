package org.fwoxford.service.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


/**
 * Created by zhuyu on 2017-06-28.
 */
public class BarcodeUtil {
    private static final String CHARSET = "utf-8";

    /**
     * 禁止生成实例，生成实例也没有意义。
     */
    private BarcodeUtil() {
    }

    /**
     * 解码，需要javase包。
     * 文件方式解码
     *
     * @param file
     * @return
     */
    public static String decode(File file) {
        BufferedImage image;
        try {
            if (file == null || file.exists() == false) {
                throw new Exception(" File not found:" + file.getPath());
            }
            image = ImageIO.read(file);
            return decodeRawImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 流方式解码
     *
     * @param input
     * @return
     */
    public static String decode(InputStream input) {

        BufferedImage image;
        try {
            if (input == null) {
                throw new Exception(" input is null");
            }

            image = ImageIO.read(input);
            return decodeRawImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * URL方式解码
     *
     * @param url
     * @return
     */
    public static String decode(String url) {

        try {

            if (url == null || url.length() == 0) {
                throw new Exception(" input is null");
            }

            URI objUri = URI.create(url);
            if (url.startsWith("data:image/")) {
                String encodingPrefix = "base64,";
                int contentStartIndex = url.indexOf(encodingPrefix) + encodingPrefix.length();
                byte[] imageData = Base64.decodeBase64(url.substring(contentStartIndex));
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

//                BufferedImage image = ImageReader.readDataURIImage(objUri);
                return decodeRawImage(image);
            }

            URL objUrl = objUri.toURL();
            HttpURLConnection httpConn = (HttpURLConnection) objUrl.openConnection();
            httpConn.connect();
            InputStream input = httpConn.getInputStream();

            return decode(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片方式解码
     *
     * @param image
     * @return
     */
    public static String decodeRawImage(BufferedImage image) throws Exception {

        if (image == null) {
            throw new Exception(" image is null");
        }

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result;

        List<BarcodeFormat> formats = new ArrayList<>();
//        formats.add(BarcodeFormat.CODE_39);
//        formats.add(BarcodeFormat.CODE_128);
//        formats.add(BarcodeFormat.EAN_13);
//        formats.add(BarcodeFormat.PDF_417);
//        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.DATA_MATRIX);

        // 解码设置编码方式为：utf-8，
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);

        result = new MultiFormatReader().decode(bitmap, hints);

        return result.getText();
    }


    /**
     * 图片方式解码
     *
     * @param image
     * @return
     */
    public static String decode(BufferedImage image) {
        try {
            return decodeRawImage(image);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

}
