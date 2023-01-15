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

public class Attestation {
    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;

    private String attestation_id;


    @BeforeClass()
    public void setup(){

        RestAssured.baseURI="https://test.mersys.io";

        reqSpec= given()
                .log().body()
                .contentType(ContentType.JSON);

    }
    @Test( priority = 1)
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
                .log().body()
                .statusCode(200)
                .extract().detailedCookies();

    }
    @Test(priority = 2)
    public void loginNegativeTest(){
        HashMap<String, String> incorrectLogCredentials= new HashMap<>();
        incorrectLogCredentials.put("username","turkeyts");
        incorrectLogCredentials.put("password","xxx");
        incorrectLogCredentials.put("rememberMe","false");


        given()
                .spec(reqSpec)
                .body(incorrectLogCredentials)
                .when()
                .post("/auth/login")
                .then()
                .log().body()
                .statusCode(401);

}   @Test(priority = 3)
    public void createAttestation(){

        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name","certificate");

        attestation_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/attestation")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", equalTo(requestBody.get("name")))
                .extract().jsonPath().getString("id");
    }


    @Test(priority = 4)
    public void createNegativeAttestation(){
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name","certificate");

            given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/attestation")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(priority = 5)
    public void editAttestation(){
        HashMap<String, String> updateReqBody= new HashMap<>();
        updateReqBody.put("id",attestation_id);
        updateReqBody.put("name","diploma");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateReqBody)
                .when()
                .put("/school-service/api/attestation")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updateReqBody.get("name")));

    }
    @Test(priority = 6)
    public void deleteAttestation(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/attestation/"+attestation_id)
                .then()
                .log().body()
                .statusCode(204);

    }

}