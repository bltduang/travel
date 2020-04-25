package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.*;

public class CategoryServiceImpl implements CategoryService {

    Map<String,Set<String>> map = new HashMap<String,Set<String>>();
    CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        try{
//            Jedis jedis = JedisUtil.getJedis();
//            map = new HashMap<String,Set<String>>();
            //1.从redis中查找分类数据
//          Set<String> category = jedis.zrange("category", 0, -1);
            Set<String> category = map.get("category");
            //2.判断是否找到
            List<Category> all = null;
            if (category == null || category.size() == 0){
                //3.如果没找到从数据库中查找
                all = categoryDao.findAll();
                //存入缓存
                Set<String> set = new HashSet<String>();
                for (int i = 0; i < all.size(); i++) {
                    //jedis.zadd("category",all.get(i).getCid(), all.get(i).getCname());
                    set.add(all.get(i).getCid()+"_"+all.get(i).getCname());
                }

                map.put("category",set);
            }else {
                //4.如果redis中有缓存
                all = new ArrayList<Category>();
                for (String name : category) {
                    Category category1 = new Category();
                    String cname = name.substring(name.indexOf("_") + 1);
                    int cid = Integer.parseInt(name.substring(0,1));
                    category1.setCname(cname);
                    category1.setCid(cid);
                    all.add(category1);
                }
                System.out.println("从redis中获取category");
            }
            return all;
        }catch (Exception e){
            System.out.println("redis服务器连接失败");
            return categoryDao.findAll();
        }

        

    }
}
