package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
//@Scope("prototype")多个实例，每次访问生成新实例
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    //构造器方法
    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    @PostConstruct//构造器之后调用，初始化方法
    public void init() {
        System.out.println("初始化AlphaService");
    }

    @PreDestroy//销毁对象前调用
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }

}
