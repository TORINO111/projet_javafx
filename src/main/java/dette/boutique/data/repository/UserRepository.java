package dette.boutique.data.repository;

import dette.boutique.core.repository.Repository;
import dette.boutique.data.entities.User;

public interface UserRepository extends Repository<User> {
    void updateClientForUser(User user);

    User selectByLogin(String login);

    User selectById(int id);
}
