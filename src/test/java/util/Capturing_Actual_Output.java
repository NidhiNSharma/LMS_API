package util;

import io.restassured.response.Response;

public class Capturing_Actual_Output {
	public static String capture_output(Response response)
	{
		try
		{
			String responseBody = response.getBody().asString();
			System.out.println("Response Body is:" +responseBody);
			return responseBody;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}

}

