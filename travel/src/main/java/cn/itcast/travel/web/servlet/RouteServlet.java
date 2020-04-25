package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String _currentPage = request.getParameter("currentPage");
        String _pageSize = request.getParameter("pageSize");
        String _cid = request.getParameter("cid");

        //接受rname路线名称
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        //2.参数处理
        int cid = 0;
        if (_cid != null && _cid.length() >0 && !"null".equals(_cid)){
            cid = Integer.parseInt(_cid);
        }

        int currentpage = 0;
        if (_currentPage != null && _currentPage.length()>0){
            currentpage = Integer.parseInt(_currentPage);
        }else {
            currentpage = 1;
        }

        int pageSize = 0;
        if (_pageSize != null && _pageSize.length()>0){
            pageSize = Integer.parseInt(_pageSize);
        }else {
            pageSize = 10;
        }

        //3.调用sevice查找pageBean对象
        PageBean<Route> routePageBean = service.pageQuery(cid, currentpage, pageSize,rname);

        //4.序列化为json，返回
        writeValue(routePageBean,response);
    }

    /**
     * 查询单个旅游线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        Route route = service.findOne(rid);
        writeValue(route,response);
    }

    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取旅游线路id
        String rid = request.getParameter("rid");

        //2.获取当前用户
        User user = (User)request.getSession().getAttribute("user");
        int uid ;
        if (user == null){
            //用户尚未登陆
            uid = 0;
        }else{
            //用户已登陆
            uid = user.getUid();
        }
        //3.调用FavoriteService查询是否搜藏
        boolean flag = favoriteService.isFavorite(Integer.parseInt(rid), uid);
        //4.返回json
        writeValue(flag,response);

    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取旅游线路id
        String rid = request.getParameter("rid");

        //2.获取当前用户
        User user = (User)request.getSession().getAttribute("user");
        int uid ;
        if (user == null){
            //用户尚未登陆
            return;
        }else{
            //用户已登陆
            uid = user.getUid();
        }
        //3.调用FavoriteService添加搜藏
        boolean flag = favoriteService.add(Integer.parseInt(rid), uid);
    }
}
