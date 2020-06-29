package cn.com.hf.contller;

import cn.com.hf.contller.utils.Authentication;
import cn.com.hf.contller.utils.MailUtil;
import cn.com.hf.verify.config.VerifyConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @ClassName CompanyController
 * @Description 企业信息查询
 * @Author wangtao
 * @Date 2020/5/11 11:29
 */
@Controller
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    private static ExecutorService exs = Executors.newFixedThreadPool(1);
    private static ExecutorService excelexs = Executors.newFixedThreadPool(1);
    private static int allNum = 3;

    @RequestMapping("/company")
    public String company(ModelMap mode, HttpServletRequest request) {
        String session_loginUser = (String) request.getSession().getAttribute(VerifyConfig.SYSTEM_SESSION_NAME);
        if (StringUtils.isEmpty(session_loginUser)) {
            mode.addAttribute(VerifyConfig.SYSTEM_LOGIN_ERRORMSG, "您还未登录,请先登录!");
            return "index";
        }
        return "company";
    }

    @RequestMapping("/success")
    public String success(ModelMap mode, HttpServletRequest request) {
        return "success";
    }

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public String upload(@RequestParam("toAddress") final String toAddress,
                         @RequestParam("file") final MultipartFile[] file, final HttpServletRequest request) throws Exception {
//        String path = "C:/Users/wt/Desktop/file";
//        String path = "/home/llin/VerifySystem_8816/uploadFile/";
//        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath() +"/static/tempUpload/";
        String path = new ApplicationHome(getClass()).getSource().getParentFile().toString() + "/uploadFile";
        String fileName = UUID.randomUUID().toString().replace("-", "");
        logger.info("path===>" + path);
        for (MultipartFile f : file) {
            String name = "";
            name = f.getOriginalFilename();
            String[] names = name.split("/");
            name = names[names.length - 1];
            //创建上一级文件夹
//            File file1 = new File(path + "/" + fileName);
//            file1.mkdirs();
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
                return "文件上传失败，请重试";
            }
        }

        new Thread() {
            public void run() {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet1 = null;
                HSSFSheet sheet2 = null;
                sheet1 = wb.createSheet("企业信息");
                sheet1.setDefaultColumnWidth(20);// 默认列宽
                // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
                HSSFRow row1 = sheet1.createRow((int) 0);
                // 第四步，创建单元格，并设置值表头 设置表头居中
                HSSFCellStyle style = wb.createCellStyle();
                // 创建一个居中格式
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                // 添加excel title
                HSSFCell cell1 = null;
                String[] excelnames1 = {"图片名称", "企业名称", "统一信用代码", "工商注册号", "组织机构代码", "企业类型", "法人姓名", "注册资本（万元）",
                        "注册币种", "实收资本（万元）", "开业日期", "经营开始日期", "经营结束日期", "登记机关", "核准时间", "登记状态", "注册日期", "吊销日期", "地址",
                        "省", "市", "区", "行政区域代码", "经营范围", "最后年报报送年度", "行业门类代码", "行业门类名称", "国民经济行业代码", "国民经济行业名称",
                        "历史名称"};
                for (int i = 0; i < excelnames1.length; i++) {
                    cell1 = row1.createCell(i);
                    cell1.setCellValue(excelnames1[i]);
                    cell1.setCellStyle(style);
                }
                sheet2 = wb.createSheet("失败数据");
                sheet2.setDefaultColumnWidth(20);// 默认列宽
                // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
                HSSFRow row2 = sheet2.createRow((int) 0);
                // 添加excel title
                HSSFCell cell2 = null;
                String[] names2 = {"失败图片名称", "营业执照号"};
                for (int i = 0; i < names2.length; i++) {
                    cell2 = row2.createCell(i);
                    cell2.setCellValue(names2[i]);
                    cell2.setCellStyle(style);
                }

                try {
                    // 获取文件夹下所有图片文件
                    traverseFolder1(path + "/" + fileName, sheet1, sheet2);
                    try {
                        FileOutputStream fout = new FileOutputStream(path + "/" + fileName + "/companyInfo.xls");
                        wb.write(fout);
                        fout.flush();
                        fout.close();
                        MailUtil.send(toAddress, path + "/" + fileName + "/companyInfo.xls", "ocr营业执照获取企业信息结果", "您好，您之前获取企业信息的结果已返回，企业信息excel文件已在附件中，请查收，无需回复！");
                        File file = new File(path + "/" + fileName + "/companyInfo.xls");// 删除服务器的excel
                        if (file.exists()) {
                            file.delete();
                        }
                        File file2 = new File(path + "/" + fileName);// 删除上传到服务器的图片
                        // 删除文件夹下的所有文件
                        File[] files = file2.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            try {
                                // 删除子文件
                                if (files[i].isFile()) {
                                    String filePath = files[i].getAbsolutePath();
                                    file = new File(filePath);
                                    // 路径为文件且不为空则进行删除
                                    if (file.isFile() && file.exists()) {
                                        file.delete();
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("出现异常", e);
                                e.printStackTrace();
                            }
                        }
                        if (file2.exists()) {// 删除文件夹
                            file2.delete();
                        }
                    } catch (Exception e) {
                        logger.error("出现异常", e);
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    logger.error("出现异常", e);
                    e.printStackTrace();
                }

            }
        }.start();
        return "success";
    }

    // 获取文件夹下所有文件
    public static List<Future<Boolean>> traverseFolder1(String path, HSSFSheet sheet1, HSSFSheet sheet2) {
//		List<String> fileList = new ArrayList<String>();
        File file = new File(path);
        Set<File> fileSet = new HashSet<File>();
        List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (fileSet.contains(file2)) {
                    continue;
                }
                fileSet.add(file2);
                try {
                    if (file2.isDirectory()) {// 目录

                    } else {// 文件
//						fileList.add(file2.getAbsolutePath());
                        final String filepath = file2.getAbsolutePath();
                        futureList.add(exs.submit(new RunnableTask(filepath, sheet1, sheet2)));
                    }
                } catch (Exception e) {
                    logger.error("出现异常", e);
                    e.printStackTrace();
                } finally {
                    fileSet.remove(file2);
                }
            }
            for (Future<Boolean> future : futureList) {
                try {
                    System.out.println(future.get());
                } catch (Exception e) {
                    logger.error("出现异常", e);
                    e.printStackTrace();
                }

            }
            /*
             * File temp_file; while (!list.isEmpty()) { temp_file = list.removeFirst();
             * files = temp_file.listFiles(); for (File file2 : files) { if
             * (file2.isDirectory()) { System.out.println("文件夹:" + file2.getAbsolutePath());
             * list.add(file2); folderNum++; } else { System.out.println("文件:" +
             * file2.getAbsolutePath()); fileNum++; } } }
             */
        } else {
            System.out.println("文件不存在!");
        }
