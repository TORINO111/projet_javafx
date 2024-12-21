package dette.boutique.core.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import dette.boutique.core.database.impl.DataBaseMySqlImpl;
import dette.boutique.core.repository.Repository;

public abstract class RepositoryDbMysqlImpl<T> extends DataBaseMySqlImpl<T> implements Repository<T> {

    protected String tableName;
    public String convertLocalDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public abstract T convertToObject(ResultSet rs);
}