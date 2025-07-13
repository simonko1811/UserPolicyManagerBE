package com.example.UserPolicyManagerWithGUI.service;

import com.example.UserPolicyManagerWithGUI.model.User;
import com.example.UserPolicyManagerWithGUI.model.Policy;
import com.example.UserPolicyManagerWithGUI.service.PolicyEvaluator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {
    private final List<User> userStore = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final File file = new File("data/users.json");
    private final PolicyEvaluator policyEvaluator;
    private final PolicyService policyService;

    public UserService(PolicyEvaluator policyEvaluator, PolicyService policyService) {
        this.policyEvaluator = policyEvaluator;
        this.policyService = policyService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (file.exists()) {
            try {
                List<User> users = objectMapper.readValue(file, new TypeReference<>() {});
                userStore.clear();
                userStore.addAll(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUsers() {
        try {
            List<User> usersToSave = userStore.stream()
                    .map(this::stripTransientFields)
                    .toList();

            //objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, userStore);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, usersToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userStore);
    }

    public Optional<User> getUser(String name) {
        return userStore.stream().filter(u -> u.getName().equals(name)).findFirst();
    }

    public boolean addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            String generated = generateUserId(user.getFirstName(), user.getLastName());
            user.setName(generated);
        }
        policyEvaluator.applyPolicies(user, policyService.getAllPolicies());
        userStore.add(user);
        saveUsers();
        return true;
    }

    public boolean updateUser(String name, User updatedUser) {
        for (int i = 0; i < userStore.size(); i++) {
            if (userStore.get(i).getName().equals(name)) {
                policyEvaluator.applyPolicies(updatedUser, policyService.getAllPolicies());
                userStore.set(i, updatedUser);
                saveUsers();
                return true;
            }
        }
        return false;
    }

    public void deleteUser(String name) {
        userStore.removeIf(u -> u.getName().equals(name));
        saveUsers();
    }

    public void recomputePoliciesForAllUsers(List<Policy> policies, PolicyEvaluator evaluator) {
        for (User user : userStore) {
            evaluator.applyPolicies(user, policies);
        }
        saveUsers();
    }

    private User stripTransientFields(User user) {
        User copy = new User();
        copy.setName(user.getName());
        copy.setFirstName(user.getFirstName());
        copy.setLastName(user.getLastName());
        copy.setEmailAddress(user.getEmailAddress());
        copy.setOrganizationUnit(user.getOrganizationUnit());
        copy.setBirthDate(user.getBirthDate());
        copy.setRegisteredOn(user.getRegisteredOn());
        copy.setPolicies(user.getPolicies());
        // DO NOT COPY policyObjects
        return copy;
    }

    private String generateUserId(String firstName, String lastName) {
        if (firstName == null || firstName.isEmpty() || lastName == null) return "";
        return (firstName.charAt(0) + lastName).toLowerCase();
    }
}