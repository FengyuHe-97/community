package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//这个注解用来声明页面是否要在登录状态下访问

@Target(ElementType.METHOD)//声明这个注解用在方法之上
@Retention(RetentionPolicy.RUNTIME)//运行时生效
public @interface LoginRequired {



}
