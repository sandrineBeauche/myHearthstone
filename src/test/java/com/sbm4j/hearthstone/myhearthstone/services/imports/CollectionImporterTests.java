package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleTesting;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonUserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CollectionImporterTests {

    protected File jsonCollectionFile;

    protected ImportCollectionAction importer;

    @BeforeEach
    public void beforeEach(){
        ClassLoader classLoader = getClass().getClassLoader();
        this.jsonCollectionFile= new File(classLoader.getResource("hsreplayCollection.json").getFile());

        Injector injector = Guice.createInjector(
                new HearthstoneModuleTesting(null, true));

        this.importer = injector.getInstance(JsonCollectionImporterTesting.class);
    }


    @Test
    public void parseCollection() throws FileNotFoundException {
        JsonUserData data = this.importer.parseUserData(this.jsonCollectionFile);
        assertNotNull(data);
    }
}
