import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class SubjectCategories {


    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String subjectCat_id;

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
    public void createSubjectCategories(){
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name", "firstsubjectcat");
        requestBody.put("code","714");
        requestBody.put("active","true");

        subjectCat_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test(priority = 4)
    public void getSubjectCategories(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/subject-categories/"+subjectCat_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void createNegativeSubjectCategories(){
        HashMap<String, String> requestBody= new HashMap<>();
        requestBody.put("name", "firstsubjectcat");
        requestBody.put("code","714");
        requestBody.put("active","true");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(requestBody)
                .when()
                .post("/school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(priority = 6)
    public void editSubjectCategories(){
        HashMap<String, String> updrequestBody= new HashMap<>();
        updrequestBody.put("id",subjectCat_id);
        updrequestBody.put("name", "onehundredsubcat");
        updrequestBody.put("code","719");
        updrequestBody.put("active","true");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updrequestBody)
                .when()
                .put("/school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(200);
    }
    @Test(priority = 7)
    public void deleteSubjectCategories(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/subject-categories/"+subjectCat_id)
                .then()
                .log().body()
                .statusCode(200);

    }
}
