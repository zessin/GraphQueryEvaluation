package com.zessin.util;

public enum GraphQueryLanguage {

    CYPHER("Cypher"),
    GREMLIN("Gremlin");

    private final String language;

    GraphQueryLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return language;
    }

}
