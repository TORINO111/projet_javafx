package dette.boutique.core.repository;

import java.util.List;

public interface Repository<T> {
    void insert(T data);

    List<T> selectAll();

    T selectById(int id);

    void remove(T element);
}
