package com.zhevakin.service_for_requester.repositories;

import com.zhevakin.service_for_requester.models.domain.Group;
import com.zhevakin.service_for_requester.models.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, String> {

    List<Environment> findByGroupsIn(Set<Group> groups);

}
