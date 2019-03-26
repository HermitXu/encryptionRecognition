package com.spinfosec.controller;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ank
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.controller.AuthControllerTest
 * @description [一句话描述]
 * @create 2018/11/23 12:12
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AuthControllerTest
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
    public void testLogin() throws Exception
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "secadmin");
        jsonObject.put("password", "047801CF090866BBAA4CF96A60D5F6A3EFA25FD6C184DCA29169BAAB94859984757252948DF95A035B08CD925CFEA82176AEF3C0FE7DC11E2749B3FB8BD6379DCDF323BEE51BCFCC7718BEA81E529813A08D61E83DA95E18C26261AF2C159465D3F0815A8077CA8B8EAD55");
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("https://localhost:8080/auth/login").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toJSONString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(contentAsString);
    }
}
