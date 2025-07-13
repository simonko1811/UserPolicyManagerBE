package com.example.UserPolicyManagerWithGUI.model.rules;

import com.example.UserPolicyManagerWithGUI.model.User;

public interface PolicyRule {
    boolean appliesTo(User user);
    String getId();
}