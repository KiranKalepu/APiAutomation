package Automation.API;

import org.testng.Assert;
import org.testng.annotations.Test;

import apiJsonFiles.ApiJsonData;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppTest {

	@Test
	public void apiAsserstionsTest() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		given().log().all().queryParam("key", "qaclick123").body(ApiJsonData.apiPostjsonData()).when()
				.post("maps/api/place/add/json").then().log().all().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)");
	}

	@Test
	public void parseApiJsonTest() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		// Creating a new place using the post Api
		String response = given().log().all().queryParam("key", "qaclick123").body(ApiJsonData.apiPostjsonData()).when()
				.post("maps/api/place/add/json").then().extract().response().asString();
		// Parsing the json data from the response of the post api
		JsonPath jp = new JsonPath(response);
		String placeId = jp.getString("place_id");
		String newAddress = "Hyderabad, India";
		// Updating the address of the new created place using the put api
		given().log().all().queryParam("key", "qaclick123").body(ApiJsonData.apiPutJsonData(placeId, newAddress)).when()
				.put("maps/api/place/update/json").then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));
		// Retrieving the json data of the updated place using the get Api
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId).when()
				.get("maps/api/place/get/json").then().log().all().assertThat().statusCode(200)
				.body("address", equalTo(newAddress));
	}

	@Test
	public void complexJsonHandilingTest() throws IOException {
		JsonPath jp = new JsonPath(new String(Files.readAllBytes(Paths.get("./jsonFiles\\BooksData.json"))));
		//1. Print No of courses returned by API
		int coursesCount = jp.getInt("courses.size()");
		System.out.println(coursesCount);
		//2. Print Purchase Amount
		int purchaseAmount = jp.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		//3. Print Title of the first course
		String courseTitle = jp.getString("courses[0].title");
		System.out.println(courseTitle);
		//4. Print All course titles and their respective Prices
		for(int i = 0; i < coursesCount; i++)
		{
			String title = jp.get("courses[" + i +"].title");
			int amount = jp.getInt("courses[" + i +"].price");
			System.out.println(title);
			System.out.println(amount);
		}
		//5. Print no of copies sold by RPA Course
		 for(int i = 0; i < coursesCount; i++)
		 {
			String title = jp.get("courses[" + i +"].title");
			if(title.equalsIgnoreCase("RPA"))
			{
				int copiesSold = jp.getInt("courses[" + i + "].copies");
				System.out.println(copiesSold);
				break;
			}
		 }
		 // 6. Verify if Sum of all Course prices matches with Purchase Amount
		 int sum = 0;
		 for(int i = 0; i < coursesCount; i++)
		 {
			int courseAmount = jp.getInt("courses[" + i +"].price");
			int copiesSold = jp.getInt("courses[" + i + "].copies");
			int amount = courseAmount * copiesSold;
			sum = sum + amount;
		 }
		 System.out.println(sum);
		 Assert.assertEquals(sum, purchaseAmount);
	}

}
