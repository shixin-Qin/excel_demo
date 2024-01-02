package com.example.excel_demo;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mode.*;
import com.example.mode.budgetOrder.OrderCreateAndSearchXmlMode;
import com.example.mode.budgetOrder.OrderCreateXmlMode;
import com.example.mode.budgetOrder.OrderNotMatchXmlMode;
import com.example.mode.budgetOrder.OrderSearchXmlMode;
import com.example.utils.CommonUtil;
import com.example.utils.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class ExcelDemoApplicationTests {

    @Test
    void contextLoads() {
    }
    
    @Test
    void name() {
        // 读取 excel 表格的路径
        String readPath = "D:\\项目文档\\20220317\\禅医积木医生列表页 - 0317.xlsx";
    
        try {
            // sheetNo --> 读取哪一个 表单
            // headLineMun --> 从哪一行开始读取( 不包括定义的这一行，比如 headLineMun为2 ，那么取出来的数据是从 第三行的数据开始读取 )
            // clazz --> 将读取的数据，转化成对应的实体，需要 extends BaseRowModel
            Sheet sheet = new Sheet(1, 1, ExcelMode.class);
        
            // 这里 取出来的是 ExcelModel实体 的集合
            List<Object> readList = EasyExcelFactory.read(new FileInputStream(readPath), sheet);
            // 存 ExcelMode 实体的 集合
            List<ExcelMode> list = new ArrayList<ExcelMode>();
            for (Object obj : readList) {
                list.add((ExcelMode) obj);
            }
            for (ExcelMode excelMode : list) {
                // System.out.println(JSON.toJSON(excelMode));
                if(excelMode.getHave().contains("见文件夹")){
                    String s1= "INSERT INTO `doc_workwx_diagnosis` VALUES (null, NOW(), NOW(), 0, ";
                    String s2= ", '某人', '复星禅城医院', 'prod/doctor/dochome_workwx/某人.png', 'prod/doctor/dochome_workwx/head/某人.png');";
                    String strName = s2.replaceAll("某人", excelMode.getName());
                    System.out.println(s1 + excelMode.getDoctorId() + strName);
                }
            }
        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    
    }
    
    @Test
    void refreshDev() throws IOException {
        // 1.1 读取总的list
        String result_path = "D:\\项目文档\\20220428\\sku\\清洗数据\\dev\\dev_result.xlsx";
        Sheet sheet = new Sheet(1, 1, SkuSuppilerMode.class);
    
        // 这里 取出来的是 ExcelModel实体 的集合
        List<Object> readList = EasyExcelFactory.read(new FileInputStream(result_path), sheet);
        // 存 ExcelMode 实体的 集合
        HashMap<String, Integer> resultMap = new HashMap<>();
        for (Object obj : readList) {
            SkuSuppilerMode skuSuppilerMode = (SkuSuppilerMode) obj;
            Integer supplierId = skuSuppilerMode.getSupplierId();
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                resultMap.put(skuSuppilerMode.getSkuId(),skuSuppilerMode.getSupplierId());
            }
        }
        
        // 2.1.1 读取platform_scenter_product
        String readPath1 = "D:\\项目文档\\20220428\\sku\\清洗数据\\dev\\platform_scenter_product.xlsx";
        Sheet sheet1 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList1 = EasyExcelFactory.read(new FileInputStream(readPath1), sheet1);
        // 2.1.2 生成sql
        BufferedWriter fileWriter1 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\dev\\platform_scenter_product.sql"));
        String format1 = "UPDATE platform_scenter_product SET supplier_id = %s WHERE sku_id = %s;";
        for (Object obj : readList1) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format1,supplierId,one.getSkuId());
                fileWriter1.write(updateSql);
                fileWriter1.newLine();
            }
        }
        fileWriter1.close();
        
        // 2.2.1 读取doc_drug_collect_record
        String readPath2 = "D:\\项目文档\\20220428\\sku\\清洗数据\\dev\\doc_drug_collect_record.xlsx";
        Sheet sheet2 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList2 = EasyExcelFactory.read(new FileInputStream(readPath2), sheet2);
        // 2.2.2 生成sql
        BufferedWriter fileWriter2 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\dev\\doc_drug_collect_record.sql"));
        String format2 = "UPDATE doc_drug_collect_record SET supplier_id = %s WHERE drug_id = %s;";
        for (Object obj : readList2) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format2,supplierId,one.getSkuId());
                fileWriter2.write(updateSql);
                fileWriter2.newLine();
            }
        }
        fileWriter2.close();
        
        // 2.3 读取doctor_prescribe_medicine_rel
        String readPath3 = "D:\\项目文档\\20220428\\sku\\清洗数据\\dev\\doctor_prescribe_medicine_rel.xlsx";
        Sheet sheet3 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList3 = EasyExcelFactory.read(new FileInputStream(readPath3), sheet3);
        // 2.2.2 生成sql
        BufferedWriter fileWriter3 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\dev\\doctor_prescribe_medicine_rel.sql"));
        String format3 = "UPDATE doctor_prescribe_medicine_rel SET supplier_id = %s WHERE sku_id = %s;";
        for (Object obj : readList3) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format3,supplierId,one.getSkuId());
                fileWriter3.write(updateSql);
                fileWriter3.newLine();
            }
        }
        fileWriter3.close();
        
    }
    
    @Test
    void refreshTest() throws IOException {
        // 1.1 读取总的list
        String result_path = "D:\\项目文档\\20220428\\sku\\清洗数据\\test\\test_result.xlsx";
        Sheet sheet = new Sheet(1, 1, SkuSuppilerMode.class);
        
        // 这里 取出来的是 ExcelModel实体 的集合
        List<Object> readList = EasyExcelFactory.read(new FileInputStream(result_path), sheet);
        // 存 ExcelMode 实体的 集合
        HashMap<String, Integer> resultMap = new HashMap<>();
        for (Object obj : readList) {
            SkuSuppilerMode skuSuppilerMode = (SkuSuppilerMode) obj;
            Integer supplierId = skuSuppilerMode.getSupplierId();
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                resultMap.put(skuSuppilerMode.getSkuId(),skuSuppilerMode.getSupplierId());
            }
        }
        
        // 2.1.1 读取platform_scenter_product
        String readPath1 = "D:\\项目文档\\20220428\\sku\\清洗数据\\test\\platform_scenter_product.xlsx";
        Sheet sheet1 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList1 = EasyExcelFactory.read(new FileInputStream(readPath1), sheet1);
        // 2.1.2 生成sql
        BufferedWriter fileWriter1 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\test\\platform_scenter_product.sql"));
        String format1 = "UPDATE platform_scenter_product SET supplier_id = %s WHERE sku_id = %s;";
        for (Object obj : readList1) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format1,supplierId,one.getSkuId());
                fileWriter1.write(updateSql);
                fileWriter1.newLine();
            }
        }
        fileWriter1.close();
        
        // 2.2.1 读取doc_drug_collect_record
        String readPath2 = "D:\\项目文档\\20220428\\sku\\清洗数据\\test\\doc_drug_collect_record.xlsx";
        Sheet sheet2 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList2 = EasyExcelFactory.read(new FileInputStream(readPath2), sheet2);
        // 2.2.2 生成sql
        BufferedWriter fileWriter2 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\test\\doc_drug_collect_record.sql"));
        String format2 = "UPDATE doc_drug_collect_record SET supplier_id = %s WHERE drug_id = %s;";
        for (Object obj : readList2) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format2,supplierId,one.getSkuId());
                fileWriter2.write(updateSql);
                fileWriter2.newLine();
            }
        }
        fileWriter2.close();
        
        // 2.3 读取doctor_prescribe_medicine_rel
        String readPath3 = "D:\\项目文档\\20220428\\sku\\清洗数据\\test\\doctor_prescribe_medicine_rel.xlsx";
        Sheet sheet3 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList3 = EasyExcelFactory.read(new FileInputStream(readPath3), sheet3);
        // 2.2.2 生成sql
        BufferedWriter fileWriter3 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\test\\doctor_prescribe_medicine_rel.sql"));
        String format3 = "UPDATE doctor_prescribe_medicine_rel SET supplier_id = %s WHERE sku_id = %s;";
        for (Object obj : readList3) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format3,supplierId,one.getSkuId());
                fileWriter3.write(updateSql);
                fileWriter3.newLine();
            }
        }
        fileWriter3.close();
        
    }
    
    @Test
    void refreshProd() throws IOException {
        // 1.1 读取总的list
        String result_path = "D:\\项目文档\\20220428\\sku\\清洗数据\\prod\\prod_result.xlsx";
        Sheet sheet = new Sheet(1, 1, SkuSuppilerMode.class);
        
        // 这里 取出来的是 ExcelModel实体 的集合
        List<Object> readList = EasyExcelFactory.read(new FileInputStream(result_path), sheet);
        // 存 ExcelMode 实体的 集合
        HashMap<String, Integer> resultMap = new HashMap<>();
        for (Object obj : readList) {
            SkuSuppilerMode skuSuppilerMode = (SkuSuppilerMode) obj;
            Integer supplierId = skuSuppilerMode.getSupplierId();
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                resultMap.put(skuSuppilerMode.getSkuId(),skuSuppilerMode.getSupplierId());
            }
        }
        
        // 2.1.1 读取platform_scenter_product
        String readPath1 = "D:\\项目文档\\20220428\\sku\\清洗数据\\prod\\platform_scenter_product.xlsx";
        Sheet sheet1 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList1 = EasyExcelFactory.read(new FileInputStream(readPath1), sheet1);
        // 2.1.2 生成sql
        BufferedWriter fileWriter1 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\prod\\platform_scenter_product.sql"));
        String format1 = "UPDATE platform_scenter_product SET supplier_id = %s WHERE sku_id = %s;";
        for (Object obj : readList1) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format1,supplierId,one.getSkuId());
                fileWriter1.write(updateSql);
                fileWriter1.newLine();
            }
        }
        fileWriter1.close();
        
        // 2.2.1 读取doc_drug_collect_record
        String readPath2 = "D:\\项目文档\\20220428\\sku\\清洗数据\\prod\\doc_drug_collect_record.xlsx";
        Sheet sheet2 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList2 = EasyExcelFactory.read(new FileInputStream(readPath2), sheet2);
        // 2.2.2 生成sql
        BufferedWriter fileWriter2 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\prod\\doc_drug_collect_record.sql"));
        String format2 = "UPDATE doc_drug_collect_record SET supplier_id = %s WHERE drug_id = %s;";
        for (Object obj : readList2) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format2,supplierId,one.getSkuId());
                fileWriter2.write(updateSql);
                fileWriter2.newLine();
            }
        }
        fileWriter2.close();
        
        // 2.3 读取doctor_prescribe_medicine_rel
        String readPath3 = "D:\\项目文档\\20220428\\sku\\清洗数据\\prod\\doctor_prescribe_medicine_rel.xlsx";
        Sheet sheet3 = new Sheet(1, 1, SkuSuppilerMode.class);
        List<Object> readList3 = EasyExcelFactory.read(new FileInputStream(readPath3), sheet3);
        // 2.2.2 生成sql
        BufferedWriter fileWriter3 = Files.newBufferedWriter(Paths.get("D:\\项目文档\\20220428\\sku\\清洗数据\\prod\\doctor_prescribe_medicine_rel.sql"));
        String format3 = "UPDATE doctor_prescribe_medicine_rel SET supplier_id = %s WHERE sku_id = \"%s\";";
        for (Object obj : readList3) {
            SkuSuppilerMode one = (SkuSuppilerMode) obj;
            Integer supplierId = resultMap.get(one.getSkuId());
            if(Objects.nonNull(supplierId) && !supplierId.equals(0)){
                String updateSql = String.format(format3,supplierId,one.getSkuId());
                fileWriter3.write(updateSql);
                fileWriter3.newLine();
            }
        }
        fileWriter3.close();
        
    }

    @Test
    public void shuiLing(){

        String json = "/Users/shixin.qin/Documents/jsons/remarkAll.json";
        File jsonFile = new File(json);
//通过上面那个方法获取json文件的内容
        String jsonData = CommonUtil.getStr(jsonFile);
//转json对象
//        List<LogVO> parse = (List<LogVO>) JSONObject.parse(jsonData);
        List<LogVO> logVOList = JSONArray.parseArray(jsonData, LogVO.class);
//获取主要数据
        HashMap<String, MsgVo> map = new HashMap<>();
        logVOList.stream().sorted((s1,s2) -> s1.getTimestamp().compareTo(s2.getTimestamp())).forEach(logVO -> {
            String[] split = logVO.getMessage().split("params:");
            MsgVo msgVo = JSONArray.parseObject(split[1], MsgVo.class);
            msgVo.setOrderId2("'" + msgVo.getOrderId() +"'");
            map.put(msgVo.getOrderId(), msgVo);
        });
        List<MsgVo> values = new ArrayList<>(map.values());
        EasyExcel.write(new File("/Users/shixin.qin/Documents/jsons/msg2.xlsx"), MsgVo.class)
                .sheet(0,"sheet1")
                .doWrite(values);
        map.values().stream().forEach(one -> System.out.println(one.toString()));
//        for (LogVO logVO : logVOList) {
//            String[] split = logVO.getMessage().split("params:");
//            MsgVo msgVo = JSONArray.parseObject(split[1], MsgVo.class);
//            map.put(msgVo.getOrderId(), msgVo);
//        }

//        for (LogVO one : logVOList) {
//            System.out.println(one.toString());
//        }
    }

    @Test
    public void shuiLing2(){

        String json = "/Users/shixin.qin/Documents/jsons/badsqlAll.json";
        File jsonFile = new File(json);
//通过上面那个方法获取json文件的内容
        String jsonData = CommonUtil.getStr(jsonFile);

//转json对象
//        List<LogVO> parse = (List<LogVO>) JSONObject.parse(jsonData);
        List<LogVO> logVOList = JSONArray.parseArray(jsonData, LogVO.class);
//获取主要数据
        HashMap<String, MsgVo> map = new HashMap<>();
        logVOList.stream().sorted((s1,s2) -> s1.getTimestamp().compareTo(s2.getTimestamp())).forEach(logVO -> {

            MsgVo msgVo = new MsgVo();
            String[] split = logVO.getMessage().split("where  id=");
            msgVo.setId(split[1].substring(0,9));
            map.put(msgVo.getId(), msgVo);
        });
        List<MsgVo> values = new ArrayList<>(map.values());
        EasyExcel.write(new File("/Users/shixin.qin/Documents/jsons/remarkId1.xlsx"), MsgVo.class)
                .sheet(0,"sheet1")
                .doWrite(values);
        map.values().stream().forEach(one -> System.out.println(one.toString()));
//        for (LogVO logVO : logVOList) {
//            String[] split = logVO.getMessage().split("params:");
//            MsgVo msgVo = JSONArray.parseObject(split[1], MsgVo.class);
//            map.put(msgVo.getOrderId(), msgVo);
//        }

//        for (LogVO one : logVOList) {
//            System.out.println(one.toString());
//        }
    }

    private static String jsonStr = "";

    @Test
    public void shuiLing5(){

        String json = "/Users/shixin.qin/Documents/jsons/remarkAll.json";
        File jsonFile = new File(json);
//通过上面那个方法获取json文件的内容
        String jsonData = CommonUtil.getStr(jsonFile);
//转json对象
//        List<LogVO> parse = (List<LogVO>) JSONObject.parse(jsonData);
        List<LogVO> logVOList = JSONArray.parseArray(jsonData, LogVO.class);
//获取主要数据
        HashMap<String, MsgVo> map = new HashMap<>();
        logVOList.stream().sorted((s1,s2) -> s1.getTimestamp().compareTo(s2.getTimestamp())).forEach(logVO -> {
            String[] split = logVO.getMessage().split("params:");
            MsgVo msgVo = JSONArray.parseObject(split[1], MsgVo.class);
            msgVo.setOrderId2("'" + msgVo.getOrderId() +"'");
            map.put(msgVo.getOrderId(), msgVo);
        });
        List<MsgVo> values = new ArrayList<>(map.values());
        EasyExcel.write(new File("/Users/shixin.qin/Documents/jsons/msg2.xlsx"), MsgVo.class)
                .sheet(0,"sheet1")
                .doWrite(values);
        map.values().stream().forEach(one -> System.out.println(one.toString()));
//        for (LogVO logVO : logVOList) {
//            String[] split = logVO.getMessage().split("params:");
//            MsgVo msgVo = JSONArray.parseObject(split[1], MsgVo.class);
//            map.put(msgVo.getOrderId(), msgVo);
//        }

//        for (LogVO one : logVOList) {
//            System.out.println(one.toString());
//        }
    }

