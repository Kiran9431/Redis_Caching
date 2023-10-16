package com.example.Redis_Caching;

import com.example.Redis_Caching.entity.Employee;
import com.example.Redis_Caching.repository.EmployeeDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest
class RedisCachingApplicationTests {

	@Autowired
	private EmployeeDao dao;
	@Test
	public void save() {

	}
	@Test
	public void addme() {
		Employee emp = new Employee();
		emp.setId(1);
		emp.setName("bvr");
		dao.save(emp);
		long startTime = System.nanoTime();
		Employee storedEmp = dao.findEmployeeById(emp.getId());
		long stopTime = System.nanoTime();
		long totalTime = stopTime - startTime;
		float seconds = ((float) totalTime / 100000);
		System.out.println("Total time Taken:" + seconds);
		Assert.assertEquals(emp.getId(), storedEmp.getId());
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

	//@Test
	//public void getfew(){
	//	List<Employee> employeeList = new ArrayList<>();
	//	employeeList.stream().filter(e -> e.getRandomSalary >= 1000 && e.getRandomSalary <= 2000).collect(Collectors.toList());
	//}


	@Test
	void testGet100EmployeesFromHash() {

		//String listKey = "Employee";
		List<Employee> expectedEmployees = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			Employee employee = new Employee(i, "Employee " + i, getRandomDepartment(), getRandomSalary());
			expectedEmployees.add(employee);
		}
		List<Employee> actualEmployees = dao.get100EmployeesFromHash();

		Assertions.assertEquals(100, actualEmployees.size());
		Assertions.assertEquals(expectedEmployees, actualEmployees);
	}

	@Test
	public void listofemployee() {
		List<Employee> employees = new ArrayList<>();
		long startTime = System.nanoTime();
		for (int i = 1; i <= 1000; i++) {
			Employee employee = new Employee(i, "Employee " + i, getRandomDepartment(), getRandomSalary());
			dao.save(employee);
		}
		long stopTime = System.nanoTime();
		long totalTime = stopTime - startTime;
		float seconds = ((float) totalTime / 100000);
		System.out.println("Total time Taken:" + seconds);
	}
	private static String getRandomDepartment() {
		String[] departments = {"HR", "IT", "Finance", "Sales"};
		return departments[new Random().nextInt(departments.length)];
	}
	private static int getRandomSalary(){
		int[] salary={40000,50000,20000,30000,40000,50000};
		return salary[new Random().nextInt(salary.length)];
	}



}
