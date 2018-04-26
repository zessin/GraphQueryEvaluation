package com.zessin.main;

import static com.zessin.util.GraphQueryLanguage.CYPHER;
import static com.zessin.util.GraphQueryLanguage.GREMLIN;

import com.zessin.neo4j.Neo4jCypherProvider;
import com.zessin.neo4j.Neo4jGremlinProvider;
import com.zessin.neo4j.Neo4jLanguageProvider;
import com.zessin.util.ApplicationLogger;
import com.zessin.util.GraphQueryLanguage;
import com.zessin.util.PropertiesHelper;

/**
 * The Main class of the application. Responsible for initializing all the process.
 *
 * @author zessin
 */
public class Application {

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("Starting the application...");
            ApplicationLogger.info("Application started");

            performDatabaseOperations(CYPHER);
            performDatabaseOperations(GREMLIN);

            System.out.println(String.format("Application ended successfully. Check %s file for detailed information of the execution.", ApplicationLogger.LOG_FILE_NAME));
            ApplicationLogger.info("Application ended. Check above lines for detailed information.");

            System.exit(0);
        } catch (final IllegalStateException ex) {
            System.out.println(String.format("Failure! Check %s file for detailed information of the execution.", ApplicationLogger.LOG_FILE_NAME));
            ApplicationLogger.error("Application ended with errors. Check above lines for detailed information.");

            System.exit(1);
        }
    }

    private static void performDatabaseOperations(GraphQueryLanguage language) throws Exception {
        long startTime;
        double estimatedTime;
        int numberOfVertices = PropertiesHelper.getNumberOfVertices();

        try(Neo4jLanguageProvider neo4jLanguageProvider = CYPHER.equals(language) ? new Neo4jCypherProvider() : new Neo4jGremlinProvider()) {
            ApplicationLogger.info("Performing database operations using " + language + " Query Language.");

            ApplicationLogger.info("Deleting all edges and vertices.");
            startTime = System.nanoTime();
            neo4jLanguageProvider.deleteAllDatabaseEdgesAndVertices();
            estimatedTime = (System.nanoTime() - startTime) / 1e9;
            ApplicationLogger.info("Time elapsed for deletion: " + estimatedTime + " seconds.");

            ApplicationLogger.info("Creating " + numberOfVertices + " vertices and " + (numberOfVertices - 1) + " edges.");
            startTime = System.nanoTime();
            neo4jLanguageProvider.addNewVerticesAndEdges();
            estimatedTime = (System.nanoTime() - startTime) / 1e9;
            ApplicationLogger.info("Time elapsed for creation: " + estimatedTime + " seconds.");
        }
    }

}
