Feature: Check the Authorization for LMS API

Scenario Outline: Check the Authorization with with different set of username & password
		Given userSkill User with  username & password from "<sheetName>" and <rowNumber> is on Endpoint: url/UserSkills
    When userSkills User sends GET request
    Then userSkills User Checks for StatusCode StatusCode and StatusMessage from "<sheetName>" sheet and <rowNumber> row

    
    Examples:
    | sheetName    | rowNumber|
    | UserSkills_Authorization| 0        |
    | UserSkills_Authorization| 1        |
    | UserSkills_Authorization| 2        |
    | UserSkills_Authorization| 3        |
   