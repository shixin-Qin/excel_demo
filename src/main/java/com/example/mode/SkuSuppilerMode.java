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
public class SkuSuppilerMode extends BaseRowModel{
    @ExcelProperty(index = 0)
    private String skuId;
    
    @ExcelProperty(index = 1)
    private Integer supplierId;
    
}
