package com.example.Redis_Caching;

import com.example.Redis_Caching.entity.Employee;
import com.example.Redis_Caching.repository.EmployeeDao;
import com.example.Redis_Caching.repository.EmployeeRepository;
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

	@Autowired
	EmployeeRepository employeeRepository;

	@PostMapping("/addEmployee")
	public String save(@RequestBody Employee employee) {
		return dao.save(employee);
	}

	@PostMapping("/addListOfEmployees")
    public String saveListOfEmployees(@RequestBody List<Employee> employeeList){
		long startTime=System.nanoTime();
        for (Employee emp:employeeList) {
            dao.save(emp);
        }
		long stopTime=System.nanoTime();
		long totalTime=stopTime-startTime;
        return "Total time taken= "+totalTime;
    }
	@GetMapping("/getfew")
	public List<Employee> findfewEmployees(){
		List<Employee> employeeList=dao.findfewEmployees();
		return employeeList;
	}

	@GetMapping("/get100")
	public List<Employee>get100EmployeesFromList(){
		List<Employee> employeeList = dao.get100EmployeesFromList();
		return employeeList;
	}


	@GetMapping("/getEmp100")
	public List<Employee>get100EmployeesFromHash(){
		List<Employee> employeeList = dao.get100EmployeesFromList();
		return employeeList;
	}

	@GetMapping("/getAllEmployees")
	public List<Employee> getAllEmployees() {
		long startTime=System.nanoTime();
		List<Employee> employeeList=dao.findAll();
		long stopTime=System.nanoTime();
		long totalTime=stopTime-startTime;
		float seconds=((float) totalTime /1000000000);
		System.out.println("total time"+seconds);
		return employeeList;
	}

	@GetMapping("/getEmp/{id}")
	@Cacheable(key = "#id", value = "Employee")
	public Employee findEmployee(@PathVariable int id) {
		return dao.findEmployeeById(id);
	}

	@DeleteMapping("/deleteEmployee/{id}")
	@CacheEvict(key = "#id", value = "Employee")
	public String remove(@PathVariable int id)   {
		return dao.deleteEmployee(id);
	}

	@GetMapping("/employees/salary-less-than-1000")
	public List<Employee> getEmployees() {
		return employeeRepository.findBySalary(50000);
	}

	public static void main(String[] args) {

		SpringApplication.run(RedisCachingApplication.class, args);
	}

}
