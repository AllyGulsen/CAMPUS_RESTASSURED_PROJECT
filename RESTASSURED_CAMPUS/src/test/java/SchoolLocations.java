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

        RestAssured.baseURI="https://demo.mersys.io/";

        reqSpec= given()
                .log().body()
                .contentType(ContentType.JSON);

    }
    @Test(priority = 1)
    public void loginPositiveTest(){

        loginCredentials= new HashMap<>();
        loginCredentials.put("username","richfield.edu");
        loginCredentials.put("password","Richfield2020!");
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
        incorrectLogCredentials.put("username","richfield.edu");
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
        requestBody.put("name", "school loc");
        requestBody.put("shortName", "SCL");
        requestBody.put("active","true");
        requestBody.put("capacity","20");
        requestBody.put("type", "CLASS");

        JSONObject schoolObj= new JSONObject();
        schoolObj.put("id","606df950d534514e5d99d287");
        requestBody.put("school",schoolObj);

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
                .get("/school-service/api/school/606df950d534514e5d99d287/location")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(priority = 5)
    public void createNegativeSchoolLocations(){
        JSONObject requestBody= new JSONObject();
        requestBody.put("name", "school loc");
        requestBody.put("shortName", "SCL");
        requestBody.put("active","true");
        requestBody.put("capacity","20");
        requestBody.put("type", "CLASS");

        JSONObject schoolObj= new JSONObject();
        schoolObj.put("id","606df950d534514e5d99d287");
        requestBody.put("school",schoolObj);

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
        updrequestBody.put("active","true");
        updrequestBody.put("capacity","20");
        updrequestBody.put("type", "CLASS");

        JSONObject schoolObj= new JSONObject();
        schoolObj.put("id","606df950d534514e5d99d287");
        updrequestBody.put("school", schoolObj);

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
    @Test(priority = 8)
    public void getNegativeSchoolLocations(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/school/5fe07e4fb064ca29931236a5/location"+schoolLoc_id)
                .then()
                .log().body()
                .statusCode(400);
    }
    @Test(priority = 9)
    public void deleteNegativeSchoolLocations(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/location/"+schoolLoc_id)
                .then()
                .log().body()
                .statusCode(400);
    }
}
