import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Departments {

    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String department_id;

    private String school_id;

    String jsonPath = ".//src/test/resources/body.json";

    Object jsonObj;
    String oldName = "schoolnme";
    String newName = "schoolnaaameeeee";



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
    public void createDepartment() throws IOException, org.json.simple.parser.ParseException {


        //Yeni json Object yarattım-->
        //filereader yardımıyla--> jsonpath den veriyi al --> JSON objecte çevir

        jsonObj = new JSONParser().parse(new FileReader(jsonPath));

        department_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(jsonObj)
                .when()
                .post("/school-service/api/department")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");

    }



/*
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name", "schooldep");
        requestBody.put("code", "999");
        requestBody.put("active","true");
        requestBody.put("school","{id,606df950d534514e5d99d287}");
        requestBody.put("sections","[]");
        requestBody.put("constants","[]");

 */
 /*
        HashMap<String, String> schoolBody= new HashMap<>();
        schoolBody.put("id","606df950d534514e5d99d287");
        requestBody.put("school",schoolBody);
        {
            "id": null,
                "name": "departmenttest",
                "code": "952",
                "active": true,
                "school": {"id": "606df950d534514e5d99d287"},
            "sections": [],
            "constants": []
        }

          */


    @Test(priority = 4)
    public void getDepartment(){

        school_id="606df950d534514e5d99d287";


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
        /*
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name", "schooldep");
        requestBody.put("code", "999");
        requestBody.put("active","true");
        requestBody.put("schoolDepartmentDTO.school","606df950d534514e5d99d287");
        //"school": {"id": "606df950d534514e5d99d287"},

         */

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
        //json objectini -->Stringe çevir
        //Json objecti Stringe çevirdim (toString ile)
        //JSON objem Stringe dönüşünce--> replace metodla eski stringi yeni stringle değiştir

        JSONObject updjsonObject= new JSONObject();
        updjsonObject.put("id",department_id);
        updjsonObject.put("name","schoolnaaaaame");
        updjsonObject.put("code","9911");
        updjsonObject.put("active","true");

        JSONObject schoolObj= new JSONObject();
        schoolObj.put("id","606df950d534514e5d99d287");

        updjsonObject.put("school",schoolObj);


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updjsonObject.toString())
                .when()
                .put("/school-service/api/department/")
                .then()
                .log().body()
                .statusCode(200);

    }
    /*
        HashMap<String, String> updaterequestBody= new HashMap<>();
        updaterequestBody.put("id",department_id);
        updaterequestBody.put("name", "schooldep");
        updaterequestBody.put("code", "9990");
        updaterequestBody.put("active","true");
        updaterequestBody.put("school.id","606df950d534514e5d99d287");
        //"school": {"id": "606df950d534514e5d99d287"},

     */




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
    @Test(priority = 8)
    public void getNegativeDepartment(){

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/school/"+school_id+"/department/")
                .then()
                .log().body()
                .statusCode(400);

    }
    @Test(priority = 9)
    public void deleteNegativeDepartment(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/department/"+department_id)
                .then()
                .log().body()
                .statusCode(400);
    }
}