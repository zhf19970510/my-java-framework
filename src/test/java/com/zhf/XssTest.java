package com.zhf;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Test;
import org.owasp.encoder.Encode;
import org.springframework.web.util.HtmlUtils;

public class XssTest {

    @Test
    public void test11() {
        String userInput = "<script>50394'%3balert(1)%2f421</script>";
        String encodedInput = Encode.forHtml(userInput);
        System.out.println("Encode:" + encodedInput);

        encodedInput = StringEscapeUtils.escapeHtml4(userInput);
        System.out.println("escapeHtml4:" + encodedInput);

        encodedInput = HtmlUtils.htmlEscape(userInput);
        System.out.println("htmlEscape:" + encodedInput);

        System.out.println(HtmlUtils.htmlUnescape(encodedInput));
    }
}
