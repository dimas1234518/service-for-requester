package com.zhevakin.service_for_requester.controllers;

import com.zhevakin.service_for_requester.models.RequestInfo;
import com.zhevakin.service_for_requester.models.domain.Group;
import com.zhevakin.service_for_requester.models.domain.Role;
import com.zhevakin.service_for_requester.models.domain.User;
import com.zhevakin.service_for_requester.services.RequestService;
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
@RequestMapping(value = "api/requests")
public class RequestsController {

    GroupService groupService;

    UserService userService;

    RequestService requestService;

    @Autowired
    public RequestsController(GroupService groupService, UserService userService, RequestService requestService) {
        this.groupService = groupService;
        this.userService = userService;
        this.requestService = requestService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<RequestInfo>> getRequests(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        // если пользователь имеет Denied - то не пускаем вообще
        if (user.getRoles().contains(Role.DENIED)) return ResponseEntity.status(401).body(null);

        Set<Role> acceptedRole = new HashSet<>();
        acceptedRole.add(Role.VIEWER);
        acceptedRole.add(Role.ADMIN);
        acceptedRole.add(Role.EDITOR);

        Set<Group> groups = user.getGroups();

        groups = groupService.getGroups(groups, acceptedRole);
        List<RequestInfo> requests = requestService.getRequests(groups);

        return ResponseEntity.ok(requests);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<RequestInfo>> getRequest(@PathVariable("id")  String id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        // если пользователь имеет Denied - то не пускаем вообще
        if (user.getRoles().contains(Role.DENIED)) return ResponseEntity.status(401).body(null);

        Set<Role> acceptedRole = new HashSet<>();
        acceptedRole.add(Role.VIEWER);
        acceptedRole.add(Role.ADMIN);
        acceptedRole.add(Role.EDITOR);

        Set<Group> groups = user.getGroups();

        groups = groupService.getGroups(groups, acceptedRole);
        List<RequestInfo> requests = requestService.getRequest(id,groups);
        if (requests == null) ResponseEntity.notFound();
        return ResponseEntity.ok(requests);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<List<RequestInfo>> saveRequests(@RequestBody List<RequestInfo> requests, Principal principal) {
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

        requestService.saveRequests(requests, groups);

        return ResponseEntity.ok(requests);
    }

}
