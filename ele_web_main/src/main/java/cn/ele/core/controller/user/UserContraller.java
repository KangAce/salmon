package cn.ele.core.controller.user;

import cn.ele.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserContraller {
    @Autowired
    UserService userservice;
    @RequestMapping("Login")
    public String Login(){
        return "success";
    }

}