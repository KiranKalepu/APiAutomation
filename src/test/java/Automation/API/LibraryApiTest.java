package Automation.API;

import static io.restassured.RestAssured.given;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import apiJsonFiles.ApiJsonData;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class LibraryApiTest {
    String bookId = "";
    
    @Test(dataProvider = "setData")
    public void libraryApiTest(String bookName, String isbnValue, String aisleValue)
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String reponse = given().log().all().body(ApiJsonData.addBookJsonData(bookName, isbnValue, aisleValue))
        .when().post("Library/Addbook.php").then().log().all().assertThat().statusCode(200).extract().asString();
        JsonPath jp = new JsonPath(reponse);
        bookId = jp.getString("ID");
        getBookData("ID", bookId);
    }

    @Test
    public void getBooksByAuthorTest()
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        getBookData("AuthorName", "John foer");
    }

    @Test(dependsOnMethods = {"libraryApiTest"})
    public void deleteNewlyAddedBook()
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        given().log().all().body(ApiJsonData.deleteBook(bookId)).
        when().delete("Library/DeleteBook.php").then().log().all().assertThat().statusCode(200);
    }

    @DataProvider(name = "setData")
    public Object[][] setData()
    {
        return new Object[][] {{Utilities.generateRandomString(6), Utilities.generateRandomString(4), Utilities.generateRandomnumber()}, 
        	{Utilities.generateRandomString(9), Utilities.generateRandomString(4), Utilities.generateRandomnumber()}, 
            {Utilities.generateRandomString(7), Utilities.generateRandomString(4), Utilities.generateRandomnumber()}};
    }

    public void getBookData(String key, String value)
    {
        given().log().all().queryParam(key, value).when().get("Library/GetBook.php")
        .then().log().all().assertThat().statusCode(200);
    }
}
