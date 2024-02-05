package services;

import assertions.AssertableResponse;
import io.restassured.http.ContentType;
import models.DlcsItem;
import models.FullUser;
import models.GamesItem;
import models.forGames.FullGame;

import java.util.List;

import static io.restassured.RestAssured.given;

public class GamesService {
    public AssertableResponse getUserGames(String jwt){
        return new AssertableResponse(given().auth().oauth2(jwt)
                .get("/user/games")
                .then());
    }

    public AssertableResponse addNewGame(String jwt, GamesItem gamesItem){
        return new AssertableResponse(given().contentType(ContentType.JSON)
                .auth().oauth2(jwt)
                .body(gamesItem)
                .post("/user/games")
                .then());
    }


    public AssertableResponse updateGame(String jwt, List<DlcsItem> dlcsItem, int gameId){
        return new AssertableResponse(given().contentType(ContentType.JSON)
                .auth().oauth2(jwt)
                .body(dlcsItem)
                .pathParam("gameId", gameId)
                .put("/user/games/{gameId}")
                .then());
    }

    public AssertableResponse deleteGame(String jwt, int gameId){
        return new AssertableResponse(given().contentType(ContentType.JSON)
                .auth().oauth2(jwt)
                .pathParam("id", gameId)
                .delete("/user/games/{id}")
                .then());
    }
}
