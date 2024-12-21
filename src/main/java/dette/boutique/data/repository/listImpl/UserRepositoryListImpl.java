package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryListImpl;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.UserRepository;

public class UserRepositoryListImpl extends RepositoryListImpl<User> implements UserRepository {

    public UserRepositoryListImpl(List<User> data) {
        super(data);
    }

    public UserRepositoryListImpl() {
        // throw new UnsupportedOperationException("Not supported yet.");
        super(new ArrayList<>());
    }

    @Override
    public void updateClientForUser(User user) {
        user.setClient(user.getClient());
    }

    @Override
    public User selectByLogin(String login) {
        return data.stream()
                .filter(client -> client.getLogin().compareTo(login) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public User selectById(int id) {
        return data.stream()
                .filter(client -> client.getId() == (id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void remove(User element) {
        data.remove(element);
    }

}
