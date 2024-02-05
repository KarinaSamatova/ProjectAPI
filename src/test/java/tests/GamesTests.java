package tests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import models.FullUser;
import models.JwtAuthData;
import models.forGames.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.CustomTpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GamesTests {
    private static Random random;
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
    }

    @Test
    public void positiveGetAdminGamesTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        List<FullGame> games = given()
                .auth().oauth2(token)
                .get("/api/user/games")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("");
//            Или: .extract().as(new TypeRef<List<String>>() {});
        Assertions.assertTrue(games.size()>0);
    }

    @Test
    public void positiveAddNewGameAdminTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        SimilarDlc similarDlc = new SimilarDlc("name", true);

        DlcsItem dlcsItem = DlcsItem.builder()
                .description("description")
                .dlcName("name")
                .isDlcFree(true)
                .price(0)
                .rating(0)
                .similarDlc(similarDlc)
                .build();

        List<DlcsItem> dlcsItems = new ArrayList<DlcsItem>();
        dlcsItems.add(dlcsItem);

        Requirements requirements = new Requirements(0, "name", 0, "name");

        List<String> tags = new ArrayList<String>();
        tags.add("tag1");

        FullGame fullGame = FullGame.builder()
                .company("name")
                .description("desc")
                .dlcs(dlcsItems)
                .gameId(0)
                .genre("name")
                .isFree(true)
                .price(0)
                .publishDate("2024-01-19T10:47:54.770Z")
                .rating(0)
                .requiredAge(true)
                .requirements(requirements)
                .tags(tags)
                .title("title")
                .build();

        Response response = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(fullGame)
                .post("/api/user/games")
                .then()
                .statusCode(201)
                .extract().as(Response.class);
        Assertions.assertEquals("Game created", response.getInfo().getMessage());
    }

    @Test
    public void negativeUpdateGameAdminTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        SimilarDlc similarDlc = new SimilarDlc("updatedName", true);
        DlcsItem dlcsItem = DlcsItem.builder()
                .description("updatedDescription")
                .dlcName("updatedName")
                .isDlcFree(true)
                .price(0)
                .rating(0)
                .similarDlc(similarDlc)
                .build();

        List<DlcsItem> dlcsItems = new ArrayList<DlcsItem>();
        dlcsItems.add(dlcsItem);

        int gameId = 5317;

        Info info = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(dlcsItems)
                .pathParam("gameId", gameId)
                .put("/api/user/games/{gameId}")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Cant update dlc game from base users", info.getMessage());
    }

    @Test
    public void positiveAddAndGetGameFromNewUserTest(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("userEva" + randomNumber)
                .pass("myP@ssw0rD")
                .build();

        models.Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", models.Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JwtAuthData jwtAuthData = new JwtAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        SimilarDlc similarDlc = new SimilarDlc("name", true);

        DlcsItem dlcsItem = DlcsItem.builder()
                .description("description")
                .dlcName("name")
                .isDlcFree(true)
                .price(0)
                .rating(0)
                .similarDlc(similarDlc)
                .build();

        List<DlcsItem> dlcsItems = new ArrayList<DlcsItem>();
        dlcsItems.add(dlcsItem);

        Requirements requirements = new Requirements(0, "name", 0, "name");

        List<String> tags = new ArrayList<String>();
        tags.add("tag1");

        FullGame fullGame = FullGame.builder()
                .company("name")
                .description("desc")
                .dlcs(dlcsItems)
                .gameId(0)
                .genre("name")
                .isFree(true)
                .price(0)
                .publishDate("2024-01-19T10:47:54.770Z")
                .rating(0)
                .requiredAge(true)
                .requirements(requirements)
                .tags(tags)
                .title("title")
                .build();

        Response response = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(fullGame)
                .post("/api/user/games")
                .then()
                .statusCode(201)
                .extract().as(Response.class);
        Assertions.assertEquals("Game created", response.getInfo().getMessage());

        given().auth().oauth2(token)
                .pathParam("id", response.getRegisterData().getGameId())
                .get("/api/user/games/{id}")
                .then()
                .statusCode(200)
                .body("gameId", equalTo(response.getRegisterData().getGameId()));
    }

}
