package tests;

import assertions.AssertableResponse;
import assertions.Condition;
import assertions.Conditions;
import assertions.GenericAssertableResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.FullUser;
import models.Info;
import models.JwtAuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.CustomTpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static assertions.Conditions.hasMessage;
import static assertions.Conditions.hasStatusCode;
import static io.restassured.RestAssured.given;

public class UserTests {
    private static Random random;
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
    }

    @Test
    public void positiveRegisterTest(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("userEva" + randomNumber)
                .pass("myP@ssw0rD")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("User created", info.getMessage());
    }

    @Test
    public void negativeRegisterLoginExistTest(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("userEva" + randomNumber)
                .pass("myP@ssw0rD")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("User created", info.getMessage());

        Info errorInfo = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("Login already exist", errorInfo.getMessage());
    }

    @Test
    public void negativeRegisterNoPasswordTest(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("userEva" + randomNumber)
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Missing login or password", info.getMessage());

//        Обертка ValidatableResponse:
        new AssertableResponse(given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then())
                .should(hasMessage("Missing login or password"))
                .should(hasStatusCode(400));

        new GenericAssertableResponse<Info>(given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then(), new TypeRef<Info>() {})
                .should(hasMessage("Missing login or password"))
                .should(hasStatusCode(400))
                .asObject()
                .getMessage();
    }

    @Test
    public void positiveAdminAuthTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserAuthTest(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("userEva" + randomNumber)
                .pass("myP@ssw0rD")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData jwtAuthData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeAuthTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("ghds2", "09ifg");

        given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void positiveGetUserInfoTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        given().auth().oauth2(token)
                .get("/api/user")
                .then()
                .statusCode(200);
    }

    @Test
    public void positiveNewUserGetInfoTest(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("userEva" + randomNumber)
                .pass("myP@ssw0rD")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData jwtAuthData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        given().auth().oauth2(token)
                .get("/api/user")
                .then()
                .statusCode(200);
    }

    @Test
    public void negativeGetUserInfoInvalidJwtTest(){
        given().auth().oauth2("some value")
                .get("/api/user")
                .then()
                .statusCode(401);
    }

    @Test
    public void negativeGetUserInfoWithoutJwtTest(){
        given()
                .get("/api/user")
                .then()
                .statusCode(401);
    }

    @Test
    public void positiveNewUserChangePasswordTest(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("userEva" + randomNumber)
                .pass("myP@ssw0rD")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData jwtAuthData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        Map<String, String> newPassword = new HashMap<>();
        String updatedPassValue = "updatedpass";
        newPassword.put("password", updatedPassValue);

        Info updatedPassInfo = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(newPassword)
                .put("/api/user")
                .then()
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User password successfully changed", updatedPassInfo.getMessage());

        jwtAuthData.setPassword(updatedPassValue);

        token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        FullUser updatedUser = given().auth().oauth2(token)
                .get("/api/user")
                .then().statusCode(200)
                .extract().as(FullUser.class);
        Assertions.assertNotEquals(user.getPass(), updatedUser.getPass());
    }

    @Test
    public void negativeAdminChangePasswordTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        Map<String, String> newPassword = new HashMap<>();
        String updatedPassValue = "updatedpass";
        newPassword.put("password", updatedPassValue);

        Info updatedPassInfo = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(newPassword)
                .put("/api/user")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Cant update base users", updatedPassInfo.getMessage());
    }

    @Test
    public void negativeDeleteAdminTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        Info info = given().auth().oauth2(token)
                .delete("/api/user")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Cant delete base users", info.getMessage());
    }

    @Test
    public void positiveDeleteNewUserTest(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("userEva" + randomNumber)
                .pass("myP@ssw0rD")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData jwtAuthData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        Info deleteUserInfo = given().auth().oauth2(token)
                .delete("/api/user")
                .then()
                .statusCode(200)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User successfully deleted", deleteUserInfo.getMessage());
    }

    @Test
    public void positiveGetAllUsersTest(){
        List<String> users = given()
                .get("/api/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("");
//            Или: .extract().as(new TypeRef<List<String>>() {});
        Assertions.assertTrue(users.size()>=3);
    }
}
