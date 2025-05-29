Feature: To check The GET method for Users

  Background: 
    Given UsersAPI User is on Endpoint: url/Users with valid username and password

  Scenario: Validate GET all Users
    When UsersAPI User sends GET request
    Then UsersAPI User validates StatusCode
    And UsersAPI JSON schema is valid
    And check the Database for all users

  Scenario Outline: Validate Users Api with specific ID
    When UsersAPI User sends GET request on userid from "<SheetName>" and <RowNumber>
    Then UsersAPI User validates StatusCode and StatusMessage from "<SheetName>" sheet and <RowNumber> row
    And UsersAPI JSON schema is valid for "<Method>"
    And UsersAPI User should receive a list of user in JSON body with the fields like user_id,name,phone_number,location,time_zone,linkedin_url from "<SheetName>" and <RowNumber>
    And UsersAPI check the Database

    Examples: 
      | SheetName          | RowNumber | Method |
      | InputData_User_GET |         0 | GET    |
      | InputData_User_GET |         1 | GET    |
      | InputData_User_GET |         2 | GET    |
      | InputData_User_GET |         3 | GET    |
      | InputData_User_GET |         4 | GET    |
      | InputData_User_GET |         5 | GET    |
