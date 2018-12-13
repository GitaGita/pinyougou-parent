package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

/**
 * @creat time: 2018年12月07日 09:27
 * @author: MRT
 * @description
 **/
public interface ItemSearchService {
    Map<String,Object> search(Map searchMap);
    void importList(List list);
    void deleItemList(List list);
}
