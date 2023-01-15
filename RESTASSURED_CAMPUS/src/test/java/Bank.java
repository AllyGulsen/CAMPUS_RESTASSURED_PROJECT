import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Bank {

    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String bank_id;

    @BeforeClass()
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
    public void createBank(){

        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name","bankacc");
        requestBody.put("iban","TR76543210");
        requestBody.put("integrationCode","111");
        requestBody.put("currency","USD");
        requestBody.put("active","true");
        requestBody.put("schoolId","6390f3207a3bcb6a7ac977f9");


        bank_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(201)
                .body("name",equalTo(requestBody.get("name")))
                .body("iban",equalTo(requestBody.get("iban")))
                .body("integrationCode",equalTo(requestBody.get("integrationCode")))
                .body("currency",equalTo(requestBody.get("currency")))
                .body("schoolId",equalTo(requestBody.get("schoolId")))
                .extract().jsonPath().getString("id");




    }

    @Test(priority = 4)
    public void getBank(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/bank-accounts/"+bank_id)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(priority = 5)
    public void createNegativeBank(){

        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name","bankacc");
        requestBody.put("iban","TR76543210");
        requestBody.put("integrationCode","111");
        requestBody.put("currency","USD");
        requestBody.put("active","true");
        requestBody.put("schoolId","5fe07e4fb064ca29931236a5");



        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(priority = 6)
    public void editBank(){

        HashMap<String, String> updateRequestBody= new HashMap<>();
        updateRequestBody.put("id",bank_id);
        updateRequestBody.put("name","bankaccc");
        updateRequestBody.put("iban","TR76543210");
        updateRequestBody.put("integrationCode","1112");
        updateRequestBody.put("currency","USD");
        updateRequestBody.put("active","true");
        updateRequestBody.put("schoolId","6390f3207a3bcb6a7ac977f9");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateRequestBody)
                .when()
                .put("/school-service/api/bank-accounts/")
                .then()
                .log().body()
                .statusCode(200)
                .body("integrationCode",equalTo(updateRequestBody.get("integrationCode")));


    }
    @Test(priority = 7)
    public void deleteBank(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/bank-accounts/"+bank_id)
                .then()
                .log().body()
                .statusCode(200);

    }

}