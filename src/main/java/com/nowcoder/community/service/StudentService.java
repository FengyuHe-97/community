package com.nowcoder.community.service;

import com.nowcoder.community.dao.StudentMapper;
import com.nowcoder.community.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;


    public Integer batchInsert(List<Student> students) {
        return this.studentMapper.batchInsertStudent(students);
    }
}




