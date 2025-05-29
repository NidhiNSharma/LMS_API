Feature: To check The PUT method for Skills
Background:
	Given Skills User is on Endpoint: url/Skills with valid username and password

Scenario Outline: Verify PUT for a Skill when user logged with UserName with Password   
    When skills User sends PUT request on id and request body in skills from "<SheetName>" and <RowNumber> with valid JSON Schema
    Then skills User validates the StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And JSON schema is valid for "<Method>" in Skills
    And skills check the Database
    Examples: 
    |SheetName         |RowNumber|Method|
    |Skills_PUT        |0        |PUT |
    |Skills_PUT        |1        |PUT |
    |Skills_PUT        |2        |PUT |
    |Skills_PUT        |3        |PUT |
    |Skills_PUT        |4        |PUT |
    |Skills_PUT        |5        |PUT |
    |Skills_PUT        |6        |PUT |
    |Skills_PUT        |7        |PUT |
    |Skills_PUT        |8        |PUT |
   
	 
Scenario Outline: Verify PUT for a Skill when user logged with UserName with Password   
    When skills User sends PUT request on id and request body in skills from "<SheetName>" and <RowNumber> with valid JSON Schema
    Then skills User validates the StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And skills check the Database
     Examples: 
         |SheetName         |RowNumber|
		     |Invalid_Key_PUT   |0        |
		     |Invalid_content_PUT|0       |
Scenario Outline: Verify PUT fails for a Skill with improper format when user logged with UserName with Password   
    When skills User sends PUT request on id and request body in text format skills from "<sheetname1>" and <rownumber1>
    Then skills User validates the StatusCode and StatusMessage from "<sheetname1>" sheet and <rownumber1> row
    And skills User should receive the skill in JSON body from "<sheetname1>" and <rownumber1>
    And skills check the Database
    Examples:
      |sheetname1    |rownumber1|
		  |Text_PUT      |0         |