package com.zhevakin.service_for_requester.services.domain;

import com.zhevakin.service_for_requester.models.domain.Group;
import com.zhevakin.service_for_requester.models.domain.Role;
import com.zhevakin.service_for_requester.repositories.domain.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void changeRoles(Group group, Set<Role> roles) {
        group.setRoles(roles);
        groupRepository.save(group);
    }

    public Group findByName(String name) {
        Group groupInDb = groupRepository.findByName(name);
        if (groupInDb == null) {
            Group group = new Group();
            group.setName(name);
            groupRepository.save(group);
            groupInDb = group;
        }
        return groupInDb;
    }


    public Set<Group> getGroups(Set<Group> groups, Set<Role> acceptedRoles) {


        Map<Long, Role> acceptedMap = acceptedRoles.stream().collect(Collectors.toMap(Role::getId, role -> role));

        Set<Group> acceptedGroups = new HashSet<>();

        for (Group group : groups) {
            Set<Role> roles = group.getRoles();
            Map<Long, Role> groupMap = roles.stream().collect(Collectors.toMap(Role::getId, role -> role));
            Map<Long, Role> result = new HashMap<>(groupMap);
            if (result.containsKey(Role.DENIED.getId())) continue;
            result.keySet().retainAll(acceptedMap.keySet());
            if (result.size() != 0) acceptedGroups.add(group);
        }
        return acceptedGroups;

    }
}
