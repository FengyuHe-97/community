package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//配置类
@SpringBootApplication
//自动扫描配置类在的包和子包，需要注解controller，component service 不同注解都可以被扫描 是功能的不同
public class CommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
