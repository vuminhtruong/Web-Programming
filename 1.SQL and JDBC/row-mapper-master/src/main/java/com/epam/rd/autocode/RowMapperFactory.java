package com.epam.rd.autocode;

import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.time.LocalDate;

public class RowMapperFactory {

    public RowMapper<Employee> employeeRowMapper() {
        return new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet resultSet) {
                try {
                    BigInteger id = BigInteger.valueOf(resultSet.getLong("ID"));
                    FullName fullName = new FullName(resultSet.getString("FIRSTNAME"),resultSet.getString("LASTNAME"),resultSet.getString("MIDDLENAME"));
                    Position position = Position.valueOf(resultSet.getString("POSITION"));
                    LocalDate localDate = resultSet.getDate("HIREDATE").toLocalDate();
                    BigDecimal salary = resultSet.getBigDecimal("SALARY");
                    return new Employee(id,fullName,position,localDate,salary);
                }
                catch (Exception ex){
                    return null;
                }
            }
        };
    }

}
