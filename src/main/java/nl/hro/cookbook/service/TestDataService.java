package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.Profile;
import nl.hro.cookbook.model.domain.ProfileImage;
import nl.hro.cookbook.model.domain.Recipe;
import nl.hro.cookbook.model.domain.RecipeImage;
import nl.hro.cookbook.model.domain.User;
import nl.hro.cookbook.security.Role;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDataService {

    private final PasswordEncoder passwordEncoder;

    public List<User> getUsers() throws IOException {

        CommonService commonService = new CommonService();
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource = resourceLoader.getResource("classpath:download.jpeg");
        ProfileImage profileImage = new ProfileImage("test.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource.getFile().toPath())));

        final User initialUser1 = new User(1L, "dion", passwordEncoder.encode("quintor"), Role.ADMIN, new Profile("Top", profileImage));
        final User initialUser2 = new User(2L, "geoffrey", passwordEncoder.encode("quintor"), Role.COMMUNITY_MANAGER, new Profile("Maverick", profileImage));
        final User initialUser3 = new User(3L, "testuser", passwordEncoder.encode("testpassword"), Role.COMMUNITY_MANAGER, new Profile("Random guy", profileImage));


        return Arrays.asList(initialUser1, initialUser2, initialUser3);
    }

    public List<Recipe> getRecipes() throws IOException {

        CommonService commonService = new CommonService();
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource1 = resourceLoader.getResource("classpath:tom-rijst-met-kip.jpg");
        RecipeImage recipeImage = new RecipeImage("gerecht1.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource1.getFile().toPath())));

        Resource resource2 =resourceLoader.getResource("classpath:kerst-ovenschotel.jpg");
        RecipeImage recipeImage1 = new RecipeImage("gerecht2.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource2.getFile().toPath())));

        Resource resource3 =resourceLoader.getResource("classpath:kipragout-1.jpg");
        RecipeImage recipeImage2 = new RecipeImage("gerecht3.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource3.getFile().toPath())));

        Recipe recipe = new Recipe(1L, "Budget recept: tomatenrijst met kip" ,
                "Voor dit recept gebruik ik snelkookrijst, deze is extra snel klaar. " +
                "Omdat je de rijst niet in alleen water kookt maar ook in tomatenblokjes stoof je als het ware de rijst gaar. " +
                "Dit duurt iets langer dan normaal. Snelkookrijst is daarom ideaal, en tevens goedkoop. " +
                "Verder gebruik ik witte kaas in het recept als lekkere zoute topping. Deze is goedkoper dan de echte feta kaas. " +
                "Maar wat is dan het verschil? Door de Europese regelgeving mag alleen kaas die is geproduceerd in Griekenland en gemaakt is van minimaal 70% schapenmelk en maximaal 30% geitenmelk feta heten. " +
                "Feta-achtige kaas die er op lijkt maar hier niet aan voldoet omdat hij bijvoorbeeld in Nederland is geproduceerd of een andere samenstelling heeft noemen ze daarom witte kaas.\n" +
                "\n" +
                "De recepten die ik maak voor deze budgetreeks kosten niet meer dan 2 euro per persoon. " +
                "Ze bevatten zo veel mogelijk verse ingrediënten en zijn een complete maaltijd. " +
                "De prijzen bij de recepten zijn meestal gebaseerd op die van de Jumbo en AH. " +
                "Waarschijnlijk kun je de recepten nog goedkoper maken door bepaalde aanbiedingen of bijvoorbeeld de groenteboer.\n" +
                "\n" +
                "De totale kosten van dit recept zijn € 6,32 : 4 = € 1,58 p.p." +
                "\n" +
                "HOOFDGERECHT – 40 MINUTEN – 4 PERSONEN\n" +
                        "\n" +
                        "Ingrediënten:\n" +
                        "250 gr snelkook rijst €0,47\n" +
                        "150 gr witte kaas €0,75\n" +
                        "½ kippenbouillonblokje €0,10\n" +
                        "400 gr tomatenblokjes €0,49\n" +
                        "1 rode ui €0,10\n" +
                        "400 gr kip €3,42\n" +
                        "1 komkommer €0,99\n" +
                        "\n" +
                        "In huis:\n" +
                        "Snuf paprikapoeder\n" +
                        "Snuf oregano\n" +
                        "Olie, boter of margarine om in te bakken\n" +
                        "\n" +
                        "Bereiding:\n" +
                        "Los het halve bouillonblokje op in 300 ml heet water. " +
                        "Snipper de ui en fruit aan in een pan (diepe koekenpan, braadpan of soeppan) met een beetje olie, " +
                        "boter of margarine. Voeg de kip in blokjes toe en bak rondom aan op hoog vuur. " +
                        "Voeg ook de snuf paprikapoeder en oregano toe. " +
                        "Voeg de rijst toe en daarna direct de tomatenblokjes en bouillon.\n" +
                        "Laat het geheel 15 minuten zachtjes doorkoken. Schep af en toe om. " +
                        "Als het vocht te snel verdampt leg je een deksel op de pan of voeg je nog een klein beetje toe. " +
                        "Proef of de rijst bijna gaar is, misschien moet hij nog een paar minuten extra koken. " +
                        "Snijd ondertussen de komkommer in kleine blokjes. Serveer de pan met tomatenrijst met kip met wat gekruimelde witte kaas en blokjes komkommer.",
                "Het is weer tijd voor een budgetrecept! Deze keer maak ik een 1 pans gerecht van kip in tomatenrijst. " +
                        "Super makkelijk en onder de 2 euro per persoon.",
                3L, recipeImage);
        Recipe recipe1 = new Recipe(2L, "Kerst ovenschotel met pompoen",
                "Ingrediënten\n" +
                        "100 gr paddenstoelen\n" +
                        "200 gr spruitjes\n" +
                        "1 rode ui\n" +
                        "400 gr pompoen (in blokjes)\n" +
                        "75 gr cranberry\n" +
                        "½ theelepel tijm\n" +
                        "3 eetlepels bloem\n" +
                        "2 eetlepels boter\n" +
                        "250 ml groentebouillon\n" +
                        "1 rol vers bladerdeeg of 6 velletjes bladerdeeg uit de vriezer\n" +
                        "½ ei (of 2 eetlepels plantardige melk)\n" +
                        "Ovenschaal\n" +
                        "Sterren uitstekers\n" +
                        "Bereiding\n" +
                        "\n" +
                        "\n" +
                        " Verwarm de oven op 200 graden. Breng een pan met water aan de kook. Kook de spruitjes en pompoen ca. 8 minuutjes voor en giet daarna af.\n" +
                        "\n" +
                        "\n" +
                        " Snipper de rode ui en fruit aan in een pan met de boter. Voeg de paddenstoelen toe in plakjes en bak even mee. " +
                        "Voeg de bloem toe en roer erdoor. Giet de bouillon erbij en roer goed door. " +
                        "Doe de spruitjes, cranberry en pompoenblokjes bij de saus in de pan. Breng het mengsel op smaak met tijm.",
                "Heerlijke ovenschotel met spruitjes, pompoen en cranberry en een krokant laagje bladerdeeg met uitgestoken sterren, leuk om te serveren met kerst!",
                3L, recipeImage1);

        Recipe recipe2 = new Recipe(3L, "Kipragout",
                "Ingrediënten\n" +
                        "2 sjalotten\n" +
                        "160 gr kip (vega)\n" +
                        "250 gr champignons (in plakjes)\n" +
                        "40 gr boter\n" +
                        "40 gr bloem\n" +
                        "300 ml kippenbouillon of groentebouillon\n" +
                        "100 ml kookroom\n" +
                        "4 pasteibakjes\n" +
                        "Paar takjes verse peterselie" +
                        "\n" +
                        "Bereiding\n" +
                        " Kook bij het gebruik van rauwe kip de kip ca. 15 minuten in een pannetje met water en trek daarna met een vork uit elkaar. Vegetarische kip stukjes kun je kort meebakken met de champignons. Bak de pasteibakjes af in de oven.\n" +
                        "\n" +
                        " Bak de plakjes champignons in een koekenpan met 1 eetlepel boter. Snipper de sjalotjes heel fijn en fruit deze glazig in een ander pannetje met de rest van de boter. Roer de bloem erdoor en bak de roux 2 minuutjes. Giet de bouillon er beetje bij beetje bij en roer met een garde tot een gladde saus. Voeg de kookroom toe en meng er door. Laat de ragout nog iets indikken.\n" +
                        "\n" +
                        "  Schep dan de stukjes kip, wat gehakte peterselie en gebakken champignons toe en warm nog een minuutje goed door. Serveer de kipragout in of naast de pasteibakjes en garneer met wat peterselie.\n" +
                        "\n" +
                        "",
                "Heerlijke snelle romige kipragout, die tevens ook heel makkelijk vegetarisch te maken is, in pasteibakjes",
                3L, recipeImage2);
        return Arrays.asList(recipe, recipe1, recipe2);
    }

}
