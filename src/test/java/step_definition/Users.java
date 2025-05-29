package step_definition;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static util.constant.LMSApiConstant.CONST_GET_SUCCESS_STATUS_CODE;
import static util.constant.LMSApiConstant.CONST_POST_SUCCESS_STATUS_CODE;
import static util.constant.LMSApiConstant.CONST_STATUS_CODE;
import static util.constant.LMSApiConstant.CONST_STATUS_MESSAGE;
import static util.constant.LMSApiConstant.CONST_USER_ID;
import static util.constant.LMSApiConstant.CONST_USER_SKILL_ID;
import static util.constant.LMSApiConstant.CONST_USERS_API;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.core.options.CurlOption.HttpMethod;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import util.ExcelReader;
import util.Fetch_Data_From_Properties_File;
import util.Fetch_Data_From_SQL;
import util.JSON_Schema_Validation;
import util.LMSPojo;
import util.Send_Request_For_Method;

public class Users {
	private LMSPojo lmsPojo;
	private Send_Request_For_Method send_Request_For_Method;

	public Users() {
		Fetch_Data_From_Properties_File data_From_Properties_File = new Fetch_Data_From_Properties_File("Users");
		this.lmsPojo = data_From_Properties_File.getLmsPojo();
		this.send_Request_For_Method = new Send_Request_For_Method("Users");
	}

