package com.epam.rd.autocode.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epam.rd.autocode.domain.Department;

public interface DepartmentDao extends Dao<Department, BigInteger> {
     List<Department> departmentList = new ArrayList<>();

    @Override
    default Optional<Department> getById(BigInteger Id){
        Department department=null;
        for(Department de : departmentList){
            if(de.getId().compareTo(Id)==0){
                department=de;
                break;
            }
        }
        return Optional.ofNullable(department);
    }

    @Override
    default List<Department> getAll() {
        return departmentList;
    }

    @Override
    default Department save(Department department) {
        Optional<Department> opDep=getById(department.getId());
        opDep.ifPresent(this::delete);
        departmentList.add(department);
        return department;
    }

    @Override
    default void delete(Department department) {
        if(departmentList.contains(department))
            departmentList.remove(department);
    }
}

