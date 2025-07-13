package com.example.UserPolicyManagerWithGUI.model;

public class Policy {
    private String id;
    private String name;
    private Condition condition;

    public static class Condition {
        private String rule;
        private String value;

        public Condition() {}

        public Condition(String rule, String value) {
            this.rule = rule;
            this.value = value;
        }

        public String getRule() {return rule;}

        public void setRule(String rule) {this.rule = rule;}

        public String getValue() {return value;}

        public void setValue(String value) {this.value = value;}
    }

    public Policy() {}

    public Policy(String id, String name, Condition condition) {
        this.id = id;
        this.name = name;
        this.condition = condition;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Condition getCondition() {return condition;}

    public void setCondition(Condition condition) {this.condition = condition;}
}
