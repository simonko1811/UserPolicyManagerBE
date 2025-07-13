package com.example.UserPolicyManagerWithGUI.model.rules;

import com.example.UserPolicyManagerWithGUI.model.User;

public class EmailDomainRule implements PolicyRule {
    private final String id;
    private final String domain;

    public EmailDomainRule(String id, String domain) {
        this.id = id;
        this.domain = domain;
    }

    @Override
    public boolean appliesTo(User user) {
        return user.getEmailAddress().endsWith("@" + domain);
    }

    @Override
    public String getId() {
        return id;
    }
}