//    @Test
//    void name10() {
//        // 读取 excel 表格的路径
//        String readPath = "/Users/shixin.qin/Downloads/db_export_8151_result_影响老订单.xlsx";
//
//        try {
//            // sheetNo --> 读取哪一个 表单
//            // headLineMun --> 从哪一行开始读取( 不包括定义的这一行，比如 headLineMun为2 ，那么取出来的数据是从 第三行的数据开始读取 )
//            // clazz --> 将读取的数据，转化成对应的实体，需要 extends BaseRowModel
//            Sheet sheet = new Sheet(1, 1, CancelOrderMode.class);
//
//            // 这里 取出来的是 ExcelModel实体 的集合
//            List<Object> readList = EasyExcelFactory.read(new FileInputStream(readPath), sheet);
//            // 存 ExcelMode 实体的 集合
//            List<CancelOrderMode> list = new ArrayList<CancelOrderMode>();
//            for (Object obj : readList) {
//                list.add((CancelOrderMode) obj);
//            }
//            for (CancelOrderMode excelMode : list) {
//                HashMap<String, String> paramMap = new HashMap<>();
//                paramMap.put("callerCode", "1595817017");
//                paramMap.put("password", "12072017");
//                String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><message>\n" +
//                        "  <serviceRequest serviceCode=\"cancelReservation\">\n" +
//                        "    <serviceParameters>\n" +
//                        "      <reservation resNumber=\"" + excelMode.getOrderPNR() +
//                        "\"/>\n" +
//                        "    </serviceParameters>\n" +
//                        "  </serviceRequest>\n" +
//                        "</message>";
//                paramMap.put("XML-Request",xml);
//                try {
//                    String res = HttpUtil.postXwwFormUrlEncoded("https://applications.europcar.com/xrs/resxml", paramMap);
//                    System.out.println("cancel PNR:" + excelMode.getOrderPNR() + " res:"+ res);
//                } catch (IOException e) {
//                    System.out.println("cancel error PNR:" + excelMode.getOrderPNR() + " res:"+ e.getMessage());
//                }
//
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    void name11() throws IOException {
//        HashMap<String, String> paramMap = new HashMap<>();
//        paramMap.put("callerCode", "3540578337");
//        paramMap.put("password", "01062022");
//        paramMap.put("XML-Request", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><message>\n" +
//                "  <serviceRequest serviceCode=\"cancelReservation\">\n" +
//                "    <serviceParameters>\n" +
//                "      <reservation resNumber=\"1167404565\"/>\n" +
//                "    </serviceParameters>\n" +
//                "  </serviceRequest>\n" +
//                "</message>");
//        String res = HttpUtil.postXwwFormUrlEncoded("https://applications.europcar.com/xrs/resxml", paramMap);
//        System.out.println(res);
//    }

    @Test
    void name12() throws Exception {
        InputStream inputStream = new FileInputStream("/Users/shixin.qin/Downloads/Locs (1).dat");
//        File file = new File("/Users/shixin.qin/Downloads/Locs_1_1.dat");
//        File convert = CharsetUtil.convert(file,  StandardCharsets.UTF_8, StandardCharsets.US_ASCII);
        Charset charset = CharsetUtil.defaultCharset(inputStream, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1);
        System.out.println(charset);
    }

    @Test
    void name13() throws FileNotFoundException {
        String readPath3 = "/Users/shixin.qin/Documents/bei/budget_订单报文.xlsx";
        Sheet sheet3 = new Sheet(1, 1, OrderXmlMode.class);
        List<Object> readList3 = EasyExcelFactory.read(new FileInputStream(readPath3), sheet3);
        List<OrderXmlMode> dataList = new ArrayList<>();
        for (Object obj : readList3) {
            OrderXmlMode orderXmlMode = (OrderXmlMode) obj;
            String[] splitPick = orderXmlMode.getPickUpPlaceCode().split("-");
            String pickPlace= splitPick[2] + splitPick[3];
            String[] splitReturn = orderXmlMode.getReturnPlaceCode().split("-");
            String returnPlace= splitReturn[2] + splitReturn[3];

            String[] split = orderXmlMode.getResLog().split("ExtendedLocationCode=\"");
            String substring1 = null;
            String substring2 = null;
            try {
                substring1 = split[1].substring(0, 6);
                substring2 = split[2].substring(0, 6);
            } catch (Exception e) {
                System.out.println(orderXmlMode.getHzcOrderId());
                continue;
            }
            if(!pickPlace.equals(substring1) || !returnPlace.equals(substring2)){
                OrderXmlMode orderXmlModeNew = new OrderXmlMode();
                orderXmlModeNew.setHzcOrderId(orderXmlMode.getHzcOrderId());
                orderXmlModeNew.setPickUpPlaceCode(orderXmlMode.getPickUpPlaceCode());
                orderXmlModeNew.setReturnPlaceCode(orderXmlMode.getReturnPlaceCode());
                orderXmlModeNew.setResLog(orderXmlMode.getResLog());
                orderXmlModeNew.setXmlPickUpPlaceCode(substring1);
                orderXmlModeNew.setXmlReturnPlaceCode(substring2);
                dataList.add(orderXmlModeNew);
            }
        }
        System.out.println("总数："+dataList.size());
        EasyExcel.write("/Users/shixin.qin/Documents/bei/budget_place_not_match_2.xlsx", OrderXmlMode.class).sheet("sheet1").doWrite(dataList);

    }


    @Test
    void name14(){
       String str = "<LocationDetails AtAirport=\"true\" Code=\"BNE\" Name=\"BRISBANE APO\" CodeContext=\"Pickup Location\" ExtendedLocationCode=\"BNET01\">\n" +
                "                                <Address>\n" +
                "                                    <StreetNmbr>BRISBANE AIRPORT TERMINAL</StreetNmbr>\n" +
                "                                    <AddressLine>EAGLE FARM</AddressLine>\n" +
                "                                    <CityName>BRISBANE QLD</CityName>\n" +
                "                                    <PostalCode>4007</PostalCode>\n" +
                "                                    <StateProv StateCode=\"QL\"/>\n" +
                "                                    <CountryName Code=\"AU\"/>\n" +
                "                                </Address>\n" +
                "                                <Telephone PhoneNumber=\"07 3000 1030\"/>\n" +
                "                            </LocationDetails>\n" +
                "                            <LocationDetails AtAirport=\"true\" Code=\"BNE\" Name=\"BRISBANE APO\" CodeContext=\"Return Location\" ExtendedLocationCode=\"BNET01\">";
        String[] split = str.split("ExtendedLocationCode=\"");
        String substring1 = split[1].substring(0, 6);
        String substring2 = split[2].substring(0, 6);
        System.out.println(substring1);
        System.out.println(substring2);
    }

    @Test
    void name14_2(){
        String str = "<?xml version=\"1.0\"?>\n" +
                "<SOAP-ENV:Envelope\n" +
                "    xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <SOAP-ENV:Body>\n" +
                "        <ns:Response TID=\"203c2418-9826-4341-a08d-0f2d139860e5\"\n" +
                "            xmlns:ns=\"http://wsg.avis.com/wsbang\">\n" +
                "            <OTA_VehRateRuleRS xsi:schemaLocation=\"http://www.opentravel.org/OTA/2008/05 OTA_VehRateRuleRS\" Target=\"Production\"\n" +
                "                xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "                xmlns=\"http://www.opentravel.org/OTA/2003/05\">\n" +
                "                <Success/>\n" +
                "                <VehRentalCore PickUpDateTime=\"2024-04-26T11:00:00\" ReturnDateTime=\"2024-06-02T11:00:00\">\n" +
                "                    <PickUpLocation LocationCode=\"BGSC1\" CodeContext=\"IATA\"/>\n" +
                "                    <ReturnLocation LocationCode=\"BGSC1\" CodeContext=\"IATA\"/>\n" +
                "                </VehRentalCore>\n" +
                "                <Vehicle AirConditionInd=\"true\" TransmissionType=\"Automatic\">\n" +
                "                    <VehType VehicleCategory=\"3\"/>\n" +
                "                    <VehClass Size=\"6\"/>\n" +
                "                    <VehGroup GroupType=\"SIPP\" GroupValue=\"IFAR\"/>\n" +
                "                    <VehMakeModel Name=\"Group F - Toyota Rav4 or similar\" Code=\"IFAR\"/>\n" +
                "                    <PictureURL>2022-toyota-rav4-xle-premium-suv-blue.png</PictureURL>\n" +
                "                </Vehicle>\n" +
                "                <RentalRate>\n" +
                "                    <RateDistance Unlimited=\"true\" DistUnitName=\"Mile\" VehiclePeriodUnitName=\"RentalPeriod\"/>\n" +
                "                    <VehicleCharges>\n" +
                "                        <VehicleCharge TaxInclusive=\"true\" Description=\"TAX/FEES/SURCH LDW $500K TPL; MAX=059DY\" GuaranteedInd=\"false\" IncludedInRate=\"true\" Amount=\"871.32\" CurrencyCode=\"GBP\" Purpose=\"1\">\n" +
                "                            <TaxAmounts>\n" +
                "                                <TaxAmount Total=\"0.00\" CurrencyCode=\"GBP\" Description=\"Taxes and surcharges\"/>\n" +
                "                            </TaxAmounts>\n" +
                "                            <Calculation UnitCharge=\"165.44\" UnitName=\"Week\" Quantity=\"5\"/>\n" +
                "                        </VehicleCharge>\n" +
                "                        <VehicleCharge TaxInclusive=\"false\" Description=\"Additional Day Charge\" GuaranteedInd=\"false\" IncludedInRate=\"true\" Amount=\"22.06\" CurrencyCode=\"GBP\" Purpose=\"10\">\n" +
                "                            <Calculation UnitCharge=\"22.06\" UnitName=\"Day\" Quantity=\"2\"/>\n" +
                "                        </VehicleCharge>\n" +
                "                        <VehicleCharge TaxInclusive=\"false\" Description=\"$10.00 EXTENSION FEE MAY APPLY\" IncludedInRate=\"false\" Amount=\"0.00\" CurrencyCode=\"GBP\" Purpose=\"28\"/>\n" +
                "                        <VehicleCharge TaxInclusive=\"false\" Description=\"FUEL POLICY IS FULL TO FULL\" IncludedInRate=\"false\" Amount=\"0.00\" CurrencyCode=\"GBP\" Purpose=\"28\"/>\n" +
                "                        <VehicleCharge TaxInclusive=\"false\" Description=\"$20.00 LATE FEE MAY APPLY\" IncludedInRate=\"false\" Amount=\"0.00\" CurrencyCode=\"GBP\" Purpose=\"28\"/>\n" +
                "                        <VehicleCharge TaxInclusive=\"false\" Description=\"VEH. LICENSE FEE\" IncludedInRate=\"true\" Amount=\"0.00\" CurrencyCode=\"GBP\" Purpose=\"6\"/>\n" +
                "                    </VehicleCharges>\n" +
                "                    <RateQualifier RateCategory=\"3\" CorpDiscountNmbr=\"H004400\" RateQualifier=\"BB\"/>\n" +
                "                </RentalRate>\n" +
                "                <TotalCharge RateTotalAmount=\"871.32\" EstimatedTotalAmount=\"871.32\" CurrencyCode=\"GBP\"/>\n" +
                "                <PricedEquips>\n" +
                "                    <PricedEquip>\n" +
                "                        <Equipment EquipType=\"119\"/>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"103.42\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"10.34\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedEquip>\n" +
                "                    <PricedEquip>\n" +
                "                        <Equipment EquipType=\"9\"/>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"133.65\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"11.14\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedEquip>\n" +
                "                    <PricedEquip>\n" +
                "                        <Equipment EquipType=\"7\"/>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"133.65\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"11.14\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedEquip>\n" +
                "                    <PricedEquip>\n" +
                "                        <Equipment EquipType=\"8\"/>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"133.65\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"11.14\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedEquip>\n" +
                "                    <PricedEquip>\n" +
                "                        <Equipment EquipType=\"151\"/>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"7.96\" CurrencyCode=\"GBP\"/>\n" +
                "                    </PricedEquip>\n" +
                "                    <PricedEquip>\n" +
                "                        <Equipment EquipType=\"13\"/>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"112.55\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"4.37\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedEquip>\n" +
                "                    <PricedEquip>\n" +
                "                        <Equipment EquipType=\"103\"/>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"224.05\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"6.36\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedEquip>\n" +
                "                    <PricedEquip>\n" +
                "                        <Equipment EquipType=\"238\"/>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"55.67\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"6.36\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedEquip>\n" +
                "                </PricedEquips>\n" +
                "                <PricedCoverages>\n" +
                "                    <PricedCoverage>\n" +
                "                        <Coverage CoverageType=\"63\" Code=\"TPL\">\n" +
                "                            <Details CoverageTextType=\"Description\">Third Party Liability (TPL)</Details>\n" +
                "                        </Coverage>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"true\" Amount=\"0.00\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"0.00\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedCoverage>\n" +
                "                    <PricedCoverage>\n" +
                "                        <Coverage CoverageType=\"35\" Code=\"PEP\">\n" +
                "                            <Details CoverageTextType=\"Description\">Personal Effects Protection (PEP)</Details>\n" +
                "                        </Coverage>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"147.18\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"3.98\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedCoverage>\n" +
                "                    <PricedCoverage>\n" +
                "                        <Coverage CoverageType=\"7\" Code=\"CDW\">\n" +
                "                            <Details CoverageTextType=\"Description\">Collision Damage Waiver (CDW)</Details>\n" +
                "                        </Coverage>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"true\" Amount=\"0.00\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"0.00\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedCoverage>\n" +
                "                    <PricedCoverage>\n" +
                "                        <Coverage CoverageType=\"32\" Code=\"PAI\">\n" +
                "                            <Details CoverageTextType=\"Description\">Personal Accident / Effects (PAI)</Details>\n" +
                "                        </Coverage>\n" +
                "                        <Charge TaxInclusive=\"false\" IncludedInRate=\"false\" Amount=\"292.88\" CurrencyCode=\"GBP\">\n" +
                "                            <Calculation UnitCharge=\"7.92\" UnitName=\"Daily\"/>\n" +
                "                        </Charge>\n" +
                "                    </PricedCoverage>\n" +
                "                </PricedCoverages>\n" +
                "                <LocationDetails AtAirport=\"false\" Code=\"BGSC1\" Name=\"SANTA CLARA\" CodeContext=\"Rental Location\" ExtendedLocationCode=\"SJCS03\">\n" +
                "                    <Address>\n" +
                "                        <StreetNmbr>2390 LAFAYETTE ST</StreetNmbr>\n" +
                "                        <CityName>SANTA CLARA</CityName>\n" +
                "                        <PostalCode>95050</PostalCode>\n" +
                "                        <StateProv StateCode=\"CA\"/>\n" +
                "                        <CountryName Code=\"US\"/>\n" +
                "                    </Address>\n" +
                "                    <Telephone PhoneNumber=\"408-445-2330\"/>\n" +
                "                </LocationDetails>\n" +
                "                <VendorMessages>\n" +
                "                    <VendorMessage Title=\"Policy Information\">\n" +
                "                        <SubSection SubTitle=\"Age Requirements\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">A customer renting a Luxury Car, Minivan, 12-Passenger Van, Full-Size SUV, Premium SUV, Standard Elite SUV or any vehicle in the Specialty/Street Fleet/Cool Car categories must be at least 25 years old. Exception: A customer with an acceptable corporate discount number can rent the above car classes at 21 years old. An underage surcharge of $27.00 per day will apply for renters 21-24 years old. For all other car classes, a customer presenting a major charge card or Budget charge card must be at least 21 years old. An underage surcharge of $27.00 per day will apply for renters 21-24 years old. A customer presenting an acceptable bank debit card must be at least 25 years old. (See Debit Card Policies for acceptable car groups and full details.) Exception: A customer presenting an acceptable bank debit card with an acceptable corporate discount number can rent at 21 years old. An underage surcharge of $27.00 per day will apply for renters 21-24 years old. A customer presenting a government-issued Visa/MasterCard or government travel/purchase orders can rent any car class at age 18 without an underage fee.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"e-Tolls\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\"> E-TOLL COLLECTION DEVICE AVAILABILITY TOLL PAYMENT TAG PASS You are responsible for payment of all tolls incurred during the rental period. We offer an optional service called e-toll that allows customers to use electronic toll lanes on highways, bridges, tunnels and other tolled passages. All vehicles are pre-equipped to electronically process tolls. If you do not pay cash for tolls, you automatically opt into our e-toll service to which you agree to pay us or our toll program administrator with whom we will share your credit card/debit information for all tolls incurred during your rental and all related fees, charges and penalties. The e-toll fees may take 4-8 weeks after the rental to be billed to your credit card/debit card on file. E-TOLL SERVICE CONVENIENCE FEES The convenience fee for e-Toll usage is $3.95 up to $5.95 USD for each day you use the E-Toll device and there is a maximum of $19.75 up to $29.75 USD per rental month, plus toll charges. There are NO service charges if e-Toll is NOT USED during the rental duration. E-TOLL UNLIMITED Available at participating locations in the following states: California, Colorado, Connecticut, Delaware, District of Columbia, Florida, Georgia, Illinois, Indiana, Kentucky, Main, Maryland, Massachusetts, New Hampshire, New Jersey, New York, North Carolina, Oklahoma, Ohio, Pennsylvania, Rhode Island, Texas, Vermont, Washington and West Virginia. If you use the unlimited E-Toll service, all cost of tolls and convenience Fees are included. For this service, you pay a flat fee of $10.99 up to $23.99 USD per each day of the rental period, regardless of whether or not you incur any tolls, or a flat fee of $54.95 up to $119.95 USD per week. E-toll unlimited must be purchased at the beginning of the rental. OPTING OUT OF E-TOLL If you do not choose e-toll unlimited at the time of rental, you may avoid the standard e-toll fees on any given day during the term of the rental if you ensure the transponder shield box is in the \"closed\" position and you pay cash for all tolls, use your own adequately funded, properly mounted and compatible electronic toll device to pay for all tolls, or pay the toll authority directly and follow the toll authority rules and requirements. For more information on tolling, please visit budget.com/etoll or check at the time of rental.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Additional Driver Policy\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">All additional drivers must be at least 25 years of age. The fee for each additional driver is $13/day with a maximum charge of $65 per rental. In California, the following may drive the car with the renters permission, are exempt from the additional driver fee, but must meet all other driver requirements: the renter's employer or fellow employee when on company business and renting under a corporate account; the renter's spouse or domestic partner; child, sibling or grandparent of the renter; the companion driver of a renter with a disability who has completed the non-licensed renter form; for insurance replacement rentals, company employees or family members who are designated on the insurance policy. All other additional drivers must complete and sign an additional driver form and present valid credit identification. A maximum of two additional drivers may complete a form. </ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Directions\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">FASTBREAK SERVICE Local Office Instructions - Counter Location Proceed to Budget rental counter. Show your drivers license and identify yourself as a Fastbreak member. The Budget rental agent will provide you with your preprinted contract and keys. *GENERAL DIRECTIONS* Local Office Instructions COUNTER: The office is located on the corner of Lafayette St and Richard Ave on the west side of Lafayette St. The location is between Shulman Ave and Richard Ave. CARS: On-site. RETURNS: Same as pick-up. AFTER-HOURS RETURNS: Not available.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Drivers License\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">At time of rental, driver must present a valid drivers license in the drivers name. All drivers must have a safe driving record. You may be asked to sign a driving record addendum or be subject to a computerized Department of Motor Vehicles check. Based upon such search, Budget reserves the right to deny a rental opportunity. Customers with a drivers license in a non-Roman alphabet may be asked to present an international driving permit (IDP) for translation purposes.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Towing\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">Towing is not permitted. Vehicles cannot be used to tow or push anything. A trailer hitch cannot be installed on the vehicles.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Fuel Policy\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\"> Fuel Service Charge. Most rentals come with a full tank of fuel, but that is not always the case. Where available, if permitted by law, if you drive less than 75 miles, you acknowledge that we will add a flat fee to the rental, the amount of which will be disclosed on the Rental Contract and at the counter prior to rental. You may avoid this charge at time of return by providing a receipt for fuel purchased at which time the flat fee will be reversed from your total rental charges. If this subparagraph (a) does not apply, there are three refueling options: 1) If you do not accept the fuel service option, where available, at the beginning of your rental, and you return the car with less fuel than was in it when you received it, as we determine in our sole discretion, we will charge you a fuel service charge at the applicable rate per-mile or rate per-gallon specified on the Rental Contract or disclosed at the location. The per-mile rate is used if you do not buy fuel during the rental. To calculate this amount, we multiply the number of miles driven, as shown on the car?s odometer (or provided by the vehicle's telematics device), times the per-mile rate shown on the Rental Contract. The per gallon rate is used if you buy fuel during the rental and provide us with a receipt on our request, but the tank is not as full when you return the car as when you received the car (by using the factory installed gauge, rounded down to the nearest 1/8 tank), times the per-gallon rate shown on the Rental Contract. Although two methods are used for ease of calculation, the per mile and per-gallon rates produce approximately the same result. Some of our cars are equipped with onboard telematics which record the actual amounts of fuel in the gas tank. In the event your car has such a device, you will be charged for the actual amount of gasoline needed to fill the tank based on the reading of this device. 2) If you accept the fuel service option at the beginning of your rental, you will be charged as shown on the Rental Contract for that purchase and you will not pay us a fuel service charge. If you choose this option, you will not incur an additional fuel service charge, but you will not receive any credit for fuel left in the tank at the time of return. If you accept the partial fuel service option at the beginning of your rental, you will be charged as shown on the Rental Contract for that purchase and you will pay a fuel service charge for any fuel not covered by the partial fuel service option. The per-gallon cost of the fuel service option will always be lower than the fuel service charge. The cost of refueling the car yourself at a local service station may be lower than the fuel service charge or the fuel service option. You acknowledge that the fuel service charge is not a retail sale of fuel. 3) You may avoid a fuel service charge if you return the car with the fuel tank as full as when you received it and, if requested by us, present a receipt for your fuel purchase. If you put fuel into the car, you must use the correct fuel (having the grade of gasoline stated on the car fuel information decal, or on-road diesel). Do not use ethanol fuel even if the car states that it is a flex-fuel vehicle. </ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Travel Into Other States\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">Vehicles at this location can be driven throughout the continental U.S. with no restrictions. Based on availability, one-way rentals may be allowed to some U.S. cities.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Credit Card Policies\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">Budget accepts most major credit cards as credit identification at the time of rental. The renter's name must be on the credit card. Accepted credit card list: Budget Charge Card, Budget International, American Express, Diner's Club, Diner's Club International, Discover, China UnionPay, JCB, MasterCard, Optima, and Visa. Some locations may not accept each of the referenced cards. You may be subject to a credit check or present additional identification. Acceptable forms of additional identification are: Valid passport or travel visa, military identification, birth certificate, marriage license, ATM card with customer name printed on face of card, health care identification card with customer?s name printed on face of card or company/college/university identification with a photograph. In some cases, you may be required to present an alternate credit card. Budget may request an authorization hold against your account for the estimated rental charges of the rental, but reserves the right in its sole discretion to request an extra value to be based on certain factors as we deem appropriate: Most rentals may require an authorization hold of the estimated rental charges plus $200.00 USD. If you have prepaid the rental, the hold amount will be $250.00 USD. While this hold is in place, the funds will not be available for your use. When the rental is over, we will process the reversal, but the bank may take time to post it back to the account. Note: Prepaid credit cards are not acceptable methods of credit identification to pick up a car at any location. One of the above mentioned cards must be presented. Prepaid credit cards are accepted at time of return only, if we can obtain full authorization from the card bank for the total charges due.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Public Liability and Property Damage\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">Information not available at this time, please check at time of rental. Public liability insurance is with in accordance of the insurance laws of the country. Customer may inquire for additional information of the benefits, conditions and acceptance at the time of rental. Note: Some credit cards provide insurance coverage, with certain limitations, as a benefit of using the card to rent vehicles. The customer is advised to contact the card issuer before the rental. Any waiver must be discussed directly with the location upon arrival. Information not available at this time, please check at time of rental.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Additional Fees and Credit Holds\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">Additional Fees Except for a surcharge for renters under 25 years of age at some locations, your total rental rate is calculated based on the information provided at time of reservation. The rate is based on the exact parameters (location, dates, etc.) of your particular rental, so changing any of your confirmed reservation parameters could result in different rates, taxes and fees. Quoted taxes and fees are subject to change which will affect your final total due at rental return. Vehicles are rented on a daily (24-hour) basis with a 29-minute grace period for returns. After 30 minutes late, a 3/4-day late charge + $.01 + taxes apply. After 90 minutes late, full-day late charges + taxes apply. If you reserve any type of vehicle that requires a credit card hold and you no longer require the rental, you must cancel the reservation before the scheduled pick-up time or you will be charged a $75.00 fee ($50.00 for a budget.com paid rental). The U.S. Government imposes a $5.00 per day Admin Rate Supplement (GARS) for U.S. Government rentals. If you selected an Optional Product (coverages, GPS, child safety seat, Roadside SafetyNet, XM Radio, etc.) with your rental: Prohibited use of your rental vehicle will void the Roadside SafetyNet Option. The Fuel Service Option price is not included in your reservation total. The prevailing market rate for fuel plus associated tax and fees will be charged when you return your car. You are responsible for replacement costs if the Optional Product or its components are lost, stolen or damaged. Products are charged on a daily (24-hour) basis. There is no grace period for returns of Optional Products, so full-day late charges will apply. If you used a coupon for your reservation: Most savings are reflected in your quoted rate. If your rental meets all coupon terms, any additional rate adjustment will be made when you pick up your car. For an upgrade, your reserved car will be upgraded at time of rental subject to car availability. This upgrade may not be used in conjunction with any other coupon, promotion or deal. The value of your coupon has been deducted from the base rate (time and mileage) charges and is reflected in the approximate total. Paper coupons, however, may not be applied online and should be presented at the counter. Please refer to all coupons for the full terms. Restrictions may apply. Once you have picked up the car, if you wish to extend the rental return date past your originally scheduled time, you must call 800-824-6287. A service fee of $10.00 will apply and your original per-day rental rate may change. If you don't call to extend your rental within 7 hours of your originally scheduled return time, a late fee will apply. A Frequent Traveler Program Surcharge or Excise Tax of up to $1.50 per day may be applied and will be assessedin connection with miles, points or credits earned pursuant to this reservation. Credit Holds For authorization hold limits, please see the following topics: Credit Card Policies Debit Card Policies </ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Required Credentials\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">At time of pickup, all drivers must present a valid driver's license in their name (see \"Driver's License Requirements\" section for complete details). - If the driver's license is not issued from the U.S., then the renter must also present a valid passport or Canadian enhanced license and a travel itinerary showing proof of return to the resident country. - If the country of residence on the renter's credentials does not match the country indicated on the reservation, the rental rate will change. - You may be asked to sign a driving record addendum or be subject to a computerized Department of Motor Vehicles check. All drivers must have a safe driving record or Budget reserves the right to deny the rental. At time of pickup, renter must present one of the following credentials: - A valid credit card in the renter's name. See the \"Credit Card Policies\" section for complete details. - A valid debit card in the renter's name (at participating locations). If using a debit card, additional documentation may be required. See the \"Debit Card Policies\" section for complete details. Note: A prepaid, loadable charge card or gift card is not an acceptable credential. When using some offer codes, you must provide association, corporate, or government credentials to prove eligibility for the special rate/benefit. Keep your reservation confirmation number because it is necessary for modification, cancellation or refund requests. Once your scheduled pick-up time passes, reservation changes cannot be made online.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Travel Into Other Countries\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">Canada: Vehicles may be driven into Canada with no restrictions. The rental counter must be notified at the time of the rental that you plan to drive into Canada so the location can provide a copy of the ?Canadian Non-Resident Insurance Card? or you may download card click here. (Provided at no cost). Based on availability, one-way rentals may be allowed to some Canada cities. Mexico: Vehicles rented at this location are not allowed to travel into Mexico. </ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Optional Coverages\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">Acceptance of Loss Damage Waiver relieves the renter and authorized additional drivers of financial responsibility if the Budget car is damaged or stolen while under rental contract. Using the vehicle in violation of any of the use restrictions listed on the rental agreement could void LDW and leave the renter fully responsible for any damage to the vehicle. LDW is not available in all states and certain restrictions may apply in some states. At the time of rental, the customer must initial whether he/she accepts or declines the LDW and/or other optional services. LDW and other optional services must be signed for at the rental counter. If LDW is not accepted, the customer may be responsible for up to the full fair market value of the car if it is damaged, vandalized or stolen during the rental. The customer may also be responsible for reimbursing Budget for the revenue lost by not being able to use the car while it is being repaired or not recovered due to theft (referred to as Loss Of Use).</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                        <SubSection SubTitle=\"Debit Card Policies\">\n" +
                "                            <Paragraph>\n" +
                "                                <ListItem Formatted=\"true\" TextFormat=\"PlainText\">Participating locations will accept a Debit card, however, it must have an accepted bank processing logo (Visa, MasterCard, AMEX, Discover, etc) at the time of rental if you are at least 25 years of agewith the following requirements: The name of the renter must be on the debit card. A debit card is accepted as credit identification for Economy cars, Compact cars, Intermediate cars, Standard cars, Full-Size cars, Intermediate SUV?s, or Standard SUV?s. The rental of any other vehicle category will require a major credit card. At airport locations, you will be required to show proof of a return airline flight that corresponds with your rental. You will be subject to a credit check to determine and ensure credit worthiness before releasing the car to you. If your credit file is frozen with Equifax, you will be required to lift the restriction prior to your rental. Lifting the restriction does not guarantee that you will be able to rent a vehicle as you will still be subject to a credit check. Budget will generally request an authorization hold against your account for the estimated rental charges of the rental, but reserves the right in its sole discretion to request an extra value to be based on certain factors as we deem appropriate: Most rentals may require an authorization hold of the estimated rental charges plus $200 USD. Rentals of 4 days or more may require an authorization hold of the estimated rental charges plus $300 USD. If you have prepaid the rental with a debit card, the hold is $250.00 USD. THESE FUNDS WILL NOT BE AVAILABLE FOR YOUR USE. When the rental is over, we will process the reversal, but the bank may take time to post it back to the account. If you fail to return the vehicle as agreed, Budget will obtain additional authorizations from your account to cover the rental charges. Budget is not responsible for any returned checks or overdraft fees based on this policy. Positive identification in addition to your driver's license may be required. Note: Prepaid Debit/Gift cards are not acceptable methods of credit identification to pick up a car at any location. Prepaid Debit/Gift cards are accepted at time of return only, if we can obtain full authorization from the card bank for the total charges due.</ListItem>\n" +
                "                            </Paragraph>\n" +
                "                        </SubSection>\n" +
                "                    </VendorMessage>\n" +
                "                </VendorMessages>\n" +
                "                <TPA_Extensions>\n" +
                "                    <Reference Type=\"Features\" ID=\"Smoke Free|4 Doors|5 Seats|Automatic|Air Conditioning|Holds 1 large, 1 small suitcases|Bluetooth SYNC|Back-up Camera|image=2022-toyota-rav4-xle-premium-suv-blue.png|thumb=2022-toyota-rav4-xle-premium-suv-blue.png|cargroup=F|category=Intermediate SUV\"/>\n" +
                "                </TPA_Extensions>\n" +
                "            </OTA_VehRateRuleRS>\n" +
                "            <!-- Processed by Direct Connect ver. 15.61 build 0 -->\n" +
                "        </ns:Response>\n" +
                "    </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>\n";
        String[] splitOrderPick = str.split("<PickUpLocation LocationCode=\"");
        int index = splitOrderPick[1].indexOf("\"");
        System.out.println(splitOrderPick[1].substring(0, index));
    }

    @Test
    void name15() throws FileNotFoundException {
        String readPath1 = "/Users/shixin.qin/Documents/bei/budget2_订单报文1.xlsx";
        Sheet sheet1 = new Sheet(1, 1, OrderCreateXmlMode.class);
        List<Object> readList1 = EasyExcelFactory.read(new FileInputStream(readPath1), sheet1);
        Sheet sheet3 = new Sheet(3, 1, OrderSearchXmlMode.class);
        List<Object> readList3 = EasyExcelFactory.read(new FileInputStream(readPath1), sheet3);

        List<OrderCreateAndSearchXmlMode> dataList = new ArrayList<>();

        List<OrderSearchXmlMode> orderSearchXmlModeList = readList3.stream().map(o -> (OrderSearchXmlMode) o).collect(Collectors.toList());

        for (Object obj : readList1){
            OrderCreateXmlMode createXmlMode = (OrderCreateXmlMode) obj;
            OrderCreateAndSearchXmlMode orderCreateAndSearchXmlMode = new OrderCreateAndSearchXmlMode();
            orderCreateAndSearchXmlMode.setHzcOrderId(createXmlMode.getHzcOrderId());
            orderCreateAndSearchXmlMode.setPickUpPlaceCode(createXmlMode.getPickUpPlaceCode());
            orderCreateAndSearchXmlMode.setReturnPlaceCode(createXmlMode.getReturnPlaceCode());
            orderCreateAndSearchXmlMode.setOrderResLog(createXmlMode.getOrderResLog());
            orderCreateAndSearchXmlMode.setMsg_id(createXmlMode.getMsg_id());
            Optional<OrderSearchXmlMode> OTA_VehRateRuleRQ = orderSearchXmlModeList.stream().filter(o -> o.getMsg_id().equals(createXmlMode.getMsg_id()) && o.getAction().equals("OTA_VehRateRuleRQ")).findFirst();
            if(OTA_VehRateRuleRQ.isPresent()){
                orderCreateAndSearchXmlMode.setAction("OTA_VehRateRuleRQ");
                orderCreateAndSearchXmlMode.setSearchResLog(OTA_VehRateRuleRQ.get().getSearchResLog());
            }
            Optional<OrderSearchXmlMode> ota_vehAvailRateRQ = orderSearchXmlModeList.stream().filter(o -> o.getMsg_id().equals(createXmlMode.getMsg_id()) && o.getAction().equals("OTA_VehAvailRateRQ")).findFirst();
            if(ota_vehAvailRateRQ.isPresent()){
                orderCreateAndSearchXmlMode.setAction("OTA_VehAvailRateRQ");
                orderCreateAndSearchXmlMode.setSearchResLog(ota_vehAvailRateRQ.get().getSearchResLog());
            }
            dataList.add(orderCreateAndSearchXmlMode);
        }

        System.out.println("总数："+dataList.size());
        EasyExcel.write("/Users/shixin.qin/Documents/bei/budget2_order_search_xmlLog_1.xlsx", OrderCreateAndSearchXmlMode.class).sheet("sheet1").doWrite(dataList);

    }

    @Test
    void name16() throws FileNotFoundException {
        String readPath1 = "/Users/shixin.qin/Documents/bei/budget2_order_search_xmlLog_1.xlsx";
        Sheet sheet1 = new Sheet(1, 1, OrderCreateAndSearchXmlMode.class);
        List<Object> readList1 = EasyExcelFactory.read(new FileInputStream(readPath1), sheet1);

        List<OrderNotMatchXmlMode> dataList = new ArrayList<>();

        for (Object obj : readList1){
            OrderCreateAndSearchXmlMode model = (OrderCreateAndSearchXmlMode) obj;
            System.out.println("hzcId:"+ model.getHzcOrderId());
            String[] splitOrderPick = model.getOrderResLog().split("<PickUpLocation LocationCode=\"");
            String orderPickUpLocationCode = splitOrderPick[1].substring(0, splitOrderPick[1].indexOf("\""));
            String[] splitOrderReturn = model.getOrderResLog().split("<ReturnLocation LocationCode=\"");
            String orderReturnLocationCode = splitOrderReturn[1].substring(0, splitOrderReturn[1].indexOf("\""));

            String[] splitSearchPick = model.getSearchResLog().split("<PickUpLocation LocationCode=\"");
            String searchPickUpLocationCode = splitSearchPick[1].substring(0, splitSearchPick[1].indexOf("\""));
            String[] splitSearchReturn = model.getSearchResLog().split("<ReturnLocation LocationCode=\"");
            String searchReturnLocationCode = splitSearchReturn[1].substring(0, splitSearchReturn[1].indexOf("\""));

            if(!orderPickUpLocationCode.equals(searchPickUpLocationCode) || !orderReturnLocationCode.equals(searchReturnLocationCode)){
                OrderNotMatchXmlMode orderNotMatchXmlMode = new OrderNotMatchXmlMode();
                BeanUtils.copyProperties(model, orderNotMatchXmlMode);
                String[] orderExtendedSplit = model.getOrderResLog().split("ExtendedLocationCode=\"");
                orderNotMatchXmlMode.setOrderPickUpLocationCode(orderPickUpLocationCode);
                orderNotMatchXmlMode.setOrderReturnLocationCode(orderReturnLocationCode);
                orderNotMatchXmlMode.setOrderPickUpExtendedLocationCode(orderExtendedSplit[1].substring(0, 6));
//                if(orderExtendedSplit.length > 2){
                    orderNotMatchXmlMode.setOrderReturnExtendedLocationCode(orderExtendedSplit[2].substring(0, 6));
                //}
                orderNotMatchXmlMode.setSearchPickUpLocationCode(searchPickUpLocationCode);
                orderNotMatchXmlMode.setSearchReturnLocationCode(searchReturnLocationCode);
                String[] searchExtendedSplit = model.getSearchResLog().split("ExtendedLocationCode=\"");
                orderNotMatchXmlMode.setSearchPickUpExtendedLocationCode(searchExtendedSplit[1].substring(0,6));
                if(searchExtendedSplit.length > 2){
                    orderNotMatchXmlMode.setSearchReturnExtendedLocationCode(searchExtendedSplit[2].substring(0,6));
                }
                dataList.add(orderNotMatchXmlMode);
            }
        }
//        List<OrderNotMatchXmlMode> collect = dataList.stream().sorted(Comparator.comparing(OrderNotMatchXmlMode::getHzcOrderId)).collect(Collectors.toList());

        System.out.println("总数："+dataList.size());
        EasyExcel.write("/Users/shixin.qin/Documents/bei/budget2_order_not_match_t_2.xlsx", OrderNotMatchXmlMode.class).sheet("sheet1").doWrite(dataList);

    }

}
