package com.zhevakin.service_for_requester.services;

import com.zhevakin.service_for_requester.models.Environment;
import com.zhevakin.service_for_requester.models.domain.Group;
import com.zhevakin.service_for_requester.repositories.EnvironmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;

    @Autowired
    public EnvironmentService(EnvironmentRepository environmentRepository) {
        this.environmentRepository = environmentRepository;
    }

    public void saveEnvironments(List<Environment> environments){

        //Принадлежность переменных к группе определять по пользователю
        environmentRepository.saveAll(environments);
    }

    public void saveEnvironments(List<Environment> environments, Set<Group> groups) {

        List<Environment> environmentsInDB = getEnvironments();
        List<Environment> savesEnvironments = new ArrayList<>();

        for (Environment environment : environments) {
            Environment envInDB = environmentsInDB.stream()
                                                    .filter(e -> e.getId().equals(environment.getId()))
                                                    .findFirst()
                                                    .orElse(null);
            if (envInDB == null) {
                environment.setGroups(groups);
                environment.setParents();
                savesEnvironments.add(environment);
            } else {
                Set<Group> groupsInDB = envInDB.getGroups();
                environment.setParents();
                groupsInDB.retainAll(groups);
                if (groupsInDB.size() != 0) {
                    environment.setGroups(groupsInDB);
                    savesEnvironments.add(environment);
                }
            }
        }

        environmentRepository.saveAll(savesEnvironments);

    }

    public Environment getEnvironment(String id, Set<Group> groups) {

        List<Environment> environments = getEnvironments(groups);

        return environments.stream()
                            .filter(e -> e.getId().equals(id))
                            .findFirst()
                            .orElse(null);

    }

    public List<Environment> getEnvironments() {
        return environmentRepository.findAll();
    }

    public List<Environment> getEnvironments(Set<Group> groups) {
        return environmentRepository.findByGroupsIn(groups);
    }

}
