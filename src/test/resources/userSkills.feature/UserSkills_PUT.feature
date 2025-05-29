Feature: To check The PUT method for UserSkills
Background:
	Given userSkills User is on Endpoint: url/UserSkills with valid username and password
Scenario Outline: Validate PUT method for UserSkills
    When userSkills User sends PUT request on id and request body from "<SheetName>" and <RowNumber> with valid JSON schema
    Then userSkills User validates StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And userSkills JSON schema is valid for "<Method>"
    And userSkills User should receive a list of users with skills in JSON body with the fields like user_skill_id,user_id,Skill_Id,months_of_exp from "<SheetName>" and <RowNumber>
    And check the Database
  Examples: 
    |SheetName               |RowNumber|Method|
    |InputData_UserSkills_PUT|0        |PUT   |
    |InputData_UserSkills_PUT|1        |PUT   |
    |InputData_UserSkills_PUT|2        |PUT   |
    |InputData_UserSkills_PUT|3        |PUT   |
    |InputData_UserSkills_PUT|4        |PUT   |
   