//		return fileList;
        return futureList;
    }

    static class RunnableTask implements Callable<Boolean> {
        String filepath;
        HSSFSheet sheet1;
        HSSFSheet sheet2;
        //		Integer i1 = 0;
//		Integer i2 = 0;
        int num = 1;// 轮询次数

        public RunnableTask(String filepath, HSSFSheet sheet1, HSSFSheet sheet2) {
            this.filepath = filepath;
            this.sheet1 = sheet1;
            this.sheet2 = sheet2;
        }

        public synchronized HSSFRow incrementi1() {
            HSSFRow row = sheet1.createRow(sheet1.getLastRowNum() + 1);
            return row;
        }

        public synchronized HSSFRow incrementi2() {
            HSSFRow row = sheet2.createRow(sheet2.getLastRowNum() + 1);
            return row;
        }

        @Override
        public Boolean call() {
            logger.info("轮询次数===>" + num);
            logger.info("图片路径===>" + filepath);
            num++;
            Map map = new HashMap();
            String errorNumber = "";// 失败图片的营业执照号
            // 图片名称
            String[] names = filepath.split("/");
            String name = names[names.length - 1];
            map.put("name", name);
            try {
                // 获取营业执照号
                String number = Authentication.method13(filepath);
                errorNumber = number;
                map.put("number", number);
                logger.info(name + "营业执照号===>" + number);
//				System.out.println("营业执照号" + number);
//				String result = "{\"entInfos\":\"{\\\"regNo\\\":\\\"371400228016303\\\",\\\"entName\\\":\\\"山东祥瑞广告装饰有限公司\\\",\\\"city\\\":\\\"德州市\\\",\\\"historyName\\\":null,\\\"county\\\":\\\"德城区\\\",\\\"frName\\\":\\\"张海宽\\\",\\\"revDate\\\":null,\\\"industryCode\\\":\\\"7240\\\",\\\"entType\\\":\\\"有限责任公司(自然人投资或控股)\\\",\\\"creditCode\\\":\\\"91371400761856209M\\\",\\\"regCapCur\\\":\\\"人民币\\\",\\\"province\\\":\\\"山东省\\\",\\\"orgCode\\\":\\\"761856209\\\",\\\"regCap\\\":\\\"300.000000\\\",\\\"ancheYear\\\":\\\"2017\\\",\\\"industryPhyCode\\\":\\\"L\\\",\\\"industryPhyName\\\":\\\"租赁和商务服务业\\\",\\\"industryName\\\":\\\"广告业\\\",\\\"canDate\\\":null,\\\"address\\\":\\\"山东省德州市德城区广川街道办事处广川路滨海首府1号门市\\\",\\\"openTo\\\":\\\"0000-00-00\\\",\\\"recCap\\\":\\\" \\\",\\\"apprDate\\\":\\\"2019-03-13\\\",\\\"openFrom\\\":\\\"2004-04-12\\\",\\\"esDate\\\":\\\"2004-04-12\\\",\\\"areaCode\\\":\\\"371400\\\",\\\"operateScope\\\":\\\"设计、制作、发布国内各类广告业务（含固定形式印刷品广告）；企业营销策划；LED灯光亮化及楼宇照明工程安装施工；加工、制作、安装、销售LED灯具；五金电料；广告材料；高低压电器；开关插座等销售（依法须经批准的项目，经相关部门批准后方可开展经营活动）。\\\",\\\"regOrg\\\":\\\"德州市市场监督管理局\\\",\\\"entStatus\\\":\\\"在营（开业）企业\\\",\\\"ancheDate\\\":null}\",\"resultCode\":\"00\",\"merorderId\":\"93120190509164202342998\",\"sign\":\"C22CC56932DE9ED11E50E25F48DFCBA9\"}";
//				map.put("result", result);
//				map.put("flag", true);
                if (number != null && !"".equals(number)) {
                    // 根据营业执照号查询企业信息
                    String result = Authentication.method7("01", number);
                    logger.info(name + "企业信息===>" + result);
                    map.put("result", result);
                    if (result != null && !"".contentEquals(result)) {
                        JSONObject json = JSON.parseObject(result);
                        if (json != null && "00".equals(json.getString("resultCode")) && json.containsKey("entInfos")) {
                            JSONObject entInfos = json.getJSONObject("entInfos");
                            if (entInfos != null) {
                                entInfos.put("name", name);
                                String[] excelvalues = {"name", "entName", "creditCode", "regNo", "orgCode", "entType",
                                        "frName", "regCap", "regCapCur", "recCap", "esDate", "openFrom", "openTo",
                                        "regOrg", "apprDate", "entStatus", "canDate", "revDate", "address", "province",
                                        "city", "county", "areaCode", "operateScope", "ancheDate", "industryPhyCode",
                                        "industryPhyName", "industryCode", "industryName", "historyName"};

                                // 第四步，创建单元格，并设置值
                                HSSFRow row = incrementi1();
                                for (int k = 0; k < excelvalues.length; k++) {
                                    row.createCell(k).setCellValue(entInfos.get(excelvalues[k]) == null ? ""
                                            : entInfos.get(excelvalues[k]).toString());
                                }
                            } else {
                                if (num <= 3) {
                                    call();
                                } else {
                                    HSSFRow row = incrementi2();
                                    row.createCell(0).setCellValue(name);
                                    row.createCell(1).setCellValue(number);
                                }
                                map.put("flag", false);
                            }
                        } else {
                            if (num <= 3) {
                                call();
                            } else {
                                HSSFRow row = incrementi2();
                                row.createCell(0).setCellValue(name);
                                row.createCell(1).setCellValue(number);
                            }
                            map.put("flag", false);
                        }
                    } else {
                        if (num <= 3) {
                            call();
                        } else {
                            HSSFRow row = incrementi2();
                            row.createCell(0).setCellValue(name);
                            row.createCell(1).setCellValue(number);
                        }
                        map.put("flag", false);
                    }
                } else {// 记录失败数据的图片数据，进行轮询
                    if (num <= 3) {
                        call();
                    } else {
                        HSSFRow row = incrementi2();
                        row.createCell(0).setCellValue(name);
                        row.createCell(1).setCellValue(number);
                    }
                    map.put("flag", false);
                    map.put("path", filepath);
                }
            } catch (Exception e) {// 记录失败数据的图片数据，进行轮询
                if (num <= 3) {
                    call();
                } else {
                    HSSFRow row = incrementi2();
                    row.createCell(0).setCellValue(name);
                    row.createCell(1).setCellValue(errorNumber);
                }
                map.put("flag", false);
                map.put("path", filepath);
                map.put("number", errorNumber);
                logger.error(filepath + "异常", e);
                e.printStackTrace();
            }
            return true;
        }
    }

    // 导入excel获取企业信息
    @RequestMapping(value = "/excelUpload", method = RequestMethod.POST)
    public String importUsers(@RequestParam("excel_file") MultipartFile excel_file, final String toAddress,
                              final HttpServletRequest request) throws Exception {
//        String path = "C:/Users/wt/Desktop/file";
//        String path = "/home/llin/VerifySystem_8816/uploadFile/";
//        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath() +"/static/tempUpload/";
        String path = new ApplicationHome(getClass()).getSource().getParentFile().toString() + "/uploadFile";
        String fileName = UUID.randomUUID().toString().replace("-", "");
        logger.info("path===>" + path);
        String name = excel_file.getOriginalFilename();
        String[] names = name.split("/");
        name = names[names.length - 1];
        logger.info(path + "/" + fileName + "/" + name);
        File file1 = new File(path + "/" + fileName + "/" + name);
        if (!file1.getParentFile().exists()) {
            file1.getParentFile().mkdirs();// 新建文件夹
        }
        try {
            file1.createNewFile();
            //FileCopyUtils.copy(excel_file.getInputStream(), Files.newOutputStream(file1.toPath()));
            excel_file.transferTo(file1);
        } catch (Exception e) {
            return "文件上传失败，请重试";
        }


        new Thread() {
            public void run() {
                try {
                    HSSFWorkbook wb = new HSSFWorkbook();
                    HSSFSheet sheet1 = null;// 成功数据
                    HSSFSheet sheet2 = null;// 失败数据
                    sheet1 = wb.createSheet("企业信息");
                    sheet1.setDefaultColumnWidth(20);// 默认列宽
                    // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
                    HSSFRow row1 = sheet1.createRow((int) 0);
                    // 第四步，创建单元格，并设置值表头 设置表头居中
                    HSSFCellStyle style = wb.createCellStyle();
                    // 创建一个居中格式
                    style.setAlignment(HorizontalAlignment.CENTER);// .setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    // 添加excel title
                    HSSFCell retcell1 = null;
                    String[] excelnames1 = {"企业名称", "统一信用代码", "工商注册号", "组织机构代码", "企业类型", "法人姓名", "注册资本（万元）", "注册币种",
                            "实收资本（万元）", "开业日期", "经营开始日期", "经营结束日期", "登记机关", "核准时间", "登记状态", "注册日期", "吊销日期", "地址", "省",
                            "市", "区", "行政区域代码", "经营范围", "最后年报报送年度", "行业门类代码", "行业门类名称", "国民经济行业代码", "国民经济行业名称",
                            "历史名称"};
                    for (int i = 0; i < excelnames1.length; i++) {
                        retcell1 = row1.createCell(i);
                        retcell1.setCellValue(excelnames1[i]);
                        retcell1.setCellStyle(style);
                    }
                    sheet2 = wb.createSheet("失败数据");
                    sheet2.setDefaultColumnWidth(20);// 默认列宽
                    // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
                    HSSFRow row2 = sheet2.createRow((int) 0);
                    // 添加excel title
                    HSSFCell retcell2 = null;
                    String[] names2 = {"企业注册号", "企业社会信用代码", "企业名称"};
                    for (int i = 0; i < names2.length; i++) {
                        retcell2 = row2.createCell(i);
                        retcell2.setCellValue(names2[i]);
                        retcell2.setCellStyle(style);
                    }

//					HSSFWorkbook workbook = null;
//					XSSFWorkbook workbook = null;
                    Workbook workbook = null;


                    // 创建Excel，读取文件内容
//					workbook = new XSSFWorkbook(FileUtils.openInputStream(file1));
                    workbook = WorkbookFactory.create(FileUtils.openInputStream(file1));

                    // 获取第一个工作表
                    Sheet sheet = workbook.getSheetAt(0);
                    // 获取sheet中第一行行号
                    int firstRowNum = sheet.getFirstRowNum();
                    // 获取sheet中最后一行行号
                    int lastRowNum = sheet.getLastRowNum();
                    logger.info("共" + lastRowNum + "条数据");
                    logger.info(firstRowNum + "");
                    // 循环插入数据
                    Set<Integer> fileSet = new HashSet<Integer>();
                    List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();
                    for (int i = 1; i <= lastRowNum; i++) {
                        if (fileSet.contains(i)) {
                            continue;
                        }
                        fileSet.add(i);
                        try {
                            String type = "";
                            String number = "";
                            Row row = sheet.getRow(i);
                            Cell cell1 = row.getCell(0);// 企业注册号
                            if (cell1 != null) {
                                cell1.setCellType(CellType.STRING);
                            }
                            String cel1 = cell1 == null ? ""
                                    : cell1.getStringCellValue() == null ? "" : cell1.getStringCellValue();
                            Cell cell2 = row.getCell(1);// 企业社会信用代码
                            if (cell2 != null) {
//								.setCellType(HSSFCell.CELL_TYPE_STRING);
                                cell2.setCellType(CellType.STRING);
                            }
                            String cel2 = cell2 == null ? ""
                                    : cell2.getStringCellValue() == null ? "" : cell2.getStringCellValue();
                            Cell cell3 = row.getCell(2);// 企业社会信用代码
                            if (cell3 != null) {
                                cell3.setCellType(CellType.STRING);
                            }
                            String cel3 = cell3 == null ? ""
                                    : cell3.getStringCellValue() == null ? "" : cell3.getStringCellValue();
                            if (!StringUtil.isBlank(cel1)) {
                                type = "01";
                                number = cel1;
                            } else if (!StringUtil.isBlank(cel2)) {
                                type = "02";
                                number = cel2;
                            } else if (!StringUtil.isBlank(cel3)) {
                                type = "03";
                                number = cel3;
                            }
                            futureList.add(excelexs.submit(new ExcelTask(type, number, sheet1, sheet2)));
                        } catch (Exception e) {
                            logger.error("出现异常", e);
                            e.printStackTrace();
                        } finally {
                            fileSet.remove(i);
                        }
                    }
                    for (Future<Boolean> future : futureList) {
                        try {
                            System.out.println(future.get());
                        } catch (Exception e) {
                            logger.error("出现异常", e);
                            e.printStackTrace();
                        }
                    }
                    FileOutputStream fout = new FileOutputStream(path + "/" + fileName + "/companyInfo.xls");
                    wb.write(fout);
                    fout.flush();
                    fout.close();
                    MailUtil.send(toAddress, path + "/" + fileName + "/companyInfo.xls", "ocr营业执照获取企业信息结果", "您好，您之前获取企业信息的结果已返回，企业信息excel文件已在附件中，请查收，无需回复！");
                    File file = new File(path + "/" + fileName + "/companyInfo.xls");// 删除服务器的excel
                    if (file.exists()) {
                        file.delete();
                    }
                    file1.delete();
                    File file2 = new File(path + "/" + fileName);
                    file2.delete();
                    // usersMapper.insert(list);//往数据库插入数据
                } catch (Exception e) {
                    logger.error("出现异常", e);
                    e.printStackTrace();
                }
            }
        }.start();
        return "success";
    }

    static class ExcelTask implements Callable<Boolean> {
        String type;
        String number;
        HSSFSheet sheet1;
        HSSFSheet sheet2;

        public ExcelTask(String type, String number, HSSFSheet sheet1, HSSFSheet sheet2) {
            this.type = type;
            this.number = number;
            this.sheet1 = sheet1;
            this.sheet2 = sheet2;
        }

        public synchronized HSSFRow incrementi1() {
            int num = sheet1.getLastRowNum() + 1;
            HSSFRow row = sheet1.createRow(num);
            return row;
        }

        public synchronized HSSFRow incrementi2() {
            int num = sheet2.getLastRowNum() + 1;
            HSSFRow row = sheet2.createRow(num);
            return row;
        }

        @Override
        public Boolean call() {
            logger.info(type + ":  " + number);
            try {
//				String result = "{\"entInfos\":\"{\\\"regNo\\\":\\\"371400228016303\\\",\\\"entName\\\":\\\"山东祥瑞广告装饰有限公司\\\",\\\"city\\\":\\\"德州市\\\",\\\"historyName\\\":null,\\\"county\\\":\\\"德城区\\\",\\\"frName\\\":\\\"张海宽\\\",\\\"revDate\\\":null,\\\"industryCode\\\":\\\"7240\\\",\\\"entType\\\":\\\"有限责任公司(自然人投资或控股)\\\",\\\"creditCode\\\":\\\"91371400761856209M\\\",\\\"regCapCur\\\":\\\"人民币\\\",\\\"province\\\":\\\"山东省\\\",\\\"orgCode\\\":\\\"761856209\\\",\\\"regCap\\\":\\\"300.000000\\\",\\\"ancheYear\\\":\\\"2017\\\",\\\"industryPhyCode\\\":\\\"L\\\",\\\"industryPhyName\\\":\\\"租赁和商务服务业\\\",\\\"industryName\\\":\\\"广告业\\\",\\\"canDate\\\":null,\\\"address\\\":\\\"山东省德州市德城区广川街道办事处广川路滨海首府1号门市\\\",\\\"openTo\\\":\\\"0000-00-00\\\",\\\"recCap\\\":\\\" \\\",\\\"apprDate\\\":\\\"2019-03-13\\\",\\\"openFrom\\\":\\\"2004-04-12\\\",\\\"esDate\\\":\\\"2004-04-12\\\",\\\"areaCode\\\":\\\"371400\\\",\\\"operateScope\\\":\\\"设计、制作、发布国内各类广告业务（含固定形式印刷品广告）；企业营销策划；LED灯光亮化及楼宇照明工程安装施工；加工、制作、安装、销售LED灯具；五金电料；广告材料；高低压电器；开关插座等销售（依法须经批准的项目，经相关部门批准后方可开展经营活动）。\\\",\\\"regOrg\\\":\\\"德州市市场监督管理局\\\",\\\"entStatus\\\":\\\"在营（开业）企业\\\",\\\"ancheDate\\\":null}\",\"resultCode\":\"00\",\"merorderId\":\"93120190509164202342998\",\"sign\":\"C22CC56932DE9ED11E50E25F48DFCBA9\"}";
                if (number != null && !"".equals(number)) {
                    // 根据营业执照号查询企业信息
                    String result = Authentication.method7(type, number);
                    logger.info(type + "，" + number + "   企业信息===>" + result);
                    if (result != null && !"".contentEquals(result)) {
                        JSONObject json = JSON.parseObject(result);
                        if (json != null && "00".equals(json.getString("resultCode")) && json.containsKey("entInfos")) {
                            JSONObject entInfos = json.getJSONObject("entInfos");
//							entInfos.put("name", name);
                            if (entInfos != null) {
                                String[] excelvalues = {"entName", "creditCode", "regNo", "orgCode", "entType",
                                        "frName", "regCap", "regCapCur", "recCap", "esDate", "openFrom", "openTo",
                                        "regOrg", "apprDate", "entStatus", "canDate", "revDate", "address", "province",
                                        "city", "county", "areaCode", "operateScope", "ancheDate", "industryPhyCode",
                                        "industryPhyName", "industryCode", "industryName", "historyName"};

                                // 第四步，创建单元格，并设置值
//								int i1 = incrementi1();
//								System.out.println("哈哈哈哈" + i1);
                                HSSFRow row = incrementi1();
                                for (int k = 0; k < excelvalues.length; k++) {
                                    row.createCell(k).setCellValue(entInfos.get(excelvalues[k]) == null ? ""
                                            : entInfos.get(excelvalues[k]).toString());
                                }
                            } else {
                                HSSFRow row = incrementi2();
                                if ("01".equals(type)) {
                                    row.createCell(0).setCellValue(number);
                                } else if ("02".equals(type)) {
                                    row.createCell(1).setCellValue(number);
                                } else if ("03".equals(type)) {
                                    row.createCell(2).setCellValue(number);
                                }
                            }
                        } else {
                            HSSFRow row = incrementi2();
                            if ("01".equals(type)) {
                                row.createCell(0).setCellValue(number);
                            } else if ("02".equals(type)) {
                                row.createCell(1).setCellValue(number);
                            } else if ("03".equals(type)) {
                                row.createCell(2).setCellValue(number);
                            }
                        }
                    } else {
                        HSSFRow row = incrementi2();
                        if ("01".equals(type)) {
                            row.createCell(0).setCellValue(number);
                        } else if ("02".equals(type)) {
                            row.createCell(1).setCellValue(number);
                        } else if ("03".equals(type)) {
                            row.createCell(2).setCellValue(number);
                        }
                    }
                } else {// 记录失败数据的图片数据，进行轮询
                    HSSFRow row = incrementi2();
                    if ("01".equals(type)) {
                        row.createCell(0).setCellValue(number);
                    } else if ("02".equals(type)) {
                        row.createCell(1).setCellValue(number);
                    } else if ("03".equals(type)) {
                        row.createCell(2).setCellValue(number);
                    }
                }
            } catch (Exception e) {// 记录失败数据的图片数据，进行轮询
                HSSFRow row = incrementi2();
                if ("01".equals(type)) {
                    row.createCell(0).setCellValue(number);
                } else if ("02".equals(type)) {
                    row.createCell(1).setCellValue(number);
                } else if ("03".equals(type)) {
                    row.createCell(2).setCellValue(number);
                }
                logger.error(type + "," + number + "异常", e);
                e.printStackTrace();
            }
            return true;
        }
    }

    @RequestMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            logger.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            logger.info("文件的后缀名为：" + suffixName);

            // 设置文件存储路径
            String filePath = "C:/Users/wt/Desktop/file2/";
            String path = filePath + fileName;

            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            return "上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }
}