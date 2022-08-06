import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainTest4 {

    public static void main(String[] args) {
        test2(Locale.US);
    }

    // 按照具体的
    public static void test1(){
        NumberFormat decimalFormat = new DecimalFormat("#,##0.0");
        String format = decimalFormat.format(new BigDecimal("123456.789"));
        System.out.println(format);
    }

    public static void test2(Locale locale){
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(locale);
        System.out.println(currencyInstance.format(new BigDecimal("123456.789")));
    }
}
