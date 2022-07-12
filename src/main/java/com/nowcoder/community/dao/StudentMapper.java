package com.nowcoder.community.dao;


import com.nowcoder.community.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {
    public Integer batchInsertStudent(@Param("students") List<Student> students);


    List<Student> selectStudents();

}