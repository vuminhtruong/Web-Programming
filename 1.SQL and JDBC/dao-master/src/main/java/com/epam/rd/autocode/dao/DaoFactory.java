package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoFactory {
    static ConnectionSource connSource;
    static Connection conn;
    static Statement stm;

    static {
        try{
            connSource = ConnectionSource.instance();
            conn = connSource.createConnection();
            stm = conn.createStatement();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public EmployeeDao employeeDAO()  {
        return new EmployeeDao() {
            final List<Employee> listemploy=EmployeeDao.employeeList;
            @Override
            public List<Employee> getByDepartment(Department department) {
                List<Employee> list = new ArrayList<>();
                for(Employee e:listemploy){
                    if(e.getDepartmentId().compareTo(department.getId())==0)
                        list.add(e);
                }
                return list;
            }

            @Override
            public List<Employee> getByManager(Employee employee) {
                List<Employee> list=new ArrayList<>();
                for(Employee e:listemploy){
                    if(e.getManagerId().compareTo(employee.getId())==0)
                        list.add(e);
                }
                return list;
            }

            {
                try {
                    String query = "SELECT * FROM EMPLOYEE";
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        BigInteger id = BigInteger.valueOf(rs.getInt(1));
                        FullName fullname = new FullName(rs.getString(2), rs.getString(3), rs.getString(4));
                        Position position = Position.valueOf(rs.getString(5));
                        LocalDate hired = rs.getDate(7).toLocalDate();
                        BigDecimal salary = rs.getBigDecimal(8);
                        BigInteger managerId = BigInteger.valueOf(rs.getInt(6));
                        BigInteger departmentId = BigInteger.valueOf(rs.getInt(9));
                        listemploy.add(new Employee(id, fullname, position, hired, salary, managerId, departmentId));
                    }
                } catch (SQLException e) {
                    throw new UnsupportedOperationException();
                }
            }
        };
    }

    public DepartmentDao departmentDAO() {
        return new DepartmentDao() {
            final List<Department> listdepart = DepartmentDao.departmentList;
            {
                try {
                    String query = "SELECT * FROM DEPARTMENT";
                    ResultSet rs = stm.executeQuery(query);
                    while (rs.next()) {
                        BigInteger id = BigInteger.valueOf(rs.getInt(1));
                        String name = rs.getString(2);
                        String location = rs.getString(3);
                        listdepart.add(new Department(id, name, location));
                    }
                } catch (SQLException e) {
                    throw new UnsupportedOperationException();
                }
            }
        };
    }
}
