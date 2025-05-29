Feature: Check the Authorization for LMS API

Scenario Outline: Check the Authorization with with different set of username & password
		Given UsersAPI User is on Endpoint: url/Users username & password from "<sheetName>" and <rowNumber>
    When UsersAPI User sends GET request
    Then UsersAPI User validates StatusCode and StatusMessage from "<sheetName>" sheet and <rowNumber> row

    
    Examples:
    | sheetName    | rowNumber|
    | Users_Authorization| 0        |
    | Users_Authorization| 1        |
    | Users_Authorization| 2        |
    | Users_Authorization| 3        |
   