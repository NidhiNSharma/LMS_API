Feature: Delete Users details
Background:
	Given UsersAPI User is on Endpoint: url/Users with valid username and password
	
Scenario Outline: Validate the delete method for Users
    When UsersAPI user sends request id ON DELETE Method from "<IDsheetName>" and <rowNumber>
    Then UsersAPI User validates StatusCode and StatusMessage from "<sheetName>" sheet and <rowNumber> row
    And  UsersAPI User check the Database to validate deletion from "<IDsheetName>" sheet and <rowNumber> row
    
    Examples:
    | sheetName             | rowNumber|IDsheetName|
    | InputData_Users_DELETE| 0        |InputData_Users_POST|
    | InputData_Users_DELETE| 1        |InputData_Users_DELETE|
    | InputData_Users_DELETE| 2        |InputData_Users_DELETE|
    | InputData_Users_DELETE| 3        |InputData_Users_DELETE|
    | InputData_Users_DELETE| 4        |InputData_Users_DELETE|
  
   