package com.hw.data.test;

import com.hw.data.entity.User;
import com.hw.data.service.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSave() {
        User user = new User();
        user.setName("zhangsan");
        user.setAccount("10000");
        user.setEmail("123@qq.com");
        user.setPassword("123456");
        userRepository.save(user);
    }

    @Test//查询所有
    public void testFindAll(){
        List<User> users = (List<User>) userRepository.findAll();
        System.out.println(users);
    }
}
