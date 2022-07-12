package com.nowcoder.community.entity;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Data
public class Student {
    @Value(value = "序号")
    private Long id;
    @Value(value = "姓名")
    private String name;
    @Value(value = "学号")
    private String number;
    @Value(value = "创建时间")
    private Date createTime;
}
