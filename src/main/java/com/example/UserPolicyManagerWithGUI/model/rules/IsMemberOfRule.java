package com.example.UserPolicyManagerWithGUI.model.rules;

import com.example.UserPolicyManagerWithGUI.model.User;

public class IsMemberOfRule implements PolicyRule {
    private final String id;
    private final String group;

    public IsMemberOfRule(String id, String group) {
        this.id = id;
        this.group = group;
    }

    @Override
    public boolean appliesTo(User user) {
        return user.getOrganizationUnit().contains(group);
    }

    @Override
    public String getId() {
        return id;
    }
}
