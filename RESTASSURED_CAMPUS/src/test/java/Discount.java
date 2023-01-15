import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Discount {

    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String discount_id;

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
    public void createDiscount(){
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("description","discdisc");
        requestBody.put("code","999");
        requestBody.put("priority","900");

        discount_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(201)
                .body("description",equalTo(requestBody.get("description")))
                .body("code",equalTo(requestBody.get("code")))
                .extract().jsonPath().getString("id");
    }

    @Test(priority = 4)
    public void getDiscount(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/discounts/"+discount_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void createNegativeDiscount(){
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("description","discdisc");
        requestBody.put("code","999");
        requestBody.put("active","true");
        requestBody.put("priority", "900");

            given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(priority = 6)
    public void editDiscount(){
        HashMap<String, String> updateRequestBody= new HashMap<>();
        updateRequestBody.put("id",discount_id);
        updateRequestBody.put("description","discdisc");
        updateRequestBody.put("code","999");
        //updateRequestBody.put("active","true");
        updateRequestBody.put("priority", "999");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateRequestBody)
                .when()
                .put("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(200)
                .body("code",equalTo(updateRequestBody.get("code")));


    }
    @Test(priority = 7)
    public void deleteDiscount(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/discounts/"+discount_id)
                .then()
                .statusCode(200);

    }

}

