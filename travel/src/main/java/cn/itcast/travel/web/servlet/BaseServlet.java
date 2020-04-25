package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI();  //   /travel/user/add

        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);

        //3.获取方法对象Method
        //this谁调用我，我代表谁
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            //4.执行方法
            //暴力反射
//            method.setAccessible(true);
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    //将数据序列化为json，后响应回前端
    public void writeValue(Object obj, HttpServletResponse response){
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        try {
            mapper.writeValue(response.getOutputStream(),obj);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("发送失败");
        }
    }

    public String writeValueAsString(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(obj);
            return  json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("序列化json失败");
        }
        return null;
    }
}
