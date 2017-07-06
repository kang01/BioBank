package org.fwoxford.service.util;

import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
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

    /**
     * 解码96孔板
     *
     * @param image
     * @return
     */
    public static String[][] decode8x12Box(BufferedImage image) {

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int rows = imageWidth < imageHeight ? 12 : 8;
        int cols = imageWidth < imageHeight ? 8 : 12;
        int chunks = rows * cols;

        int chunkWidth = image.getWidth() / cols;
        int chunkHeight = image.getHeight() / rows;

        String[][] codes = new String[rows][cols];
        try {
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    BufferedImage originalImage = cropImage(image, chunkWidth * y, chunkHeight * x, chunkWidth, chunkHeight, image.getType());
                    BufferedImage codeImage = originalImage;

//                    String path = "C:\\Users\\Temp01\\IdeaProjects\\BioBank";
//                    ImageIO.write(codeImage, "jpg", new File(path + "/temp/img_" + x + "_" + y + "_o.jpg"));

                    String code = decode(codeImage);
                    if (code == null) {
                        // 如果未能识别图像，将图像发大旋转增强图像的识别率
                        code = simpleEnhanceDecode(codeImage);
                    }
//                    code = null;
                    if (code == null) {
                        // 如果未能识别图像，将图像中的编码区域精确提取，过滤不需要的混淆区域，增加图片的识别率。
                        codeImage = advanceEnhanceDecode(codeImage);
//                        ImageIO.write(codeImage, "jpg", new File(path + "/temp/img_" + x + "_" + y + ".jpg"));

                        code = decode(codeImage);
                        if (code == null) {
                            code = simpleEnhanceDecode(codeImage);
                        }
                    }

                    codes[x][y] = code;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return codes;
    }

    private static BufferedImage advanceEnhanceDecode(BufferedImage image) {
        String code = null;

        BufferedImage codeImage = image;

        // 图像的中心点
        int cx = codeImage.getWidth() / 2;
        int cy = codeImage.getHeight() / 2;

        // 获取有效范围中的编码颜色
        int codeColor = getCodeColor(codeImage);

        // 偏移角度
        double angle = 0;
        // 偏移位置
        int offsetX = 0;
        int offsetY = 0;
        // 是否角度修正完成
        boolean isCorrectAngle = false;

        Point referenceLine = new Point();
        // 参考线Y轴值
        Integer referenceLineY = 0;
        // 参考线X轴值
        Integer referenceLineX = 0;

        while (!isCorrectAngle) {
            isCorrectAngle = checkCorrectingAngle(codeImage, codeColor, referenceLine);
            referenceLineX = (int) referenceLine.getX() + 3;
            referenceLineY = (int) referenceLine.getY() + 3;
            if (isCorrectAngle) {
                break;
            }
            angle += Math.toRadians(0.45);
            if (angle > Math.PI * 3) {
                // 超过一圈半也没有找到角度，图像有问题，不能识别
                break;
            }

            int ox = codeImage.getWidth() / 2;
            int oy = codeImage.getHeight() / 2;
            BufferedImage tempImage = new BufferedImage(codeImage.getWidth(), codeImage.getHeight(), codeImage.getType());
            Graphics2D g2d = tempImage.createGraphics();
            g2d.rotate(angle, ox, oy);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
            codeImage = tempImage;
        }

        if (!isCorrectAngle) {
            return codeImage;
        }

        codeImage = correctingImage(image, angle, referenceLineX, referenceLineY);
        return codeImage;
    }

    // 有效图像的最大识别范围，图像的编码将出现在这个范围中（cx-minScope, cy-minScope,  cx+minScope, cy+minScope)
    static int minScope = 45;
    // 真实编码的实际尺寸的半径。
    static int minRadius = 24;
    // 颜色比较的阈值
    static int colorDistance = 100;

    private static String simpleEnhanceDecode(BufferedImage image) {
        String code = null;
        // 如果未能识别图像，将图像发大旋转增强图像的识别率
        int nRetryTimes = 7;
        int angle = 45;
        BufferedImage newImage = zoomOutImage(image, 2);
        while (code == null && nRetryTimes > 0) {
            newImage = rotate(newImage, angle * nRetryTimes);
            code = BarcodeUtil.decode(newImage);
            --nRetryTimes;
        }

        return code;
    }

    private static boolean checkCorrectingAngle(BufferedImage codeImage, int codeColor, Point outReferenceLine) {
        boolean isCorrectAngle = false;
        int cx = codeImage.getWidth() / 2;
        int cy = codeImage.getHeight() / 2;

        int tempY = cy - minScope;
        int tempW = 2;
        int tempH = 2;
        int startY = 0;
        double maxYCount = 1.9 * minRadius;
        double maxXCount = 1.8 * minRadius;

        for (int tempX = cx; !isCorrectAngle && tempX < cx + minScope; tempX++) {
            // 某两列出现同样编码颜色的连续行数
            int countOfColor = 0;
            startY = 0;
            tempY = cy - minScope;
            for (int j = tempY; !isCorrectAngle && j <= cy + minScope; j++) {
                int color1 = codeImage.getRGB(tempX, j);
                int color2 = codeImage.getRGB(tempX + 1, j);
                if (color1 == 0 || color2 == 0) {
                    continue;
                }

                if (colorComp(color1, codeColor, colorDistance)
                    || colorComp(color2, codeColor, colorDistance)
                    ) {
                    // 确定有一行两列有编码的颜色
                    startY = startY == 0 ? j : startY;

                    // 记录行所在位置
                    tempY = j;
                } else {
                    startY = 0;
                    tempY = 0;
                }

                // 两列连续相同行数大于比较值，表明找到疑似Y轴参考线
                isCorrectAngle = tempY - startY >= maxYCount;
            }
//            for (int j = cy - minScope; !isCorrectAngle && j <= cy + minScope; j++) {
//                int color1 = codeImage.getRGB(tempX, j);
//                int color2 = codeImage.getRGB(tempX + 1, j);
//                if (color1 == 0 || color2 == 0) {
//                    continue;
//                }
//
//                if (colorComp(color1, codeColor, colorDistance) || colorComp(color2, codeColor, colorDistance)) {
//                    // 确定有一行两列有编码的颜色
//                    startY = startY == 0 ? j : startY;
//
//                    // 记录行所在位置
//                    tempY = j;
//                    countOfColor++;
//                } else {
//                    countOfColor = 0;
//                }
//
//                // 两列连续相同行数大于比较值，表明找到疑似Y轴参考线
//                if (countOfColor < maxYCount) {
//                    continue;
//                }
//
//                isCorrectAngle = countOfColor >= maxYCount;
//            }

            if (!isCorrectAngle){
                continue;
            }

            // 找到参考线Y的最底位置。
            int countOfOtherColor = 0;
            for (int i = tempY; i < cy + minScope; ++i){
                int color1 = codeImage.getRGB(tempX, i);

                if (!colorComp(color1, codeColor, colorDistance)){
                    countOfOtherColor++;
                } else {
                    countOfOtherColor = 0;
                }

                if (countOfOtherColor >= 5){
                    tempY = i-5;
                    break;
                }
            }

            int countOfColor1 = 0;
            int countOfColor2 = 0;
            for (int i = cx - minScope; i < cx + minScope; ++i) {
                // 获取编码区域的头三行颜色
                int color1 = codeImage.getRGB(i, startY - 1);
                int color12 = codeImage.getRGB(i, startY + 1);
                int color13 = codeImage.getRGB(i, startY);

                // 获取编码区域的底三行颜色
                int color2 = codeImage.getRGB(i, tempY + 1);
                int color22 = codeImage.getRGB(i, tempY - 1);
                int color23 = codeImage.getRGB(i, tempY);

                if (color1 == 0 || color2 == 0 || color12 == 0 || color22 == 0 || color13 == 0 || color23 == 0) {
                    continue;
                }

                countOfColor1 = colorComp(color1, codeColor, colorDistance)
                    || colorComp(color12, codeColor, colorDistance) || colorComp(color13, codeColor, colorDistance)
                    ? 1 + countOfColor1 : 0;
                countOfColor2 = colorComp(color2, codeColor, colorDistance)
                    || colorComp(color22, codeColor, colorDistance) || colorComp(color23, codeColor, colorDistance)
                    ? 1 + countOfColor2 : 0;

                // 如果头或底的相同颜色行数大于比较值，表明找到疑似X轴参考线
                if (countOfColor1 < maxXCount && countOfColor2 < maxXCount) {
                    continue;
                }

                // 如果当前的X轴参考线的最后一列与Y轴参考线不在同一个位置，表明X轴和Y轴参考线都没有找到，需要继续寻找。
                isCorrectAngle = Math.abs(i - tempX) < 5;
                if (isCorrectAngle) {
                    break;
                }
            }

            if (isCorrectAngle){
                outReferenceLine.setLocation(tempX, tempY);
            }
        }

        return isCorrectAngle;
    }

    private static int getCodeColor(BufferedImage image){
        int cx = image.getWidth() / 2;
        int cy = image.getHeight() / 2;

        // 获取有效范围中的编码颜色
        int[] colors = new int[minScope];
        colors = image.getRGB(cx, cy, minScope, 1, colors, 0, minScope);
        Integer[] arrColors = Arrays.stream(colors).boxed().toArray(Integer[]::new);
        int codeColor = Collections.min(Arrays.asList(arrColors));

        colors = image.getRGB(cx, cy, 1, minScope, colors, 0, 1);
        arrColors = Arrays.stream(colors).boxed().toArray(Integer[]::new);
        codeColor = Math.min(codeColor, Collections.min(Arrays.asList(arrColors)));

        return codeColor;
    }

    private static BufferedImage correctingImage(BufferedImage image, double angle, int referenceLineX, int referenceLineY){
        int cx = image.getWidth() / 2;
        int cy = image.getHeight() / 2;
        int codeColor = getCodeColor(image);

        BufferedImage codeImage = image;

        // 修正原图的角度
        BufferedImage tempImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = tempImage.createGraphics();
        g2d.rotate(angle, cx, cy);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // 找到位置的偏移量
        int tcx = tempImage.getWidth() / 2;
        int tcy = tempImage.getHeight() / 2;
        int tx2 = 0;
        int ty2 = 0;
        for (int i = tcx + minScope - 1; i > tcx; --i) {
            int color1 = tempImage.getRGB(i, cy);
            if (i < referenceLineX && colorComp(color1, codeColor, colorDistance)) {
                tx2 = i;
                break;
            }
        }

        if (tx2 == 0) {
            return codeImage;
        }

        for (int i = tcy + minScope - 1; i > tcy; --i) {
            int color1 = tempImage.getRGB(tx2, i);
            int color2 = tempImage.getRGB(tx2 - 1, i);
            int color3 = tempImage.getRGB(tx2 - 2, i);
            if (i < referenceLineY &&
                (colorComp(color1, codeColor, colorDistance)
                    || colorComp(color2, codeColor, colorDistance)
                    || colorComp(color3, codeColor, colorDistance))) {
                ty2 = i;
                break;
            }
        }

        if (ty2 == 0) {
            return codeImage;
        }

        int tox = tx2 - tcx - minRadius;
        int toy = ty2 - tcy - minRadius;

        int tx1 = tx2 - 2 * minRadius - 5;
        int ty1 = ty2 - 2 * minRadius - 5;
        tx2 += 5;
        ty2 += 5;
        int tw = tx2 - tx1;
        int th = ty2 - ty1;

        // 修正位置偏移
        BufferedImage tempImage1 = new BufferedImage(tempImage.getWidth(), tempImage.getHeight(), tempImage.getType());
        g2d = tempImage1.createGraphics();
        g2d.translate(-tox, -toy);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(tempImage, 0, 0, null);
        g2d.dispose();


        int sx1 = tempImage1.getWidth() / 2 - (minScope - 10);
        int sy1 = tempImage1.getHeight() / 2 - (minScope - 10);
        int sx2 = sx1 + 2 * (minScope - 10);
        int sy2 = sy1 + 2 * (minScope - 10);
        codeImage = cropImage(tempImage1, sx1, sy1, sx2 - sx1, sy2 - sy1, tempImage1.getType());

        return codeImage;
    }

    private static BufferedImage cropImage(BufferedImage src, int x, int y, int width, int height, int type) {
        BufferedImage img = new BufferedImage(width, height, type);

        //写入图像内容
        Graphics2D g2d = img.createGraphics();
//                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(src, 0, 0, width, height,
            x, y, x + width, y + height, null);
        g2d.dispose();

        return img;
    }

    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
            - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
            - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }

    private static BufferedImage rotate(BufferedImage src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(
            src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
//        res = new BufferedImage(src_width, src_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (rect_des.width > src_width) {
            // transform
            g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        }

        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
        g2.drawImage(src, null, null);
        return res;
    }

    private static BufferedImage zoomOutImage(BufferedImage originalImage, Integer times) {
        int width = originalImage.getWidth() * times;
        int height = originalImage.getHeight() * times;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    private static boolean colorComp(int rbg1, int rgb2, int distance) {
        //设置阀值,小于Distance就可认定像素a和像素b相似
        if (distance == 0) {
            distance = 150;
        }

        Color color1 = new Color(rbg1);
        Color color2 = new Color(rgb2);

        //通过HSV比较两个子RGB的色差
        //比较两个RGB的色差
        int absR = color1.getRed() - color2.getRed();
        int absG = color1.getGreen() - color2.getGreen();
        int absB = color1.getBlue() - color2.getBlue();

        return Math.sqrt(absR * absR + absG * absG + absB * absB) < distance;
    }

}
