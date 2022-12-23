package com.epam.rd.autocode.service;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ServiceFactory implements EmployeeService {
    private static Statement statement;
    private static CompanyTree tree;
    static {
        try {
            Connection connection = ConnectionSource.instance().createConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sqlQuery = "SELECT * FROM EMPLOYEE LEFT JOIN DEPARTMENT ON EMPLOYEE.DEPARTMENT = DEPARTMENT.ID";
            tree = new CompanyTree(statement.executeQuery(sqlQuery));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public EmployeeService employeeService(){
        return new ServiceFactory();
    }
    private List < Employee > getEmployeeListPrevValue(String sqlQuery) throws SQLException {
        List < Employee > listEmployee = new ArrayList<>();
        ResultSet rs = statement.executeQuery(sqlQuery);
        while(rs.next()){
            listEmployee.add(tree.getEmployeePrevValue(rs.getInt(1)));
        }
        return listEmployee;
    }


    @Override
    public List<Employee> getAllSortByHireDate(Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE ORDER BY HIREDATE LIMIT " + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try {
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Employee> getAllSortByLastname(Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE ORDER BY LASTNAME LIMIT " + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try {
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Employee> getAllSortBySalary(Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE ORDER BY SALARY LIMIT " + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try {
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE LEFT JOIN DEPARTMENT ON EMPLOYEE.DEPARTMENT = DEPARTMENT.ID ORDER BY DEPARTMENT.NAME,EMPLOYEE.LASTNAME LIMIT "
                + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try{
            return getEmployeeListPrevValue(sqlQuery);
        } catch(SQLException e){
            return null;
        }

    }

    @Override
    public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.DEPARTMENT = " + department.getId().toString() + " ORDER BY EMPLOYEE.HIREDATE LIMIT "
                + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try{
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e){
            return null;
        }

    }

    @Override
    public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.DEPARTMENT = " + department.getId().toString() + " ORDER BY EMPLOYEE.SALARY LIMIT "
                + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try{
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e){
            return null;
        }
    }

    @Override
    public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.DEPARTMENT = " + department.getId().toString() + " ORDER BY EMPLOYEE.LASTNAME LIMIT "
                + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try{
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e){
            return null;
        }

    }

    @Override
    public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.MANAGER = " + manager.getId().toString() + " ORDER BY EMPLOYEE.LASTNAME LIMIT "
                + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try{
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e){
            return null;
        }

    }

    @Override
    public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.MANAGER = " + manager.getId().toString() + " ORDER BY EMPLOYEE.HIREDATE LIMIT "
                + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try{
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e){
            return null;
        }
    }

    @Override
    public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
        String sqlQuery = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.MANAGER = " + manager.getId().toString() + " ORDER BY EMPLOYEE.SALARY LIMIT "
                + paging.itemPerPage + " OFFSET " + paging.itemPerPage * (paging.page - 1);
        try{
            return getEmployeeListPrevValue(sqlQuery);
        } catch (SQLException e){
            return null;
        }

    }

    @Override
    public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
        return getEmployeeChainValue(employee.getId().intValue());
    }

    @Override
    public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
        String sqlQuery = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.DEPARTMENT = " + department.getId().toString() + " ORDER BY EMPLOYEE.SALARY DESC"
                + " LIMIT " + salaryRank;
        try {
            List < Employee > listEmployee = new ArrayList < > ();
            listEmployee = getEmployeeListPrevValue(sqlQuery);
            return listEmployee.get(salaryRank - 1);
        } catch (SQLException e){
            return null;
        }

    }
    private Employee getEmployeeChainValue(int empID) {
        return tree.getEmployeeChainValue(empID);
    }
    static class CompanyTree {
        private final TreeMap < Integer, Integer > employeeID = new TreeMap< >();
        private Ver[] vertex;
        private int[] deg;
        private int[] dad;
        private int countVertex = 0;

        public CompanyTree(ResultSet rs) throws SQLException {
            buildTree(rs);
        }

        private void buildTree(ResultSet rs) throws SQLException {
            while(rs.next()){
                int id = rs.getInt(1);
                employeeID.put(id,++countVertex);
            }
            deg = new int[countVertex + 2];
            dad = new int[countVertex + 2];
            Arrays.fill(deg,0);
            Arrays.fill(dad,0);
            vertex = new Ver[countVertex + 2];
            for(int u = 1;u <= countVertex;++u){
                vertex[u] = new Ver();
            }
            while(rs.previous()){
                int empId = rs.getInt(1);
                int manaId = rs.getInt(6);
                if(manaId != 0){
                    ++deg[employeeID.get(empId)];
                    addEdges(employeeID.get(manaId),employeeID.get(empId));
                }
            }
            for(int u = 1;u <= countVertex;++u){
                if(deg[u] == 0){
                    find(u,-1,rs);
                }
            }
        }

        private void addEdges(int u,int v){
            vertex[u].adj.add(v);
        }

        private void find(int u,int par,ResultSet rs) throws SQLException{
            rs.absolute(u);
            BigInteger id = BigInteger.valueOf(rs.getInt(1));
            FullName fullname = new FullName(rs.getString(2),rs.getString(3),rs.getString(4));
            Position position = Position.valueOf(rs.getString(5));
            LocalDate hired = rs.getDate(7).toLocalDate();
            BigDecimal salary = rs.getBigDecimal(8);
            Employee chainManager = (par != -1 ? vertex[par].chainValue : null);
            Employee prevManager = (par != -1 ? vertex[par].curValue : null);
            Department department = (rs.getInt(10) == 0 ? null : new Department(BigInteger.valueOf(rs.getInt(10)),rs.getString(11),rs.getString(12)));
            vertex[u].chainValue = new Employee(id,fullname,position,hired,salary,chainManager,department);
            vertex[u].prevValue = new Employee(id,fullname,position,hired,salary,prevManager,department);
            vertex[u].curValue = new Employee(id,fullname,position,hired,salary,null,department);
            dad[u] = par;
            for(int v : vertex[u].adj){
                find(v,u,rs);
            }
        }
        class Ver{
            public ArrayList< Integer > adj;
            public Employee curValue;
            public Employee chainValue;
            public Employee prevValue;
            Ver(){
                adj = new ArrayList<>();
                curValue = null;
                chainValue = null;
                prevValue = null;
            }
        }
        public Employee getEmployeeChainValue(int empID){
            return vertex[getEmployeeVertex(empID)].chainValue;
        }
        public Employee getEmployeePrevValue(int empID){
            return vertex[getEmployeeVertex(empID)].prevValue;
        }
        public int getEmployeeVertex(int empID){
            return employeeID.get(empID);
        }
    }
}
