module com.sbm4j.hearthstone.myhearthstone {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.persistence;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires org.hsqldb;

    exports com.sbm4j.hearthstone.myhearthstone;

    opens com.sbm4j.hearthstone.myhearthstone.model to com.google.gson, org.hibernate.orm.core;
    opens com.sbm4j.hearthstone.myhearthstone to javafx.fxml, org.hibernate.orm.core;
}