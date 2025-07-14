package kz.zzhalelov.server.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}