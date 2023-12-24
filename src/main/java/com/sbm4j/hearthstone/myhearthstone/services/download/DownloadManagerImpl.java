package com.sbm4j.hearthstone.myhearthstone.services.download;

import com.google.inject.Inject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.sbm4j.hearthstone.myhearthstone.model.BattleAccount;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadManagerImpl implements DownloadManager {

    protected static final Logger logger = LogManager.getLogger();

    @Inject
    protected ConfigManager config;


    @Override
    public File downloadFile(String url, String filename) throws IOException {
        logger.info("Download " + url + " to " + filename);
        try(InputStream in = new URL(url).openStream()){
            Files.copy(in, Paths.get(filename));
            return new File(filename);
        }
    }


    protected BrowserContext getBrowser(Playwright playwright, boolean headless){
        File contextPath = this.config.getChromiumContextPath();
        BrowserContext context = playwright.chromium().launchPersistentContext(contextPath.toPath(),
                new BrowserType.LaunchPersistentContextOptions().setHeadless(headless)
        );
        return context;
    }

    protected Page navigateToHsReplay(BrowserContext context){
        Page page = context.newPage();
        page.navigate("https://hsreplay.net/");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        Locator cookiesAccept = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("J'ACCEPTE"));
        if (cookiesAccept.isVisible()) {
            cookiesAccept.click();
        }
        return page;
    }

    @Override
    public Boolean connectToHSReplay(BattleAccount account) {
        try(Playwright playwright = Playwright.create()){
            BrowserContext context = this.getBrowser(playwright, false);
            Page page = context.pages().get(0);
            page.navigate("https://hsreplay.net/account/login/");

            Locator connexionLoc = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Connexion"));
            Locator connected = page.getByRole(AriaRole.BUTTON,
                    new Page.GetByRoleOptions().setName(account.getBattleTag() + " (Europe)"));

            if(connected.isVisible()){
                return true;
            }
            else{
                if(!connexionLoc.isVisible()){
                    logger.info("disconnect");
                    page.navigate("https://hsreplay.net/account/logout/");
                    page.navigate("https://hsreplay.net/account/login/");
                }

                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Connexion via Blizzard")).click();
                page.getByPlaceholder("E-mail ou n° de téléphone").fill(account.getEmail());
                page.getByPlaceholder("Mot de passe").click();
                page.getByPlaceholder("Mot de passe").fill(account.getPassword());
                page.waitForClose(() -> {});
            }

            return true;
        }
    }

    @Override
    public void disconnectFromHSreplay() {
        try(Playwright playwright = Playwright.create()){
            BrowserContext context = this.getBrowser(playwright, false);
            Page page = context.pages().get(0);
            page.navigate("https://hsreplay.net/account/logout/");
        }
    }


    @Override
    public String downloadCollectionFile(BattleAccount account) {

        String jsonContent = null;
        try (Playwright playwright = Playwright.create()) {
            BrowserContext context = this.getBrowser(playwright, true);

            Page page1 = context.newPage();
            page1.navigate("https://hsreplay.net/api/v1/collection/?region=2&account_lo="
                        + account.getAccount_lo() + "&type=CONSTRUCTED&format=json");
            Locator jsonLoc = page1.getByText("{\"collection\":{");
            jsonContent = jsonLoc.textContent();

            return jsonContent;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return jsonContent;
    }


}
