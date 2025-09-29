package cz.mendelu.ea.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByCountry(String country);
    
    @Query("SELECT u FROM User u WHERE u.dateOfBirth > ?1")
    List<User> findUsersYoungerThan(java.time.LocalDate date);
    
    @Query("SELECT u FROM User u WHERE SIZE(u.favorites) > ?1")
    List<User> findUsersWithMoreFavoritesThan(Integer count);
} 