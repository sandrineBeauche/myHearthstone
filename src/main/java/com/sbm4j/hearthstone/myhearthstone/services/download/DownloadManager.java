package com.sbm4j.hearthstone.myhearthstone.services.download;

import com.sbm4j.hearthstone.myhearthstone.model.BattleAccount;

import java.io.File;
import java.io.IOException;

public interface DownloadManager {

    static String bigCardImagesUrl =
            "https://art.hearthstonejson.com/v1/render/latest/frFR/512x/";

    static String smallCardImagesUrl =
            "https://art.hearthstonejson.com/v1/render/latest/frFR/256x/";

    static String tileCardImagesUrl =
            "https://art.hearthstonejson.com/v1/tiles/";

    File downloadFile(String url, String filename) throws IOException;

    Boolean connectToHSReplay(BattleAccount account);

    void disconnectFromHSreplay();

    String downloadCollectionFile(BattleAccount account);


}
