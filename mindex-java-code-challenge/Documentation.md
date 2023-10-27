### Key Updates:
- Implemented Lombok
- Switched away from Autowired to final implementation
- Added Unit tests to provide coverage over added code

### New Endpoints
The following endpoints are available to use:
```
* CREATE Compensation
    * HTTP Method: POST 
    * URL: localhost:8080/compensation
    * PAYLOAD: Compensation
    * RESPONSE: Compensation
* READ Compensation
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}/compensation
    * RESPONSE: Compensation
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}/reporting-structure
    * RESPONSE: ReportingStructure

```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
The Compensation has a JSON schema of:
```json
{
  "type":"Compensation",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "salary": {
      "type": "integer"
    },
    "effectiveDate": {
          "type": "date"
    }
  }
}
```
The ReportingStructure has a JSON schema of:
```json
{
  "type":"ReportingStructure",
  "properties": {
    "employee": {
      "type": "employee"
    },
    "numberOfReports": {
      "type": "integer"
    }
  }
}
```

### Task 1
Create a new type, ReportingStructure, that has two properties: employee and numberOfReports.

Created New Exceptions/Objects:
- ReportingStructure (Object)
- InvalidEmployeeException

Changes added to solve this coding question:
- Added new endpoint to EmployeeController
  - URL: localhost:8080/employee/{id}/reporting-structure
  - Method Name: getReportingStructure(String id)
- Added new method in EmployeeService/EmployeeServiceImpl
  - Method Name: getStructure(String id)

Based off of data provided I have added code that loops through the employees using for each loops to calculate and 
determine how many direct reporters that the ID provided has under them. I have added comments through-out the code that
 that describes how I addressed the problem. I have included null and empty checks while processing/checking for the direct
reporters. As well as additional exception handling when looping through those direct reports.


### Task 2
Create a new type, Compensation. A Compensation has the following fields: employee, salary, and effectiveDate. Create 
two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
Compensation from the persistence layer.

Created New Classes/Interfaces/Exceptions:
- CompensationService and CompensationServiceImpl
- CompensationException
- Compensation
- CompensationRepository

Changes added to solve this coding question:
- Added 2 new endpoints to EmployeeController
    - URL:localhost:8080/compensation
      - Method Name: compensationCreate(compensation)
    - URL:localhost:8080/employee/{id}/compensation
        - Method Name: compensationRead(id)
- New methods in CompensationService/CompensationServiceImpl
    - Method Name: create(Compensation compensation);
    - Method Name: read(String id);

I have created 2 new endpoints to perform these actions. In the methods for these I perform checks to validate that the 
employee ids provided are valid, and then perform the required actions. Added additional validation over the compensation
 object as well before we attempt to add it to the mongo db.

