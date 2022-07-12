package com.nowcoder.community.service;

import com.nowcoder.community.dao.StudentMapper;
import com.nowcoder.community.entity.Student;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;


    public Integer batchInsert(List<Student> students) {
        return this.studentMapper.batchInsertStudent(students);
    }


    public void exportExcelFile(HttpServletResponse response) throws IOException {
        // 查询所有用户信息
        List<Student> studentEntities = studentMapper.selectStudents();


        String xlsFile_name = "userInfos" + ".xlsx";

        String[] title = {"id", "name", "number","CreateTime"};
        String[][] values = new String[studentEntities.size()][4];

        for(int i = 0;i < studentEntities.size(); i++) {

            values[i][0] = studentEntities.get(i).getId().toString();
            values[i][1] = studentEntities.get(i).getName();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ss HH:mm:ss");


            values[i][2] = studentEntities.get(i).getNumber();
            values[i][3] = dateFormat.format(studentEntities.get(i).getCreateTime());
        }

        // 生成表格
        SXSSFWorkbook wb = new SXSSFWorkbook();

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("sheet1");

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        SXSSFRow row = (SXSSFRow) sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        //声明列对象
        SXSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = (SXSSFCell) row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = (SXSSFRow) sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + xlsFile_name);
        response.flushBuffer();
        OutputStream outputStream = response.getOutputStream();
        wb.write(response.getOutputStream());
        outputStream.flush();
        outputStream.close();
    }

}




