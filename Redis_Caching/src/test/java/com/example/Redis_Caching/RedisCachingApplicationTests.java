package com.example.Redis_Caching;

import com.example.Redis_Caching.entity.Employee;
import com.example.Redis_Caching.repository.EmployeeDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
class RedisCachingApplicationTests {

	@Autowired
	private EmployeeDao dao;
	@Test
	public void save() {

	}
	@Test
	public void addme(){
		int a =1;
		int b= 4;
		int c = a+b;
		Assert.assertEquals(5,c);
	}

	@Test
	public void  findEmployee() {
		Employee employee = dao.findEmployeeById(98656);
		Assert.assertEquals(98656, employee.getId());
	}
	@Test
	public void remove(){
		Assert.assertEquals(98656, 98656);
	}

	@Test
	public void test_to_add_list_of_employees() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonFilePath="C:\\Users\\kiran\\Downloads\\Redis_Caching\\Redis_Caching\\src\\main\\resources\\emp.json";
		List <Employee> employees = objectMapper.readValue(Paths.get(jsonFilePath).toFile(), new TypeReference<List<Employee>>() {});
		long startTime=System.nanoTime();
		RedisCachingApplication rca=new RedisCachingApplication();
		rca.saveListOfEmployees(employees);
		long stopTime=System.nanoTime();
		long totalTime=stopTime-startTime;
		System.out.println(totalTime);
	}
}
