package com.example.mode;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description
 * @Author qinshixin
 * @Date 2023/8/14 18:48
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
public class MsgVo {
    @ExcelProperty(value = "orderId", index = 0)
    private  String orderId;
    @ExcelProperty(value = "remark", index = 1)
    private String remark;
    @ExcelProperty(value = "orderId2", index = 2)
    private  String orderId2;

    @ExcelProperty(value = "Id", index = 3)
    private  String id;
}
