package step_definition;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static util.constant.LMSApiConstant.CONST_GET_SUCCESS_STATUS_CODE;
import static util.constant.LMSApiConstant.CONST_POST_SUCCESS_STATUS_CODE;
import static util.constant.LMSApiConstant.CONST_SCENARIO;
import static util.constant.LMSApiConstant.CONST_SKILL_ID;
import static util.constant.LMSApiConstant.CONST_SKILL_NAME;
import static util.constant.LMSApiConstant.CONST_SKILLS_API;
import static util.constant.LMSApiConstant.CONST_STATUS_CODE;
import static util.constant.LMSApiConstant.CONST_STATUS_MESSAGE;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hamcrest.collection.IsMapContaining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.core.options.CurlOption.HttpMethod;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import util.ExcelReader;
import util.ExcelWriter;
import util.Fetch_Data_From_Properties_File;
import util.Fetch_Data_From_SQL;
import util.JSON_Schema_Validation;
import util.LMSPojo;
import util.Send_Request_For_Method;
public class SkillMaster {
	
	private LMSPojo lmsPojo;
	private Send_Request_For_Method send_Request_For_Method;

	public SkillMaster() {
		Fetch_Data_From_Properties_File data_From_Properties_File = new Fetch_Data_From_Properties_File("Skills");
		this.lmsPojo = data_From_Properties_File.getLmsPojo();
		this.send_Request_For_Method = new Send_Request_For_Method("Skills");
	}


   @Given("Skills User is on Endpoint: url\\/Skills with valid username and password")
	public void skills_user_is_on_endpoint_url_user_skills() throws IOException {
		
		this.lmsPojo.setRequest_URL(
				Send_Request_For_Method.request_URL(this.lmsPojo.getUserName(), this.lmsPojo.getPassword()));

	}
	
