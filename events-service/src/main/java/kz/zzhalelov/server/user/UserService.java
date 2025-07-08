package kz.zzhalelov.server.user;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> findAll();

    void delete(long userId);
}