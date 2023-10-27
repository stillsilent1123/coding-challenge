package com.mindex.challenge.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//Switched to using Lombok to generate getters and setters
@Data
public class Employee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<Employee> directReports;
}