	@When("skills User sends GET request")
	public void user_sends_get_request_on_skills() throws InvalidFormatException, IOException {
		
		this.lmsPojo.setStr_basePath("/"+CONST_SKILLS_API);
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
		
		this.lmsPojo.setRes_response(this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.GET, "", "", 0));
	}
	
	@When("User sends GET request on skill id from {string} and {int}")
	public void user_sends_get_request_on_skill_id_from_and(String SheetName, int RowNumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), SheetName);
        this.lmsPojo.setStr_skillid(testData.get(RowNumber).get(CONST_SKILL_ID));
		
		
		this.lmsPojo.setStr_basePath("/"+CONST_SKILLS_API + this.lmsPojo.getStr_skillid());

		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());

		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.GET, "", "", 0);
		this.lmsPojo.setRes_response(response);

	}
	@When("skills User sends POST request body in skills from {string} and {int} with valid JSON Schema")
	public void user_sends_POST_request_body_from_and(String SheetName,int RowNumber) throws InvalidFormatException, IOException {
		this.lmsPojo.setStr_basePath("/"+CONST_SKILLS_API);
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
        System.out.println("Final URI:" + this.lmsPojo.getStr_FinalURI());
		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.POST, this.lmsPojo.getExcelPath(), SheetName, RowNumber);
		this.lmsPojo.setRes_response(response);
		
	}
	@When("skills User sends POST request body in text format skills from {string} and {int}")
	public void skills_User_sends_POST_request_body_in_text_format_skills_from(String SheetName,int RowNumber) throws InvalidFormatException, IOException {
		this.lmsPojo.setStr_basePath("/"+CONST_SKILLS_API);
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
        System.out.println("Final URI:" + this.lmsPojo.getStr_FinalURI());
       
        String numericColumns = this.lmsPojo.getNumericColumns();
        ExcelReader reader = new ExcelReader();
        System.out.println("Excel path:" + this.lmsPojo.getExcelPath());
        System.out.println("Sheetname:"+ SheetName );
		List<Map<String, String>> excelRows = reader.getData("./" + this.lmsPojo.getExcelPath(), SheetName);
		Map<String, Object> finalMap = new HashMap<>();
        Map<String, String> row = excelRows.get(RowNumber);
		row.remove(CONST_SCENARIO);
		row.remove(CONST_STATUS_CODE);
		row.remove(CONST_STATUS_MESSAGE);
		row.remove(CONST_SKILL_ID);
		for (Map.Entry<String, String> entry : row.entrySet()) {
			Object value = entry.getValue();
		    if (numericColumns.contains(entry.getKey())) {
			value = (StringUtils.isNotEmpty(entry.getValue()) && StringUtils.isNumeric(entry.getValue()))
					? Integer.parseInt(entry.getValue())
					: entry.getValue();
		  }
		  finalMap.put(entry.getKey(), value);
	   }
	  ObjectMapper mapper = new ObjectMapper();
	  String requestString = mapper.writeValueAsString(finalMap);
	  Response response = this.lmsPojo.getRequest_URL().header(HttpHeaders.CONTENT_TYPE, ContentType.TEXT).body(requestString).post(this.lmsPojo.getStr_FinalURI());
      System.out.println("Response:" + response);

		if (response != null) {
			System.out.println("Response :\n" + response.asString());
			}
		this.lmsPojo.setRes_response(response);
	}
	
	@When("skills User sends PUT request on id and request body in skills from {string} and {int} with valid JSON Schema")
	public void user_sends_put_request_body_in_skills_from_and_with_valid_json_schema(String SheetName, Integer RowNumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), SheetName);
		
		this.lmsPojo.setStr_skillid(testData.get(RowNumber).get(CONST_SKILL_ID));
		System.out.println(this.lmsPojo.getStr_skillid());
		this.lmsPojo.setStr_basePath("/"+CONST_SKILLS_API + "/" + this.lmsPojo.getStr_skillid());
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
		System.out.println(this.lmsPojo.getStr_FinalURI());
		
        
		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.PUT, this.lmsPojo.getExcelPath(), SheetName, RowNumber);
		this.lmsPojo.setRes_response(response);
	}
	@When("skills User sends PUT request on id and request body in text format skills from {string} and {int}")
	public void skills_User_sends_PUT_request_body_in_text_format_skills_from(String SheetName,int RowNumber) throws InvalidFormatException, IOException {
		this.lmsPojo.setStr_basePath("/"+CONST_SKILLS_API);
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());
        System.out.println("Final URI:" + this.lmsPojo.getStr_FinalURI());
        
        
       
        String numericColumns = this.lmsPojo.getNumericColumns();
        ExcelReader reader = new ExcelReader();
        System.out.println("Excel path:" + this.lmsPojo.getExcelPath());
        System.out.println("Sheetname:"+ SheetName );
		List<Map<String, String>> excelRows = reader.getData("./" + this.lmsPojo.getExcelPath(), SheetName);
		Map<String, Object> finalMap = new HashMap<>();
        Map<String, String> row = excelRows.get(RowNumber);
		row.remove(CONST_SCENARIO);
		row.remove(CONST_STATUS_CODE);
		row.remove(CONST_STATUS_MESSAGE);
		row.remove(CONST_SKILL_ID);
		for (Map.Entry<String, String> entry : row.entrySet()) {
			Object value = entry.getValue();
		    if (numericColumns.contains(entry.getKey())) {
			value = (StringUtils.isNotEmpty(entry.getValue()) && StringUtils.isNumeric(entry.getValue()))
					? Integer.parseInt(entry.getValue())
					: entry.getValue();
		  }
		  finalMap.put(entry.getKey(), value);
	   }
	  ObjectMapper mapper = new ObjectMapper();
	  String requestString = mapper.writeValueAsString(finalMap);
	  Response response = this.lmsPojo.getRequest_URL().header(HttpHeaders.CONTENT_TYPE, ContentType.TEXT).body(requestString).post(this.lmsPojo.getStr_FinalURI());
        

		if (response != null) {
			System.out.println("Response :\n" + response.asString());
			}
		this.lmsPojo.setRes_response(response);
	}

	@When("skills User sends DELETE skill id ON DELETE Method from {string} and {int}")
	public void user_sends_delete_skill_id_on_delete_method_from_and_row_number(String SheetName, Integer RowNumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), SheetName);

		this.lmsPojo.setStr_skillid(testData.get(RowNumber).get(CONST_SKILL_ID));
		this.lmsPojo.setStr_basePath("/"+CONST_SKILLS_API + this.lmsPojo.getStr_skillid());
		this.lmsPojo.setStr_FinalURI(this.lmsPojo.getStr_baseURL() + this.lmsPojo.getStr_basePath());

		Response response = this.send_Request_For_Method.Sent_request(this.lmsPojo.getStr_FinalURI(),
				this.lmsPojo.getRequest_URL(), HttpMethod.DELETE, this.lmsPojo.getExcelPath(), SheetName, RowNumber);
		this.lmsPojo.setRes_response(response);
	}
	
	@Then("skills User should receive the skill in JSON body from {string} and {int}")
	public void user_should_receive_created_skill_in_json_body_from_and(String SheetName, Integer RowNumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), SheetName);

		Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());
       
		Map<String, String> row = testData.get(RowNumber);
		
		for (Map.Entry<String, String> entry : row.entrySet()) {
			for (Map.Entry<String, Object> mapResponse : jsonMap.entrySet()) {
				if (mapResponse.getKey() == entry.getKey()) {
					assertEquals(mapResponse.getValue(), entry.getValue());
				}

			}
			
		}
	   
	}
	@Then("skills JSON schema is valid")
	public void json_schema_is_valid() {
		
		this.lmsPojo.setStr_SchemaFileSkills(this.lmsPojo.getStr_GETSkillsSchema());
		
		if ((this.lmsPojo.getRes_response().getStatusCode() == CONST_GET_SUCCESS_STATUS_CODE)
				|| (this.lmsPojo.getRes_response().getStatusCode() == CONST_POST_SUCCESS_STATUS_CODE)) {
			JSON_Schema_Validation.cls_JSON_SchemaValidation(this.lmsPojo.getRes_response(),
					this.lmsPojo.getStr_SchemaFileSkills());
		}
	
		
	}
	
	@Then("JSON schema is valid for {string} in Skills")
	public void json_schema_is_valid_for_get_in_skills(String Method) {
		switch (Method) {
		case "GET":
			
			this.lmsPojo.setStr_SchemaFileSkills(this.lmsPojo.getStr_GETidSkillsSchema());
			break;
		case "POST":
			this.lmsPojo.setStr_SchemaFileSkills(this.lmsPojo.getStr_POSTSkillsSchema());
			break;
		case "PUT":
			this.lmsPojo.setStr_SchemaFileSkills(this.lmsPojo.getStr_POSTSkillsSchema());
			break;
		}
		if ((this.lmsPojo.getRes_response().getStatusCode() == CONST_GET_SUCCESS_STATUS_CODE)
				|| (this.lmsPojo.getRes_response().getStatusCode() == CONST_POST_SUCCESS_STATUS_CODE)) {
			JSON_Schema_Validation.cls_JSON_SchemaValidation(this.lmsPojo.getRes_response(),
					this.lmsPojo.getStr_SchemaFileSkills());
		}
	}

	@Then("skills User validates StatusCode")
	public void user_validates_the_status_code() {
		assertEquals(this.lmsPojo.getRes_response().getStatusCode(), 200);
	}
	
	@Then("skills User validates the StatusCode and StatusMessage from {string} sheet and {int} row")
	public void user_validates_the_statuscode_and_statusmessage_from(String SheetName, int RowNumber) throws InvalidFormatException, IOException {
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> testData = reader.getData(this.lmsPojo.getExcelPath(), SheetName);

		Map<String, String> scenario = testData.get(RowNumber);
		String statusCodeString = scenario.get(CONST_STATUS_CODE);

		if (!StringUtils.isEmpty(statusCodeString)) {
			this.lmsPojo.setStatus_code(Integer.parseInt(statusCodeString));
		}

		this.lmsPojo.setStatus_message(testData.get(RowNumber).get(CONST_STATUS_MESSAGE));

		Response response = this.lmsPojo.getRes_response();
        System.out.println("GetStatusCode"+ response.getStatusCode());
        System.out.println("comparison:"+ this.lmsPojo.getStatus_code());
		assertNotNull(response);
		assertEquals(response.getStatusCode(), this.lmsPojo.getStatus_code());

		Map<String, Object> jsonMap = extractResponse(response);
		assertNotNull(jsonMap);
		if (this.lmsPojo.getStatus_message()!="") {
		assertEquals(jsonMap.get("message"), this.lmsPojo.getStatus_message());
		}
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> extractResponse(Response response) throws JsonMappingException, JsonProcessingException {
		ResponseBody responseBody = response.getBody();
		assertNotNull(responseBody);
		Map<String, Object> jsonMap = null;
		ObjectMapper mapper = new ObjectMapper();
		jsonMap = mapper.readValue(responseBody.asString(), Map.class);
		return jsonMap;
	}


	
	@Then("check the Database for skills")
	public void check_the_database_for_all_skills() throws JsonMappingException, JsonProcessingException {
		    String getStr_Query="select skill_id,skill_name from tbl_lms_skill_master";
			Map<String, String> queryResult = Fetch_Data_From_SQL.connect(this.lmsPojo.getStr_DBURL(),
					this.lmsPojo.getStr_DBUserName(), this.lmsPojo.getStr_DBPWD(), getStr_Query);
			Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());

			if (CollectionUtils.sizeIsEmpty(queryResult)) {
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

	


	@And("skills check the Database")
	public void check_the_database() throws JsonMappingException, JsonProcessingException {
		Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());
		
		if (jsonMap.get(CONST_SKILL_ID) != null) {
			String queryString = "select skill_id,skill_name from tbl_lms_skill_master where skill_id='"
					+ jsonMap.get(CONST_SKILL_ID) + "'";
		
		

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

						} else {
							assertEquals((String) jsonMapFinal.getValue(), queryResultFinal.getValue());
						}
					}
				}

			}
		}
		}
	}
	@And("skills User checks the Database to validate deletion from {string} sheet and {int} row")
	public void skills_user_check_the_Database_to_validate_deletion(String sheetName,int rowNumber ) throws Throwable, IOException {
		Map<String, Object> jsonMap = extractResponse(this.lmsPojo.getRes_response());
		ExcelReader reader = new ExcelReader();
		List<Map<String, String>> DeltestData = reader.getData(this.lmsPojo.getExcelPath(), sheetName);
		if (jsonMap.get(CONST_SKILL_ID) != null) {
		String queryString = "SELECT EXISTS(SELECT * FROM tbl_lms_userskill_map WHERE user_skill_id='"
				+ DeltestData.get(rowNumber).get(CONST_SKILL_ID) + "'";

		Map<String, String> queryResult = Fetch_Data_From_SQL.connect(this.lmsPojo.getStr_DBURL(),
				this.lmsPojo.getStr_DBUserName(), this.lmsPojo.getStr_DBPWD(), queryString);
		Boolean Output;
		
			
		
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

}

