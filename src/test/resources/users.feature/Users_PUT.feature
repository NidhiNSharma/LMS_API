Feature: To check The PUT method for Users

  Background: 
    Given UsersAPI User is on Endpoint: url/Users with valid username and password

  Scenario Outline: Validate PUT method for Users
    When UsersAPI User sends PUT request on id and request body from "<SheetName>" and <RowNumber> with valid JSON schema
    Then UsersAPI User validates StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And UsersAPI JSON schema is valid for "<Method>"
    And UsersAPI User should receive a list of user in JSON body with the fields like user_id,name,phone_number,location,time_zone,linkedin_url from "<SheetName>" and <RowNumber>
    And UsersAPI check the Database

    Examples: 
      | SheetName           | RowNumber | Method |
      | InputData_Users_PUT |         0 | PUT    |
      #| InputData_Users_PUT |         1 | PUT    |
      #| InputData_Users_PUT |         2 | PUT    |
      #| InputData_Users_PUT |         3 | PUT    |
      #| InputData_Users_PUT |         4 | PUT    |
