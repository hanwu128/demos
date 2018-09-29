package com.example.cache.mapper;

import com.example.cache.bean.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    @Select("select * from employee where id = #{id}")
    public Employee getEmpById(Integer id);

    @Select("update employee set lastName = #{lastName}, email=#{email}, gender=#{gender}, dId=#{dId} where id = #{id}")
    public void updateEmp(Employee employee);

    @Select("delete from employee where id = #{id}")
    public void deleteEmpById(Integer id);

    @Select("insert into employee(lastName,email,gender,dId) values(#{lastName},#{email},#{gender},#{dId})")
    public void insertEmp(Employee employee);

    @Select("select * from employee where lastName = #{lastName}")
    Employee getEmpByName(String lastName);
}
