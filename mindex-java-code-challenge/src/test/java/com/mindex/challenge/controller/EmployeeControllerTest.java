package com.mindex.challenge.controller;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.CompensationException;
import com.mindex.challenge.exceptions.InvalidEmployeeException;
import com.mindex.challenge.service.impl.CompensationServiceImpl;
import com.mindex.challenge.service.impl.EmployeeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeServiceImpl employeeService;
    @Mock
    private CompensationServiceImpl compensationService;


    @Test
    public void testGetReportingStructure() {
        UUID employeeId = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setEmployeeId(employeeId.toString());
        testEmployee.setDirectReports(new LinkedList<>());

        ReportingStructure expectedReportingStructure = new ReportingStructure();
        expectedReportingStructure.setEmployee(testEmployee);
        expectedReportingStructure.setNumberOfReports(0);

        when(employeeService.getStructure(employeeId.toString())).thenReturn(expectedReportingStructure);

        ReportingStructure reportingStructure = employeeController.getReportingStructure(employeeId.toString());

        assertEquals(expectedReportingStructure, reportingStructure);
    }

    @Test
    public void testPostCompensation() {
        UUID employeeId = UUID.randomUUID();

        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployeeId(employeeId.toString());
        expectedCompensation.setSalary(123000);
        expectedCompensation.setEffectiveDate(new Date());

        when(compensationService.create(expectedCompensation)).thenReturn(expectedCompensation);

        Compensation actualCompensation = employeeController.compensationCreate(expectedCompensation);
        assertEquals(expectedCompensation, actualCompensation);
    }
    @Test
    public void testGetCompensation() {
        UUID employeeId = UUID.randomUUID();
        Compensation expectedCompensation = new Compensation();
        expectedCompensation.setEmployeeId(employeeId.toString());
        expectedCompensation.setSalary(123000);
        expectedCompensation.setEffectiveDate(new Date());

        when(compensationService.read(expectedCompensation.getEmployeeId())).thenReturn(expectedCompensation);

        Compensation actualCompensation = employeeController.compensationRead(expectedCompensation.getEmployeeId());
        assertEquals(expectedCompensation, actualCompensation);
    }
}

