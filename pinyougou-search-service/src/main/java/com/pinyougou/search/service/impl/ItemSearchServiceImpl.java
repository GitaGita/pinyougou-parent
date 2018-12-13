package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @creat time: 2018年12月07日 09:29
 * @author: MRT
 * @description
 **/
@Service(timeout = 3000)
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Map<String, Object> search(Map searchMap) {
        String keyWords= (String) searchMap.get("keywords");
        searchMap.put("keywords",keyWords.replace(" ",""));
        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(searchList(searchMap));
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList", categoryList);
        String categoryName=(String)searchMap.get("category");
        if(!"".equals(categoryName)){//如果有分类名称
            map.putAll(searchBrandAndSpecList(categoryName));
        }else{//如果没有分类名称，按照第一个查询
            if(categoryList.size()>0){
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }
        return map;
    }

    private Map searchList(Map searchMap) {
        Map map = new HashMap();
        HighlightQuery query = new SimpleHighlightQuery();
        //高亮显示处理
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");//设置高亮的域
        highlightOptions.setSimplePrefix("<em style='color:red'>");//高亮前缀
        highlightOptions.setSimplePostfix("</em>");//高亮后缀
        query.setHighlightOptions(highlightOptions);//设置高亮选项
        //按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //按分类查询
        if(!"".equals(searchMap.get("category"))){
            Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filteQuety=new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filteQuety);
        }
        //按品牌查询
        if(!"".equals(searchMap.get("brand"))){
            Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filteQuety=new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filteQuety);

        }
        //按规格查询
        if(searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map) searchMap.get("spec");
            for(String key:specMap.keySet() ){
                Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
                FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //按价格过滤
        if(!"".equals(searchMap.get("price")) ){
            String[] price = ((String) searchMap.get("price")).split("-");
            if(!price[0].equals("0")){ //如果最低价格不等于0
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if(!price[1].equals("*")){ //如果最高价格不等于*
                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_price").lessThanEqual(price[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        String sortValue= (String) searchMap.get("sort");//Desc;Asc
        String sortField= (String) searchMap.get("sortField");//排序字段
        if(sortValue!=null&&!sortValue.equals("")){
            if(sortValue.equals("ASC")){
               Sort sort= new Sort(Sort.Direction.ASC,"item_"+sortField);
               query.addSort(sort);
            }
            if(sortValue.equals("DESC")){
                Sort sort=new Sort(Sort.Direction.DESC,"item_"+sortField);
                query.addSort(sort);
            }
        }
        //分页查询
        Integer pageNo= (Integer) searchMap.get("pageNo");
        if(pageNo==null){
            pageNo=1;
        }
        Integer pageSize= (Integer) searchMap.get("pageSize");
        if (pageSize==null){
            pageSize=20;
        }
        query.setOffset((pageNo-1)*pageSize);
        query.setRows(pageSize);
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        for (HighlightEntry<TbItem> h : page.getHighlighted()) {//循环高亮入口集合
            TbItem item = h.getEntity();//获取原实体类
            if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));//设置高亮的结果
            }
        }

        map.put("total",page.getTotalElements());
        map.put("totalPages",page.getTotalPages());
        map.put("rows", page.getContent());
        return map;
    }

    /*
     * @Description: 根据关键字搜索分类列表
     * @param: [searchMap]
     * @return: java.util.List
     * @author: TanZhiTong
     * @Date: 2018/12/7
     */
    private List searchCategoryList(Map searchMap) {
        List<String> list = new ArrayList<>();
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        GroupResult<TbItem> result = page.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> groupEntryPage = result.getGroupEntries();
        List<GroupEntry<TbItem>> content = groupEntryPage.getContent();
        for (GroupEntry<TbItem> entry : content) {
            list.add(entry.getGroupValue());
        }
        return list;
    }
    /*
    * @Description:从缓存中查询规格和品牌列表
    * @param: [category]
    * @return: java.util.Map
    * @author: TanZhiTong
    * @Date: 2018/12/7
    */
    public Map searchBrandAndSpecList(String category){
        Map map=new HashMap();
        Long typeTempleteId= (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if(typeTempleteId!=null){
            List brandList= (List) redisTemplate.boundHashOps("brandList").get(typeTempleteId);
            List specList= (List) redisTemplate.boundHashOps("specList").get(typeTempleteId);
            map.put("brandList",brandList);
            map.put("specList",specList);
        }
        return map;
    }

    /*
     * @Description: 将Item列表导入solr中
     * @param: [list]
     * @return: void
     * @author: TanZhiTong
     * @Date: 2018/12/11
     */
    @Override
    public void importList(List list) {
        System.out.println("xinzeng"+list);
        solrTemplate.saveBean(list);
        solrTemplate.commit();
    }
    /*
    * @Description: 从solr中删除Item
    * @param: [list]
    * @return: void
    * @author: TanZhiTong
    * @Date: 2018/12/11
    */
    @Override
    public void deleItemList(List list) {
        System.out.println("shanchu"+list);
        Query query=new SimpleQuery();
        Criteria criteria=new Criteria("item_goodsid").in(list);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();

    }

}
