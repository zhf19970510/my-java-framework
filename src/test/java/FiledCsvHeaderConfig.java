import java.util.HashMap;
import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2022-02-12 13:09
 * @description：
 **/
public class FiledCsvHeaderConfig {

    public static final Map<String, String> EMPLOYEE_HEADER_MAP = new HashMap<>();

    static {
        EMPLOYEE_HEADER_MAP.put("empNo", "编号");
        EMPLOYEE_HEADER_MAP.put("name", "姓名");
        EMPLOYEE_HEADER_MAP.put("mobile", "手机号");
        EMPLOYEE_HEADER_MAP.put("entryDate", "入职日期");
        EMPLOYEE_HEADER_MAP.put("address", "现住址");
    }
}
