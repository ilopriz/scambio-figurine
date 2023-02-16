package com.danzir.scambio.figurine.scrapper.navigation;

import com.danzir.scambio.figurine.scrapper.config.ConfigProperties;
import com.danzir.scambio.figurine.data.model.Album;
import com.danzir.scambio.figurine.data.model.Figurina;
import com.danzir.scambio.figurine.data.model.Record;
import com.danzir.scambio.figurine.data.model.User;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Navigation {

    private static final Logger logger = LoggerFactory.getLogger(Navigation.class);

    private static final int MIN_TIMEOUT_MILLIS = 1000;

    @Value("${user.home}")
    private String userHome;

    private static int screenNum = 0;

    private static Path runFolder;
    private String username;
    private String password;

    public Navigation(ConfigProperties configProperties){
        username = configProperties.getUsername();
        logger.trace("username: {}", username);

        password = configProperties.getPassword();
        logger.trace("password: {}", password);
    }

    @PostConstruct
    public void init(){
        String runFolderName = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(System.currentTimeMillis());
        runFolder = Paths.get(userHome).resolve("scambio-figurine-scrapper").resolve(runFolderName);
        logger.trace("runFolder: {}", runFolder);
    }

    public synchronized void takeScreenshot(Page page){
        Path screenPath = runFolder.resolve(Paths.get("screenshot", ++screenNum+".png"));
        page.screenshot(new Page.ScreenshotOptions().setPath(screenPath));
    }

    public void login(Page page){
        logger.debug("Starting new user session in page " + page.hashCode());
        page.navigate("https://www.scambio-figurine.it/");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("SEI GIA' ISCRITTA/O")).click();
        page.getByPlaceholder("Nome utente o email").fill(username);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password")).fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ACCEDI")).click();

        try{
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("No, grazie")).click(new Locator.ClickOptions().setTimeout(5000));
        }
        catch (TimeoutError e){
            logger.trace("no element for No grazie: {}", e.getMessage());
        }
    }

    public void goToScambi(Page page){
        page.navigate("https://www.scambio-figurine.it/g-check-glb.php");
        try{
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ho capito!")).click(new Locator.ClickOptions().setTimeout(5000));
        }
        catch (TimeoutError e){
            logger.trace("no element for Ho capito {}", e.getMessage());
        }

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("AGGIORNA")).click();

        page.waitForSelector("//div[contains(@id,\"chk-\") and @onclick][1]");
        logger.debug("User session start in page " + page.hashCode() + " complete");
    }

    public List<String> getAllUsersIds(Page page){
        logger.info("Getting all user ids");
        String userSelector = "//div[contains(@id,\"chk-\") and @onclick]";
        while (true){
            List<Locator> divUsers = page.locator(userSelector).all();
            page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
            try{
                page.waitForSelector(userSelector+"["+(divUsers.size()+1)+"]", new Page.WaitForSelectorOptions().setTimeout(30000));
            }
            catch (TimeoutError e){
                break;
            }
        }
        List<Locator> allUsers = page.locator(userSelector).all();
        List<String> usersIds = allUsers.stream().map(user -> user.getAttribute("id").replace("chk-", "")).collect(Collectors.toList());
        logger.info("usersIds size: {}", usersIds.size());
        logger.info("usersIds: {}", usersIds);
        return usersIds;
    }

    /*
    public List<String> getAllUsersIds(Page page){
        logger.info("Getting all user ids");
        String userSelector = "//div[contains(@id,\"chk-\") and @onclick]";
        List<Locator> allUsers = page.locator(userSelector).all();
        List<String> usersIds = allUsers.stream().limit(2).map(user -> user.getAttribute("id").replace("chk-", "")).collect(Collectors.toList());
        logger.info("usersIds size: {}", usersIds.size());
        logger.info("usersIds: {}", usersIds);
        return usersIds;
    }
*/

    public Record readUserData(Page page, String userId){
        try {
            logger.trace("##################################################################################################");
            logger.trace("userId: {}", userId);
            page.navigate("https://www.scambio-figurine.it/getprofile.php?memberid=" + userId);

            String userName = page.locator("//div[@id=\"getprofile\"]/div[1]/h2").textContent();
            logger.trace("userName: {}", userName);

            String feedbackPositivi = "0";
            try {
                feedbackPositivi = page.locator("//div[@id=\"getprofile\"]/div[1]/div[2]/div/span[@class=\"badge thumbsup\"]").textContent(new Locator.TextContentOptions().setTimeout(MIN_TIMEOUT_MILLIS));
            } catch (TimeoutError e) {
                logger.trace("no element for feedbackPositivi: {}", e.getMessage());
            }
            logger.trace("feedbackPositivi: {}", feedbackPositivi);

            String feedbackNegativi = "0";
            try {
                feedbackNegativi = page.locator("//div[@id=\"getprofile\"]/div[1]/div[2]/div/span[@class=\"badge thumbsdown\"]").textContent(new Locator.TextContentOptions().setTimeout(MIN_TIMEOUT_MILLIS));
            } catch (TimeoutError e) {
                logger.trace("no element for feedbackNegativi: {}", e.getMessage());
            }
            logger.trace("feedbackNegativi: {}", feedbackNegativi);

            String provincia = null;
            try {
                provincia = page.locator("//div[@id=\"getprofile\"]/div[1]/div[3]/strong[1]").textContent(new Locator.TextContentOptions().setTimeout(MIN_TIMEOUT_MILLIS));
                logger.trace("provincia: {}", provincia);
            } catch (TimeoutError e) {
                logger.trace("no element for provincia: {}", e.getMessage());
            }

            String comune = null;
            try {
                comune = page.locator("//div[@id=\"getprofile\"]/div[1]/div[3]/strong[2]").textContent(new Locator.TextContentOptions().setTimeout(MIN_TIMEOUT_MILLIS));
                logger.trace("comune: {}", comune);
            } catch (TimeoutError e) {
                logger.trace("no element for comune: {}", e.getMessage());
            }

            boolean scambioPosta = false;
            try {
                page.locator("//div[@id=\"getprofile\"]/div[1]/div[3]/span/img[contains(@src, \"mail.png\")]").isVisible();
                scambioPosta = true;
            } catch (TimeoutError e) {
                logger.trace("no element for scambioPosta: {}", e.getMessage());
            }
            logger.trace("scambioPosta: {}", scambioPosta);

            boolean scambioMano = false;
            try {
                page.locator("//div[@id=\"getprofile\"]/div[1]/div[3]/span/img[contains(@src, \"exchange.png\")]").isVisible();
                scambioMano = true;
            } catch (TimeoutError e) {
                logger.trace("no element for scambioMano: {}", e.getMessage());
            }
            logger.trace("scambioMano: {}", scambioMano);

            String dataRegistrazione = null;
            try {
                dataRegistrazione = page.locator("//div[@id=\"getprofile\"]/div[1]/div[4]/b[1]").textContent(new Locator.TextContentOptions().setTimeout(MIN_TIMEOUT_MILLIS));
                logger.trace("dataRegistrazione: {}", dataRegistrazione);
            } catch (TimeoutError e) {
                logger.trace("no element for dataRegistrazione: {}", e.getMessage());
            }

            String dataUltimoAccesso = null;
            try {
                dataUltimoAccesso = page.locator("//div[@id=\"getprofile\"]/div[1]/div[4]/b[2]").textContent(new Locator.TextContentOptions().setTimeout(MIN_TIMEOUT_MILLIS));
                logger.trace("dataUltimoAccesso: {}", dataUltimoAccesso);
            } catch (TimeoutError e) {
                logger.trace("no element for ultimoAccesso: {}", e.getMessage());
            }

            User user = new User(userId, userName, Integer.parseInt(feedbackPositivi), Integer.parseInt(feedbackNegativi), provincia, comune, scambioPosta, scambioMano, dataRegistrazione, dataUltimoAccesso);

            page.getByText("Calciatori Panini 2022 2023", new Page.GetByTextOptions().setExact(true)).click();

            String albumSelector = "//tr[contains(@onclick,\"gInfoAlbum\")]/td[@style]";
            Locator albumLocator = page.locator(albumSelector);

            page.waitForSelector(albumSelector + "/h4/div[2]");

            List<Figurina> doppie = new ArrayList<>();
            List<String> mancanti = new ArrayList<>();

            boolean listingDoppie = false;
            boolean listingMancanti = false;

            List<Locator> albumNodes = albumLocator.locator("//child::*").all();
            for (Locator albumNode : albumNodes) {
                String textContent = albumNode.textContent().trim();
                if(StringUtils.hasText(textContent)) {
                    if ("doppie".equalsIgnoreCase(textContent)) {
                        listingDoppie = true;
                        listingMancanti = false;
                    } else if ("mancanti".equalsIgnoreCase(textContent)) {
                        listingDoppie = false;
                        listingMancanti = true;
                    } else if (!Objects.isNull(albumNode.getAttribute("class"))) {
                        if (listingDoppie) {
                            Pattern pattern = Pattern.compile("^(\\w+)(\\s\\((\\d+)\\))?$");
                            Matcher matcher = pattern.matcher(textContent);
                            matcher.matches();
                            String numero = matcher.group(1);
                            String qtaStr = matcher.group(3);
                            int qta = Integer.parseInt(Optional.ofNullable(qtaStr).orElse("1"));
                            Figurina figurina = new Figurina(numero, qta);
                            doppie.add(figurina);
                        } else if (listingMancanti) {
                            mancanti.add(textContent);
                        }
                    }
                }
            }

            logger.trace("doppie: {}", doppie);
            logger.trace("mancanti: {}", mancanti);

            Album album = new Album(userId, doppie, mancanti);

            return new Record(user, album);
        }
        catch (TimeoutError e){
            logger.error("Error in processing userId {}", userId, e);
            return null;
        }
    }

    public Record readSessionUserData(Page page){
        logger.info("Reading session user data");
        page.getByText("CALCIATORI PANINI 2022 2023").click();
        page.waitForSelector("//div[@id=\"gfigurine\"]/h2");

        String selectorMancanti = "//div[./div/table//button[contains(@class,\"btn-danger\")]]/h2/span";
        List<String> mancanti = new ArrayList<>();
        List<Locator> mancantiLocators = page.locator(selectorMancanti).all();
        for (Locator mancante : mancantiLocators){
            mancanti.add(mancante.textContent());
        }

        String selectorDoppieDiv = "//div[./div/table//span[@class=\"btn-success\"]]";
        String selectorDoppieNumero = "//h2/span";
        String selectorDivQta = "//div/table//span";
        List<Figurina> doppie = new ArrayList<>();
        List<Locator> doppieDivLocators = page.locator(selectorDoppieDiv).all();
        for (Locator divLocator : doppieDivLocators){
            String numero = divLocator.locator(selectorDoppieNumero).textContent();
            String qta = divLocator.locator(selectorDivQta).textContent();
            doppie.add(new Figurina(numero, Integer.parseInt(qta)));
        }

        String userId = "0";
        User user = new User(userId);

        Album album = new Album(userId, doppie, mancanti);

        return new Record(user, album);
    }

}
