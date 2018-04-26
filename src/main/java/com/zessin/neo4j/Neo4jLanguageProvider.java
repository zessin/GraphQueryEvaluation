package com.zessin.neo4j;

import java.text.SimpleDateFormat;

public interface Neo4jLanguageProvider extends AutoCloseable {

    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    void deleteAllDatabaseEdgesAndVertices() throws Exception;

    void addNewVerticesAndEdges() throws Exception;

}
