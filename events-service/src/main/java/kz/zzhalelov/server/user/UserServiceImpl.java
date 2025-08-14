package kz.zzhalelov.server.user;

import kz.zzhalelov.server.exception.ConflictException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, ContentNegotiatingViewResolver contentNegotiatingViewResolver) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        userRepository.findUserByEmail(user.getEmail())
                .filter(u -> !u.equals(user))
                .ifPresent(u -> {
                    throw new ConflictException("Пользователь с таким email уже существует");
                });
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}