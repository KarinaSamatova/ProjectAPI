package tests;
import Listener.AdminUser;
import Listener.AdminUserResolver;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import models.FullUser;
import models.GamesItem;
import models.forGames.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import services.GamesService;
import services.UserService;
import utils.CustomTpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static assertions.Conditions.hasMessage;
import static assertions.Conditions.hasStatusCode;
import static utils.RandomTestData.*;
@ExtendWith(AdminUserResolver.class)
public class GamesRefactoredTests {
    private static GamesService gamesService;
    private static UserService userService;
    private static Random random;
    private FullUser user;
    @BeforeEach
    public void initTestUser(){
        user = getRandomUser();
    }
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080/api/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        gamesService = new GamesService();
        userService = new UserService();
        random = new Random();
    }

    @Test
    public void positiveGetAdminGamesTest(@AdminUser FullUser admin){
        String token = userService.auth(admin)
                .should(hasStatusCode(200))
                .asJwt();
        Assertions.assertNotNull(token);

        List<GamesItem> games = gamesService.getUserGames(token)
                .should(hasStatusCode(200))
                .asList(GamesItem.class);
        Assertions.assertTrue(games.size()>0);
    }

    @Test
    public void positiveAddNewGameNewUserTest(){
        userService.register(user)
                .should(hasStatusCode(201))
                .should(hasMessage("User created"));

        String token = userService.auth(user)
                .should(hasStatusCode(200))
                .asJwt();
        Assertions.assertNotNull(token);

        gamesService.addNewGame(token, getRandomGame())
                .should(hasStatusCode(201))
                .should(hasMessage("Game created"));
    }

    @Test
    public void negativeAddNewGameAdminTest(@AdminUser FullUser admin){
        String token = userService.auth(admin)
                .asJwt();

        gamesService.addNewGame(token, getRandomGame())
                .should(hasStatusCode(400))
               .should(hasMessage("Limit of games, user can have only 20 games"));
    }

    @Test
    public void positiveUpdateGameNewUserTest(){
        userService.register(user);

        String token = userService.auth(user)
                .asJwt();

       RegisterData data = gamesService.addNewGame(token, getRandomGame())
               .as("register_data", RegisterData.class);

       gamesService.updateGame(token, getRandomDlc(), data.getGameId())
               .should(hasStatusCode(200))
               .should(hasMessage("DlC successfully changed"));
    }
    @Test
    public void negativeUpdateGameAdminTest(@AdminUser FullUser admin){
        String token = userService.auth(admin)
                .asJwt();

        List<GamesItem> games = gamesService.getUserGames(token)
                .should(hasStatusCode(200))
                .asList(GamesItem.class);

        GamesItem randomGame = games.get(random.nextInt(20));

        gamesService.updateGame(token, getRandomDlc(), randomGame.getGameId())
                .should(hasStatusCode(400))
                .should(hasMessage("Cant update dlc game from base users"));
    }

    @Test
    public void negativeDeleteGameAdminTest(@AdminUser FullUser admin){
        String token = userService.auth(admin)
                .asJwt();

        gamesService.deleteGame(token, 5425)
                .should(hasStatusCode(400))
                .should(hasMessage("Cant delete game from base users"));
    }

    @Test
    public void positiveDeleteGameNewUserTest(){
        userService.register(user);

        String token = userService.auth(user)
                .asJwt();

        RegisterData data = gamesService.addNewGame(token, getRandomGame())
                .as("register_data", RegisterData.class);

        gamesService.deleteGame(token, data.getGameId())
                .should(hasStatusCode(200))
                .should(hasMessage("Game successfully deleted"));
    }
}

