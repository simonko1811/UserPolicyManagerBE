package com.example.UserPolicyManagerWithGUI.controller;

import com.example.UserPolicyManagerWithGUI.model.Policy;
import com.example.UserPolicyManagerWithGUI.model.User;
import com.example.UserPolicyManagerWithGUI.service.PolicyEvaluator;
import com.example.UserPolicyManagerWithGUI.service.PolicyService;
import com.example.UserPolicyManagerWithGUI.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PolicyService policyService;
    private final PolicyEvaluator policyEvaluator;

    public UserController(UserService userService, PolicyService policyService, PolicyEvaluator policyEvaluator) {
        this.userService = userService;
        this.policyService = policyService;
        this.policyEvaluator = policyEvaluator;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<Policy> allPolicies = policyService.getAllPolicies();
        for (User user : users) {
            policyEvaluator.applyPolicies(user, allPolicies);
        }
        return users;
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> getUser(@PathVariable String name) {
        return userService.getUser(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        boolean created = userService.addUser(user);
        return created ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<User> updateUser(@PathVariable String name, @RequestBody User updatedUser) {
        boolean updated = userService.updateUser(name, updatedUser);
        return updated ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteUser(@PathVariable String name) {
        userService.deleteUser(name);
        return ResponseEntity.noContent().build();
    }
}