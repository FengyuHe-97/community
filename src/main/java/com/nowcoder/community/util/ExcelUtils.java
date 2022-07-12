package com.nowcoder.community.util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtils {
    //上传的是以.xls后缀的
    private final static String excel2003L =".xls";
    //上传的是以.xlsx后缀的
    private final static String excel2007U =".xlsx";

    public static List<List<Object>> getListByExcel(InputStream in, String fileName) throws Exception {
        //创建 list 模拟Excel表结构
        List<List<Object>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(in,fileName);
        if(null == work){
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;  //页数
        Row row = null;  //行数
        Cell cell = null;  //列数

        list = new ArrayList<List<Object>>();
        //遍历Excel中所有的sheet页
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if(sheet==null){continue;}

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if(row==null){continue;}

                //遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    //获取第i页第j行第y列的值
                    cell = row.getCell(y);
                    li.add(getValue(cell));
                }
                list.add(li);
            }
        }
        return list;
    }
    public static  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
        Workbook wb = null;
        //获取Excel后缀
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        //跟据后缀创建工作簿
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }
    public static String getValue(Cell cell) {
        String value = "";
        if(null==cell){
            return value;
        }
        switch (cell.getCellType()) {
            //数值型
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    //如果是date类型则 ，获取该cell的date值
                    Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = format.format(date);;
                }else {// 纯数字
                    BigDecimal big=new BigDecimal(cell.getNumericCellValue());
                    value = big.toString();
                    //解决1234.0  去掉后面的.0
                    if(null!=value&&!"".equals(value.trim())){
                        String[] item = value.split("[.]");
                        if(1<item.length&&"0".equals(item[1])){
                            value=item[0];
                        }
                    }
                }
                break;
            //字符串类型
            case STRING:
                //如果是string类型进行下划线转驼峰处理
                value = cell.getStringCellValue();
                break;
            // 公式类型
            case FORMULA:
                //读公式计算值
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getStringCellValue();
                }
                break;
            // 布尔类型
            case BOOLEAN:
                value = " "+ cell.getBooleanCellValue();
                break;
            default:
                value = cell.getStringCellValue();
        }
        if("null".endsWith(value.trim())){
            value="";
        }
        return value;
    }

    public static void setFileValueByFieldName(Object object, String fieldName, Object val) {
        //获取object类的所有属性
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            //对所有属性进行遍历
            for (int i = 0; i < fields.length; i++) {
                //获取第i个属性
                Field field = fields[i];
                //如果Excel的某一列的列名(fieldName)与
                //在第i个属性上面添加的@Value注解的value一样，进行下面的操作
                if (fieldName.equals(field.getAnnotation(Value.class).value())) {
                    //如果field为私有属性，也可以对它进行操作
                    field.setAccessible(true);
                    //获取该属性的数据类型，如果是Integer类型的
                    if (field.getType() == Integer.class) {
                        //把val转成Integer存到该属性里面
                        field.set(object, Integer.valueOf(val.toString()));
                    } else if (field.getType() == Long.class) {
                        field.set(object, Long.valueOf(val.toString()));
                        //如果时LocalDateTime类型的
                    } else if (field.getType() == LocalDateTime.class) {
                        //先把他格式化
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime time = LocalDateTime.parse(val.toString(), formatter);
                        field.set(object, time);
                        //如果其他类型的直接存
                    } else if (field.getType() == Date.class) {
                        //先把他格式化
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
                        Date time = format.parse(val.toString());
                        field.set(object, time);
                        //如果其他类型的直接存
                    } else {
                        field.set(object, val);
                    }
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
