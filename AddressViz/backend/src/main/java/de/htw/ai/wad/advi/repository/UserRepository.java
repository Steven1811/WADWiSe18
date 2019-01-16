package de.htw.ai.wad.advi.repository;

import de.htw.ai.wad.advi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
}