package cn.com.hf.contller.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QrCodeUtil
 * @Description TODO
 * @Author wangtao
 * @Date 2020/5/11 16:42
 */
public class QRCodeUtil {

    public static void qrCodeEncode(String encodeddata, File destFile) throws IOException {
        Qrcode qrcode = new Qrcode();
        qrcode.setQrcodeErrorCorrect('M');  // 纠错级别（L 7%、M 15%、Q 25%、H 30%）和版本有关
        qrcode.setQrcodeEncodeMode('B');
        qrcode.setQrcodeVersion(7);     // 设置Qrcode包的版本

        byte[] d = encodeddata.getBytes("GBK"); // 字符集
        BufferedImage bi = new BufferedImage(139, 139, BufferedImage.TYPE_INT_RGB);
        // createGraphics   // 创建图层
        Graphics2D g = bi.createGraphics();

        g.setBackground(Color.WHITE);   // 设置背景颜色（白色）
        g.clearRect(0, 0, 139, 139);    // 矩形 X、Y、width、height
        g.setColor(Color.BLACK);    // 设置图像颜色（黑色）

        if (d.length > 0 && d.length < 123) {
            boolean[][] b = qrcode.calQrcode(d);
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b.length; j++) {
                    if (b[j][i]) {
                        g.fillRect(j * 3 + 2, i * 3 + 2, 3, 3);
                    }
                }
            }
        }

//        Image img = ImageIO.read(new File("D:/tt.png"));  logo
//        g.drawImage(img, 25, 55,60,50, null);

        g.dispose(); // 释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象
        bi.flush(); // 刷新此 Image 对象正在使用的所有可重构的资源

        ImageIO.write(bi, "png", destFile);
//          System.out.println("Input Encoded data is：" + encodeddata);
    }

    /**
     * 解析二维码，返回解析内容
     *
     * @param imageFile
     * @return
     */
    public static String qrCodeDecode(File imageFile) {
        String decodedData = null;
        QRCodeDecoder decoder = new QRCodeDecoder();
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            decodedData = new String(decoder.decode(new J2SEImage(image)), "GBK");
//              System.out.println("Output Decoded Data is：" + decodedData);
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedData;
    }

    static class J2SEImage implements QRCodeImage {
        BufferedImage image;

        public J2SEImage(BufferedImage image) {
            this.image = image;
        }

        public int getWidth() {
            return image.getWidth();
        }

        public int getHeight() {
            return image.getHeight();
        }

        public int getPixel(int x, int y) {
            return image.getRGB(x, y);
        }
    }

    public static String getQrcode(File file1) throws Exception {
        MultiFormatReader formatReader = new MultiFormatReader();
        Path file = file1.toPath();
        BufferedImage image = ImageIO.read(file.toFile());
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //L级：约可纠错7%的数据码字,M级：约可纠错15%的数据码字,Q级：约可纠错25%的数据码字,H级：约可纠错30%的数据码字
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        hints.put(EncodeHintType.MARGIN, 2);
//        Result result = formatReader.decode(binaryBitmap, hints);
        String result = "";

        QRCodeMultiReader qc = new QRCodeMultiReader();//一张图片有多张二维码取最后一个
        Result[] r = qc.decodeMultiple(binaryBitmap, hints);
        if (r != null && r.length > 0) {
            for (int i = 0; i < r.length; i++) {
                String resultStrTemp = r[i].getText();
                if(resultStrTemp.indexOf("https://mobile.eycard.cn") >= 0){
                    return resultStrTemp;
                }
            }
        }

        //没有复合的取最后一个
        result = r[r.length-1].getText();

//        System.out.println("二维码解析结果：" + result.toString());
//        System.out.println("二维码的格式：" + result.getBarcodeFormat());
//        System.out.println("二维码的文本内容：" + result.getText());
        return result;
    }

    public static void main(String[] args) throws Exception {
//        String filePath = "C:\\Users\\wt\\Desktop\\贾老师专属\\贾老师专属";
        String filePath = "C:\\Users\\wt\\Desktop\\test";
        File file = new File(filePath);
        File[] fs = file.listFiles();
        for (File f : fs) {
            //遍历File[]数组
            if (!f.isDirectory()) {
                //若非目录(即文件)，则打印
                String url = getQrcode(f);
                System.out.println(f.getName().substring(0, f.getName().indexOf(".")) + ": " + url);
//                System.out.println(url);
            }
        }
//        File qrFile = new File(filePath);
//        String str = getQrcode(qrFile);

    }

    public static Map<String, String> string2Map(String param) {
        Map<String, String> map = new HashMap<String, String>();
        String[] params = param.split("&");
        for (String str : params) {
            String[] strs = str.split("=");
            map.put(strs[0], strs[1]);
        }
        return map;
    }

}