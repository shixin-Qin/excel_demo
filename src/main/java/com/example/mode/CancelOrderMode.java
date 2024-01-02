package com.example.mode;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * [一句话描述该类的功能]
 *
 * @author qinshixin
 * @version 1.0.0
 * @createTime 2022/3/18 11
 */
public class CancelOrderMode extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String orderId;

    public CancelOrderMode() {
    }

    
    @ExcelProperty(index = 1)
    private String orderPNR;

    public CancelOrderMode(String orderId, String orderPNR) {
        this.orderId = orderId;
        this.orderPNR = orderPNR;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderPNR() {
        return orderPNR;
    }

    public void setOrderPNR(String orderPNR) {
        this.orderPNR = orderPNR;
    }
}
