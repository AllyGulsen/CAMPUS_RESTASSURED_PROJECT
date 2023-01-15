import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Departments {

    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String department_id;

    private String school_id;

    String jsonPath = ".//src/test/resources/body.json";


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
    public void createDepartment() throws IOException, ParseException {

        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name","schoolnme");
        requestBody.put("code","1909");
        requestBody.put("school","6390f3207a3bcb6a7ac977f9");


        department_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/department")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");

    }

    @Test(priority = 4)
    public void getDepartment(){

        school_id="6390f3207a3bcb6a7ac977f9";


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/school/"+school_id+"/department/")
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void createNegativeDepartment(){

        JSONObject jsonObj= new JSONObject();
        jsonObj.put("code","1909");
        jsonObj.put("name","schoolnme");
        jsonObj.put("active","true");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(jsonObj)
                .when()
                .post("/school-service/api/department")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(priority = 6)
    public void editDepartment() throws IOException, ParseException {

        HashMap<String,String> editDepartmentBody= new HashMap<>();
        editDepartmentBody.put("id",department_id);
        editDepartmentBody.put("name","schoolnaaaaame");
        editDepartmentBody.put("code","9911");
        editDepartmentBody.put("school","6390f3207a3bcb6a7ac977f9");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(editDepartmentBody)
                .when()
                .put("/school-service/api/department/")
                .then()
                .log().body()
                .statusCode(200);

    }
    @Test(priority = 7)
    public void deleteDepartment(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/department/"+department_id)
                .then()
                .log().body()
                .statusCode(204);


    }

}