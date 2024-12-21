package dette.boutique.data.repository.jdbcMysqlImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbMysqlImpl;
import dette.boutique.data.entities.Details;
import dette.boutique.data.repository.DetailsRepository;

public class DetailsRepositoryJdbcMysqlImpl extends RepositoryDbMysqlImpl<Details> implements DetailsRepository {

    @Override
    public void insert(Details data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public List<Details> selectAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAll'");
    }

    @Override
    public Details selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(Details element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void setFields(PreparedStatement pstmt, Details element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFields'");
    }

    @Override
    public String generateSql(Details element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Details convertToObject(ResultSet rs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToObject'");
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, Details element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
