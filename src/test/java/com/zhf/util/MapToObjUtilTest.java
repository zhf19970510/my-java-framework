package com.zhf.util;

import com.zhf.entity.Student;
import com.zhf.util.pojo.Grand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.*;

@RunWith(SpringRunner.class)
public class MapToObjUtilTest {

    /**
     * 测试下来总共耗费100多点毫秒
     * @throws Exception
     */
    @Test
    public void testObjToMap() throws Exception{
        System.out.println("test testObjToMap ================ \n");

        List<Student> students = generateStudents();

        List<Map<String, String>> list = new ArrayList<>();
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (Student student : students) {
            list.add(MapToObjectUtil.objToMap(student));
        }
        stopwatch.stop();
        System.out.println("objToMap " + 10000 + " times cost :" + stopwatch.getTotalTimeMillis());
//        for (Map<String, String> stringStringMap : list) {
//            System.out.println("====== one student info as follows==========");
//            stringStringMap.forEach((k, v) -> {
//                System.out.println(k + " : " + v);
//            });
//        }

        System.out.println();
    }

    @Test
    public void test2(){

        List<Student> students = generateStudents();
        List<Map<String, Object>> list = new ArrayList<>();
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (Student student : students) {
            list.add(MyBeanUtils.objectToMap(student));
        }
        stopwatch.stop();
        System.out.println("MyBeanUtils.objectToMap " + 10000 + " times cost :" + stopwatch.getTotalTimeMillis());
    }

    @Test
    public void testMapToObject() throws Exception{
        Map<String, Object> stringStringMap = generateMap();
        Student student = MapToObjectUtil.transFormAnotherBean(stringStringMap, Student.class);
        System.out.println(student);
    }

    public Map<String, Object> generateMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", "222");
        map.put("name", "ccc");
        map.put("birthday", "2022-08-07 09:34:34");
        return map;
    }


    public List<Student> generateStudents(){
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Student student = new Student();
            student.setId(i);
            student.setName("aaa" + i);
            student.setBirthday(new Date());
            students.add(student);
        }
        return students;
    }

    @Test
    public void mapToBean() {
        Map<String, Object> map = new HashMap<>();
        map.put("grandName", "abc");
        map.put("boss", "大小姐");
        List<com.zhf.util.pojo.Student> list1 = new ArrayList<>();
        com.zhf.util.pojo.Student student = new com.zhf.util.pojo.Student();
        student.setNo("1");
        student.setName("aaa");
        student.setAge("32");
        list1.add(student);

        com.zhf.util.pojo.Student student1 = new com.zhf.util.pojo.Student();
        student1.setNo("1");
        student1.setName("aaa");
        student1.setAge("32");
        list1.add(student1);

        map.put("studentList", list1);

        Grand grand = MapToObjectUtil.mapToBean(map, Grand.class);
        System.out.println(grand);

        List<String> strList = new ArrayList<>();
        strList.add("abc");
        strList.add("def");
        strList.add("123");
        strList.add("456");
        strList.removeIf("123"::equals);
        System.out.println(strList);
    }
}
