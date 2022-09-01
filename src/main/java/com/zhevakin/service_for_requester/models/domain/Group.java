package com.zhevakin.service_for_requester.models.domain;

import com.zhevakin.service_for_requester.models.Environment;
import com.zhevakin.service_for_requester.models.RequestInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class Group {

    public final static Group ALL_GROUPS = new Group(1L, "ALL_GROUPS");
    public final static Group NO_GROUP = new Group(2L, "NO_GROUP");
    public final static Group ADMINS = new Group(3L, "ADMINS");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

    @Transient
    @ManyToMany(mappedBy = "groups")
    private Set<User> users;

    @Transient
    @ManyToMany(mappedBy = "groups")
    private Set<Environment> environments;

    @Transient
    @ManyToMany(mappedBy = "groups")
    private Set<RequestInfo> requests;

    public Group() {
    }

    public Group(String name, Set<Role> roles) {
        this.name = name;
        this.roles = roles;
    }

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
