package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @creat time: 2018年11月27日 22:10
 * @author: MRT
 * @description：运营商后台品牌Controller层
 **/
@RestController
@RequestMapping(value = "/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    /*
     * @Description: 查询所有品牌
     * @param: []
     * @return: java.util.List<com.pinyougou.pojo.TbBrand>
     * @author: TanZhiTong
     * @Date: 2018/11/27
     */
    @RequestMapping(value = "/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }
    /*
    * @Description: 查询品牌分页结果
    * @param: [pageNum, pageSize]
    * @return: entity.PageResult
    * @author: TanZhiTong
    * @Date: 2018/11/30
    */
    @RequestMapping("/findPage")
    public PageResult findPage(int pageNum,int pageSize){
        return brandService.findPage(pageNum, pageSize);
    }
    /*
    * @Description:根据唯一ID查询品牌
    * @param: [id]
    * @return: com.pinyougou.pojo.TbBrand
    * @author: TanZhiTong
    * @Date: 2018/11/30
    */
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }
    /*
    * @Description: 更新品牌
    * @param: [tbBrand]
    * @return: entity.Result
    * @author: TanZhiTong
    * @Date: 2018/11/30
    */
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand){
        try{
            brandService.update(tbBrand);
            return new Result(true,"成功");
        }catch (Exception ec){
            ec.printStackTrace();
            return new Result(false,"失败");
        }
    }
    /*
    * @Description: 增加品牌
    * @param: [tbBrand]
    * @return: entity.Result
    * @author: TanZhiTong
    * @Date: 2018/11/30
    */
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand){
        try{
            brandService.add(tbBrand);
            return new Result(true,"成功");
        }catch (Exception ec){
            ec.printStackTrace();
            return new Result(false,"失败");
        }
    }
    /*
    * @Description:删除
    * @param: [ids]
    * @return: entity.Result
    * @author: TanZhiTong
    * @Date: 2018/11/30
    */
    @RequestMapping("/delete")
    public Result delete(long[] ids){
        try{
            brandService.delete(ids);
            return new Result(true,"成功");
        }catch (Exception ec){
            ec.printStackTrace();
            return new Result(false,"失败");
        }
    }
    /*
    * @Description: 带条件的分页查询
    * @param: [brand, page, rows]
    * @return: entity.PageResult
    * @author: TanZhiTong
    * @Date: 2018/11/30
    */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand brand, int page, int rows  ){
        return brandService.findPage(brand, page, rows);
    }
    /*
    * @Description：
    * @param: []
    * @return: java.util.List<java.util.Map>
    * @author: TanZhiTong
    * @Date: 2018/12/1
    */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        return brandService.selectOptionList();
    }
}
