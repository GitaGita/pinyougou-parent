package com.pinyougou.pojo;

import java.io.Serializable;
//品牌表
public class TbBrand implements Serializable{
    //主键
    private Long id;
    //品牌名称
    private String name;
    //品牌首字母
    private String firstChar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }
}