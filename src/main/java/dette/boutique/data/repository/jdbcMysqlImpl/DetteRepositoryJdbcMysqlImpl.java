package dette.boutique.data.repository.jdbcMysqlImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbMysqlImpl;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.repository.DetteRepository;

public class DetteRepositoryJdbcMysqlImpl extends RepositoryDbMysqlImpl<Dette> implements DetteRepository {

    @Override
    public void insert(Dette data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void remove(Dette element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void setFields(PreparedStatement pstmt, Dette element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFields'");
    }

    @Override
    public String generateSql(Dette element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public List<Dette> selectAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAll'");
    }

    @Override
    public Dette selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public Dette convertToObject(ResultSet rs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToObject'");
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, Dette element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFieldsUpdate'");
    }

}
