package com.zhevakin.service_for_requester.services.domain;

import com.zhevakin.service_for_requester.models.domain.Group;
import com.zhevakin.service_for_requester.models.domain.Role;
import com.zhevakin.service_for_requester.models.domain.User;
import com.zhevakin.service_for_requester.repositories.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changeRoles(User user, Set<Role> roles) {
        user.setRoles(roles);
        userRepository.save(user);
    }

    public User getByLogin(String username) {
        return findByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void changeGroups(User user, Set<Group> groups) {
        user.setGroups(groups);
        userRepository.save(user);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User '%s' not found", username);
//        }
//        return new User
//    }
//
//    private Collection<? extends GrantedAuthority>
}
