
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class Fields {


    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String field_id;

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
    public void createFields(){
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put( "name","lecturedate");
        requestBody.put("code","661");
        requestBody.put("type","DATE");
        requestBody.put("schoolId", "56390f3207a3bcb6a7ac977f9");

        field_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/entity-field")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");


    }

    @Test(priority = 4)
    public void getFields(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/entity-field/"+field_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void createNegativeFields(){
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put( "name","lecturedate");
        requestBody.put("code","661");
        requestBody.put("type","DATE");
        requestBody.put("schoolId", "56390f3207a3bcb6a7ac977f9");

            given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/entity-field")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(priority = 6)
    public void editFields(){
        HashMap<String, String> updrequestBody= new HashMap<>();
        updrequestBody.put("id",field_id);
        updrequestBody.put( "name", "lessondate");
        updrequestBody.put("code", "660");
        updrequestBody.put("type", "DATE");
        updrequestBody.put("systemField","false");
        updrequestBody.put("schoolId", "6390f3207a3bcb6a7ac977f9");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updrequestBody)
                .when()
                .put("/school-service/api/entity-field")
                .then()
                .log().body()
                .statusCode(200);

    }
    @Test(priority = 7)
    public void deleteFields(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/entity-field/"+field_id)
                .then()
                .log().body()
                .statusCode(204);

    }

}

