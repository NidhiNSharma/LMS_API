Feature: To check The POST method for UserSkills
Background:
	Given userSkills User is on Endpoint: url/UserSkills with valid username and password
	
Scenario Outline: Validate POST all UserSkills    
    When userSkills User sends POST request body from "<SheetName>" and <RowNumber> with valid JSON Schema
    Then userSkills User validates StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And userSkills JSON schema is valid for "<Method>"
    And userSkills User should receive a list of users with skills in JSON body with the fields like user_skill_id,user_id,Skill_Id,months_of_exp from "<SheetName>" and <RowNumber>
    And check the Database
  Examples: 
    |SheetName                |RowNumber|Method|
    |InputData_UserSkills_POST|0        |POST|
   	|InputData_UserSkills_POST|1        |POST|
   	|InputData_UserSkills_POST|2        |POST|
   	|InputData_UserSkills_POST|3        |POST|
   	|InputData_UserSkills_POST|4        |POST|
   	|InputData_UserSkills_POST|5        |POST|
   	|InputData_UserSkills_POST|6        |POST|
   	|InputData_UserSkills_POST|7        |POST|
   	|InputData_UserSkills_POST|8        |POST|

