package com.zessin.neo4j;

import static org.neo4j.driver.v1.Values.parameters;

import java.util.Date;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import com.zessin.util.PropertiesHelper;

public class Neo4jCypherProvider implements Neo4jLanguageProvider {

    private final Driver driver;

    public Neo4jCypherProvider() {
        driver = GraphDatabase.driver(PropertiesHelper.getDatabaseUri(), AuthTokens.basic(PropertiesHelper.getDatabaseUsername(), PropertiesHelper.getDatabasePassword()));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    @Override
    public void deleteAllDatabaseEdgesAndVertices() throws Exception {
        try (Session session = driver.session()) {
            session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    tx.run("MATCH (x)-[r]->(y) " +
                           "DELETE r");

                    tx.run("MATCH (x) " +
                           "DELETE x");

                    return null;
                }
            });
        }
    }

    @Override
    public void addNewVerticesAndEdges() throws Exception {
        try (Session session = driver.session()) {
            session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    tx.run("CREATE (p:person) " +
                           "SET     p.name = $personName ",
                           parameters("personName", "Person Number " + 1));

                    for (int i = 2; i <= PropertiesHelper.getNumberOfVertices(); i++) {
                        tx.run("MATCH  (a:person { name: $friendName }) " +
                               "CREATE (b:person { name: $newPersonName }) " +
                               "CREATE (b)-[:knows { since: \"" + sdf.format(new Date()) + "\" }]->(a)",
                               parameters("friendName", "Person Number " + (i - 1), "newPersonName", "Person Number " + i));
                    }

                    return null;
                }
            });
        }
    }

}
