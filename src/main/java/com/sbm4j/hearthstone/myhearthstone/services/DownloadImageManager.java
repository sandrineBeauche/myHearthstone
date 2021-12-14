package com.sbm4j.hearthstone.myhearthstone.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadImageManager {

    protected static String bigCardImagesUrl =
            "https://art.hearthstonejson.com/v1/render/latest/frFR/512x/";

    protected static String smallCardImagesUrl =
            "https://art.hearthstonejson.com/v1/render/latest/frFR/256x/";

    protected static String tileCardImagesUrl =
            "https://art.hearthstonejson.com/v1/tiles/";

    protected static final Logger logger = LogManager.getLogger();

    protected File downloadImageFile(String url, String filename) throws IOException {
        logger.info("Download " + url + " to " + filename);
        try(InputStream in = new URL(url).openStream()){
            Files.copy(in, Paths.get(filename));
            return new File(filename);
        }
    }

}
