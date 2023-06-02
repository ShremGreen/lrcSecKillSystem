package com.lrc.seckill;

import com.lrc.seckill.mapper.UserMapper;
import com.lrc.seckill.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        User user = userMapper.selectById("18763749928");
        System.out.println(user);
    }
}
