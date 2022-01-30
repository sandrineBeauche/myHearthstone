package com.sbm4j.hearthstone.myhearthstone.utils;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceUtil {

    public static String getResourceContent(String resourceName) throws IOException {
        InputStream in = ResourceUtil.class.getClassLoader().getResourceAsStream(resourceName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line = buf.readLine();
        while(line != null){
            builder.append(line);
            line = buf.readLine();
        }
        return builder.toString();
    }

}
