module com.sbm4j.hearthstone.myhearthstone {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.persistence;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires org.hsqldb;
    requires org.apache.logging.log4j;
    requires org.apache.commons.codec;
    requires thumbnailator;
    requires de.saxsys.mvvmfx;
    requires de.saxsys.mvvmfx.guice;
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires com.google.guice;
    requires java.sql;

    exports com.sbm4j.hearthstone.myhearthstone;

    opens com.sbm4j.hearthstone.myhearthstone to javafx.fxml, org.hibernate.orm.core, de.saxsys.mvvmfx, com.google.guice;
    opens com.sbm4j.hearthstone.myhearthstone.model.json to com.google.gson;
    opens com.sbm4j.hearthstone.myhearthstone.views to javafx.fxml, de.saxsys.mvvmfx, com.google.guice;
    opens com.sbm4j.hearthstone.myhearthstone.viewmodel to com.google.guice, de.saxsys.mvvmfx;
    //opens com.sbm4j.hearthstone.myhearthstone.services to com.google.guice;
    opens com.sbm4j.hearthstone.myhearthstone.services.images to com.google.guice;
    opens com.sbm4j.hearthstone.myhearthstone.services.config to com.google.guice;
    opens com.sbm4j.hearthstone.myhearthstone.services.db to com.google.guice;
    opens com.sbm4j.hearthstone.myhearthstone.services.download to com.google.guice;
    opens com.sbm4j.hearthstone.myhearthstone.services.imports to com.google.guice;
    opens com.sbm4j.hearthstone.myhearthstone.model to com.google.gson, com.google.guice, de.saxsys.mvvmfx, javafx.fxml, org.hibernate.orm.core;
}