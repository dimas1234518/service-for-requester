package com.zhevakin.service_for_requester.repositories.domain;

import com.zhevakin.service_for_requester.models.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
