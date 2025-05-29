Feature: To check The POST method for Users
Background:
	Given UsersAPI User is on Endpoint: url/Users with valid username and password

Scenario Outline: Validate POST Users   
    When UsersAPI User  sends POST request body from "<SheetName>" and <RowNumber> with valid JSON Schema
    Then UsersAPI User validates StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And  UsersAPI JSON schema is valid for "<Method>"
    And  UsersAPI User should receive the user details in JSON body from "<SheetName>" and <RowNumber>
    And  UsersAPI check the Database
    Examples: 
    |SheetName  				 |RowNumber|Method|
    |InputData_Users_POST| 0       | POST |
    |InputData_Users_POST| 1       | POST |
    |InputData_Users_POST| 2       | POST |
    |InputData_Users_POST| 3       | POST |
    |InputData_Users_POST| 4       | POST |
    |InputData_Users_POST| 5       | POST |
    |InputData_Users_POST| 6       | POST |
    |InputData_Users_POST| 7       | POST |
    |InputData_Users_POST| 8       | POST |
    |InputData_Users_POST| 9       | POST |
    |InputData_Users_POST| 10       | POST |
    |InputData_Users_POST| 11       | POST |
    |InputData_Users_POST| 12       | POST |
    |InputData_Users_POST| 13       | POST |
    |InputData_Users_POST| 14       | POST |
    |InputData_Users_POST| 15       | POST |
    |InputData_Users_POST| 16       | POST |
    |InputData_Users_POST| 17       | POST |
    |InputData_Users_POST| 18       | POST |
    |InputData_Users_POST| 19       | POST |
    |InputData_Users_POST| 20       | POST |
    |InputData_Users_POST| 21       | POST |
    |InputData_Users_POST| 22       | POST |
    |InputData_Users_POST| 23       | POST |
    |InputData_Users_POST| 24       | POST |
    |InputData_Users_POST| 25       | POST |
    |InputData_Users_POST| 26       | POST |
    |InputData_Users_POST| 27       | POST |
    
    |location_POST       | 0        |POST |
    |time_zone_POST      | 0        |POST |
    |linkedin_url_POST   |0         |POST |
    |education_ug_POST   |0         |POST |
    |education_pg_POST   |0         |POST |
    |visa_status_POST    |0         |POST |
    |comments_POST       |0         |POST |
    |Invalid_Content     |0         |POST |
	 