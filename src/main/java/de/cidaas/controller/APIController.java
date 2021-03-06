package de.cidaas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Component
public class APIController {
	@RequestMapping("/")
	public String index() {
		return "Hello World!";
	}

	@PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, path = "/myprofile")
    @ResponseBody
	public Map<String,String> myprofile() {    	
		Map<String,String> profileObj=new HashMap<String,String>();
		profileObj.put("firstName", "john");
		profileObj.put("lastName", "wiliams");
		profileObj.put("role", "USER");
		return profileObj;
	}
    
	@PreAuthorize("(hasRole('HR') && hasRole('Manager')) || hasAuthority('a:read')")
    @RequestMapping(method = RequestMethod.GET, path = "/v1/api/myprofile1")
    @ResponseBody
	public Map<String,String> myprofile1() {    	
		Map<String,String> profileObj=new HashMap<String,String>();
		profileObj.put("firstName", "wiliams");
		profileObj.put("lastName", "john");
		profileObj.put("role", "USER");
		return profileObj;
	}
    
	@PreAuthorize("hasAuthority('a:read') && hasAuthority('manage') ")
    @RequestMapping(method = RequestMethod.GET, path = "/v1/api/myprofile2")
    @ResponseBody
	public Map<String,String> myprofile2() {    	
		Map<String,String> profileObj=new HashMap<String,String>();
		profileObj.put("firstName", "wiliams");
		profileObj.put("lastName", "john");
		profileObj.put("role", "USER");
		return profileObj;
	}
    
	@PreAuthorize("hasRole('HR')")
    @RequestMapping(method = RequestMethod.GET, path = "/employeelist")
    @ResponseBody
	public List<Object> employeeList(ServletRequest request) {		
		List<Object> empList=new ArrayList<Object>();
		Map<String,String> profileObj1=new HashMap<String,String>();
		profileObj1.put("firstName", "john");
		profileObj1.put("lastName", "wiliams");
		profileObj1.put("role", "USER");
		empList.add(profileObj1);
		Map<String,String> profileObj2=new HashMap<String,String>();
		profileObj2.put("firstName", "David");
		profileObj2.put("lastName", "Johnson");
		profileObj2.put("role", "USER");
		empList.add(profileObj2);
		return empList;
	}
	
	@PreAuthorize("hasAuthority('a:read')")
	@RequestMapping(method = RequestMethod.GET, path = "/holidaylist")	
	@ResponseBody
	public List<Object> holidayList(ServletRequest request) {		
		List<Object> holidayList=new ArrayList<Object>();
		Map<String,String> holidayObj1=new HashMap<String,String>();
		holidayObj1.put("Date", "1-1-2019");
		holidayObj1.put("reason", "New year");
		holidayList.add(holidayObj1);
		Map<String,String> holidayObj2=new HashMap<String,String>();
		holidayObj2.put("Date", "25-12-2019");
		holidayObj2.put("reason", "Christmas Day");
		holidayList.add(holidayObj2);
		return holidayList;
	}	
	
	@PreAuthorize("hasRole('HR') && hasRole('Manager')")
	@RequestMapping(method = RequestMethod.GET, path = "/managerholidaylist")
	@ResponseBody
	public List<Object> localHolidayList(ServletRequest request) {
		List<Object> holidayList=new ArrayList<Object>();
		Map<String,String> holidayObj1=new HashMap<String,String>();
		holidayObj1.put("Date", "1-1-2019");
		holidayObj1.put("reason", "New year");
		holidayList.add(holidayObj1);
		Map<String,String> holidayObj2=new HashMap<String,String>();
		holidayObj2.put("Date", "25-3-2019");
		holidayObj2.put("reason", "Christmas Day");
		holidayList.add(holidayObj2);
		return holidayList;
	}
	
	@PreAuthorize("hasAnyAuthority('a:read', 'ROLE_HR')")
	@RequestMapping(method = RequestMethod.GET, path = "/holidayandemployeelist")
	@ResponseBody
	public List<Object> bothEmpListAndHolidayList(ServletRequest request) {	
		List<Object> holidayList=new ArrayList<Object>();
		Map<String,String> holidayObj1=new HashMap<String,String>();
		holidayObj1.put("Date", "1-1-2019");
		holidayObj1.put("reason", "New year");
		holidayList.add(holidayObj1);
		Map<String,String> holidayObj2=new HashMap<String,String>();
		holidayObj2.put("Date", "25-12-2019");
		holidayObj2.put("reason", "Christmas Day");
		holidayList.add(holidayObj2);
		return holidayList;
	}

	@PreAuthorize("denyAll()")
	@RequestMapping(method = RequestMethod.GET, path = "/leavetype")
	@ResponseBody
	public List<String> leaveType() {
		List<String> leaveTypeList=new ArrayList<String>();
		leaveTypeList.add("Vacation Leave");
		leaveTypeList.add("Sick Leave");
		return leaveTypeList;
	}
}
