package com.zhf.controller;

import com.zhf.util.HttpUtil1s;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OAuth2Controller {

//    @Autowired
//    private MemberFeginService memberFeginService;

    @RequestMapping("/oauth2/weibo/success")
    public String weiboOAuth(@RequestParam("code") String code
                                , HttpSession session, HttpServletResponse response) throws Exception {
        System.out.println("code = " + code);
        Map<String,String> body = new HashMap<>();
        body.put("client_id","4134519082");
        body.put("client_secret","d5b358e14d327bcfead40da4202ca71d");
        body.put("grant_type","authorization_code");
        body.put("redirect_uri","http://auth.msb.com/oauth2/weibo/success");
        body.put("code",code);
        // 根据Code获取对应的Token信息
        HttpResponse post = HttpUtil1s.doPost("https://api.weibo.com"
                , "/oauth2/access_token"
                , "post"
                , new HashMap<>()
                , null
                , body
        );
        int statusCode = post.getStatusLine().getStatusCode();
        if(statusCode != 200){
            // 说明获取Token失败,就调回到登录页面
            return "redirect:http://auth.msb.com/login.html";
        }
        // 说明获取Token信息成功
        String json = EntityUtils.toString(post.getEntity());
//        SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
//        R r = memberFeginService.socialLogin(socialUser);
//        if(r.getCode() != 0){
//            // 登录错误
//            return "redirect:http://auth.msb.com/login.html";
//        }
//        String entityJson = (String) r.get("entity");
//        MemberVO memberVO = JSON.parseObject(entityJson, MemberVO.class);
//        session.setAttribute(AuthConstant.AUTH_SESSION_REDIS,memberVO);
        // 注册成功就需要调整到商城的首页
        return "redirect:http://mall.msb.com/home";
    }
}
