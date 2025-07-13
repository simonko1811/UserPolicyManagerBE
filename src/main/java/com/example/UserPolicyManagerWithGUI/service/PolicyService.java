package com.example.UserPolicyManagerWithGUI.service;

import com.example.UserPolicyManagerWithGUI.model.Policy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {
    private final List<Policy> policyStore = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final File file = new File("data/policies.json");

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (file.exists()) {
            try {
                List<Policy> policies = objectMapper.readValue(file, new TypeReference<>() {});
                policyStore.clear();
                policyStore.addAll(policies);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePolicies() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, policyStore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Policy> getAllPolicies() {
        return new ArrayList<>(policyStore);
    }

    public Optional<Policy> getPolicy(String id) {
        return policyStore.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public boolean addPolicy(Policy policy) {
        if (getPolicy(policy.getId()).isPresent()) return false;
        policyStore.add(policy);
        savePolicies();
        return true;
    }

    public boolean updatePolicy(String id, Policy updatedPolicy) {
        for (int i = 0; i < policyStore.size(); i++) {
            if (policyStore.get(i).getId().equals(id)) {
                policyStore.set(i, updatedPolicy);
                savePolicies();
                return true;
            }
        }
        return false;
    }

    public void deletePolicy(String id) {
        policyStore.removeIf(p -> p.getId().equals(id));
        savePolicies();
    }
}