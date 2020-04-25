package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    //根据cid查询总记录数
    int findTotalCount(int cid,String rname);
    //根据cid，start，pageSize查询返回的数据集合
    List<Route> findByPage(int cid,int start,int pageSize,String rname);

    Route findOneById(int i);
}
