package com.zhevakin.service_for_requester.services;

import com.zhevakin.service_for_requester.enums.TypeRequest;
import com.zhevakin.service_for_requester.models.RequestInfo;
import com.zhevakin.service_for_requester.models.domain.Group;
import com.zhevakin.service_for_requester.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public void saveRequests(List<RequestInfo> requests, Set<Group> groups) {

        // Получить коллекцию, у коллекции вытащить группы и присвоить ее всем остальным запросам
        // Если ранее не было коллекции, то взять группы, к которым принадлежит пользователь, принадлежность определять по коллекции

        List<RequestInfo> requestsInDB = getRequests();
        List<RequestInfo> savesRequests = new ArrayList<>();

        for (RequestInfo requestInfo : requests) {
            RequestInfo reqInDB = requestsInDB.stream()
                    .filter(e -> e.getId().equals(requestInfo.getId()))
                    .findFirst()
                    .orElse(null);
            if (reqInDB == null) {
                requestInfo.setGroups(groups);
                savesRequests.add(requestInfo);
            } else {
                Set<Group> groupsInDB = reqInDB.getGroups();
                groupsInDB.retainAll(groups);
                if (groupsInDB.size() != 0) {
                    requestInfo.setGroups(groupsInDB);
                    savesRequests.add(requestInfo);
                }
            }
        }

        requestRepository.saveAll(savesRequests);
    }

    public List<RequestInfo> getRequests() {
        return requestRepository.findAll();
    }

    public List<RequestInfo> getRequests(Set<Group> groups) {
        return requestRepository.findByGroupsIn(groups);
    }

    public List<RequestInfo> getRequest(String id, Set<Group> groups) {
        List<RequestInfo> requests = getRequests(groups);
        RequestInfo collection = requests.stream()
                                            .filter(r -> r.getId().equals(id))
                                            .findFirst()
                                            .orElse(null);
        if (collection == null) return null;
        else if (collection.getTypeRequest() != TypeRequest.COLLECTIONS) return null;
        return parseRequests(collection, requests);
    }

    private List<RequestInfo> parseRequests(RequestInfo root, List<RequestInfo> allRequests) {
        List<RequestInfo> requests = new ArrayList<>();
        requests.add(root);
        for (RequestInfo request : allRequests) {
            if (request.getId().equals(root.getId())) continue;
            if (findParent(root, request, allRequests)) requests.add(request);
        }
        return requests;
    }

    private boolean findParent(RequestInfo root, RequestInfo request, List<RequestInfo> allRequests) {
        RequestInfo parentRequest = allRequests.stream()
                                                .filter(r -> r.getId().equals(request.getParent()))
                                                .findFirst()
                                                .orElse(null);
        if (parentRequest == null) return false;
        if (parentRequest.getTypeRequest() == TypeRequest.COLLECTIONS) {
            return root.getId().equals(parentRequest.getId());
        } else {
            return findParent(root, parentRequest, allRequests);
        }
    }
}
