package com.mindex.challenge.data;


import lombok.*;


//Used Lombok to generate getters and setters
@Data
public class ReportingStructure {
    private Employee employee;
    private Integer numberOfReports;
}
