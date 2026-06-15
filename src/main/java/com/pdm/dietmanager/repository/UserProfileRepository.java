package com.pdm.dietmanager.repository;

import com.pdm.dietmanager.entity.User;
import com.pdm.dietmanager.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserProfile 엔티티의 데이터베이스 접근 인터페이스.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
    boolean existsByUser(User user);
}
