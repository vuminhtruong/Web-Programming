package com.epam.rd.autocode;


import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class SetMapperFactory {

    public SetMapper<Set<Employee>> employeesSetMapper() {
       return new SetMapper<Set<Employee>>() {
           @Override
           public Set<Employee> mapSet(ResultSet resultSet) {
               Set < Employee > employeeSet = new TreeSet<>(Comparator.comparingInt(Employee::hashCode));
               try {
                   CompanyTree tree = new CompanyTree(resultSet);
                   resultSet.beforeFirst();
                   while(resultSet.next()){
                       employeeSet.add(tree.getEmployeeChainValue(resultSet.getInt(1)));
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
               return employeeSet;
           }
       };
    }
    static class CompanyTree {
        private final TreeMap< Integer, Integer > employeeID = new TreeMap< >();
        private Vertex[] gr;
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
            int[] degIn = new int[countVertex + 2];
            dad = new int[countVertex + 2];
            Arrays.fill(degIn,0);
            Arrays.fill(dad,0);
            gr = new Vertex[countVertex + 2];
            for(int u = 1;u <= countVertex;++u){
                gr[u] = new Vertex();
            }
            while(rs.previous()){
                int empId = rs.getInt(1);
                int manaId = rs.getInt(6);
                if(employeeID.get(manaId) != null){
                    ++degIn[employeeID.get(empId)];
                    addEdges(employeeID.get(manaId),employeeID.get(empId));
                }
            }
            for(int u = 1;u <= countVertex;++u){
                if(degIn[u] == 0){
                    dfs(u,-1,rs);
                }
            }
        }

        private void addEdges(int u,int v){
            gr[u].adj.add(v);
        }

        private void dfs(int u,int par,ResultSet rs) throws SQLException{
            rs.absolute(u);
            BigInteger id = BigInteger.valueOf(rs.getInt(1));
            FullName fullname = new FullName(rs.getString(2),rs.getString(3),rs.getString(4));
            Position position = Position.valueOf(rs.getString(5));
            LocalDate hired = rs.getDate(7).toLocalDate();
            BigDecimal salary = rs.getBigDecimal(8);
            Employee chainManager = (par != -1 ? gr[par].chainValue : null);
            Employee prevManager = (par != -1 ? gr[par].curValue : null);
            gr[u].chainValue = new Employee(id,fullname,position,hired,salary,chainManager);
            gr[u].prevValue = new Employee(id,fullname,position,hired,salary,prevManager);
            gr[u].curValue = new Employee(id,fullname,position,hired,salary,null);
            dad[u] = par;
            for(int v : gr[u].adj){
                dfs(v,u,rs);
            }
        }

        class Vertex{
            public ArrayList< Integer > adj;
            public Employee curValue;
            public Employee chainValue;
            public Employee prevValue;
            Vertex(){
                adj = new ArrayList < > ();
                curValue = null;
                chainValue = null;
                prevValue = null;
            }
        }
        public int getEmployeeVertex(int empID){
            return employeeID.get(empID);
        }

        public Employee getEmployeeChainValue(int empID){
            return gr[getEmployeeVertex(empID)].chainValue;
        }
    }
}
