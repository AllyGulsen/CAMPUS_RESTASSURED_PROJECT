import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class SchoolLocations {


    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String schoolLoc_id;

    @BeforeClass
    public void setup(){

        RestAssured.baseURI="https://test.mersys.io";

        reqSpec= given()
                .log().body()
                .contentType(ContentType.JSON);

    }
    @Test(priority = 1)
    public void loginPositiveTest(){

        loginCredentials= new HashMap<>();
        loginCredentials.put("username","turkeyts");
        loginCredentials.put("password","xxxx");
        loginCredentials.put("rememberMe","true");

        cookies=given()
                .spec(reqSpec)
                .body(loginCredentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().detailedCookies();

    }
    @Test(priority = 2)
    public void loginNegativeTest(){
        HashMap<String, String> incorrectLogCredentials= new HashMap<>();
        incorrectLogCredentials.put("username","turkeyts");
        incorrectLogCredentials.put("password","!");
        incorrectLogCredentials.put("rememberMe","false");


        given()
                .spec(reqSpec)
                .body(incorrectLogCredentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);

    }
    @Test(priority = 3)
    public void createSchoolLocations(){

       JSONObject requestBody= new JSONObject();
        requestBody.put("name","school loc");
        requestBody.put("shortName","SCL");
        requestBody.put("capacity", 20);
        requestBody.put("type", "CLASS");
        requestBody.put("school","6390f3207a3bcb6a7ac977f9");

        schoolLoc_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody.toString())
                .when()
                .post("/school-service/api/location")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test(priority = 4)
    public void getSchoolLocations(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/school/6390f3207a3bcb6a7ac977f9/location")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(priority = 5)
    public void createNegativeSchoolLocations(){
        JSONObject requestBody= new JSONObject();
        requestBody.put("name","school loc");
        requestBody.put("shortName","SCL");
        requestBody.put("capacity",20);
        requestBody.put("type", "CLASS");
        requestBody.put("school","6390f3207a3bcb6a7ac977f9");

       given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody.toString())
                .when()
                .post("/school-service/api/location")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(priority = 6)
    public void editSchoolLocations(){

        JSONObject updrequestBody= new JSONObject();
        updrequestBody.put("id",schoolLoc_id);
        updrequestBody.put("name", "school place");
        updrequestBody.put("shortName", "SCP");
        updrequestBody.put("capacity",10);
        updrequestBody.put("type", "CLASS");
        updrequestBody.put("school","6390f3207a3bcb6a7ac977f9");


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updrequestBody.toString())
                .when()
                .put("/school-service/api/location")
                .then()
                .log().body()
                .statusCode(200);
    }
    @Test(priority = 7)
    public void deleteSchoolLocations(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/location/"+schoolLoc_id)
                .then()
                .log().body()
                .statusCode(200);
    }
}
