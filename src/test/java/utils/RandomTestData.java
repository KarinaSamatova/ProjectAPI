package utils;

import com.github.javafaker.Faker;
import models.DlcsItem;
import models.FullUser;
import models.GamesItem;
import models.Requirements;
import models.SimilarDlc;


import java.time.LocalDateTime;
import java.util.*;

public class RandomTestData {
    private static Random random = new Random();
    private static Faker faker = new Faker();
    public static FullUser getRandomUserWithGames(){
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login(faker.name().username() + randomNumber)
                .pass(faker.internet().password())
                .games(Collections.singletonList(getRandomGame()))
                .build();

        return user;
    }
    public static FullUser getRandomUser(){
        int randomNumber = Math.abs(random.nextInt());
        return FullUser.builder()
                .login("userEva" + randomNumber)
                .pass("myP@ssw0rD")
                .build();
    }

    public static FullUser getAdminUser(){
        return FullUser.builder()
                .login("admin")
                .pass("admin")
                .build();
    }

    public static GamesItem getRandomGame(){
        SimilarDlc similarDlc = SimilarDlc.builder()
                .isFree(true)
                .dlcNameFromAnotherGame(faker.funnyName().name())
                .build();

        DlcsItem dlcsItem = DlcsItem.builder()
                .rating(faker.random().nextInt(10))
                .price(faker.random().nextInt(1, 500))
                .description(faker.funnyName().name())
                .dlcName(faker.dragonBall().character())
                .isDlcFree(false)
                .similarDlc(similarDlc)
                .build();

        Requirements requirements = Requirements.builder()
                .ramGb(faker.random().nextInt(4, 16))
                .osName("Windows")
                .hardDrive(faker.random().nextInt(30, 70))
                .videoCard("NVIDEA")
                .build();

        GamesItem gamesItem = GamesItem.builder()
                .requirements(requirements)
                .genre(faker.book().genre())
                .price(faker.random().nextInt(400))
                .description(faker.funnyName().name())
                .company(faker.company().name())
                .isFree(false)
                .title(faker.beer().name())
                .rating(faker.random().nextInt(10))
                .publishDate(LocalDateTime.now().toString())
                .requiredAge(random.nextBoolean())
                .tags(Arrays.asList("shooter", "quests"))
                .dlcs(Collections.singletonList(dlcsItem))
                .build();

        return gamesItem;
    }

    public static List<DlcsItem> getRandomDlc(){
        SimilarDlc similarDlc = SimilarDlc.builder()
                .isFree(true)
                .dlcNameFromAnotherGame(faker.funnyName().name())
                .build();

        DlcsItem dlcsItem = DlcsItem.builder()
                .rating(faker.random().nextInt(10))
                .price(faker.random().nextInt(1, 500))
                .description(faker.funnyName().name())
                .dlcName(faker.dragonBall().character())
                .isDlcFree(false)
                .similarDlc(similarDlc)
                .build();
        return Collections.singletonList(dlcsItem);
    }
}
