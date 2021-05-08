package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest

//测试环境使用相同的配置类
@ContextConfiguration(classes = CommunityApplication.class)

//哪个类想得到容器就继承这个接口
public class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	@Test
	public void testApplicationContext() {
		System.out.println(applicationContext);

		//此处调用依赖的是接口，所以想改，动实现类就可以 这里不用动（降耦合）
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());

		//指定bean的名字，容器直接按名字获取bean
		alphaDao = applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(alphaDao.select());

	}

	@Test
	public void testBeanManagement() {
		//被Spring容器管理的bean 默认是使用单例的
		//调用两次，只实例化 初始化 销毁 一次
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);

		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	@Test
	//主动获取容器 比较麻烦
	public void testBeanConfig() {
		SimpleDateFormat simpleDateFormat =
				applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	//此处为依赖注入的演示
	@Autowired//声明 注入容器
	@Qualifier("alphaHibernate")//假设一个dao接口有不同bean容器 指定bean的名字注入
	private AlphaDao alphaDao;//表示希望Spring 把AlphaDao 注入给 alphaDao这个属性

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI() {
		System.out.println(alphaDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}

}
