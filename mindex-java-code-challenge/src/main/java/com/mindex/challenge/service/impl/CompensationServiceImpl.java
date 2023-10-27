package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exceptions.CompensationException;
import com.mindex.challenge.exceptions.DataValidationException;
import com.mindex.challenge.exceptions.InvalidEmployeeException;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    private final CompensationRepository compensationRepository;
    private final EmployeeService employeeService;

    //Create method for new Compensation Service
    @Override
    public Compensation create(Compensation compensation) {
        //Validating that employee ID isn't empty or null
        if (compensation == null || (compensation.getEmployeeId() == null || compensation.getEmployeeId().isEmpty())){
            throw new DataValidationException("There was not an employee ID provided.");
        }
        //Pull Current Employees Details using implemented code to check and ensure that this is a valid employee
        employeeService.read(compensation.getEmployeeId());

        //Validate the rest of compensation object
        if (compensation.getSalary() == null){
            throw new DataValidationException("There was not a salary provided");
        }

        //Post Compensation record into Mongo DB.
        LOG.debug("Creating compensation for valid employee [{}]", compensation);
        compensationRepository.insert(compensation);

        return compensation;
    }

    //Read method for new Compensation Service
    @Override
    public Compensation read(String id) {

        //Validating that employee ID isn't empty or null
        if (id == null || id.isEmpty()){
            throw new DataValidationException("There was not an employee ID provided.");
        }

        //Pull Current Employees Details using implemented code to check and ensure that this is a valid employee
        employeeService.read(id);
        //Attempt to pull compensation object for given ID
        LOG.debug("Reading compensation with id [{}]", id);
        Compensation compensation = compensationRepository.findByEmployeeId(id);

        //Throw custom exception if this value does not exist.
        if (compensation == null) {
            throw new CompensationException("Compensation value not found for the provided employee ID: " + id);
        }

        return compensation;
    }
}
