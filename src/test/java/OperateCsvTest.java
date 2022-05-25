import com.csvreader.CsvWriter;
import com.zhf.util.OperateCsvUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2022-02-12 11:58
 * @description：
 **/
public class OperateCsvTest {


    @SneakyThrows
    public static void writeDataToCsv(String filePath) {
        File file = new File(filePath);
        List content = new ArrayList();
        CsvWriter csvWriter = new CsvWriter(new FileOutputStream(filePath),',', StandardCharsets.UTF_8);
        String[] titles = new String[]{"编号", "姓名", "手机号", "入职日期", "现住址"};
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"001", "张三", "13333333333", "20210910", "aabb"});
        data.add(new String[]{"002", "张四", "13333344444", "20220213", "aacc"});
        data.add(new String[]{"003", "张五", "13333555555", "20211111", "aacc"});
        data.add(new String[]{"004", "张六", "133388888888", "20210830", "aadb"});
        csvWriter.writeRecord(titles);
        for(String [] d : data){
            csvWriter.writeRecord(d);
        }
        if(csvWriter != null){
            csvWriter.close();
        }
    }

    @Test
    public void testWriteDataToCsv(){
        String[] titles = new String[]{"编号", "姓名", "手机号", "入职日期", "现住址"};
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"001", "张三", "13333333333", "20210910", "aabb"});
        data.add(new String[]{"002", "张四", "13333344444", "20220213", "aacc"});
        data.add(new String[]{"003", "张五", "13333555555", "20211111", "aacc"});
        data.add(new String[]{"004", "张六", "133388888888", "20210830", "aadb"});
        OperateCsvUtil.writeDataToCsv("D:\\test\\text.csv", titles, data);
    }

    @Test
    public void testReadDataFromFile(){
        List<String[]> retData = OperateCsvUtil.readDataFromFile("D:\\test\\text.csv");
        for(String[] data : retData){
            for(String s: data){
                System.out.print(s + " ");
            }
            System.out.println();
        }

    }

    @Test
    public void testReadCsvDataToListEntity(){
        String filePath = "D:\\test\\text.csv";
        List<Employee> employees = OperateCsvUtil.readCsvDataToListEntity(filePath, FiledCsvHeaderConfig.EMPLOYEE_HEADER_MAP, Employee.class);
        if(CollectionUtils.isEmpty(employees)){
            return;
        }
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }
}
