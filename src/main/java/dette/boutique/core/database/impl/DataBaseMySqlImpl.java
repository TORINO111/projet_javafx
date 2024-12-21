package dette.boutique.core.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dette.boutique.core.database.DataBase;

public abstract class DataBaseMySqlImpl<T> implements DataBase<T> {
    protected final String URL = "jdbc:mysql://localhost:3306/dette_boutique";
    protected final String USER = "root";
    protected final String PASSWORD = "";
    protected Connection connection = null;
    protected PreparedStatement ps = null;

    @Override
    public Connection connexion() {
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établir la connexion
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion établie avec succès !");
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trouvé.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'établissement de la connexion.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deconnexion() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Connexion fermée avec succès.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResultSet executeQuery() {
        try {
            if (ps == null) {
                throw new IllegalStateException(
                        "PreparedStatement non initialisé. Appelle `init()` avec une requête SQL.");
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
            e.printStackTrace();
            return null; // Ou lance une exception personnalisée
        }
    }

    @Override
    public int executeUpdate() {
        try {
            if (ps == null) {
                throw new IllegalStateException(
                        "PreparedStatement non initialisé. Appelle `init()` avec une requête SQL.");
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la mise à jour : " + e.getMessage());
            e.printStackTrace();
            return -1; // Ou lance une exception personnalisée
        }
    }

    @Override
    public void init(String sql) {
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("Connexion non disponible ou fermée.");
            }

            String sqlUpperCase = sql.toUpperCase().trim();

            if (sqlUpperCase.startsWith("INSERT")) {
                ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            } else {
                ps = connection.prepareStatement(sql);
            }
            System.out.println("PreparedStatement initialisé pour la requête : " + sql);
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation du PreparedStatement : " + e.getMessage());
            e.printStackTrace();
        }
    }
}