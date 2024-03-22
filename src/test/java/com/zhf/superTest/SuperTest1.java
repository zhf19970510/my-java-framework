package com.zhf.superTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SuperTest1 {

    @Test
    public void addStudent() {
//        List<? super People> studentList = new ArrayList<>();
//        addPeople(studentList);
        List<Student> studentList1 = new ArrayList<>();
        addPeople(studentList1);
        for (Student student : studentList1) {
            student.setSno(student.getName() + "sno");
            student.setClz(student.getAge() + "clz");
        }
        for (Student student : studentList1) {
            System.out.println(student);
        }
    }

    @Test
    public void addPeople() {
        List<People> peopleList = new ArrayList<>();
        addPeople(peopleList);
        for (People people : peopleList) {
            System.out.println(people);
        }
    }

    public void addPeople(List<? super Student> peopleList) {
        for (int i = 0; i < 10; i++) {
            Student people = new Student();
            people.setName("name" + i);
            people.setSex("sex" + i);
            people.setAge(i);
            peopleList.add(people);
        }
    }

}