	@Given("UsersAPI User is on Endpoint: url\\/Users with valid username and password")
	public void UsersAPI_user_is_on_endpoint_url_users_with_valid_username_and_password() throws IOException {
		this.lmsPojo.setRequest_URL(Send_Request_For_Method.request_URL(this.lmsPojo.getUserName(), this.lmsPojo.getPassword()));

	}
	@Given("UsersAPI User is on Endpoint: url\\/Users username & password from {string} and {int}")
	public void users_API_user_is_on_endpoint_url_users_username_password_from_and(String sheetName, Integer rowNumber) throws Throwable, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);
		this.lmsPojo.setRequest_URL(
				Send_Request_For_Method.request_URL(testData.get(rowNumber).get("UserName"), testData.get(rowNumber).get("Password")));
	    
	}

	@When("UsersAPI User sends GET request")
	public void UsersAPI_user_sends_request_users_api() throws InterruptedException, InvalidFormatException, IOException {
		this.lmsPojo.setStr_basePath(CONST_USERS_API);
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
		this.lmsPojo.setRes_response(this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.GET, "", "", 0));
	}
	
	@When("UsersAPI User sends GET request on userid from {string} and {int}")
	public void UsersAPI_user_sends_get_request_on_userid_from_and(String sheetName, int rowNumber)
			throws IOException, InvalidFormatException, InterruptedException {
		System.out.println(sheetName);
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);

		this.lmsPojo.setStr_userid(testData.get(rowNumber).get("Users_ID"));
		this.lmsPojo.setStr_basePath(CONST_USERS_API+"/" + this.lmsPojo.getStr_userid());

		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
		
		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.GET, "", "", 0);
		this.lmsPojo.setRes_response(response);
	}
	
	@When("UsersAPI User  sends POST request body from {string} and {int} with valid JSON Schema")
	public void users_api_sends_post_request_body_from_and_with_valid_json_schema(String SheetName, Integer RowNumber) throws InvalidFormatException, IOException {
		this.lmsPojo.setStr_basePath("/Users");
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
        System.out.println("Final URI:" + this.lmsPojo.getStr_FinalURI());
		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.POST, this.lmsPojo.getExcelPath(), SheetName, RowNumber);
		this.lmsPojo.setRes_response(response);
	}
	
	@When("UsersAPI User sends PUT request on id and request body from {string} and {int} with valid JSON schema")
	public void UsersAPI_user_sends_put_request_body_from_and(String sheetName, Integer rowNumber)
			throws InvalidFormatException, IOException {

		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);

		this.lmsPojo.setStr_userid(testData.get(rowNumber).get(CONST_USER_ID));
		this.lmsPojo.setStr_basePath("/Users/" + this.lmsPojo.getStr_userid());
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());

		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.PUT, this.lmsPojo.getExcelPath(), sheetName, rowNumber);
		this.lmsPojo.setRes_response(response);

	}

	
	@Then("UsersAPI JSON schema is valid")
	public void UsersAPI_json_schema_is_valid() {
		this.lmsPojo.setStr_SchemaFilePath(this.lmsPojo.getGET_AllSchemaFilePath());
		System.out.println(this.lmsPojo.getGET_AllSchemaFilePath());
		JSON_Schema_Validation.cls_JSON_SchemaValidation(this.lmsPojo.getRes_response(),
		this.lmsPojo.getGET_AllSchemaFilePath());
		
////		this.lmsPojo.setStr_SchemaFileallusers("User_Schema_GET.json");"src/test/resources/" +
////		if ((this.lmsPojo.getRes_response().getStatusCode() == CONST_GET_SUCCESS_STATUS_CODE)
////				|| (this.lmsPojo.getRes_response().getStatusCode() == CONST_POST_SUCCESS_STATUS_CODE)) {
////			System.out.println("USER Schema file is" + this.lmsPojo.getStr_SchemaFileallusers());
////			JSON_Schema_Validation.cls_JSON_SchemaValidation(this.lmsPojo.getRes_response(),
////					this.lmsPojo.getStr_SchemaFileallusers());
//		}
	}
	
	@Then("UsersAPI JSON schema is valid for {string}")
	public void UsersAPI_json_schema_is_valid_for(String MethodName) {
		System.out.println(MethodName);
		switch (MethodName) {
		case "GET":
			this.lmsPojo.setStr_SchemaFilePath(this.lmsPojo.getGET_SchemaFilePath());
			break;
		case "POST":
			this.lmsPojo.setStr_SchemaFilePath(this.lmsPojo.getPOST_SchemaFilePath());
			break;
		case "PUT":
			this.lmsPojo.setStr_SchemaFilePath(this.lmsPojo.getPOST_SchemaFilePath());
			break;
		}
		System.out.println("Schema File Path is" + this.lmsPojo.getStr_SchemaFilePath());
		if ((this.lmsPojo.getRes_response().getStatusCode() == CONST_GET_SUCCESS_STATUS_CODE)
				|| (this.lmsPojo.getRes_response().getStatusCode() == CONST_POST_SUCCESS_STATUS_CODE)) {
			JSON_Schema_Validation.cls_JSON_SchemaValidation(this.lmsPojo.getRes_response(),
					this.lmsPojo.getStr_SchemaFilePath());
		}

	}
	@Then("UsersAPI User should receive the user details in JSON body from {string} and {int}")
	public void users_api_should_receive_the_user_details_in_json_body_from_and(String SheetName, Integer RowNumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), SheetName);

		Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());

		Map<String, String> row = testData.get(RowNumber);
		for (Map.Entry<String, String> entry : row.entrySet()) {
			for (Map.Entry<String, Object> mapResponse : jsonMap.entrySet()) {
				if (mapResponse.getKey() == entry.getKey()) {
					assertEquals(mapResponse.getKey(), entry.getKey());
				}

			}
		}
	}

	@Then("UsersAPI User validates StatusCode")
	public void UsersAPI_user_validates_status_code() throws InvalidFormatException, IOException {
		assertEquals(this.lmsPojo.getRes_response().getStatusCode(), 200);
		System.out.println("Status Code actual : " + this.lmsPojo.getRes_response().getStatusCode());
	}
	
	
	@Then("UsersAPI User validates StatusCode and StatusMessage from {string} sheet and {int} row")
	public void UsersAPI_user_receives_status_code_with(String sheetName, int rowNumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);

		Map<String, String> scenario = testData.get(rowNumber);
		String statusCodeString = scenario.get(CONST_STATUS_CODE);

		if (!StringUtils.isEmpty(statusCodeString)) {
			this.lmsPojo.setStatus_code(Integer.parseInt(statusCodeString));
		}

		this.lmsPojo.setStatus_message(testData.get(rowNumber).get(CONST_STATUS_MESSAGE));
		Response response = this.lmsPojo.getRes_response();
		assertNotNull(response);
		assertEquals(response.getStatusCode(), this.lmsPojo.getStatus_code());

		Map<String, Object> jsonMap = extractResponse(response);
		assertNotNull(jsonMap);
		if (this.lmsPojo.getStatus_message() != "") {
			assertEquals(jsonMap.get("message"), this.lmsPojo.getStatus_message());
		}
		
