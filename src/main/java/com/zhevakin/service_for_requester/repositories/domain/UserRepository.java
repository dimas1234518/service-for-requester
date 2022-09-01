package com.zhevakin.service_for_requester.repositories.domain;

import com.zhevakin.service_for_requester.models.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
