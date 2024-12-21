module dette.boutique {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jakarta.persistence;
    requires org.hibernate.orm.jpamodelgen;
    requires org.hibernate.orm.core;
    requires mysql.connector.j;
    requires org.yaml.snakeyaml;
    requires org.postgresql.jdbc;
    requires lombok;

    opens dette.boutique to javafx.fxml, jakarta.persistence, org.hibernate.orm.core, org.hibernate.orm.jpamodelgen;
    opens dette.boutique.controllers to javafx.fxml, jakarta.persistence, org.hibernate.orm.core, org.hibernate.orm.jpamodelgen, javafx.controls;
    exports dette.boutique.controllers to javafx.fxml, javafx.graphics;
    exports dette.boutique to javafx.fxml, javafx.graphics;
}
