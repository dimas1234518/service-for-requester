package com.zhevakin.service_for_requester.repositories.domain;

import com.zhevakin.service_for_requester.models.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByName(String name);
}
