package com.example.Redis_Caching.repository;

import com.example.Redis_Caching.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    List<Employee> findBySalary(int salary);
}
