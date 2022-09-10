import java.util.HashSet;
import java.util.Set;

public class MainTest5 {

    public static void main(String[] args) {

        Set<String> set1 = new HashSet<>();
        set1.add("aaa");
        set1.add("bbb");
        set1.add("ccc");
        set1.add("aaa");
        String[] array = set1.toArray(new String[0]);
        for (String s : array) {
            System.out.println(s);
        }
        System.out.println("=====");
    }
}
