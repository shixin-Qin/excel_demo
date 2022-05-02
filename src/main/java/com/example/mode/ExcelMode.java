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
public class ExcelMode extends BaseRowModel {
    
    @ExcelProperty(index = 0)
    private Integer doctorId;
    
    public ExcelMode() {
    }
    
    public ExcelMode(Integer doctorId, String name, String pre, String have) {
        this.doctorId = doctorId;
        this.name = name;
        this.pre = pre;
        this.have = have;
    }
    
    public Integer getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPre() {
        return pre;
    }
    
    public void setPre(String pre) {
        this.pre = pre;
    }
    
    public String getHave() {
        return have;
    }
    
    public void setHave(String have) {
        this.have = have;
    }
    
    @ExcelProperty(index = 1)
    private String name;
    
    @ExcelProperty(index = 2)
    private String pre;
    
    
    @ExcelProperty(index = 3)
    private String have;
}
