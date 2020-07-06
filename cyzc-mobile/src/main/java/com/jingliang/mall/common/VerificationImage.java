package com.jingliang.mall.common;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片验证码类
 *
 * @author Zhenfeng Li
 * @date 2020-03-19 10:05:21
 */
public class VerificationImage {
    /**
     * 小图长
     */
    static int targetHeight = 50;
    /**
     * 小图宽
     */
    static int targetWidth = 40;
    /**
     * 半径
     */
    static int circleR = 6;
    /**
     * 距离点
     */
    static int r1 = 3;


    /**
     * @return
     */
    private static int[][] getBlockData() {

        int[][] data = new int[targetHeight][targetWidth];
        double x2 = targetHeight - circleR;

        //随机生成圆的位置
        double h1 = circleR + Math.random() * (targetWidth - 3 * circleR - r1);
        double po = circleR * circleR;

        double xbegin = targetHeight - circleR - r1;
        double ybegin = targetWidth - circleR - r1;

        for (int i = 0; i < targetHeight; i++) {
            for (int j = 0; j < targetWidth; j++) {
                double d3 = Math.pow(i - x2, 2) + Math.pow(j - h1, 2);
                double d2 = Math.pow(j - 2, 2) + Math.pow(i - h1, 2);
                if ((j <= ybegin && d2 <= po) || (i >= xbegin && d3 >= po)) {
                    data[i][j] = 0;
                } else {
                    data[i][j] = 1;
                }

            }
        }
        return data;
    }

    /**
     * @param oriImage
     * @param targetImage
     * @param templateImage
     * @param x
     * @param y
     */
    private static void cutByTemplate(BufferedImage oriImage, BufferedImage targetImage, int[][] templateImage, int x, int y) {
        for (int i = 0; i < targetHeight; i++) {
            for (int j = 0; j < targetWidth; j++) {
                int rgb = templateImage[i][j];
                // 原图中对应位置变色处理
                AtomicInteger rgbOri = new AtomicInteger(oriImage.getRGB(x + i, y + j));

                if (rgb == 1) {
                    //抠图上复制对应颜色值
                    targetImage.setRGB(i, j, rgbOri.get());
                    //原图对应位置颜色变化
                    oriImage.setRGB(x + i, y + j, rgbOri.get() & 0x363636);
                } else {
                    //这里把背景设为透明
                    targetImage.setRGB(i, j, rgbOri.get() & 0x00ffffff);
                }
            }
        }
    }

    /**
     * @param y
     * @return
     */
    public static Map<String, String> createImage(int y) {
        Map<String, String> resultMap = new HashMap<>(5);
        try {
            List<String> imgPats = Arrays.asList("8240a880b981a6d8695a11148476bf2.png");
            Random random = new Random();
            int nextInt = random.nextInt(imgPats.size());
            ClassPathResource classPathResource = new ClassPathResource("img/" + imgPats.get(nextInt));
            BufferedImage bufferedImage = ImageIO.read(classPathResource.getInputStream());
            int width = bufferedImage.getWidth() -110;
            random = new Random();
            int x = 55 + random.nextInt(width);
            BufferedImage target = new BufferedImage(targetHeight, targetWidth, BufferedImage.TYPE_4BYTE_ABGR);
            cutByTemplate(bufferedImage, target, getBlockData(), x, y);
            //大图
            resultMap.put("img_b", "data:image/png;base64," + getImageBase64(bufferedImage));
            //小图
            resultMap.put("img_m", "data:image/png;base64," + getImageBase64(target));
            resultMap.put("x", x + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * @param image
     * @return
     * @throws IOException
     */
    public static String getImageBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        //转成byte数组
        byte[] b = out.toByteArray();
        //生成base64编码
        return Base64.getEncoder().encodeToString(b);
    }
}
