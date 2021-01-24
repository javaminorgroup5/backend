package nl.hro.cookbook.service;

import lombok.RequiredArgsConstructor;
import nl.hro.cookbook.model.domain.*;
import nl.hro.cookbook.security.Role;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDataService {

    private final PasswordEncoder passwordEncoder;
    private final CommonService commonService;

    public List<User> getUsers() throws IOException {

        CommonService commonService = new CommonService();
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource = resourceLoader.getResource("classpath:download.jpeg");
        Image profileImage = new Image("test.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource.getFile().toPath())));

        final User initialUser1 = new User("dion@quintor.nl", passwordEncoder.encode("quintor"), Role.ADMIN, new Profile("DionRecipeGuy45", profileImage), new ArrayList<>());
        initialUser1.setId(1L);
        final User initialUser2 = new User("geoffrey@quintor.nl", passwordEncoder.encode("quintor"), Role.COMMUNITY_MANAGER, new Profile("Maverick12", profileImage), new ArrayList<>());
        initialUser2.setId(2L);
        final User initialUser3 = new User("testuser@test.nl", passwordEncoder.encode("testpassword"), Role.COMMUNITY_MANAGER, new Profile("TheRecipeTester492", profileImage), new ArrayList<>());
        initialUser3.setId(3L);
        final User initialUser4 = new User("anuar@test.nl", passwordEncoder.encode("quintor"), Role.COMMUNITY_MANAGER, new Profile("Anuar", profileImage), new ArrayList<>());
        initialUser4.setId(4L);
        return Arrays.asList(initialUser1, initialUser2, initialUser3, initialUser4);
    }

    public List<Category> getCategories() throws IOException {
        final Category initialCategory1 = new Category("Italiaanse keuken", new ArrayList<>());
        initialCategory1.setId(1L);
        final Category initialCategory2 = new Category("Marokkaanse keuken", new ArrayList<>());
        initialCategory2.setId(2L);
        final Category initialCategory3 = new Category("Hollandse keuken", new ArrayList<>());
        initialCategory3.setId(3L);
        final Category initialCategory4 = new Category("Chinese keuken", new ArrayList<>());
        initialCategory4.setId(4L);
        return Arrays.asList(initialCategory1, initialCategory2, initialCategory3, initialCategory4);
    }

    public List<Recipe> getRecipes() throws IOException {

        CommonService commonService = new CommonService();
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        Resource resource1 = resourceLoader.getResource("classpath:tom-rijst-met-kip.jpg");
        Image recipeImage = new Image("gerecht1.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource1.getFile().toPath())));

        Resource resource2 =resourceLoader.getResource("classpath:kerst-ovenschotel.jpg");
        Image recipeImage1 = new Image("gerecht2.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource2.getFile().toPath())));

        Resource resource3 =resourceLoader.getResource("classpath:kipragout-1.jpg");
        Image recipeImage2 = new Image("gerecht3.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource3.getFile().toPath())));

        Recipe recipe = new Recipe("Budget recept: tomatenrijst met kip" ,
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
                3L, 1L,recipeImage, new ArrayList<>());
        Recipe recipe1 = new Recipe("Kerst ovenschotel met pompoen",
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
                3L, 1L,recipeImage1, new ArrayList<>());

        Recipe recipe2 = new Recipe("Kipragout",
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
                3L, 1L,recipeImage2, new ArrayList<>());
        return Arrays.asList(recipe, recipe1, recipe2);
    }

    public List<Group> getGroups() throws IOException {
        List<Category> categories = getCategories();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:group.jpg");
        Image image = new Image("group.jpg", "file", commonService.compressBytes(Files.readAllBytes(resource.getFile().toPath())));
        final Group initialGroup1 = new Group("PastaGroep", "Leuke pasta groep", 1L, categories.get(0),  Group.GroupPrivacy.OPEN,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup1.setId(1L);
        final Group initialGroup2 = new Group("RodeSauzen", "Roder dan rood", 1L,categories.get(0),  Group.GroupPrivacy.OPEN,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup2.setId(2L);
        final Group initialGroup3 = new Group("Bloemkoollovers", "Bloemkool is een groente die hoort bij het geslacht kool uit de kruisbloemenfamilie (Brassicaceae). De botanische naam voor bloemkool is Brassica oleracea convar. ", 2L, categories.get(2), Group.GroupPrivacy.OPEN,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup3.setId(3L);
        final Group initialGroup4 = new Group("Italiaanse keukengroep", "De Italiaanse keuken omvat de inheemse kookkunst van het Italiaanse schiereiland. Deze keuken is zeer gevarieerd en seizoensgebonden.", 2L, categories.get(0), Group.GroupPrivacy.OPEN,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup4.setId(4L);
        final Group initialGroup5 = new Group("Marokkaanse keuken", "Couscous Habibi", 2L, categories.get(1), Group.GroupPrivacy.OPEN,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup5.setId(5L);
        final Group initialGroup6 = new Group("RamsayItes", "Koken net Gordon Ramsay! ", 3L, categories.get(3), Group.GroupPrivacy.OPEN,new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image);
        initialGroup6.setId(6L);
        return Arrays.asList(initialGroup1, initialGroup2, initialGroup3, initialGroup4, initialGroup5, initialGroup6);
    }

    public List<Message> getFeeds() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource1 = resourceLoader.getResource("classpath:bananenpannenkoekjes.jpg");
        Image image1 = new Image("bananenpannenkoekjes", "file", commonService.compressBytes(Files.readAllBytes(resource1.getFile().toPath())));
        final Message message1 = new Message("Makkelijke en gezonde glutenvrije bananenpannenkoekjes gemaakt van banaan en ei, lekker om mee te ontbijten of als tussendoortje", 3L, 6L, "Bananentoetje", 1L, image1);
        Resource resource2 = resourceLoader.getResource("classpath:bananenbrood-met-noten.jpg");
        Image image2 = new Image("bananenbrood-met-noten", "file", commonService.compressBytes(Files.readAllBytes(resource2.getFile().toPath())));
        final Message message2 = new Message("Gezond bananenbrood met een heerlijke crunch van pecannoten en walnoten", 3L, 6L, "Notenspecial", 1L, image2);
        Resource resource3 = resourceLoader.getResource("classpath:kerst-ovenschotel.jpg");
        Image image3 = new Image("kerst-ovenschotel", "file", commonService.compressBytes(Files.readAllBytes(resource3.getFile().toPath())));
        final Message message3 = new Message("Kerst ovenschotel met pompoen", 3L, 6L, "Kerst special", 1L, image3);
        return Arrays.asList(message1, message2, message3);
    }
}
