package step_definition;

import util.LMSPojo;
import util.Send_Request_For_Method;

import static org.junit.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static util.constant.LMSApiConstant.CONST_GET_SUCCESS_STATUS_CODE;
import static util.constant.LMSApiConstant.CONST_USERSKILLSMAP_ENDPOINT;
import static util.constant.LMSApiConstant.CONST_USERSKILLSMAP_SKILLQUERY_ENDPOINT;
import static util.constant.LMSApiConstant.CONST_USERSKILLSMAP_USERQUERY_ENDPOINT;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

public class UserSkillsMap {
	
	private LMSPojo lmsPojo;
	private Send_Request_For_Method send_Request_For_Method;
	
	/* Constructor - initialize LMS Pojo with data from properties files*/
	public UserSkillsMap() {
		Fetch_Data_From_Properties_File data_From_Properties_File = new Fetch_Data_From_Properties_File("UserSkillsMap");
		this.lmsPojo = data_From_Properties_File.getLmsPojo();
		this.send_Request_For_Method = new Send_Request_For_Method("UserSkillsMap");
	}
	
	@Given("UserSkillsMap User is on Endpoint: url\\/UserSkillsMap with valid username and password")
	public void user_skills_map_user_is_on_endpoint_url_user_skills_map_with_valid_username_and_password() throws IOException {
		this.lmsPojo.setRequest_URL(Send_Request_For_Method.request_URL(this.lmsPojo.getUserName(), this.lmsPojo.getPassword()));
	}

	@When("UserSkillsMap User sends GET request")
	public void user_skills_map_user_sends_get_request() throws InterruptedException, InvalidFormatException, IOException {
		this.lmsPojo.setStr_basePath(CONST_USERSKILLSMAP_ENDPOINT);
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
		this.lmsPojo.setRes_response(this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.GET, "", "", 0));
	}

	@Then("UserSkillsMap User validates StatusCode")
	public void user_skills_map_user_validates_status_code() {
		assertEquals(this.lmsPojo.getRes_response().getStatusCode(),200);
	}

	@Then("UserSkillsMap JSON schema is valid")
	public void user_skills_map_json_schema_is_valid() {
		this.lmsPojo.setStr_SchemaFilePath(this.lmsPojo.getUser_Skill_SchemaFilePath());
		if (this.lmsPojo.getRes_response().getStatusCode() == CONST_GET_SUCCESS_STATUS_CODE) {
			JSON_Schema_Validation.cls_JSON_SchemaValidation(this.lmsPojo.getRes_response(), 
					this.lmsPojo.getStr_SchemaFilePath());
		}
	}

	@When("UserSkillsMap_user User sends GET request on user id from {string} and {int}")
	public void user_skills_map_user_user_sends_get_request_on_user_id_from_and(String sheetName, Integer rowNumber) 
			throws IOException, InvalidFormatException, InterruptedException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);

		//this.lmsPojo.setStr_basePath("/UserSkillsMap?user_id=" + (testData.get(rowNumber).get("user_id")));
		this.lmsPojo.setStr_basePath(CONST_USERSKILLSMAP_USERQUERY_ENDPOINT + (testData.get(rowNumber).get("user_id")));		
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());

		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.GET, "", "", 0);
		this.lmsPojo.setRes_response(response);
	}

	@Then("UserSkillsMap_user User validates StatusCode and StatusMessage from {string} sheet and {int} row")
	public void user_skills_map_user_user_validates_status_code_and_status_message_from_sheet_and_row(String sheetName, Integer rowNumber)
			throws InvalidFormatException, IOException {
		
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);

		Map<String, String> scenario = testData.get(rowNumber);
		String statusCodeString = scenario.get("StatusCode");

		if (!StringUtils.isEmpty(statusCodeString)) {
			this.lmsPojo.setStatus_code(Integer.parseInt(statusCodeString));
		}

		this.lmsPojo.setStatus_message(testData.get(rowNumber).get("Status_Message"));

		Response response = this.lmsPojo.getRes_response();

		assertNotNull(response);
		assertEquals(response.getStatusCode(), this.lmsPojo.getStatus_code());

		Map<String, Object> jsonMap = extractResponse(response);
		assertNotNull(jsonMap);
		if (this.lmsPojo.getStatus_message() != "") {
			assertEquals(jsonMap.get("message"), this.lmsPojo.getStatus_message());
		}
	}

	@When("UserSkillsMap_skill User sends GET request on user id from {string} and {int}")
	public void user_skills_map_skill_user_sends_get_request_on_user_id_from_and(String sheetName, Integer rowNumber)
			throws InvalidFormatException, IOException{
		
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);

		this.lmsPojo.setStr_basePath(CONST_USERSKILLSMAP_SKILLQUERY_ENDPOINT + (testData.get(rowNumber).get("skill_id")));
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());

		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.GET, "", "", 0);
		this.lmsPojo.setRes_response(response);
	}

	@Then("UserSkillsMap_skill User validates StatusCode and StatusMessage from {string} sheet and {int} row")
	public void user_skills_map_skill_user_validates_status_code_and_status_message_from_sheet_and_row(String sheetName, Integer rowNumber)
			throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);

		Map<String, String> scenario = testData.get(rowNumber);
		String statusCodeString = scenario.get("StatusCode");

		if (!StringUtils.isEmpty(statusCodeString)) {
			this.lmsPojo.setStatus_code(Integer.parseInt(statusCodeString));
		}

		this.lmsPojo.setStatus_message(testData.get(rowNumber).get("Status_Message"));

		Response response = this.lmsPojo.getRes_response();

		assertNotNull(response);
		assertEquals(response.getStatusCode(), this.lmsPojo.getStatus_code());

		Map<String, Object> jsonMap = extractResponse(response);
		assertNotNull(jsonMap);
		if (this.lmsPojo.getStatus_message() != "") {
			assertEquals(jsonMap.get("message"), this.lmsPojo.getStatus_message());
		}
	}

	@Then("UserSkillsMap_skill JSON schema is valid")
	public void user_skills_map_skill_json_schema_is_valid() {
		this.lmsPojo.setStr_SchemaFilePath(this.lmsPojo.getSkill_User_SchemaFilePath());
		if (this.lmsPojo.getRes_response().getStatusCode() == CONST_GET_SUCCESS_STATUS_CODE) {
			JSON_Schema_Validation.cls_JSON_SchemaValidation(this.lmsPojo.getRes_response(), 
					this.lmsPojo.getStr_SchemaFilePath());
		}
	}
	
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
