package de.cidaas.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.cidaas.config.WebSecurityConfig;
import de.cidaas.interceptor.service.MockTokenService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebSecurityConfig.class })
class MvcWebApplicationFunctionalTest {
	protected MockMvc mvc;
	String token;
	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	@Test
	public void shouldAllowAccessToRootPath() throws Exception {
		MockHttpServletResponse response = getResponse("/", null);
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToMyProfileWithoutToken() throws Exception {
		MockHttpServletResponse response = getResponse("/myprofile", null);
		assertEquals(401, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToMyProfileWithValidToken() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndWithScopeARead();
		MockHttpServletResponse response = getResponse("/myprofile", token);
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToMyProfileWithExpiredToken() throws Exception {
		token = MockTokenService.getExpiredToken();
		MockHttpServletResponse response = getResponse("/myprofile", token);
		assertEquals(401, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToMyProfileWithInvalidToken() throws Exception {
		token = MockTokenService.getInvalidToken();
		MockHttpServletResponse response = getResponse("/myprofile", token);
		assertEquals(401, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToHolidayListWithValidToken() throws Exception {
		token = MockTokenService.getTokenWithoutRoleHRAndWithScopeARead();
		MockHttpServletResponse response = getResponse("/holidaylist", token);
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToHolidayListWithInvalidToken() throws Exception {
		token = MockTokenService.getInvalidToken();
		MockHttpServletResponse response = getResponse("/holidaylist", token);
		assertEquals(401, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToHolidayListWithValidTokenWithoutScope() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndWithoutScopeARead();
		MockHttpServletResponse response = getResponse("/holidaylist", token);
		assertEquals(403, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToEmployeeListWithValidToken() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndWithoutScopeARead();
		MockHttpServletResponse response = getResponse("/employeelist", token);
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToEmployeeListWithInvalidToken() throws Exception {
		token = MockTokenService.getInvalidToken();
		MockHttpServletResponse response = getResponse("/employeelist", token);
		assertEquals(401, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToEmployeeListWithValidTokenWithoutRole() throws Exception {
		token = MockTokenService.getTokenWithoutRoleHRAndWithScopeARead();
		MockHttpServletResponse response = getResponse("/employeelist", token);
		assertEquals(403, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToHolidayandemployeeListWithValidToken() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndWithScopeARead();
		MockHttpServletResponse response = getResponse("/holidayandemployeelist", token);
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToHolidayandemployeeListWithInvalidToken() throws Exception {
		token = MockTokenService.getInvalidToken();
		MockHttpServletResponse response = getResponse("/holidayandemployeelist", token);
		assertEquals(401, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToHolidayandemployeeListWithValidTokenWithScope() throws Exception {
		token = MockTokenService.getTokenWithoutRoleHRAndWithScopeARead();
		MockHttpServletResponse response = getResponse("/holidayandemployeelist", token);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void shouldAllowAccessToHolidayandemployeeListWithValidTokenWithRole() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndWithoutScopeARead();
		MockHttpServletResponse response = getResponse("/holidayandemployeelist", token);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void shouldAllowAccessToManagerHolidayListWithValidToken() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndManagerAndWithoutScopeARead();
		MockHttpServletResponse response = getResponse("/managerholidaylist", token);
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void shouldNotAllowAccessToManagerHolidayListWithValidTokenWithoutCompleteRole() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndWithoutScopeARead();
		MockHttpServletResponse response = getResponse("/managerholidaylist", token);
		assertEquals(403, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToMyProfile1WithValidTokenWithWithCompleteRoleWithoutScope() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndManagerAndWithoutScopeARead();
		MockHttpServletResponse response = getResponse("/v1/api/myprofile1", token);
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToMyProfile1WithValidTokenWithCompleteRoleWithScope() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndManagerAndWithScopeARead();
		MockHttpServletResponse response = getResponse("/v1/api/myprofile1", token);
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToMyProfile1WithValidTokenWithoutCompleteRoleWithScope() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndWithScopeARead();
		MockHttpServletResponse response = getResponse("/v1/api/myprofile1", token);
		assertEquals(200, response.getStatus());		
	}

	@Test
	public void shouldNotAllowAccessToMyProfile1WithValidTokenWithoutCompleteRoleWithoutScope() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndWithoutScopeARead();
		MockHttpServletResponse response = getResponse("/v1/api/myprofile1", token);
		assertEquals(403, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToMyProfile2WithValidTokenWithCompleteScope() throws Exception {
		token = MockTokenService.getTokenWithoutRoleHRAndWithScopeAReadAndManage();
		MockHttpServletResponse response = getResponse("/v1/api/myprofile2", token);
		assertEquals(200, response.getStatus());		
	}
	
	@Test
	public void shouldAllowAccessToMyProfile2WithValidTokenWithoutCompleteScope() throws Exception {
		token = MockTokenService.getTokenWithoutRoleHRAndWithScopeARead();
		MockHttpServletResponse response = getResponse("/v1/api/myprofile2", token);
		assertEquals(403, response.getStatus());		
	}
	
	@Test
	public void shouldNotAllowAccessToLeaveType() throws Exception {
		token = MockTokenService.getTokenWithRoleHRAndManagerAndWithScopeAReadAndManage();
		MockHttpServletResponse response = getResponse("/leavetype", token);
		assertEquals(403, response.getStatus());		
	}
	
	private MockHttpServletResponse getResponse(String uri, String token) throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
					.get(uri)
					.accept(MediaType.APPLICATION_JSON_VALUE)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
					.andReturn();
		System.out.print("Content: " + mvcResult.getResponse().getContentAsString());
		return mvcResult.getResponse();
	}
}
