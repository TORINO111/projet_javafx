package dette.boutique.core.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dette.boutique.core.database.DataBase;

public class DataBasePostgreImpl<T> implements DataBase<T> {
    // URL de connexion pour PostgreSQL
    protected final String URL = "jdbc:postgresql://localhost:5432/dette_boutique_java";
    protected final String USER = "postgres";
    protected final String PASSWORD = "1111";
    protected Connection connection = null;
    protected PreparedStatement ps = null;

    @Override
    public Connection connexion() {
        try {
            // Charger le driver JDBC
            Class.forName("org.postgresql.Driver");
            // Établir la connexion
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
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
            return null;
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
            return -1;
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
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation du PreparedStatement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setFields(PreparedStatement pstmt, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFields'");
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFieldsUpdate'");
    }

    @Override
    public String generateSql(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Object convertToObject(ResultSet resultSet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToObject'");
    }
}
