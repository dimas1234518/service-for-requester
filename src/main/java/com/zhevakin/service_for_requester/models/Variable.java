package com.zhevakin.service_for_requester.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "variables")
public class Variable implements Serializable{

    private String name;
    private String value;

    @Id
    private String id;

    private int rowId;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    private Environment environment;

    public Variable(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Variable() {
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Variable(String id) {
        this.id = id;
    }

    @Override
    public String toString() { return name; }
}
