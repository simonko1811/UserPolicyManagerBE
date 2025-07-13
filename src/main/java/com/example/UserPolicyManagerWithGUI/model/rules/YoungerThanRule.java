package com.example.UserPolicyManagerWithGUI.model.rules;

import com.example.UserPolicyManagerWithGUI.model.User;
import java.time.LocalDate;
import java.time.Period;

public class YoungerThanRule implements PolicyRule {
    private final String id;
    private final int age;

    public YoungerThanRule(String id, int age) {
        this.id = id;
        this.age = age;
    }

    @Override
    public boolean appliesTo(User user) {
        return Period.between(user.getBirthDate(), LocalDate.now()).getYears() < age;
    }

    @Override
    public String getId() {
        return id;
    }
}