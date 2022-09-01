package com.zhevakin.service_for_requester.models.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Group> groups;

    public User() { }

    public User(String username, String password, Set<Role> roles, Set<Group> groups) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.groups = groups;
    }

}
