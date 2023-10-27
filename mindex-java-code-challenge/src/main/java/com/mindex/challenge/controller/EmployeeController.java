package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


//Implemented Lombok
@RestController
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    //Swapped from auto wired to final
    private final EmployeeService employeeService;

    private final CompensationService compensationService;



    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    //New Reporting Structure endpoint. Since this still is employee based I decided to not create a new Controller for these items.
    @GetMapping("/employee/{id}/reporting-structure")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
        LOG.debug("Received reporting structure request for the given employee ID [{}]", id);
        return employeeService.getStructure(id);

    }

    //New POST Compensation endpoint. Since this still is employee based I decided to not create a new Controller for these items.
    @PostMapping("/compensation")
    public Compensation compensationCreate(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);
        return compensationService.create(compensation);
    }

    //New GET Compensation endpoint by ID. Since this still is employee based I decided to not create a new Controller for these items.
    @GetMapping("/employee/{id}/compensation")
    public Compensation compensationRead(@PathVariable String id) {
        LOG.debug("Received employee compensation read request for id [{}]", id);
        return compensationService.read(id);
    }
}
