package cz.mendelu.ea.domain.user;

import cz.mendelu.ea.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        return repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public User updateUser(Long id, User user) {
        user.setId(id);
        return repository.save(user);
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException();
        }
        repository.deleteById(id);
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(NotFoundException::new);
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(NotFoundException::new);
    }

    public List<User> getUsersByCountry(String country) {
        return repository.findByCountry(country);
    }

    public List<User> getUsersYoungerThan(LocalDate date) {
        return repository.findUsersYoungerThan(date);
    }

    public List<User> getUsersWithMoreFavoritesThan(Integer count) {
        return repository.findUsersWithMoreFavoritesThan(count);
    }

    // Complex calculations using streams
    public Map<String, Long> getUsersCountByCountry() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        User::getCountry,
                        Collectors.counting()
                ));
    }

    public Map<Integer, Long> getUsersCountByAgeGroup() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        user -> LocalDate.now().getYear() - user.getDateOfBirth().getYear(),
                        Collectors.counting()
                ));
    }

    public Map<String, Double> getAverageFavoritesCountByCountry() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        User::getCountry,
                        Collectors.averagingDouble(user -> user.getFavorites().size())
                ));
    }

    public Map<String, List<User>> getTopUsersByCountry(int limit) {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        User::getCountry,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                users -> users.stream()
                                        .sorted((u1, u2) -> Integer.compare(
                                                u2.getFavorites().size(),
                                                u1.getFavorites().size()
                                        ))
                                        .limit(limit)
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public Map<String, Double> getAverageUserAgeByCountry() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        User::getCountry,
                        Collectors.averagingDouble(user ->
                                LocalDate.now().getYear() - user.getDateOfBirth().getYear()
                        )
                ));
    }
} 