Feature: To check The GET method for UserSkillsMap API

Background:
	Given  UserSkillsMap User is on Endpoint: url/UserSkillsMap with valid username and password

Scenario: Validate GET all User-to-skill mappings

    When UserSkillsMap User sends GET request
    Then UserSkillsMap User validates StatusCode
    And UserSkillsMap JSON schema is valid
  	
Scenario Outline: Validate GET one particular User-to-skill mappings with specific user ID
	
    When UserSkillsMap_user User sends GET request on user id from "<SheetName>" and <RowNumber>
    Then UserSkillsMap_user User validates StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And UserSkillsMap JSON schema is valid
    
    Examples: 
    |SheetName               				|RowNumber|
    |InputData_UserSkillsMap_userGET|0        |
   	|InputData_UserSkillsMap_userGET|1        |
   	|InputData_UserSkillsMap_userGET|2        |
   	|InputData_UserSkillsMap_userGET|3        |
   	
Scenario Outline: Validate GET one particular Skill-to-users mappings with specific skill ID
	
    When UserSkillsMap_skill User sends GET request on user id from "<SheetName>" and <RowNumber>
    Then UserSkillsMap_skill User validates StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And UserSkillsMap_skill JSON schema is valid

    Examples: 
    |SheetName               				|RowNumber|
    |InputData_UserSkillsMap_skilGET|0        |
   	|InputData_UserSkillsMap_skilGET|1        |
   	|InputData_UserSkillsMap_skilGET|2        |
   	|InputData_UserSkillsMap_skilGET|3        |
