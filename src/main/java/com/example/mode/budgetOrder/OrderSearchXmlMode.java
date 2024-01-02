package com.example.mode.budgetOrder;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [一句话描述该类的功能]
 *
 * @author qinshixin
 * @version 1.0.0
 * @createTime 2022/5/2 17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchXmlMode extends BaseRowModel{

    @ExcelProperty(index = 0)
    private String action;
    
    @ExcelProperty(index = 1)
    private String searchResLog;

    @ExcelProperty(index = 2)
    private String msg_id;

}
