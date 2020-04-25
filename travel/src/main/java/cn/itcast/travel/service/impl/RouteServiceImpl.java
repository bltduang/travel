package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImageDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImageDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao dao = new RouteDaoImpl();
    private RouteImageDao imageDao = new RouteImageDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        //封装pageBean
        PageBean<Route> pageBean = new PageBean<Route>();
        //设置当前页码
        pageBean.setCurrentPage(currentPage);
        //设置每页显示条数
        pageBean.setPageSize(pageSize);
        //查询总记录数，并设置
        int totalCount = dao.findTotalCount(cid,rname);
        pageBean.setTotalCount(totalCount);
        //设置总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount/pageSize :(totalCount/pageSize) + 1;
        pageBean.setTotalPage(totalPage);
        //设置显示数据；
        int start = (currentPage - 1) * pageSize;
        List<Route> list = dao.findByPage(cid, start, pageSize,rname);
        pageBean.setList(list);
        return pageBean;
    }

    @Override
    public Route findOne(String rid) {
        //根据id查询单个线路
        Route route = dao.findOneById(Integer.parseInt(rid));
        //根据rid查询线路图片集合
        List<RouteImg> routeImgByRid = imageDao.findRouteImgByRid(route.getRid());
        route.setRouteImgList(routeImgByRid);
        //根据sid查询商家信息
        Seller seller = sellerDao.findSellerBySid(route.getSid());
        route.setSeller(seller);

        //查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }
}
