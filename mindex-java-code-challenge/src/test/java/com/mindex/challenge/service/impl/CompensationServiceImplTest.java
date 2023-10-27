package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.CompensationException;
import com.mindex.challenge.exceptions.DataValidationException;
import com.mindex.challenge.exceptions.InvalidEmployeeException;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    @InjectMocks
    private CompensationServiceImpl compensationService;

    @Mock
    private EmployeeServiceImpl employeeService;
    @Mock
    private CompensationRepository compensationRepository;


    @Test
    public void testPostCompensation() {
        UUID employeeId = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setEmployeeId(employeeId.toString());
        testEmployee.setDirectReports(new LinkedList<>());

        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployeeId(employeeId.toString());
        expectedCompensation.setSalary(123000);
        expectedCompensation.setEffectiveDate(new Date());



        when(employeeService.read(testEmployee.getEmployeeId())).thenReturn(testEmployee);

        Compensation actualCompensation = compensationService.create(expectedCompensation);
        assertEquals(expectedCompensation, actualCompensation);
    }

    @Test
    public void testPostCompensationEmptySalary() {
        UUID employeeId = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setEmployeeId(employeeId.toString());
        testEmployee.setDirectReports(new LinkedList<>());

        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployeeId(employeeId.toString());
        expectedCompensation.setSalary(null);
        expectedCompensation.setEffectiveDate(new Date());



        when(employeeService.read(testEmployee.getEmployeeId())).thenReturn(testEmployee);

        assertThrows(DataValidationException.class, () ->  compensationService.create(expectedCompensation));
    }

    @Test
    public void testPostCompensationNullEmployeeId() {

        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployeeId(null);
        assertThrows(DataValidationException.class, () -> compensationService.create(expectedCompensation));
    }
    @Test
    public void testGetCompensationNullEmployeeId() {

        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployeeId(null);
        assertThrows(DataValidationException.class, () -> compensationService.read(null));
    }
    @Test
    public void testPostCompensationEmptyEmployeeId() {

        Compensation expectedCompensation = new Compensation();
        assertThrows(DataValidationException.class, () -> compensationService.create(expectedCompensation));
    }

    @Test
    public void testGetCompensation() {
        UUID employeeId = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setEmployeeId(employeeId.toString());
        testEmployee.setDirectReports(new LinkedList<>());

        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployeeId(employeeId.toString());
        expectedCompensation.setSalary(123000);
        expectedCompensation.setEffectiveDate(new Date());



        when(employeeService.read(testEmployee.getEmployeeId())).thenReturn(testEmployee);
        when(compensationRepository.findByEmployeeId(testEmployee.getEmployeeId())).thenReturn(expectedCompensation);

        Compensation actualCompensation = compensationService.read(employeeId.toString());
        assertEquals(expectedCompensation, actualCompensation);
    }
    @Test
    public void testErrorCreateCompensationInvalidEmployee() {
        UUID employeeId = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId(employeeId.toString());
        testEmployee.setDirectReports(new LinkedList<>());

        Compensation compensation = new Compensation();
        compensation.setEmployeeId(employeeId.toString());
        compensation.setSalary(123000);
        compensation.setEffectiveDate(new Date());

        when(employeeService.read(testEmployee.getEmployeeId())).thenThrow(InvalidEmployeeException.class);

        assertThrows(InvalidEmployeeException.class, () -> compensationService.create(compensation));
    }

    @Test
    public void testErrorGetCompensationInvalidEmployee() {
        UUID employeeId = UUID.randomUUID();

        Compensation compensation = new Compensation();
        compensation.setEmployeeId(employeeId.toString());
        compensation.setSalary(123000);
        compensation.setEffectiveDate(new Date());

        when(employeeService.read(compensation.getEmployeeId())).thenThrow(InvalidEmployeeException.class);

        assertThrows(InvalidEmployeeException.class, () -> compensationService.read(employeeId.toString()));
    }
    @Test
    public void testErrorGetCompensationInvalidCompensation() {
        UUID employeeId = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId(employeeId.toString());
        testEmployee.setDirectReports(new LinkedList<>());

        Compensation compensation = new Compensation();
        compensation.setEmployeeId(employeeId.toString());
        compensation.setSalary(123000);
        compensation.setEffectiveDate(new Date());

        when(employeeService.read(testEmployee.getEmployeeId())).thenReturn(testEmployee);
        when(compensationRepository.findByEmployeeId(testEmployee.getEmployeeId())).thenReturn(null);

        assertThrows(CompensationException.class, () -> compensationService.read(employeeId.toString()));
    }
}

