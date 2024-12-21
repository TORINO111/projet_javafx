package dette.boutique.core.repository.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import dette.boutique.core.database.impl.DataBasePostgreImpl;
import dette.boutique.core.repository.Repository;

public class RepositoryDbPostgreImpl<T> extends DataBasePostgreImpl<T> implements Repository<T> {
    @Override
    public void insert(T entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public List<T> selectAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public T selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void setFields(PreparedStatement pstmt, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFields'");
    }

    @Override
    public void setFieldsUpdate(PreparedStatement pstmt, T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFields'");
    }

    @Override
    public String generateSql(T element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateSql'");
    }

    @Override
    public Object convertToObject(ResultSet resultSet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
