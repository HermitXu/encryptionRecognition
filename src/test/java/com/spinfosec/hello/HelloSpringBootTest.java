package com.spinfosec.hello;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author ank
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.hello.HelloSpringBootTest
 * @description [一句话描述]
 * @create 2018/10/11 17:48
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloSpringBootTest
{


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void sayHelloTest() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("https://localhost:8080/sayHello").param("noncetimestamp", "k7g2Qnen9PKkuQYGCc4TrqhXtsmWNizdujql7XXg5TzQTRqcYbm10AWzGuG7SnTKGdTBcNN7vm6CIYCZYM/Pgg=="))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("test hello springboot!"));
    }

}
