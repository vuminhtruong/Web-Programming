package com.epam.rd.autocode.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;

public interface EmployeeDao extends Dao<Employee, BigInteger> {
    List<Employee> employeeList = new ArrayList<>();

    @Override
    default Optional<Employee> getById(BigInteger Id) {
        Employee employee = null;
        for(Employee em : employeeList){
            if(em.getId().compareTo(Id)==0){
                employee=em;
                break;
            }
        }
        return Optional.ofNullable(employee);
    }

    @Override
    default List<Employee> getAll() {
        return employeeList;
    }

    @Override
    default Employee save(Employee employee) {
        if(!employeeList.contains(employee))
            employeeList.add(employee);
        return employee;
    }

    @Override
    default void delete(Employee employee) {
        employeeList.remove(employee);
    }

    List<Employee> getByDepartment(Department department);
    List<Employee> getByManager(Employee employee);

}

