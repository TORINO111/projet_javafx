package dette.boutique.data.repository.jdbcMysqlImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryDbMysqlImpl;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.repository.EtatRepository;

public class EtatRepositoryJdbcMysqlImpl extends RepositoryDbMysqlImpl<Etat> implements EtatRepository {

    @Override
    public void insert(Etat element) {
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public List<Etat> selectAll() {
        throw new UnsupportedOperationException("Unimplemented method 'selectAll'");
    }

    @Override
    public void setFields(PreparedStatement pstmt, Etat element) {
        throw new UnsupportedOperationException("Unimplemented method 'setFields'");
    }

    @Override
    public String generateSql(Etat element) {
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Etat selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(Etat element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    public Etat convertToObject(ResultSet rs) {
        throw new UnsupportedOperationException("Unimplemented method 'convertToObject'");
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, Etat element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
