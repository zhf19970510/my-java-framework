package com.zhf.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.StopWatch;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * @author: 曾鸿发
 * @create: 2022-06-04 16:45
 * @description：
 **/

public class MapperTest {


    public void mappingBySpringBeanUtils(PersonDO personDO, int times) {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = new PersonDTO();
            org.springframework.beans.BeanUtils.copyProperties(personDO, personDTO);
            personDTO.setAge(i);
        }
        stopwatch.stop();
        System.out.println("mappingBySpringBeanUtils " + times + " times cost :" + stopwatch.getTotalTimeMillis());
    }


    public void mappingByCglibBeanCopier(PersonDO personDO, int times) {
        BeanCopier copier = BeanCopier.create(PersonDO.class, PersonDTO.class, false);
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = new PersonDTO();
            copier.copy(personDO, personDTO, null);
            //personDTO.setAge(personDTO.getAge() + 1);
        }
        stopwatch.stop();
        System.out.println("mappingByCglibBeanCopier " + times + " times cost :" + stopwatch.getTotalTimeMillis());
    }


    public void mappingByApacheBeanUtils(PersonDO personDO, int times)
            throws InvocationTargetException, IllegalAccessException {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = new PersonDTO();
            org.apache.commons.beanutils.BeanUtils.copyProperties(personDTO, personDO);
            personDTO.setAge(i);
        }
        stopwatch.stop();
        System.out.println("mappingByApacheBeanUtils " + times + " times cost :" + stopwatch.getTotalTimeMillis());
    }


    public void mappingByApachePropertyUtils(PersonDO personDO, int times)
            throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = new PersonDTO();
            PropertyUtils.copyProperties(personDTO, personDO);
            personDTO.setAge(i);
        }
        stopwatch.stop();
        System.out.println("mappingByApachePropertyUtils " + times + " times cost :" + stopwatch.getTotalTimeMillis());
    }


    public void mappingByDozer(PersonDO personDO, int times) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < times; i++) {
            PersonDTO personDTO = mapper.map(personDO, PersonDTO.class);
            personDTO.setAge(i);
        }
        stopwatch.stop();
        System.out.println("mappingByDozer " + times + " times cost :" + stopwatch.getTotalTimeMillis());
    }

    public static void main(String[] args)
            throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException {
        PersonDO personDO = new PersonDO();
        personDO.setName("Hollis");
        personDO.setAge(26);
        personDO.setBirthday(new Date());
        personDO.setId(1);
        MapperTest mapperTest = new MapperTest();
        mapperTest.mappingByCglibBeanCopier(personDO, 100);
        mapperTest.mappingByCglibBeanCopier(personDO, 100000);
        mapperTest.mappingByCglibBeanCopier(personDO, 1000000);
        mapperTest.mappingByCglibBeanCopier(personDO, 10000000);
        mapperTest.mappingByCglibBeanCopier(personDO, 100000000);

        System.out.println("======================");
        mapperTest.mappingBySpringBeanUtils(personDO, 100);
        mapperTest.mappingBySpringBeanUtils(personDO, 100000);
        mapperTest.mappingBySpringBeanUtils(personDO, 1000000);
        mapperTest.mappingBySpringBeanUtils(personDO, 10000000);
        mapperTest.mappingBySpringBeanUtils(personDO, 100000000);

        System.out.println("======================");


        mapperTest.mappingByCglibBeanCopier(personDO, 100);
        mapperTest.mappingByCglibBeanCopier(personDO, 100000);
        mapperTest.mappingByCglibBeanCopier(personDO, 1000000);
        mapperTest.mappingByCglibBeanCopier(personDO, 10000000);
        mapperTest.mappingByCglibBeanCopier(personDO, 100000000);

        System.out.println("======================");


        mapperTest.mappingByApachePropertyUtils(personDO, 100);
        mapperTest.mappingByApachePropertyUtils(personDO, 100000);
        mapperTest.mappingByApachePropertyUtils(personDO, 1000000);
        mapperTest.mappingByApachePropertyUtils(personDO, 10000000);
        mapperTest.mappingByApachePropertyUtils(personDO, 100000000);

        System.out.println("======================");


        mapperTest.mappingByApacheBeanUtils(personDO, 100);
        mapperTest.mappingByApacheBeanUtils(personDO, 100000);
        mapperTest.mappingByApacheBeanUtils(personDO, 1000000);
        mapperTest.mappingByApacheBeanUtils(personDO, 10000000);
        mapperTest.mappingByApacheBeanUtils(personDO, 100000000);

        System.out.println("======================");

        mapperTest.mappingByDozer(personDO, 100);
        mapperTest.mappingByDozer(personDO, 100000);
        mapperTest.mappingByDozer(personDO, 1000000);
        mapperTest.mappingByDozer(personDO, 10000000);
        mapperTest.mappingByDozer(personDO, 100000000);

        System.out.println("======================");

    }
}
