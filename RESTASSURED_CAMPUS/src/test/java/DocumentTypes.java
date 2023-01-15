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
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.io.FileReader;
import org.json.JSONObject;

public class DocumentTypes {

    private RequestSpecification reqSpec;
    private HashMap<String, String> loginCredentials;
    private Cookies cookies;
    private String document_id;

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
        public void createDocument(){

        JSONObject requestBody= new JSONObject();
        requestBody.put("name", "diploma");
        requestBody.put("description","graduation");

            JSONArray jsnlist= new JSONArray();
            jsnlist.put("CERTIFICATE");
            requestBody.put("attachmentStages",jsnlist);
            requestBody.put("schoolId", "6390f3207a3bcb6a7ac977f9");

            String body= requestBody.toString();
            System.out.println("POSITIVE BODY:"+ body);

        document_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(body)
                .when()
                .post("/school-service/api/attachments/create/")
                .then()
                .log().all()
                .statusCode(201)
                .extract().jsonPath().getString("id");
        }

    @Test(priority = 4)
    public void getDocument(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/attachments/"+document_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void editDocument(){
        JSONObject updrequestBody= new JSONObject();
        updrequestBody.put("id",document_id);
        updrequestBody.put("name", "certification");
        updrequestBody.put("description","SDET Certificate");

        JSONArray jsnlist= new JSONArray();
        jsnlist.put("CERTIFICATE");
        updrequestBody.put("attachmentStages",jsnlist);

        updrequestBody.put("active", "true");
        updrequestBody.put("required","true");
        updrequestBody.put("useCamera","false");
        updrequestBody.put("schoolId", "6390f3207a3bcb6a7ac977f9");

            given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updrequestBody.toString())
                .when()
                .put("/school-service/api/attachments")
                .then()
                .log().all()
                .statusCode(200);

    }
    
    @Test(priority = 6)
    public void deleteDocument(){
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/attachments/"+document_id)
                .then()
                .statusCode(200);

    }


}


