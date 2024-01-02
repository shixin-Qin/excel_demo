package com.example.mode;

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
public class OrderXmlMode extends BaseRowModel{

    @ExcelProperty(index = 0)
    private String hzcOrderId;
    
    @ExcelProperty(index = 1)
    private String pickUpPlaceCode;

    @ExcelProperty(index = 2)
    private String returnPlaceCode;

    @ExcelProperty(index = 3)
    private String resLog;

    @ExcelProperty(index = 4)
    private String xmlPickUpPlaceCode;

    @ExcelProperty(index = 5)
    private String xmlReturnPlaceCode;

}
