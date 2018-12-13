package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbGoods;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.group.Goods;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */

	void update(Goods goods);

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);
	/*
	* @Description: //修改商品审核状态
	* @param: [ids, status]
	* @return: void
	* @author: TanZhiTong
	* @Date: 2018/12/5
	*/
	public void updateStatus(Long[] ids,String status);
	/*
	* @Description: 根据SPUID查询SPU列表
	* @param: [Ids, status]
	* @return: java.util.List<com.pinyougou.pojo.TbItem>
	* @author: TanZhiTong
	* @Date: 2018/12/11
	*/
	List<TbItem> findItemListByGoodsIdandStatus(Long[] Ids,String status);
	
}
