package com.zhevakin.service_for_requester.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhevakin.service_for_requester.models.domain.Group;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "environments")
public class Environment implements Serializable {

    private String name;

    @OneToMany( mappedBy = "environment",
            cascade = CascadeType.ALL,
                orphanRemoval = true)
    private List<Variable> variables;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Group> groups;

    @Id
    private String id;
    private static final String beginVariable = "\\{";
    private static final String endVariable = "\\}";

    public Environment(String name, List<Variable> variables) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.variables = variables;
    }

    public Environment(String id, String name, List<Variable> variables) {
        this.id = id;
        this.name = name;
        this.variables = variables;
    }

    public Environment() { }

    public Environment(String id) {
        this.id = id;
    }

    public String getId() { return id;}

    public void updateName (String name) { this.name = name; }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public void addVariable(Variable... variables) {
        for (Variable variable : variables) {
            variable.setEnvironment(this);
            this.variables.add(variable);
        }
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public void addVariable(Variable variable, int index) { variables.add(index, variable); }

    public List<Variable> getVariables() { return variables; }

    @Override
    public String toString() { return name; }

    public void setParents() {

        for (Variable variable : this.getVariables()) {
            variable.setEnvironment(this);
        }

    }
}
