import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class GradeLevels {
    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String grade_id;

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
    public void createGradeLevel(){

        JSONObject requestBody= new JSONObject();
        requestBody.put("name", "Agradelevel");
        requestBody.put("shortName", "AGRL");

        JSONArray schoolIdsList= new JSONArray();
        schoolIdsList.put("6390f3207a3bcb6a7ac977f9");
        requestBody.put("schoolIds",schoolIdsList);
        requestBody.put("order", "123");



        grade_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody.toString())
                .when()
                .post("/school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test(priority = 4)
    public void getGradeLevel(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/grade-levels/"+grade_id)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(priority = 5)
    public void createNegativeGradeLevel(){

        JSONObject requestBody= new JSONObject();
        requestBody.put("name", "Agradelevel");
        requestBody.put("shortName", "AGRL");

        JSONObject nextGradeLevelObj= new JSONObject();
        nextGradeLevelObj.put("id","6390f3207a3bcb6a7ac977f9");
        requestBody.put("nextGradeLevel",nextGradeLevelObj);
        requestBody.put("order", "123");


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody.toString())
                .when()
                .post("/school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(400);


    }

    @Test(priority = 6)
    public void editGradeLevel(){
       JSONObject updrequestBody= new JSONObject();
        updrequestBody.put("id",grade_id);
        updrequestBody.put("name","Bgradelevel");
        updrequestBody.put("shortName","BGRL");
        updrequestBody.put("order", "1234");

        JSONArray schoolIdsList= new JSONArray();
        schoolIdsList.put("6390f3207a3bcb6a7ac977f9");

        updrequestBody.put("schoolIds",schoolIdsList);

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updrequestBody.toString())
                .when()
                .put("/school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(200);
    }
    @Test(priority = 7)
    public void deleteGradeLevel(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/grade-levels/"+grade_id)
                .then()
                .log().body()
                .statusCode(200);
    }


}
