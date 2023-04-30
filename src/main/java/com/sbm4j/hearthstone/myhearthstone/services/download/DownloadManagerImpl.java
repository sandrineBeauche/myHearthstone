package com.sbm4j.hearthstone.myhearthstone.services.download;

//import com.microsoft.playwright.Browser;
//import com.microsoft.playwright.Page;
//import com.microsoft.playwright.Playwright;
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

    @Override
    public File downloadFile(String url, String filename) throws IOException {
        logger.info("Download " + url + " to " + filename);
        try(InputStream in = new URL(url).openStream()){
            Files.copy(in, Paths.get(filename));
            return new File(filename);
        }
    }

    @Override
    public File downloadCollectionFile(String userId, String password) {
        /*
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate("http://playwright.dev");
            System.out.println(page.title());
        }
        catch(Exception ex){
            logger.error(ex.getMessage(), ex);
        }
         */
        return null;
    }


}
