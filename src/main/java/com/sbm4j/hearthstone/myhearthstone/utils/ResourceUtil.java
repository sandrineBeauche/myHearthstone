package com.sbm4j.hearthstone.myhearthstone.utils;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;

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

    public static File getAppRoot(){
        String path = System.getProperty("user.dir");
        if(path.contains("bin")) {
            return new File(path).getParentFile();
        }
        else {
            return new File(path);
        }
    }

}
