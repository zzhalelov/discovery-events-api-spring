package kz.zzhalelov.server.user;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> findAll(List<Long> ids);

    List<User> findAll(Pageable pageable);

    void delete(long userId);
}