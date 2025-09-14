import com.zhf.util.PasswordUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.tomcat.util.security.MD5Encoder;

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

        String zhf1 = PasswordUtil.passwordEncode("zhf", "123456");
        String zhf2 = PasswordUtil.passwordEncode("zhf", "123456");
        System.out.println(zhf1);
        System.out.println(zhf2);
        System.out.println(zhf1.equals(zhf2));

        String encode = MD5Encoder.encode("123456".getBytes());
        System.out.println(encode);

        String s = Md5Crypt.md5Crypt("123456".getBytes());
        System.out.println(s);

        String s1 = DigestUtils.md5Hex("123456");
        System.out.println(s1);

        String s2 = Md5Crypt.md5Crypt("123456".getBytes(), "$1$66666666");


//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String encode1 = bCryptPasswordEncoder.encode("123456");
//        bCryptPasswordEncoder.matches("123456", encode1);
    }
}
