package org.chpir.android.roster.models;

public class Center {
    private String identifier;
    private String name;

    public Center(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }
}