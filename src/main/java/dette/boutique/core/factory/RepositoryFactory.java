package dette.boutique.core.factory;

public interface RepositoryFactory<T> {
    T create(String persistence);
}