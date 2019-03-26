package com.spinfosec.controller.system;

import com.spinfosec.controller.AuthController;
import com.spinfosec.service.srv.IAuthSrv;
import com.spinfosec.service.srv.ISystemSrv;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName AdminsControllerTest
 * @Description: 〈一句话功能简述〉
 * @date 2018/10/12
 * All rights Reserved, Designed By SPINFO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration

public class AdminsControllerTest
{

	private MockMvc mockMvc;

	@InjectMocks
	private AdminsController adminsController;

	@Mock
	private ISystemSrv systemSrv;

	@Before
	public void setUp() throws Exception
	{
		this.mockMvc = MockMvcBuilders.standaloneSetup(adminsController).build();
		// MockitoAnnotations.initMocks(this);
	}

	@Test
	public void pageUser() throws Exception
	{
		this.mockMvc.perform(MockMvcRequestBuilders.get("/system/admin/page").param("page", "1").param("rows", "10"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
	}

	@Test
	public void saveSpAdmins() throws Exception
	{
	}

	@Test
	public void updateSpAdmins() throws Exception
	{
	}

	@Test
	public void deleteSpAdmins() throws Exception
	{
	}

}