//		Map<String, Object> jsonMap = extractResponse_user(response);
//		assertNotNull(jsonMap);
//		if (this.lmsPojo.getStatus_message()!="") {
//			assertEquals(jsonMap.get("message"), this.lmsPojo.getStatus_message());
//		}
	}

	@Then("check the Database for all users")
	public void check_the_database_for_all_users() {
		String getStr_Query = "SELECT user_id, CONCAT(user_first_name,',', user_last_name) as name, user_phone_number as phone_number, user_location as location, user_time_zone as time_zone, user_linkedin_url as linkedin_url FROM public.tbl_lms_user";
		Map<String, String> queryResult = Fetch_Data_From_SQL.connect(this.lmsPojo.getStr_DBURL(),
				this.lmsPojo.getStr_DBUserName(), this.lmsPojo.getStr_DBPWD(), getStr_Query);
		System.out.println(queryResult);
		Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());
		
		if (queryResult == null || queryResult.isEmpty()) {

			System.out.println("No records found");

		} else {
			for (Map.Entry<String, Object> jsonMapFinal : jsonMap.entrySet()) {
				for (Map.Entry<String, String> queryResultFinal : queryResult.entrySet()) {
					if (jsonMapFinal.getKey().equalsIgnoreCase(queryResultFinal.getKey())) {
						Object obj = jsonMapFinal.getValue();

						if (obj != null && obj instanceof Integer) {
							assertEquals(Integer.toString((Integer) jsonMapFinal.getValue()),
									queryResultFinal.getValue());

						} else {
							assertEquals((String) jsonMapFinal.getValue(), queryResultFinal.getValue());
						}
					}
				}

			}
		}
	}

	
	@And("^UsersAPI check the Database$")
	public void usersAPI_check_the_database() throws Throwable {
		Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());
		String queryString = "SELECT user_id, CONCAT(user_first_name,',', user_last_name) as name, user_phone_number as phone_number, user_location as location, user_time_zone as time_zone, user_linkedin_url as linkedin_url FROM public.tbl_lms_user where user_id='" + jsonMap.get("user_id") + "'";

		Map<String, String> queryResult = Fetch_Data_From_SQL.connect(this.lmsPojo.getStr_DBURL(),
				this.lmsPojo.getStr_DBUserName(), this.lmsPojo.getStr_DBPWD(), queryString);

		if (queryResult == null || queryResult.isEmpty()) {

			System.out.println("No records found");

		} else {
			for (Map.Entry<String, Object> jsonMapFinal : jsonMap.entrySet()) {
				for (Map.Entry<String, String> queryResultFinal : queryResult.entrySet()) {
					if (jsonMapFinal.getKey().equalsIgnoreCase(queryResultFinal.getKey())) {
						Object obj = jsonMapFinal.getValue();

						if (obj != null && obj instanceof Integer) {
							assertEquals(Integer.toString((Integer) jsonMapFinal.getValue()),
									queryResultFinal.getValue());
						} else if (obj != null && obj instanceof Long) {
							assertEquals(Long.toString((Long) jsonMapFinal.getValue()),
									queryResultFinal.getValue());
						} else {				
							assertEquals((String) jsonMapFinal.getValue(), queryResultFinal.getValue());
						}
					}
				}

			}
		}
	}

	@When("UsersAPI user sends request id ON DELETE Method from {string} and {int}")
	public void users_api_sends_request_id_on_delete_method_from_and(String sheetName, int rowNumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData_del = reader.getData(this.lmsPojo.getExcelPath(), sheetName);

		this.lmsPojo.setStr_userid(testData_del.get(rowNumber).get(CONST_USER_ID));
		this.lmsPojo.setStr_basePath("/Users/" + this.lmsPojo.getStr_userid());
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());

		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.DELETE, this.lmsPojo.getExcelPath(), sheetName, rowNumber);
		this.lmsPojo.setRes_response(response);
	}


	@Then("UsersAPI User should receive a list of user in JSON body with the fields like user_id,name,phone_number,location,time_zone,linkedin_url from {string} and {int}")
	public void user_should_receive_a_list_of_users_in_json_body_with_the_fields_like_user_id_name_phone_number_location_time_zone_linkedin_url_from_and(
			String sheetName, int rowNumber) throws Throwable, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);
		if ((this.lmsPojo.getRes_response().getStatusCode() == CONST_GET_SUCCESS_STATUS_CODE)
				|| (this.lmsPojo.getRes_response().getStatusCode() == CONST_POST_SUCCESS_STATUS_CODE)) {
			Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());
	
			Map<String, String> row = testData.get(rowNumber);
			for (Map.Entry<String, String> entry : row.entrySet()) {
				for (Map.Entry<String, Object> mapResponse : jsonMap.entrySet()) {
					if (mapResponse.getKey() == entry.getKey()) {
						assertEquals(mapResponse.getValue(), entry.getValue());
					}
				}
			}
		}
	}
	
	@Then("UsersAPI User check the Database to validate deletion from {string} sheet and {int} row")
	public void users_api_check_the_database_to_validate_deletion_from_sheet_and_row(String sheetName, Integer rowNumber) throws Throwable, IOException {
		Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> DeltestData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);
		if (jsonMap.get("user_id") != null) {
		String queryString = "SELECT EXISTS(SELECT * FROM tbl_lms_user WHERE user_id='"
				+ DeltestData.get(rowNumber).get(CONST_USER_ID) + "'";

		Map<String, String> queryResult = Fetch_Data_From_SQL.connect(this.lmsPojo.getStr_DBURL(),
				this.lmsPojo.getStr_DBUserName(), this.lmsPojo.getStr_DBPWD(), queryString);
		Boolean Output;
		for (Map.Entry<String, Object> jsonMapFinal : jsonMap.entrySet()) {
			for (Map.Entry<String, String> queryResultFinal : queryResult.entrySet()) {
				if (jsonMapFinal.getKey().equalsIgnoreCase(queryResultFinal.getKey())) {
					Object obj = jsonMapFinal.getValue();
                    assertEquals((String) jsonMapFinal.getValue(), queryResultFinal.getValue());
					
				}
			}

		}
		if (queryResult == null || queryResult.isEmpty()) {
			Output = true;
			
			
		} else {
			Output = false;
		}

		if (Output == true) {
			
			System.out.println("Success");

		} else {

			System.out.println("Failed to delete");
		}

	}


	}


//@SuppressWarnings({ "unchecked", "rawtypes" })
//private Map<String, Object> extractResponse(Response response) {
//	ResponseBody responseBody = response.getBody();
//	assertNotNull(responseBody);
//	Map<String, Object> jsonMap = null;
//	ObjectMapper mapper = new ObjectMapper();
//	try {
//		jsonMap = (Map<String, Object>) mapper.readValue(responseBody.asString(), new TypeReference<List<Map<String, Object>>>(){});
//	} catch (JsonProcessingException e) {
//		// System.err.println(e.getMessage());
//	}
//	System.out.println("XXXXXXX" + jsonMap.toString());
//	return jsonMap;
//}
@SuppressWarnings({ "unchecked", "rawtypes" })
private Map<String, Object> extractResponse(Response response) {
	ResponseBody responseBody = response.getBody();
	assertNotNull(responseBody);
	Map<String, Object> jsonMap = null;
	ObjectMapper mapper = new ObjectMapper();
	try {
		jsonMap = mapper.readValue(responseBody.asString(), Map.class);
	} catch (JsonProcessingException e) {
		// System.err.println(e.getMessage());
	}
	return jsonMap;
}


}