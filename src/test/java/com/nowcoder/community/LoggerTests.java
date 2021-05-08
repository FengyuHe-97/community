package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggerTests {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTests.class);

    @Test
    public void testLogger() {
        System.out.println(logger.getName());

        //由低到高的4个级别logger 调整输出级别在application.properties里
        //实际开发不同级别的logger会存到不同文件
        //# logger 设置日志级别到debug
        //logging.level.com.nowcoder.community=debug
        // logging.file= D:/Files/MSD/OnlineCourse/niuke/data/community.log
        logger.debug("debug log");
        logger.info("info log");
        logger.warn("warn log");
        logger.error("error log");

    }
}
