package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//springMVC注解
@Controller
@RequestMapping("/alpha") //声明请求路径
public class AlphaController {


    @RequestMapping("/hello")
    @ResponseBody//声明访问的不是网页
    public String sayHello() {
        return "Hello Spring Boot.";
    }

    @Autowired
    private AlphaService alphaService;


    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    //传统接收请求方式
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        System.out.println(request.getMethod());//请求方式
        System.out.println(request.getServletPath());//请求路径
        Enumeration<String> enumeration = request.getHeaderNames();//请求的消息头 得到所有请求行的key（Enumeration是迭代器）
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        //http://localhost:8080/community/alpha/http?code=123&name=zhnagsan 这样输入就会得到参数
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try (
                PrintWriter writer = response.getWriter();//此处语法 括号内相当于finally
        ) {
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GET请求

    // 查询所有学生 分页显示
    // /students?current=1&limit=20
    @RequestMapping(path = "/students", method = RequestMethod.GET)//声明请求路径，方式
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /student/123 这种获取方式参数是路径的一部分
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) { // 路径变量
        System.out.println(id);
        return "a student";
    }

    // POST请求
    //这里通过static中的静态模板 得到请求 demo：http://localhost:8080/community/html/student.html
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {//参数和表单一致，就可以传过来
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 响应HTML数据

    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    //@ResponseBody 返回html就不要加这个注解
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "张三");
        mav.addObject("age", 30);
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {//若返回类型是String 表示返回的是view的路径 view在templates里
        model.addAttribute("name", "北京大学");
        model.addAttribute("age", 80);
        return "/demo/view";
    }

    // 响应JSON数据(异步请求)
    // Java对象 -> JSON字符串 -> JS对象  json是具有特定格式的字符串
    //DispatcherServlet会自动检测到返回的Map转换为json字符串传给浏览器

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.00);
        return emp;
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "李四");
        emp.put("age", 24);
        emp.put("salary", 9000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "王五");
        emp.put("age", 25);
        emp.put("salary", 10000.00);
        list.add(emp);

        return list;
    }


}
