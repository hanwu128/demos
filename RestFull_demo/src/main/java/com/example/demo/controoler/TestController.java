package com.example.demo.controoler;

import com.example.demo.beans.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "test")
public class TestController {

    @GetMapping(value = "/userList")
    public List<User> userList(){
        List<User> list = new ArrayList<>();
        list.add(new User("zhangsan",22,'2',"北京"));
        return list;
    }

    @RequestMapping(value = "/userMap/{name}")
    public Map<String,User> userMap(@PathVariable String name){
        Map<String,User> map = new HashMap<>();
        map.put("one",new User("zhangsan",22,'n',"beijing"));
        map.put("two",new User("lisi",32,'n',"shanghai"));
        return map;
    }
}
