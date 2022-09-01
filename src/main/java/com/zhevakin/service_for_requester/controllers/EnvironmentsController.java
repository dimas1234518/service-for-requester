package com.zhevakin.service_for_requester.controllers;

import com.zhevakin.service_for_requester.models.domain.Group;
import com.zhevakin.service_for_requester.models.domain.Role;
import com.zhevakin.service_for_requester.models.domain.User;
import com.zhevakin.service_for_requester.models.Environment;
import com.zhevakin.service_for_requester.services.EnvironmentService;
import com.zhevakin.service_for_requester.services.domain.GroupService;
import com.zhevakin.service_for_requester.services.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Validated
@RequestMapping(value = "api/environments")
public class EnvironmentsController {

    // TODO: доработать роли и привелегии

    private final UserService userService;

    private final GroupService groupService;

    private final EnvironmentService environmentService;

    @Autowired
    public EnvironmentsController(UserService userService, GroupService groupService, EnvironmentService environmentService) {
        this.userService = userService;
        this.groupService = groupService;
        this.environmentService = environmentService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Environment>> getEnvironments(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        // если пользователь имеет Denied - то не пускаем вообще
        if (user.getRoles().contains(Role.DENIED)) return ResponseEntity.status(401).body(null);

        Set<Role> acceptedRole = new HashSet<>();
        acceptedRole.add(Role.VIEWER);
        acceptedRole.add(Role.ADMIN);
        acceptedRole.add(Role.EDITOR);

        Set<Group> groups = user.getGroups();

        groups = groupService.getGroups(groups, acceptedRole);
        List<Environment> environments = environmentService.getEnvironments(groups);


        return ResponseEntity.ok(environments);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Environment> getEnvironment(@PathVariable("id") String id, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        // если пользователь имеет Denied - то не пускаем вообще
        if (user.getRoles().contains(Role.DENIED)) return ResponseEntity.status(401).body(null);

        Set<Role> acceptedRole = new HashSet<>();
        acceptedRole.add(Role.VIEWER);
        acceptedRole.add(Role.ADMIN);
        acceptedRole.add(Role.EDITOR);

        Set<Group> groups = user.getGroups();

        groups = groupService.getGroups(groups, acceptedRole);
        Environment environment = environmentService.getEnvironment(id,groups);
        if (environment == null) ResponseEntity.notFound();
        return ResponseEntity.ok(environment);
    }


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<List<Environment>> saveEnvironments(@RequestBody List<Environment> environments, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        // если пользователь имеет Denied - то не пускаем вообще
        if (user.getRoles().contains(Role.DENIED)) return ResponseEntity.status(401).body(null);
        Set<Role> acceptedRole = new HashSet<>();
        acceptedRole.add(Role.ADMIN);
        acceptedRole.add(Role.EDITOR);

        Set<Group> groups = user.getGroups();
        groups = groupService.getGroups(groups, acceptedRole);
        if (!user.getRoles().containsAll(acceptedRole)) {
            if (groups.size() == 0) return ResponseEntity.status(401).body(null);
        }

        environmentService.saveEnvironments(environments, groups);

        return ResponseEntity.ok(environments);
    }

}
