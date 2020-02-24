package com.employee.dao;


import static org.assertj.core.api.Assertions.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.employee.entity.Employee;

@Sql(scripts= {"classpath:/db/create-table.sql", "classpath:/db/insert-employee.sql"})
@ContextConfiguration("classpath:data-context.xml")
@RunWith(SpringRunner.class)
public class EmployeeDaoImplTest {
	
	@Autowired
	private Environment env;
	
	@Autowired
	EmployeeDao employeeDaoImpl;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setup() throws Exception {
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	

	
	@Test
	public void dbconnectionTest() throws SQLException {
		String jdbcUrl = env.getProperty("test.jdbcUrl");
		String user = env.getProperty("test.user");
		String password = env.getProperty("test.password");
		String driver = env.getProperty("test.driver");
		
		System.out.println(jdbcUrl +" "+user+" "+password);
		
		Connection dbCon = null;
		
		try {
			dbCon = DriverManager.getConnection(jdbcUrl, user, password);
			assertThat(dbCon).isNotNull();
		}
		
		catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		finally {
			dbCon.close();
		}
	}
	
	@Test
	public void saveEmployeeToDBTest() {
		
		
		assertThat(employeeDaoImpl).isNotNull();

		Employee newEmployee = new Employee();
		Date employeeDate = Date.valueOf("2000-7-24");
		
		newEmployee.setFirstName("Mary");
		newEmployee.setLastName("Black");
		newEmployee.setEmail("mary@mail.com");
		newEmployee.setPhoneNumber("0705857546755");
		
		newEmployee.setDateOfBirth(employeeDate);
		
		employeeDaoImpl.saveEmployee(newEmployee);
		
		int id = newEmployee.getEmployeeId();
		
		System.out.println("New Employee Id --> " + id);
		
		Employee existingEmployee = employeeDaoImpl.getById(id);
		assertThat(existingEmployee).isNotNull();
		
		employeeDaoImpl.saveEmployee(newEmployee);
		
		
	}
	
	@Test
	public void getEmployeeByEmailTest() {
		assertThat(employeeDaoImpl).isNotNull();
		Employee savedEmployee = employeeDaoImpl.getByEmail("ray@mail.com");
		
		assertThat(savedEmployee).isNotNull();
		assertThat(savedEmployee.getEmployeeId()).isEqualTo(4);
		System.out.println(savedEmployee);
	}
	

}
