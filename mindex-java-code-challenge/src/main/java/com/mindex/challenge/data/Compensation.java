package com.mindex.challenge.data;


import lombok.*;

import java.time.ZonedDateTime;
import java.util.Date;

//Used Lombok to generate getters and setters
@Data
public class Compensation {

    private String employeeId;

    private Integer salary;

    private Date effectiveDate;

}
