import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2022-04-18 18:36
 * @description：
 **/
public class MainTest3 {

    public static void main(String[] args) {
        // Map<String, String> map = new HashMap<>();
        // map.put("123", "123");
        // map.put()

        // for(String key : map.keySet()){
        //     System.out.println(key);
        //     System.out.println(map.get(key));
        // }
        // System.out.println("end");
        //
        // Pair<String, String> pair = Pair.of("key", "value");
        // System.out.println(pair.getKey());


        HashMap<Integer, String> map = new HashMap<>();

        map.put(1, "1");
        map.put(2, "2");
        map.put(0, "0");
        map.put(3, "3");
        map.put(4, "4");
        Iterator<Integer> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            Integer next = iterator.next();
            if(next == 0){
                iterator.remove();
            }
        }

        for (Map.Entry<Integer, String> integerStringEntry : map.entrySet()) {
            System.out.println(integerStringEntry.getKey() + " " + integerStringEntry.getValue());
        }

        // for (Map.Entry<Integer, String> integerStringEntry : map.entrySet()) {
        //     if(integerStringEntry.getKey() == 2){
        //         map.remove(2);
        //     }
        // }

        for (Map.Entry<Integer, String> integerStringEntry : map.entrySet()) {
            System.out.println(integerStringEntry.getKey() + " " + integerStringEntry.getValue());
        }

        {
            String s3 = "12345\0aaa";
            System.out.println(s3);
        }

        System.out.println("\\0");
    }


}
