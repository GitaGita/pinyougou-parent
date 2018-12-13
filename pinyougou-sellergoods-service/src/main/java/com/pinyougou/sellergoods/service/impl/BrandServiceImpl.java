package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @creat time: 2018年11月27日 22:04
 * @author: MRT
 * @description
 **/
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper tbBrandMapper;

    /*
     * @Description:返回所有的品牌列表
     * @param: []
     * @return: java.util.List<com.pinyougou.pojo.TbBrand>
     * @author: TanZhiTong
     * @Date: 2018/11/30
     */
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    /*
     * @Description: 放回查询品牌分页结果
     * @param: [pageNum, pageSize]
     * @return: entity.PageResult
     * @author: TanZhiTong
     * @Date: 2018/11/30
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*
     * @Description:根据唯一id 查询品牌
     * @param: [id]
     * @return: com.pinyougou.pojo.TbBrand
     * @author: TanZhiTong
     * @Date: 2018/11/30
     */
    @Override
    public TbBrand findOne(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    /*
     * @Description:增加品牌
     * @param: [tbBrand]
     * @return: void
     * @author: TanZhiTong
     * @Date: 2018/11/30
     */
    @Override
    public void add(TbBrand tbBrand) {
        tbBrandMapper.insert(tbBrand);
    }

    /*
     * @Description: 更新品牌
     * @param: [tbBrand]
     * @return: void
     * @author: TanZhiTong
     * @Date: 2018/11/30
     */
    @Override
    public void update(TbBrand tbBrand) {
        int i=tbBrandMapper.updateByPrimaryKey(tbBrand);
    }

    /*
    * @Description:删除品牌
    * @param: [id]
    * @return: void
    * @author: TanZhiTong
    * @Date: 2018/11/30
    */
    @Override
    public void delete(long[] ids) {
       for(long id:ids){
           tbBrandMapper.deleteByPrimaryKey(id);
       }
    }
    /*
    * @Description: 带条件的分页查询
    * @param: [brand, pageNum, pageSize]
    * @return: entity.PageResult
    * @author: TanZhiTong
    * @Date: 2018/11/30
    */
    @Override
    public PageResult findPage(@RequestBody TbBrand brand, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample example=new TbBrandExample();
        TbBrandExample.Criteria criteria=example.createCriteria();
        if(brand!=null){
            if(brand.getName()!=null&&brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if(brand.getFirstChar()!=null && brand.getFirstChar().length()>0){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }
        Page<TbBrand> page= (Page<TbBrand>) tbBrandMapper.selectByExample(example);
        return new PageResult(page.getTotal(),page.getResult());
    }
    /*
    * @Description: 下拉框选择品牌列表
    * @param: []
    * @return: java.util.List<java.util.Map>
    * @author: TanZhiTong
    * @Date: 2018/12/1
    */
    @Override
    public List<Map> selectOptionList() {
        return tbBrandMapper.selectOptionList();
    }
}
