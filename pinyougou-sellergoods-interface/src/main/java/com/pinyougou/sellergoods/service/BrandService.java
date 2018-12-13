package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @creat time: 2018年11月27日 22:00
 * @author: MRT
 * @description:品牌接口
 **/
public interface BrandService {
    List<TbBrand> findAll();
    PageResult findPage(int pageNum, int pageSize);
    TbBrand findOne(Long id);
    void add(TbBrand tbBrand);
    void update(TbBrand tbBrand);
    void delete(long[] id);
    PageResult findPage(TbBrand brand,int pageNum, int pageSize);
    List<Map> selectOptionList();
}
