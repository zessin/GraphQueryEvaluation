package com.zessin.main;

import com.zessin.neo4j.Neo4jCypherConnector;
import com.zessin.util.ApplicationLogger;

/**
 * The Main class of the application. Responsible for initializing all the process.
 *
 * @author zessin
 */
public class Application {

    public static void main(String[] args) throws Exception {
        try(Neo4jCypherConnector neo4jCypherConnector = new Neo4jCypherConnector()) {
            System.out.println("Starting the application...");
            ApplicationLogger.info("Application started");

            neo4jCypherConnector.printGreeting("Hello!");
            neo4jCypherConnector.deleteAllDatabaseNodes();

            System.out.println(String.format("Application ended. Check %s file for detailed information of the execution.", ApplicationLogger.LOG_FILE_NAME));
            ApplicationLogger.info("Application ended. Check above lines for detailed information.");
            System.exit(0);
        } catch (final IllegalStateException ex) {
            System.out.println(String.format("Failure! Check %s file for detailed information of the execution.", ApplicationLogger.LOG_FILE_NAME));
            ApplicationLogger.error("Application ended with errors. Check above lines for detailed information.");
            System.exit(1);
        }
    }

}
