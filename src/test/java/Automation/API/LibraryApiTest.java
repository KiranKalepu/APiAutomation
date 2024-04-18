package Automation.API;

import static io.restassured.RestAssured.given;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import apiJsonFiles.ApiJsonData;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class LibraryApiTest {
    
    @Test(dataProvider = "setData")
    public void addBookApiTest(String bookName, String isbnValue, String aisleValue)
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String reponse = given().log().all().body(ApiJsonData.addBookJsonData(bookName, isbnValue, aisleValue))
        .when().post("Library/Addbook.php").then().log().all().assertThat().statusCode(200).extract().asString();
        JsonPath jp = new JsonPath(reponse);
        String id = jp.getString("ID");
    }

    @DataProvider(name = "setData")
    public Object[][] setData()
    {
        return new Object[][] {{"Kiran", "Test", "012"}, {"Automation", "testBi", "890"}, {"Api Test", "Lest", "456"}};
    }
}
