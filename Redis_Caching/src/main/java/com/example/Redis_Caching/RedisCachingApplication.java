package com.example.Redis_Caching;

import com.example.Redis_Caching.entity.Employee;
import com.example.Redis_Caching.repository.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("employee")
@EnableCaching
public class RedisCachingApplication {
	@Autowired
	private EmployeeDao dao;

	@PostMapping
	public Employee save(@RequestBody Employee employee) {
		return dao.save(employee);
	}
	@PutMapping
	@CachePut(key = "#id", value = "Employee")
	public Employee update(@PathVariable int id){
		return dao.update(id);
	}

	@GetMapping
	public List<Employee> getAllEmployees() {
		return dao.findAll();
	}
	@Cacheable(key = "#id", value = "Employee",unless ="result.salary > 500")
	@GetMapping("/{id}")
	public Employee findEmployee(@PathVariable int id) {
		return dao.findEmployeeById(id);
	}

	@DeleteMapping("/{id}")
	@CacheEvict(key = "#id", value = "Employee")
	public String remove(@PathVariable int id)   {
		return dao.deleteEmployee(id);
	}

	public static void main(String[] args) {
		SpringApplication.run(RedisCachingApplication.class, args);
	}

}
