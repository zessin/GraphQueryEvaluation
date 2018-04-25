package com.zessin.neo4j;

import static org.neo4j.driver.v1.Values.parameters;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import com.zessin.util.PropertiesHelper;

public class Neo4jCypherConnector implements AutoCloseable {

    private final Driver driver;

    public Neo4jCypherConnector() {
        driver = GraphDatabase.driver(PropertiesHelper.getDatabaseUri(), AuthTokens.basic(PropertiesHelper.getDatabaseUsername(), PropertiesHelper.getDatabasePassword()));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void deleteAllDatabaseNodes() {
        try (Session session = driver.session()) {
            session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    tx.run("MATCH (x) " +
                           "DELETE x  ");

                    return null;
                }
            });
        }
    }

    public void printGreeting(final String message) {
        try (Session session = driver.session()) {

            String greeting = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    StatementResult result = tx.run("CREATE (a:Greeting) " +
                                                    "SET     a.message = $message " +
                                                    "RETURN  a.message + ', from node ' + id(a)",
                                                    parameters("message", message));
                    return result.single().get(0).asString();
                }
            });

            System.out.println(greeting);
        }
    }

}
