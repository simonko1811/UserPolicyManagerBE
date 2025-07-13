package com.example.UserPolicyManagerWithGUI.service;

import com.example.UserPolicyManagerWithGUI.model.Policy;
import com.example.UserPolicyManagerWithGUI.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PolicyEvaluator {

    public void applyPolicies(User user, List<Policy> policies) {
        Set<String> matchedIds = new HashSet<>();
        //System.out.println("Checking policies for user: " + user.getName());

        for (Policy p : policies) {
            //System.out.println("Evaluating policy: " + p.getId() + " with rule: " + p.getCondition().getRule());
            Integer ageLimit = null;
            try {
                ageLimit = Integer.parseInt(p.getCondition().getValue());
            } catch (NumberFormatException ignored) {}

            if ("youngerThan".equals(p.getCondition().getRule()) &&
                    ageLimit != null &&
                    user.getBirthDate() != null &&
                    user.getBirthDate().plusYears(ageLimit).isAfter(LocalDate.now())) {
                matchedIds.add(p.getId());
            }

            if ("emailDomainIs".equals(p.getCondition().getRule()) &&
                    user.getEmailAddress() != null &&
                    user.getEmailAddress().endsWith("@" + p.getCondition().getValue())) {
                matchedIds.add(p.getId());
            }

            if ("isMemberOf".equals(p.getCondition().getRule()) &&
                    user.getOrganizationUnit() != null &&
                    user.getOrganizationUnit().contains(p.getCondition().getValue())) {
                matchedIds.add(p.getId());
            }
        }

        user.setPolicies(new ArrayList<>(matchedIds));

        List<Policy> matchedPolicies = policies.stream()
                .filter(p -> matchedIds.contains(p.getId()))
                .toList();

        user.setPolicyObjects(matchedPolicies);
    }
}
