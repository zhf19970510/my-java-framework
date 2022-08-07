package com.zhf.sync.demo1;

import com.zhf.MyJavaFrameworkApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJavaFrameworkApplication.class)
public class MySyncDataServiceTest {

    @Autowired
    private MySyncDataService mySyncDataService;

    @Test
    public void testSyncData() {
        MySyncDataService.RequestParamModel requestParamModel = new MySyncDataService.RequestParamModel();
        requestParamModel.setParam("abc");
        requestParamModel.setJsonBody("jsonBody");
        requestParamModel.setResource("haha");
        requestParamModel.setRowNum(1000);
        requestParamModel.setStartRow(1);
        mySyncDataService.syncData(
                requestParamModel,
                () -> {
                    System.out.println("sync start...");
                },
                resultData -> {
                    for (Map<String, Object> resultDatum : resultData) {
                        resultDatum.forEach((k, v) ->{
                            System.out.println(k + ":" + v);
                        });
                        System.out.println("========================");
                    }
                });
    }

}
