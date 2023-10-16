package com.example.Redis_Caching.repository;

import com.example.Redis_Caching.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class EmployeeDao {
    public static final String HASH_KEY = "Employee";
    @Autowired
    private RedisTemplate template;

    public String save(Employee employee){
        template.opsForHash().put(HASH_KEY,employee.getId(),employee);
        return "employee added successfully...........";
    }
    public List<Employee> findfewEmployees() {
        List<Employee> employees = new ArrayList<>();
        Set<String> employ= template.keys("*");
        Set<String> keys = template.keys("Employee:*");
        ObjectMapper objectMapper = new ObjectMapper(); // Create ObjectMapper outside the loop

        for (String key : keys){
            Object object = template.opsForHash();

            if (object != null) {
                Employee employee = objectMapper.convertValue(object, Employee.class);

                if (employee.getId() < 100) {
                    employees.add(employee);
                }
            }
        }
        return employees;
    }

    public List<Employee> get100EmployeesFromHash() {
        int batchSize = 100;
        List<Employee> employees = new ArrayList<>();
        HashOperations<String, String, Employee> hashOperations = template.opsForHash();
        Cursor<Map.Entry<String, Employee>> cursor = hashOperations.scan(
                HASH_KEY,
                ScanOptions.scanOptions()
                        .match("*")
                        .count(batchSize) // Number of elements to retrieve at a time
                        .build()
        );

        while (cursor.hasNext()) {
            Map.Entry<String, Employee> entry = cursor.next();
            employees.add(entry.getValue());

            if (employees.size() >= batchSize) {
                break;
            }
        }
        cursor.close();

        return employees;
    }


    public List<Employee> get100EmployeesFromList() {
        ListOperations<String, Object> listOperations = template.opsForList();
        List<Object> employees;
        employees = listOperations.range(HASH_KEY, 0, 99);
        List<Employee> result = new ArrayList<>();
        for (Object employee : employees) {
            if (employee instanceof Employee) {
                result.add((Employee) employee);
            }
        }
        return result;
    }

    public List<Employee> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public Employee findEmployeeById(int id){
        System.out.println("called findEmployeeById() from DB");
        return (Employee) template.opsForHash().get(HASH_KEY,id);
    }

    public String deleteEmployee(int id){
        template.opsForHash().delete(HASH_KEY,id);
        return "employee removed !!";
    }
}
