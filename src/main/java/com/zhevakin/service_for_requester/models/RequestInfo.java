package com.zhevakin.service_for_requester.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhevakin.service_for_requester.models.domain.Group;
import com.zhevakin.service_for_requester.enums.TextMode;
import com.zhevakin.service_for_requester.enums.TypeAuthorization;
import com.zhevakin.service_for_requester.enums.TypeRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "requests")
public class RequestInfo{

    @Id
    private String id;
    private HttpMethod requestMethod;
    private String name;
    private TypeRequest typeRequest;
    private String request;
    private String parent;
    //private final String fullRequest;
    private TypeAuthorization typeAuthorization;
    @Column(length = 2048)
    private String authToken;
    private String body;
    private TextMode typeBody;
    private TextMode typeResponseBody;
    private Date updateDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Group> groups;

    public RequestInfo() {

    }

    public RequestInfo(String id){
        this.id = id;
    }

    public boolean updateParent(String parent) {
        this.parent = parent;
        return true;
    }

    @Override
    public String toString() { return name;}

}
