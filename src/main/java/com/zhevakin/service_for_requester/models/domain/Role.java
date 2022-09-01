package com.zhevakin.service_for_requester.models.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    public static final Role VIEWER  = new Role(1L, "ROLE_VIEWER");
    public static final Role ADMIN   = new Role(2L, "ROLE_ADMIN");
    public static final Role EDITOR  = new Role(3L, "ROLE_EDITOR");
    public static final Role DENIED  = new Role(4L, "ROLE_DENIED"); // Доступ запрещен
    public static final Role NO_ROLE = new Role(5L, "ROLE_NO"); // Нет ролей, необходимо , чтобы управление было за счет групповых политик


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<Group> groups;


    @Override
    public String getAuthority() {
        return null;
    }

    public Role() {

    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
