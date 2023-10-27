package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.DataValidationException;
import com.mindex.challenge.exceptions.InvalidEmployeeException;
import com.mindex.challenge.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    //Swapped from auto wired to final
    private final EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    //Updated away from Generic Runtime Exception in case there was ever a need/desire to implement a different response code for this situation
    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new InvalidEmployeeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    //Obtain Structure for Employee Provided
    @Override
    public ReportingStructure getStructure(String id) {
        LOG.debug("Retrieving employee Reporting Structure [{}]", id);
        int count = 0;
        ReportingStructure reportingStructure = new ReportingStructure();

        //Validating that employee ID isn't empty or null before we call the read method.
        if (id == null || id.isEmpty()){
            throw new DataValidationException("There was not an employee ID provided.");
        }

        // Using read method since error handling is already defined instead of re-adding already implemented items.
        Employee employee1 = read(id);
        //Set Employee Object with Employees details
        reportingStructure.setEmployee(employee1);
        //Added Try/Catch to log if there was an invalid employee ID found in direct reports.
        try{
        //Null/Empty Check to validate that they do indeed have direct reports.
        if (employee1.getDirectReports() != null && !employee1.getDirectReports().isEmpty()){
            count +=employee1.getDirectReports().size();
            // Check next level of employees to check if they also have direct reports
            for (Employee directReports : employee1.getDirectReports()) {
                Employee employee2 = read(directReports.getEmployeeId());
                //Null/Empty Check to validate that they do indeed have direct reports.
                if (employee2.getDirectReports() != null && !employee2.getDirectReports().isEmpty()) {
                        count += employee2.getDirectReports().size();
                }
            }
        }
        }catch (InvalidEmployeeException e){
            log.error("There was a problem with a employee number during the check of direct reports." ,e);
        }
        //Set the number of total direct reports to top level employee
        reportingStructure.setNumberOfReports(count);

        //Return completed ReportingStructure Object
        return reportingStructure;
    }
}
