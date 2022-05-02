package com.example.excel_demo;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.example.mode.ExcelMode;
import com.example.mode.SkuSuppilerMode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
}
