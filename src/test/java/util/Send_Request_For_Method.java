package util;

import static util.constant.LMSApiConstant.CONST_SCENARIO;
import static util.constant.LMSApiConstant.CONST_SKILL_ID;
import static util.constant.LMSApiConstant.CONST_STATUS_CODE;
import static util.constant.LMSApiConstant.CONST_STATUS_MESSAGE;
import static util.constant.LMSApiConstant.CONST_USER_ID;
import static util.constant.LMSApiConstant.CONST_USER_SKILL_ID;
import static util.constant.LMSApiConstant.CONST_USERSKILLS_API;
import static util.constant.LMSApiConstant.CONST_SKILL_ID;
import static util.constant.LMSApiConstant.CONST_SKILLS_API;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.core.options.CurlOption.HttpMethod;
import io.restassured.RestAssured;
import io.restassured.internal.path.json.JSONAssertion;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Send_Request_For_Method {
	public static String NewAPIEndpoint;
	private LMSPojo lmsPojo;

	public Send_Request_For_Method(String API_Endpoint) {
		NewAPIEndpoint = API_Endpoint;
		
		Fetch_Data_From_Properties_File data_From_Properties_File = new Fetch_Data_From_Properties_File(API_Endpoint);
		this.lmsPojo = data_From_Properties_File.getLmsPojo();
	}

	public static RequestSpecification request_URL(String Username, String Password) {

		// Request object
		return RestAssured.given().auth().preemptive().basic(Username, Password);

	}

	// public static Response Sent_request(String strURL,RequestSpecification
	// httpRequest, HttpMethod httpmethod,String strCoulumnName[], String
	// str_Input[]) {
	public Response Sent_request(String strURL, RequestSpecification httpRequest, HttpMethod httpmethod,
			String ExcelPath, String SheetName,int rowNumber) throws InvalidFormatException, IOException {
		Response response = null;
		String numericColumns = this.lmsPojo.getNumericColumns();
		switch (httpmethod) {
		case POST:
			ExcelReader reader = new ExcelReader();
			List<Map<String, String>> excelRows = reader.getData("./" + ExcelPath, SheetName);
			Map<String, Object> finalMap = new HashMap<>();

			Map<String, String> row = excelRows.get(rowNumber);
			row.remove(CONST_SCENARIO);
			row.remove(CONST_STATUS_CODE);
			row.remove(CONST_STATUS_MESSAGE);
			row.remove(CONST_USER_SKILL_ID);
			row.remove(CONST_SKILL_ID);
			row.remove(CONST_USER_ID);
			if (NewAPIEndpoint!=CONST_USERSKILLS_API) {
				row.remove(CONST_USER_ID);				
			}
			
			for (Map.Entry<String, String> entry : row.entrySet()) {
				Object value = entry.getValue();
				if (NewAPIEndpoint==CONST_SKILLS_API) {
					if (value.equals("true") || value.equals("false") ) {
						boolean b = Boolean.valueOf(value.toString());
						value=b;
					}

	            if (StringUtils.isNumeric(entry.getValue())) {
					       value =  Integer.parseInt(entry.getValue());
						    
					}
				}

				if (numericColumns.contains(entry.getKey())) {
					value = (StringUtils.isNotEmpty(entry.getValue()) && StringUtils.isNumeric(entry.getValue()))
							? Integer.parseInt(entry.getValue())
							: entry.getValue();
				}
				finalMap.put(entry.getKey(), value);
			}
			ObjectMapper mapper = new ObjectMapper();
			String requestString = mapper.writeValueAsString(finalMap);
			
			System.out.println("Request is :" +requestString);
			response = httpRequest.header(HttpHeaders.CONTENT_TYPE, ContentType.JSON).body(requestString).post(strURL);

			if (response != null) {
				System.out.println("Response :\n" + response.asString());

				Map<String, Object> unmatchedFields = getUnMatchedFields(requestString, response.asString());

				for (Map.Entry<String, Object> map : unmatchedFields.entrySet()) {
					ExcelWriter writer = new ExcelWriter();
					boolean status;
					status = writer.writeExcelFile(ExcelPath, SheetName, rowNumber + 1, map.getKey(),
							map.getValue());
				}

			}
			break;

		case PUT:

			ExcelReader reader_put = new ExcelReader();

			List<Map<String, String>> excelRows_put = reader_put.getData("./" + ExcelPath, SheetName);
			Map<String, Object> finalMap_put = new HashMap<>();
			Map<String, String> row_put = excelRows_put.get(rowNumber);
			row_put.remove(CONST_USER_SKILL_ID);
			row_put.remove(CONST_SKILL_ID);
            row_put.remove(CONST_SCENARIO);
			row_put.remove(CONST_STATUS_CODE);
			row_put.remove(CONST_STATUS_MESSAGE);
			for (Map.Entry<String, String> entry : row_put.entrySet()) {
				Object value = entry.getValue();
				if (NewAPIEndpoint==CONST_SKILLS_API) {
					if (value.equals("true") || value.equals("false") ) {
						boolean b = Boolean.valueOf(value.toString());
						value=b;
					}
					
					if (StringUtils.isNumeric(entry.getValue())) {
					       value =  Integer.parseInt(entry.getValue());
						    
					}
				}
				if (numericColumns.contains(entry.getKey())) {
					value = (StringUtils.isNotEmpty(entry.getValue()) && StringUtils.isNumeric(entry.getValue()))
							? Long.parseLong(entry.getValue())
							: entry.getValue();
				}
				finalMap_put.put(entry.getKey(), value);
			}
			
			ObjectMapper mapper_put = new ObjectMapper();
			String requestString_put = mapper_put.writeValueAsString(finalMap_put);
			httpRequest.header(HttpHeaders.CONTENT_TYPE, ContentType.JSON);
			httpRequest.body(requestString_put);
			response = httpRequest.put(strURL);

			if (response != null) {
				System.out.println("Response :\n" + response.asString());
			}
			break;

		case GET:
			RestAssured.baseURI = strURL;
			response = httpRequest.when().get(RestAssured.baseURI);
			System.out.println("Response :" + response.asString());
			break;

		case DELETE:
			RestAssured.baseURI = strURL;
			response = httpRequest.when().delete(RestAssured.baseURI);
			System.out.println("Response :" + response.asString());
			break;

		default:
			break;

		}

		return response;
	}


	private Map<String, Object> getUnMatchedFields(String requestString, String responseString) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashedMap<>();
		try {
			Map<String, Object> requestMap = mapper.readValue(requestString, Map.class);
			Map<String, Object> responseMap = mapper.readValue(responseString, Map.class);

			for (Map.Entry<String, Object> response : responseMap.entrySet()) {
				boolean matchFound = Boolean.FALSE;
				for (Map.Entry<String, Object> request : requestMap.entrySet()) {
					if (response.getKey().equals(request.getKey())) {
						matchFound = Boolean.TRUE;
					}
				}
				if (!matchFound && !"message".equalsIgnoreCase(response.getKey())) {
					map.put(response.getKey(), response.getValue());
				}
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return map;
	}

}
