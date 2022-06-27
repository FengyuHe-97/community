package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Student;
import com.nowcoder.community.service.StudentService;
import com.nowcoder.community.util.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("student")
public class StudentController {

    @Value("${community.path.upload}")
    private String uploadPath;

    @Autowired
    private StudentService studentService;

    @RequestMapping(path = "/excelUpload", method = RequestMethod.GET)
    public String getExcelPage() {
        return "/site/excelUpload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = uploadPath + "/excel/";
            // 为了防止文件名冲突，获取当前时间+文件原名生成新的文件名
            String fileName=System.currentTimeMillis()+file.getOriginalFilename();

            fileName=filePath+fileName;

            // 上传文件
            File f=new File(fileName);
            file.transferTo(f);


            //输入流，获取刚刚上传文件转成输入流
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));

            //定义一个list变量，模拟excel结构
            List<List<Object>> list = ExcelUtils.getListByExcel(fileInputStream, fileName);

            //定义firstRows变量，用来获取第一行，就是标题，每列名字
            List<Object> firstRows = null;
            //定义studentList变量，用来存储文件内容(学生信息)
            List<Student> studentList=new ArrayList<>();
            //如果 list 不为空，大小大于0则获取第一行存到firstRows 里面
            if (list != null && list.size() > 0) {
                firstRows = list.get(0);
            }else {
                //否则返回 failure
                return "failure";
            }

            //对list进行遍历，因为第一行是标题，不用存到数据库，所以从第二行开始遍历
            for (int i = 1; i < list.size(); i++) {
                //获取第i行数据
                List<Object> rows = list.get(i);
                //定义student遍历，存储第i行数据
                Student student = new Student();
                //对第i行数据进行遍历，
                for (int j = 0; j < rows.size(); j++) {
                    //获取第i行第j列数据，存到cellVal 变量里面
                    String cellVal = (String) rows.get(j);
                    //调用setFileValueByFieldName函数，把数据存到student对应的属性里面
                    ExcelUtils.setFileValueByFieldName(student, firstRows.get(j).toString().trim(), cellVal);
                }
                //把student变量加到studentList
                studentList.add(student);
            }

            //调用批量插入方法，把数据存到数据库
            Integer flag=this.studentService.batchInsert(studentList);
            if(flag!=0){
                return "ok";
            }else {
                return "failure";
            }

        } catch (Exception e) {
            return "failure";
        }
    }

    @PostMapping ("/uploadExcel")

    public String uploadExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if(fileMap == null || fileMap.size() == 0) {

        }

        Collection<MultipartFile> files = fileMap.values();

        ArrayList<Object> str = new ArrayList<>();

        for (MultipartFile file : files) {
            String req = file.getOriginalFilename();
            if(StringUtils.isBlank(req)) {
                continue;
            }

        }
        return "";
    }



}
