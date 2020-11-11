package cn.com.hf.contller;

import cn.com.hf.contller.utils.HttpUtils;
import cn.com.hf.contller.utils.MailUtil;
import cn.com.hf.contller.utils.QRCodeUtil;
import cn.com.hf.verify.config.VerifyConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.jsoup.Connection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName QRCodeController
 * @Description
 * @Author wangtao
 * @Date 2020/5/11 16:53
 */
@Controller
public class QRCodeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/qrcode")
    public String qrcode(ModelMap mode, HttpServletRequest request) {
        String session_loginUser = (String) request.getSession().getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
        if (StringUtils.isEmpty(session_loginUser)) {
            mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, "您还未登录,请先登录!");
            return "index";
        }
        return "qrcode";
    }

    @RequestMapping("/resolve")
    @ResponseBody
    public String resolve(@RequestParam final MultipartFile qrcodeFile) throws Exception{
        //            String path = "C:/Users/wt/Desktop/file";
//        String path = "/home/llin/VerifySystem_8816/uploadFile/";
//        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath() +"/static/tempUpload/";
        String path = new ApplicationHome(getClass()).getSource().getParentFile().toString()+"/uploadFile";
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String name = qrcodeFile.getOriginalFilename();
        String[] names = name.split("/");
        name = names[names.length - 1];
        //创建文件夹
        File file1 = new File(path + "/" + fileName);
        try {
            file1.mkdirs();
            //写文件
            file1 = new File(path + "/" + fileName + "/" + name);
            file1.createNewFile();
            qrcodeFile.transferTo(file1);

            //获取二维码连接
//            String str = QRCodeUtil.qrCodeDecode(file1);
            String str = QRCodeUtil.getQrcode(file1);
            logger.info("二维码解析结果：" + str);
            if (StringUtils.isBlank(str)) {
                return "非立招二维码";
            }
            if (str.indexOf("http") >= 0 && str.indexOf(".eycard.") < 0 && str.indexOf(".mpay.") < 0) {//短地址需要解析
                String url = "https://v1.alapi.cn/api/url/query?url=" + str;
                Response response = HttpUtils.get(url);
                String result = response.body();
                logger.info("长地址解析结果=====>" + result);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject json = JSON.parseObject(result);
                    String code = json.getString("code");
                    if ("200".equals(code)) {
                        JSONObject data = json.getJSONObject("data");
                        if (data != null) {
                            str = data.getString("long_url");
                        }
                    }
                }
            }
            logger.info("立招入口=====>" + str);
            if (str.indexOf("?") < 0 || (str.indexOf(".eycard.") < 0 && str.indexOf(".mpay.") < 0)) {
                return "非立招二维码";
            }
            //获取参数
            String param = str.substring(str.indexOf("?") + 1, str.length());
            logger.info("参数：" + param);
            Map<String, String> paramMap = QRCodeUtil.string2Map(param);
            //清除缓存地址
            String url = "https://mobile.eycard.cn/qr2?";
            boolean flag = false;
            //判断立招入口
            String uri = str.substring(0, str.indexOf("?"));
            String lastname = uri.substring(uri.lastIndexOf("/") + 1);
            logger.info("uri=====>" + uri);
            logger.info("lastname=====>" + lastname);
            if ("qr".equals(lastname)) {
                String qc = paramMap.get("qc");
                if (StringUtils.isNotBlank(qc)) {
                    flag = true;
                    url += "qc=" + qc;
                }
            } else if ("guideapp".equals(lastname)) {
                String md = paramMap.get("md");
                String cd = paramMap.get("cd");
                if (StringUtils.isNotBlank(md) && StringUtils.isNotBlank(cd)) {
                    flag = true;
                    url += "merid=" + md + "&termid=" + cd;
                }
            } else if ("guidemark".equals(lastname)) {
                String md = paramMap.get("md");
                String cd = paramMap.get("cd");
                if (StringUtils.isNotBlank(md) && StringUtils.isNotBlank(cd)) {
                    flag = true;
                    url += "md=" + md + "&cd=" + cd;
                }
            } else if ("minicup".equals(lastname) || "normcup".equals(lastname)) {
                String qrCode = paramMap.get("qrCode");
                if (StringUtils.isNotBlank(qrCode)) {
                    flag = true;
                    qrCode = URLDecoder.decode(qrCode, "UTF-8");
                    String md = qrCode.substring(qrCode.lastIndexOf("/") + 1);
                    url += "md=" + md + "&cd=";
                }
            } else {
                return "非立招二维码";
            }
            //清除缓存
            if (flag) {
                logger.info("清除缓存=====>" + url);
                HttpUtils.get(url);
            }

        } catch (Exception e) {
            logger.error("系统异常", e);
            return "失败";
        }
        //删除图片文件
        file1.delete();
        //删除文件夹
        File file2 = new File(path + "/" + fileName);
        file2.delete();
        return "成功";
    }

    @RequestMapping("/originUrl")
    public String originUrl(ModelMap mode, HttpServletRequest request) {
//        String session_loginUser = (String) request.getSession().getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
//        if (StringUtils.isEmpty(session_loginUser)) {
//            mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, "您还未登录,请先登录!");
//            return "index";
//        }
        return "originUrl";
    }

    @RequestMapping("/getUrl")
    @ResponseBody
    public String getUrl(@RequestParam("toAddress") final String toAddress, @RequestParam("file") final MultipartFile[] file) throws Exception{
//        String path = "C:/Users/wt/Desktop/file";
//        String path = "/home/llin/VerifySystem_8816/uploadFile/";
//        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath() +"/static/tempUpload/";
        String path = new ApplicationHome(getClass()).getSource().getParentFile().toString() +"/uploadFile";
        String fileName = UUID.randomUUID().toString().replace("-", "");
        logger.info("path===>" + path + "/" + fileName);
        //上传文件到服务器
        for (MultipartFile f : file) {
            //获取文件名
            String name = "";
            name = f.getOriginalFilename();
            String[] names = name.split("/");
            name = names[names.length - 1];
            File file1 = new File(path + "/" + fileName + "/" + name);
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();// 新建文件夹
            }
            try {
                file1.createNewFile();
                f.transferTo(file1);
            } catch (Exception e) {
                logger.error("出现异常", e);
                e.printStackTrace();
//                return;
                return "文件上传失败，请重试";
            }
        }

        new Thread() {
            @Override
            public void run() {
                //创建excel
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet1 = null;
                sheet1 = wb.createSheet("解析结果");
//        sheet1.setDefaultColumnWidth(20);// 默认列宽
                // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
                HSSFRow row1 = sheet1.createRow((int) 0);
                // 第四步，创建单元格，并设置值表头 设置表头居中
                HSSFCellStyle style = wb.createCellStyle();
                // 创建一个居中格式
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                // 添加excel title
                HSSFCell cell1 = null;
                String[] excelnames1 = {"图片名称", "解析地址", "文件类型"};
                for (int i = 0; i < excelnames1.length; i++) {
                    cell1 = row1.createCell(i);
                    cell1.setCellValue(excelnames1[i]);
                    cell1.setCellStyle(style);
                }
                sheet1.setColumnWidth(0, 20 * 256);
                sheet1.setColumnWidth(1, 40 * 256);
                sheet1.setColumnWidth(2, 40 * 256);

                String str = "";
                JSONArray json = new JSONArray();

                File picfile = new File(path + "/" + fileName);
                File[] fs = picfile.listFiles();
                logger.info("==开始遍历解析图片==");
                for (File f : fs) {
                    //创建行
                    HSSFRow row = sheet1.createRow(sheet1.getLastRowNum() + 1);
                    //遍历File[]数组
                    if (!f.isDirectory()) {
                        //若非目录(即文件)，则打印
                        try {
                            String url = QRCodeUtil.getQrcode(f);
                            String name = f.getName().substring(0, f.getName().indexOf("."));
                            String filetype = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                            row.createCell(0).setCellValue(name);
                            row.createCell(1).setCellValue(url);
                            row.createCell(2).setCellValue(filetype);
                            str += "<span>" + name + ":" + url + ":" + filetype + "</span><br>";
                            //解析结束删除文件
                        } catch (Exception e) {
                            logger.error(f.getName() + "解析错误");
                        }
                        f.delete();
                    }
                }

                try {
                    //生成excel文件
                    FileOutputStream fout = new FileOutputStream(path + "/" + fileName + "/payCodeUrl.xls");
                    wb.write(fout);
                    fout.flush();
                    fout.close();

                    //发送邮件
                    MailUtil.send(toAddress, path + "/" + fileName + "/payCodeUrl.xls",
                            "立招二维码解析地址", "您好，您之前解析立招二维码的结果已返回，解析结果excel文件已在附件中，请查收，无需回复！",
                            "二维码解析结果.xls");

                    File urlfile = new File(path + "/" + fileName + "/payCodeUrl.xls");// 删除服务器的excel
                    if (urlfile.exists()) {
                        urlfile.delete();
                    }

                } catch (Exception e) {
                    logger.error("系统异常", e);
                }

                //删除文件夹
                picfile.delete();
            }//run
        }.start();
        return "文件已上传，稍后将会邮件通知您结果";
    }//geturl
}