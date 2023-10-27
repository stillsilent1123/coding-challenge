package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
        assertEquals(expected.getDirectReports(), actual.getDirectReports());
    }

    @Test
    public void testGetStructureFirstLevel() {
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

        when(employeeRepository.findByEmployeeId(testEmployee.getEmployeeId())).thenReturn(testEmployee);

        ReportingStructure actualReportingStructure = employeeService.getStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, actualReportingStructure.getEmployee());
        assertEquals(expectedReportingStructure, actualReportingStructure);
    }
    @Test
    public void testEmptyEmployeeID() {
        assertThrows(DataValidationException.class, () -> employeeService.getStructure(null));
    }

    @Test
    public void testGetStructureSecondLevel() {
        UUID employeeId = UUID.randomUUID();
        UUID employee2Id = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setEmployeeId(employeeId.toString());

        Employee linkedEmployeeID = new Employee();
        linkedEmployeeID.setEmployeeId(employee2Id.toString());

        List<Employee> directReports = new ArrayList<>();

        directReports.add(linkedEmployeeID);
        testEmployee.setDirectReports(directReports);



        Employee linkedEmployeeFullDetails = new Employee();
        linkedEmployeeID.setEmployeeId(employee2Id.toString());

        linkedEmployeeFullDetails.setFirstName("Paul");
        linkedEmployeeFullDetails.setLastName("McCartney");
        linkedEmployeeFullDetails.setDepartment("Engineering");
        linkedEmployeeFullDetails.setPosition("Developer 1");
        linkedEmployeeFullDetails.setEmployeeId(employee2Id.toString());
        linkedEmployeeFullDetails.setDirectReports(new LinkedList<>());


        ReportingStructure expectedReportingStructure = new ReportingStructure();
        expectedReportingStructure.setEmployee(testEmployee);
        expectedReportingStructure.setNumberOfReports(1);

        when(employeeRepository.findByEmployeeId(testEmployee.getEmployeeId())).thenReturn(testEmployee);
        when(employeeRepository.findByEmployeeId(linkedEmployeeFullDetails.getEmployeeId())).thenReturn(linkedEmployeeFullDetails);


        ReportingStructure actualReportingStructure = employeeService.getStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, actualReportingStructure.getEmployee());
        assertEquals(expectedReportingStructure, actualReportingStructure);
    }

    @Test
    public void testGetStructureThirdLevel() {
        UUID employeeId = UUID.randomUUID();
        UUID employee2Id = UUID.randomUUID();
        UUID employee3Id = UUID.randomUUID();

        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setEmployeeId(employeeId.toString());

        Employee linkedEmployeeID = new Employee();
        linkedEmployeeID.setEmployeeId(employee2Id.toString());

        List<Employee> directReports = new ArrayList<>();

        directReports.add(linkedEmployeeID);
        testEmployee.setDirectReports(directReports);



        Employee linkedEmployeeFullDetails = new Employee();
        linkedEmployeeID.setEmployeeId(employee2Id.toString());

        linkedEmployeeFullDetails.setFirstName("Paul");
        linkedEmployeeFullDetails.setLastName("McCartney");
        linkedEmployeeFullDetails.setDepartment("Engineering");
        linkedEmployeeFullDetails.setPosition("Developer 1");
        linkedEmployeeFullDetails.setEmployeeId(employee2Id.toString());

        Employee secondLinkedEmployeeID = new Employee();
        secondLinkedEmployeeID.setEmployeeId(employee3Id.toString());

        List<Employee> directReportsSecondLevel = new ArrayList<>();

        directReportsSecondLevel.add(secondLinkedEmployeeID);
        linkedEmployeeFullDetails.setDirectReports(directReportsSecondLevel);



        Employee secondlinkedEmployeeFullDetails = new Employee();
        secondlinkedEmployeeFullDetails.setEmployeeId(employee3Id.toString());
        secondlinkedEmployeeFullDetails.setFirstName("Pete");
        secondlinkedEmployeeFullDetails.setLastName("Best");
        secondlinkedEmployeeFullDetails.setDepartment("Engineering");
        secondlinkedEmployeeFullDetails.setPosition("Developer 1I");
        secondlinkedEmployeeFullDetails.setEmployeeId(employee3Id.toString());
        secondlinkedEmployeeFullDetails.setDirectReports(new LinkedList<>());


        ReportingStructure expectedReportingStructure = new ReportingStructure();
        expectedReportingStructure.setEmployee(testEmployee);
        expectedReportingStructure.setNumberOfReports(2);

        when(employeeRepository.findByEmployeeId(testEmployee.getEmployeeId())).thenReturn(testEmployee);
        when(employeeRepository.findByEmployeeId(linkedEmployeeFullDetails.getEmployeeId())).thenReturn(linkedEmployeeFullDetails);
        when(employeeRepository.findByEmployeeId(secondlinkedEmployeeFullDetails.getEmployeeId())).thenReturn(secondlinkedEmployeeFullDetails);


        ReportingStructure actualReportingStructure = employeeService.getStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, actualReportingStructure.getEmployee());
        assertEquals(expectedReportingStructure, actualReportingStructure);
    }


    @Test
    public void testGetStructureExceptionInLoop() {
        UUID employeeId = UUID.randomUUID();
        UUID employee2Id = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setEmployeeId(employeeId.toString());

        Employee linkedEmployeeID = new Employee();
        linkedEmployeeID.setEmployeeId(employee2Id.toString());

        List<Employee> directReports = new ArrayList<>();

        directReports.add(linkedEmployeeID);
        testEmployee.setDirectReports(directReports);



        Employee linkedEmployeeFullDetails = new Employee();
        linkedEmployeeID.setEmployeeId(employee2Id.toString());

        linkedEmployeeFullDetails.setFirstName("Paul");
        linkedEmployeeFullDetails.setLastName("McCartney");
        linkedEmployeeFullDetails.setDepartment("Engineering");
        linkedEmployeeFullDetails.setPosition("Developer 1");
        linkedEmployeeFullDetails.setEmployeeId(employee2Id.toString());
        linkedEmployeeFullDetails.setDirectReports(new LinkedList<>());


        ReportingStructure expectedReportingStructure = new ReportingStructure();
        expectedReportingStructure.setEmployee(testEmployee);
        expectedReportingStructure.setNumberOfReports(1);

        when(employeeRepository.findByEmployeeId(testEmployee.getEmployeeId())).thenReturn(testEmployee);
        when(employeeRepository.findByEmployeeId(linkedEmployeeFullDetails.getEmployeeId())).thenReturn(null);


        ReportingStructure actualReportingStructure = employeeService.getStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, actualReportingStructure.getEmployee());
        assertEquals(expectedReportingStructure, actualReportingStructure);
    }

    @Test
    public void testErrorInvalidEmployee() {
        UUID employeeId = UUID.randomUUID();
        Employee testEmployee = new Employee();
        testEmployee.setEmployeeId(employeeId.toString());
        testEmployee.setDirectReports(new LinkedList<>());

        ReportingStructure expectedReportingStructure = new ReportingStructure();
        expectedReportingStructure.setEmployee(testEmployee);
        expectedReportingStructure.setNumberOfReports(0);

        when(employeeRepository.findByEmployeeId(testEmployee.getEmployeeId())).thenReturn(null);

        assertThrows(InvalidEmployeeException.class, () -> employeeService.getStructure(testEmployee.getEmployeeId()));
    }
}

