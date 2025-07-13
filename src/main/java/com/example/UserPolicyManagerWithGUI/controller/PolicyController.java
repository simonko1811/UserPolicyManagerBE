package com.example.UserPolicyManagerWithGUI.controller;

import com.example.UserPolicyManagerWithGUI.model.Policy;
import com.example.UserPolicyManagerWithGUI.service.PolicyService;
import com.example.UserPolicyManagerWithGUI.service.UserService;
import com.example.UserPolicyManagerWithGUI.service.PolicyEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private UserService userService;

    @Autowired
    private PolicyEvaluator policyEvaluator;

    @GetMapping
    public List<Policy> getAllPolicies() {
        return policyService.getAllPolicies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getPolicy(@PathVariable String id) {
        Optional<Policy> policy = policyService.getPolicy(id);
        return policy.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addPolicy(@RequestBody Policy policy) {
        boolean added = policyService.addPolicy(policy);
        if (added) {
            userService.recomputePoliciesForAllUsers(policyService.getAllPolicies(), policyEvaluator);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Policy with this ID already exists");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePolicy(@PathVariable String id, @RequestBody Policy policy) {
        boolean updated = policyService.updatePolicy(id, policy);
        if (updated) {
            userService.recomputePoliciesForAllUsers(policyService.getAllPolicies(), policyEvaluator);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePolicy(@PathVariable String id) {
        policyService.deletePolicy(id);
        userService.recomputePoliciesForAllUsers(policyService.getAllPolicies(), policyEvaluator);
        return ResponseEntity.ok("Policy deleted");
    }
}
