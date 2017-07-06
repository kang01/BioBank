package org.fwoxford.service;

import org.fwoxford.BioBankApp;
import org.fwoxford.service.util.BarcodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by zhuyu on 2017-06-28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
@Transactional
public class BarcodeServiceTest {
    private final Logger log = LoggerFactory.getLogger(ReportExportingServiceIntTest.class);

    @Test
    public void testDecodeDataMatrixImage() {
        try {
            File dir = new File(".");
            System.out.println(dir.getCanonicalPath());
            System.out.println(dir.getAbsolutePath());

            String dataUrl = "data:image/jpeg;base64,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";

            String result = BarcodeUtil.decode(dataUrl);

            assertThat(result).isEqualToIgnoringCase("LV1003378238");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDecodeDataMatrixArrayImage() {
        try {
            File dir = new File(".");
            System.out.println(dir.getCanonicalPath());
            System.out.println(dir.getAbsolutePath());

            String path = dir.getCanonicalPath();
            dir = new File("./temp");
            if (!dir.exists()){
                dir.mkdir();
            }

            File imageFile = new File(path + "/96_8x12_tube_code_white.jpg");
            FileInputStream fis = new FileInputStream(imageFile);
            BufferedImage image = ImageIO.read(fis);
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            int rows = imageWidth < imageHeight ? 12 : 8;
            int cols = imageWidth < imageHeight ? 8 : 12;
            int chunks = rows * cols;

            int chunkWidth = image.getWidth() / cols;
            int chunkHeight = image.getHeight() / rows;

            int count = 0;
            BufferedImage imgs[] = new BufferedImage[chunks];
            String[][] codes = new String[rows][cols];
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    //设置小图的大小和类型
                    imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                    //写入图像内容
                    Graphics2D gr = imgs[count].createGraphics();
                    gr.drawImage(image, 0, 0,
                        chunkWidth, chunkHeight,
                        chunkWidth* y, chunkHeight * x,
                        chunkWidth * y + chunkWidth,
                        chunkHeight * x + chunkHeight, null);
                    gr.dispose();


                    BufferedImage biggerImg = autoRotate(imgs[count]);

                    ImageIO.write(biggerImg, "jpg", new File(path+"/temp/img_"+x+"_"+y+".jpg"));
                    ImageIO.write(imgs[count], "jpg", new File(path+"/temp/img_"+x+"_"+y+"_o.jpg"));
                    int nRetryTimes = 7;
                    String code = BarcodeUtil.decode(imgs[count]);
                    if (code == null){
                        code = BarcodeUtil.decode(biggerImg);
                    }
                    if (code == null){
                        biggerImg = zoomOutImage(biggerImg, 2);
                        code = BarcodeUtil.decode(biggerImg);
                    }
//                    BufferedImage newImage = rotate(imgs[count], 90);
                    while (code == null && nRetryTimes > 0){
//                        newImage = rotate(newImage, 90);
//                        biggerImg = autoRotate(biggerImg);
                        biggerImg = rotate(biggerImg, 90);
                        code = BarcodeUtil.decode(biggerImg);
                        --nRetryTimes;
                    }
                    if (nRetryTimes == 0){
                        code = "------------";
                    }

                    System.out.print(code + ", ");
                    codes[x][y] = code;
                    count++;
                }
                System.out.println();
            }

            //log.info("codes", codes);
            assertThat(codes).isNotEmpty();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDecodeDataMatrixArrayImage2() {
        try {
            File dir = new File(".");
            System.out.println(dir.getCanonicalPath());
            System.out.println(dir.getAbsolutePath());

            String path = dir.getCanonicalPath();
            dir = new File("./temp");
            if (!dir.exists()){
                dir.mkdir();
            }

            File imageFile = new File(path + "/96_8x12_tube_code_white.jpg");
            FileInputStream fis = new FileInputStream(imageFile);
            BufferedImage image = ImageIO.read(fis);
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            int rows = imageWidth < imageHeight ? 12 : 8;
            int cols = imageWidth < imageHeight ? 8 : 12;
            int chunks = rows * cols;

            int chunkWidth = image.getWidth() / cols;
            int chunkHeight = image.getHeight() / rows;

            int count = 0;
            BufferedImage imgs[] = new BufferedImage[chunks];
            String[][] codes = new String[rows][cols];
            for (int x = 0; x < rows; x++) {
                for (int y = 0; y < cols; y++) {
                    //设置小图的大小和类型
                    imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                    //写入图像内容
                    Graphics2D g2d = imgs[count].createGraphics();
//                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(image, 0, 0,
                        chunkWidth, chunkHeight,
                        chunkWidth* y, chunkHeight * x,
                        chunkWidth * y + chunkWidth,
                        chunkHeight * x + chunkHeight, null);
                    g2d.dispose();

                    BufferedImage codeImage = imgs[count];
                    ImageIO.write(codeImage, "jpg", new File(path+"/temp/img_"+x+"_"+y+"_o.jpg"));


                    String code = BarcodeUtil.decode(codeImage);

                    if (code == null){
                        int nRetryTimes = 7;
                        while (code == null && nRetryTimes > 0){
                            BufferedImage newImage = rotate(codeImage, 45 * nRetryTimes);
                            code = BarcodeUtil.decode(newImage);
                            --nRetryTimes;
                        }
                    }
                    if (code == null){
                        int cx = codeImage.getWidth() / 2;
                        int cy = codeImage.getHeight() / 2;
                        int minScope = 45;
                        int minRadius = 24;

                        int[] colors =  new int[minScope];
                        colors = codeImage.getRGB(cx, cy, minScope, 1, colors, 0, minScope);
                        Integer[] arrColors = Arrays.stream(colors).boxed().toArray( Integer[]::new );
                        int codeColor = Collections.min(Arrays.asList(arrColors));

                        colors = codeImage.getRGB(cx, cy, 1, minScope, colors, 0, 1);
                        arrColors = Arrays.stream(colors).boxed().toArray( Integer[]::new );
                        codeColor = Math.min(codeColor, Collections.min(Arrays.asList(arrColors)));

                        int colorDistance = 100;

                        double angle = 0;
                        int offsetX = 0;
                        int offsetY = 0;
                        boolean isCorrectAngle = false;
                        while (!isCorrectAngle){
                            int tempX = cx;
                            int tempY = cy - minScope;
                            int tempW = 2;
                            int tempH = 2;
                            int startY = 0;
                            while(!isCorrectAngle && tempX < cx + minScope){

                                int countOfColor = 0;
                                for(int j = cy - minScope; j <= cy + minScope; j++){
                                    int color1 = codeImage.getRGB(tempX, j);
                                    int color2 = codeImage.getRGB(tempX+1, j);
                                    if (color1 == 0 || color2 == 0){
                                        continue;
                                    }

                                    if (colorComp(color1, codeColor, colorDistance) || colorComp(color2, codeColor, colorDistance)){
                                        startY = startY == 0 ? j : startY;
                                        tempY = j;
                                        countOfColor++;
                                    } else {
                                        countOfColor = 0;
                                    }

                                    if (countOfColor >= 1.9*minRadius){
                                        int countOfColor1 = 0;
                                        int countOfColor2 = 0;
                                        for (int i = cx - minScope; i < cx + minScope; ++i){
                                            color1 = codeImage.getRGB(i, startY);
                                            int color12 = codeImage.getRGB(i, startY+1);
                                            int color13 = codeImage.getRGB(i, startY+2);
                                            color2 = codeImage.getRGB(i, tempY);
                                            int color22 = codeImage.getRGB(i, tempY-1);
                                            int color23 = codeImage.getRGB(i, tempY-2);

                                            if (color1 == 0 || color2 == 0 || color12 == 0 || color22 == 0 || color13 == 0 || color23 == 0){
                                                continue;
                                            }

                                            countOfColor1 = colorComp(color1, codeColor, colorDistance) || colorComp(color12, codeColor, colorDistance) || colorComp(color13, codeColor, colorDistance) ? 1+countOfColor1 : 0;
                                            countOfColor2 = colorComp(color2, codeColor, colorDistance) || colorComp(color22, codeColor, colorDistance) || colorComp(color23, codeColor, colorDistance) ? 1+countOfColor2 : 0;

                                            if (countOfColor1 >= 1.8*minRadius || countOfColor2 >= 1.8*minRadius){
                                                if (i > tempX){
                                                    break;
                                                }
                                                isCorrectAngle = true;
                                                break;
                                            }
                                        }
                                        if (isCorrectAngle){
                                            offsetX = tempX - cx - minRadius;
                                            offsetY = tempY - cy - minRadius;
                                            break;
                                        }
                                    }
                                }

                                tempX += 1;
                            }

                            if (isCorrectAngle){
                                BufferedImage tempImage = new BufferedImage(imgs[count].getWidth(), imgs[count].getHeight(), imgs[count].getType());
                                g2d = tempImage.createGraphics();
                                g2d.rotate(angle, cx, cy);
//                                g2d.translate(-offsetX, -offsetY);
                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2d.drawImage(imgs[count], 0,0, null);
                                g2d.dispose();

                                int tcx = tempImage.getWidth() / 2;
                                int tcy = tempImage.getHeight() / 2;
                                int tx2 = 0;
                                int ty2 = 0;
                                for (int i = tcx + minScope - 1; i > tcx; --i){
                                    int color1 = tempImage.getRGB(i, cy);
                                    if (colorComp(color1, codeColor, colorDistance)){
                                        if (i > tempX){
                                            continue;
                                        }
                                        tx2 = i;
                                        break;
                                    }
                                }

                                if (tx2 != 0){
                                    for (int i = tcy + minScope - 1; i > tcy; --i){
                                        int color1 = tempImage.getRGB(tx2, i);
                                        int color2 = tempImage.getRGB(tx2-1, i);
                                        int color3 = tempImage.getRGB(tx2-2, i);
                                        if (colorComp(color1, codeColor, colorDistance) || colorComp(color2, codeColor, colorDistance) || colorComp(color3, codeColor, colorDistance)){
                                            if (i > tempY){
                                                continue;
                                            }
                                            ty2 = i;
                                            break;
                                        }
                                    }
                                }
                                int tox = tx2 - tcx - minRadius;
                                int toy = ty2 - tcy - minRadius;

                                int tx1 = tx2 - 2*minRadius - 5;
                                int ty1 = ty2 - 2*minRadius - 5;
                                tx2 += 5;
                                ty2 += 5;
                                int tw = tx2 - tx1;
                                int th = ty2 - ty1;

                                BufferedImage tempImage1 = new BufferedImage(tempImage.getWidth(), tempImage.getHeight(), tempImage.getType());
                                g2d = tempImage1.createGraphics();
                                g2d.translate(-tox, -toy);
                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2d.drawImage(tempImage, 0,0, null);
                                g2d.dispose();


//                                minScope = 30;
                                int dx1 = 0;
                                int dy1 = 0;
                                int dx2 = 2 * (minScope - 15);
                                int dy2 = 2 * (minScope - 15);
                                int sx1 = tempImage1.getWidth() / 2 - (minScope - 10);
                                int sy1 = tempImage1.getHeight() / 2 - (minScope - 10);
                                int sx2 = sx1 + 2 * (minScope - 10);
                                int sy2 = sy1 + 2 * (minScope - 10);
                                codeImage = new BufferedImage(dx2-dx1, dx2-dx1, tempImage1.getType());
                                g2d = codeImage.createGraphics();
//                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2d.setBackground(Color.white);
                                g2d.fillRect(0,0,dx2-dx1,dy2-dy1);
                                g2d.drawImage(tempImage1, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, Color.white, null);
//                                g2d.drawImage(tempImage, dx1, dy1, dx1+tw, dy1+th, tx1, ty1, tx2, ty2, Color.white, null);
                                g2d.dispose();

                                ImageIO.write(codeImage, "jpg", new File(path+"/temp/img_"+x+"_"+y+".jpg"));
                                break;
                            }

                            int ox = codeImage.getWidth() / 2;
                            int oy = codeImage.getHeight() / 2;
                            angle += Math.PI / 180 / 2;
                            BufferedImage tempImage = new BufferedImage(codeImage.getWidth(), codeImage.getHeight(), codeImage.getType());
                            g2d = tempImage.createGraphics();
//                            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                            g2d.rotate(angle, ox, oy);
                            g2d.drawImage(imgs[count], 0,0, null);
                            g2d.dispose();
                            codeImage = tempImage;

                            if (angle > Math.PI * 2.5){
                                System.out.println("x: "+x+"; y: "+y);
                                break;
                            }
                        }
                        code = BarcodeUtil.decode(codeImage);
                        if (code == null){
                            BufferedImage newImage = zoomOutImage(codeImage, 2);
                            code = BarcodeUtil.decode(newImage);
                        }
                        if (code == null){
                            int nRetryTimes = 4;
                            codeImage = zoomOutImage(codeImage, 2);
                            while (code == null && nRetryTimes > 0){
                                BufferedImage newImage = rotate(codeImage, 90 * nRetryTimes);
                                code = BarcodeUtil.decode(newImage);
                                --nRetryTimes;
                            }
                        }
                    }
                    if (code == null){
                        code = "------------";
                    }

                    codes[x][y] = code;
                    count++;
                }
                System.out.println();
            }

            for (int i = 0; i < codes.length; ++i){
                for(int j = 0; j < codes[i].length; ++j){
                    System.out.print(codes[i][j] + ", ");
                }
                System.out.println();
            }
            //log.info("codes", codes);
            assertThat(codes).isNotEmpty();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDecodeDataMatrixArrayImage3(){
        File dir = new File(".");
        try {
            System.out.println(dir.getCanonicalPath());

            System.out.println(dir.getAbsolutePath());

            String path = dir.getCanonicalPath();
            dir = new File("./temp");
            if (!dir.exists()){
                dir.mkdir();
            }

            File imageFile = new File(path + "/96_8x12_tube_code_white.jpg");
            FileInputStream fis = new FileInputStream(imageFile);
            BufferedImage image = ImageIO.read(fis);

            System.out.println(new Date());
            String[][] codes = BarcodeUtil.decode8x12Box(image);
            System.out.println(new Date());

            for (int i = 0; i < codes.length; ++i){
                for(int j = 0; j < codes[i].length; ++j){
                    if (codes[i][j] == null){
                        codes[i][j] = "------------";
                    }
                    System.out.print(codes[i][j] + ", ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage autoRotate(BufferedImage src){
        BufferedImage biggerImg = src;
        int chunkWidth = src.getWidth();
        int chunkHeight = src.getHeight();
        int cx = biggerImg.getWidth() / 2;
        int cy = biggerImg.getHeight() / 2;
        int[] colors = new int[biggerImg.getWidth()];
        colors = biggerImg.getRGB(0, cy, biggerImg.getWidth(), 1, colors, 0, 1);
        Integer[] arrColors = Arrays.stream(colors).boxed().toArray( Integer[]::new );
        int minColor = Collections.min(Arrays.asList(arrColors));

        biggerImg = cropImage(biggerImg, cx - 45, cy - 45, cx + 45, cy + 45);

        cx = biggerImg.getWidth() / 2;
        cy = biggerImg.getHeight() / 2;
        ArrayList<Integer> arrEndPos = new ArrayList<>();
        ArrayList<Integer> arrColorPos = new ArrayList<>();

        for (int j = cy; j < biggerImg.getHeight(); ++j) {
            for(int i = cx; i < biggerImg.getWidth(); ++i) {
                int previousColorIndex = arrColorPos.size() == 0 ? cy : arrColorPos.get(arrColorPos.size() - 1);
                int currentColor = biggerImg.getRGB(i, j);
                if (colorComp(currentColor, minColor, 100)) {
                    arrColorPos.add(i);
                } else if (!arrColorPos.isEmpty() && i - previousColorIndex > 20){
                    break;
                }
            }

            if (arrColorPos.isEmpty()){
                break;
            }

            arrEndPos.add(Collections.max(arrColorPos));
            arrColorPos.clear();
        }

        if (!arrEndPos.isEmpty()){
            int pos = Collections.max(arrEndPos);
            int index = arrEndPos.indexOf(pos);
            Point p1 = new Point(pos, cy + index);
            Point p2 = new Point(arrEndPos.get(arrEndPos.size()-1), cy + arrEndPos.size());

            double angle = Math.atan2(p2.x-p1.x, p2.y-p1.y);
            double angleD = 180*angle/Math.PI;

//            biggerImg = new BufferedImage(chunkWidth,chunkHeight,src.getType());
            int offsetY = index;
            int offsetX = pos - cx - 26;

            cx = src.getWidth() / 2; cy = src.getHeight() / 2;
            AffineTransform tr = new AffineTransform();
            tr.rotate(angle, cx,cy);
            tr.translate(-offsetX, -offsetY);

            Graphics2D g = (Graphics2D) src.getGraphics();
            g.transform(tr);
            g.dispose();

            biggerImg = new BufferedImage(biggerImg.getWidth(),biggerImg.getHeight(),biggerImg.getType());
            g = biggerImg.createGraphics();
            tr = new AffineTransform();
            tr.rotate(angle, biggerImg.getWidth()/2,biggerImg.getHeight()/2);
            tr.translate(-offsetX, -offsetY);
            g.transform(tr);
            g.drawImage(src,0,0, biggerImg.getWidth(),biggerImg.getHeight(), cx - 30, cy - 30, cx + 30, cy+30, Color.white, null);
            g.dispose();

            int r = 40;
//            biggerImg = cropImage(biggerImg, cx-r, cy-r,cx+r, cy+r);

            for(int i = 0; i < biggerImg.getWidth(); ++i) {
                for (int j = 0; j < biggerImg.getHeight(); ++j) {
                    int currentColor = biggerImg.getRGB(i, j);
                    if (!colorComp(currentColor, minColor, 200)) {
                        biggerImg.setRGB(i, j, Color.white.getRGB());
                    }
                }
            }
        }

        return biggerImg;
    }

    public static Rectangle calcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90) {
            if(angel / 90 % 2 == 1){
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

    public static BufferedImage rotate(BufferedImage src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(
            src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
//        res = new BufferedImage(src_width, src_height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();

        if (rect_des.width > src_width){
            // transform
            g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        }

        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }

    /**
     * 对图片进行放大
     * @param originalImage 原始图片
     * @param times 放大倍数
     * @return
     */
    public static BufferedImage  zoomOutImage(BufferedImage  originalImage, Integer times){
        int width = originalImage.getWidth()*times;
        int height = originalImage.getHeight()*times;
        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0,0,width,height,null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage cropImage(BufferedImage bufferedImage, int startX, int startY, int endX, int endY) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (startX == -1) {
            startX = 0;
        }
        if (startY == -1) {
            startY = 0;
        }
        if (endX == -1) {
            endX = width - 1;
        }
        if (endY == -1) {
            endY = height - 1;
        }
        BufferedImage result = new BufferedImage(endX - startX, endY - startY, bufferedImage.getType());
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                int rgb = bufferedImage.getRGB(x, y);
                result.setRGB(x - startX, y - startY, rgb);
            }
        }
        return result;
    }

    private static boolean colorComp(int rbg1, int rgb2, int distance) {
        //设置阀值,小于Distance就可认定像素a和像素b相似
        if (distance == 0){
            distance = 150;
        }

        Color color1 = new Color(rbg1);
        Color color2 = new Color(rgb2);

        //通过HSV比较两个子RGB的色差
        //比较两个RGB的色差
        int absR=color1.getRed() - color2.getRed();
        int absG=color1.getGreen() - color2.getGreen();
        int absB=color1.getBlue() - color2.getBlue();

        return Math.sqrt(absR*absR+absG*absG+absB*absB) < distance;
    }

    /**
     * 先按最小宽高为size等比例绽放, 然后图像居中抠出半径为radius的圆形图像
     *
     * @param img
     * @param size 要缩放到的尺寸
     * @param radius 圆角半径
     * @param type 1:高度与宽度的最大值为maxSize进行等比缩放 , 2:高度与宽度的最小值为maxSize进行等比缩放
     * @return
     */
    private static BufferedImage getRoundedImage(BufferedImage img, int size, int radius, int type) {

        BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();

        //先按最小宽高为size等比例绽放, 然后图像居中抠出直径为size的圆形图像
        Image fixedImg = getScaledImage(img, size, type);
        g.drawImage(fixedImg, (size - fixedImg.getWidth(null)) / 2, (size - fixedImg.getHeight(null)) / 2, null);//在适当的位置画出

        //圆角
        if (radius > 0) {
            RoundRectangle2D round = new RoundRectangle2D.Double(0, 0, size, size, radius * 2, radius * 2);
            Area clear = new Area(new Rectangle(0, 0, size, size));
            clear.subtract(new Area(round));
            g.setComposite(AlphaComposite.Clear);

            //抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fill(clear);
            g.dispose();
        }
        return result;
    }
    /**
     * 针对高度与宽度进行等比缩放
     *
     * @param img
     * @param maxSize 要缩放到的尺寸
     * @param type 1:高度与宽度的最大值为maxSize进行等比缩放 , 2:高度与宽度的最小值为maxSize进行等比缩放
     * @return
     */
    private static Image getScaledImage(BufferedImage img, int maxSize, int type) {
        int w0 = img.getWidth();
        int h0 = img.getHeight();
        int w = w0;
        int h = h0;
        if (type == 1) {
            w = w0 > h0 ? maxSize : (maxSize * w0 / h0);
            h = w0 > h0 ? (maxSize * h0 / w0) : maxSize;
        } else if (type == 2) {
            w = w0 > h0 ? (maxSize * w0 / h0) : maxSize;
            h = w0 > h0 ? maxSize : (maxSize * h0 / w0);
        }
        Image image = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);//在适当的位置画出
        return result;
    }
}
