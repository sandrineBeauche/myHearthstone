package com.sbm4j.hearthstone.myhearthstone.services.download;

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

}
