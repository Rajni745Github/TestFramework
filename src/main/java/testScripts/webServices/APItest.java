package testScripts.webServices;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
/**
 * 
 * API tests sample - Stand alone file
 * @author Rajni
 *
 */
public class APItest {

	@Test
	public void sendReqTest() {

		RestAssured.baseURI = "https://www.sampleapi.com";

		RequestSpecification request = RestAssured.given();
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("content-type", "application/json");

		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("id", "1112");
		mapObj.put("token", "zyciwwn122");

		Map<String, Object> dataObj = new HashMap<String, Object>();
		{
			dataObj.put("name", "rajni");
			dataObj.put("surname", "sharma");

		}
		mapObj.put("users", Arrays.asList(dataObj));
		Response response = request.headers(requestHeaders).body(mapObj).when().post("/weather/");
		System.out.println(response.getBody().asString());
	}
}