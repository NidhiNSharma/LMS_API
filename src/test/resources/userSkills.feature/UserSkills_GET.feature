Feature: To check The GET method for UserSkills
Background:
	Given  userSkills User is on Endpoint: url/UserSkills with valid username and password
Scenario: Validate GET all UserSkills
    When userSkills User sends GET request
    Then userSkills User validates StatusCode
    And userSkills JSON schema is valid
    And check the Database for userSkills
	
	Scenario Outline: Validate UserSkills Api with specific ID  
    When userSkills User sends GET request on  id from "<SheetName>" and <RowNumber>
    Then userSkills User validates StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And userSkills JSON schema is valid for "<Method>"
    And check the Database
    Examples: 
    |SheetName               |RowNumber|Method|
    |InputData_UserSkills_GET|0        |GET|
   	|InputData_UserSkills_GET|1        |GET|
   	|InputData_UserSkills_GET|2        |GET|
   	|InputData_UserSkills_GET|3        |GET|
   	|InputData_UserSkills_GET|4        |GET|
   	|InputData_UserSkills_GET|5        |GET|
    
 