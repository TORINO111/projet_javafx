package dette.boutique.core.repository.impl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.Repository;

public class RepositoryListImpl<T> implements Repository<T> {
    protected List<T> data = new ArrayList<>();

    public RepositoryListImpl(List<T> data) {
        this.data = data;
    }

    @Override
    public void insert(T element) {
        data.add(element);
    }

    @Override
    public List<T> selectAll() {
        return new ArrayList<>(data);
    }

    @Override
    public void remove(T element) {
        data.remove(element);
    }

    @Override
    public T selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

}