package dette.boutique.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface DataBase<T> {
    Connection connexion() ;

    void deconnexion() ;

    ResultSet executeQuery() ;

    int executeUpdate() ;

    void setFields(PreparedStatement pstmt, T element) ;
    
    void setFieldsUpdate(PreparedStatement pstmt, T element) ;

    String generateSql(T element);

    void init(String sql) ;

    Object convertToObject(ResultSet resultSet);
}
