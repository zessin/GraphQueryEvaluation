package com.zessin.neo4j;

import java.util.Date;
import java.util.Iterator;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import com.steelbridgelabs.oss.neo4j.structure.Neo4JElementIdProvider;
import com.steelbridgelabs.oss.neo4j.structure.Neo4JGraph;
import com.steelbridgelabs.oss.neo4j.structure.providers.Neo4JNativeElementIdProvider;
import com.zessin.util.PropertiesHelper;

public class Neo4jGremlinProvider implements Neo4jLanguageProvider {

    private final Driver driver;
    private final Neo4JElementIdProvider<?> vertexIdProvider = new Neo4JNativeElementIdProvider();
    private final Neo4JElementIdProvider<?> edgeIdProvider = new Neo4JNativeElementIdProvider();

    public Neo4jGremlinProvider() {
        driver = GraphDatabase.driver(PropertiesHelper.getDatabaseUri(), AuthTokens.basic(PropertiesHelper.getDatabaseUsername(), PropertiesHelper.getDatabasePassword()));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    @Override
    public void deleteAllDatabaseEdgesAndVertices() throws Exception {
        try (Graph graph = new Neo4JGraph(driver, vertexIdProvider, edgeIdProvider)) {
            try (org.apache.tinkerpop.gremlin.structure.Transaction transaction = graph.tx()) {
                Iterator<Edge> edges = graph.edges();
                Iterator<Vertex> vertices = graph.vertices();

                while (edges.hasNext()) {
                    edges.next().remove();
                }

                while (vertices.hasNext()) {
                    vertices.next().remove();
                }

                transaction.commit();
            }
        }
    }

    @Override
    public void addNewVerticesAndEdges() throws Exception {
        try (Graph graph = new Neo4JGraph(driver, vertexIdProvider, edgeIdProvider)) {
            try (org.apache.tinkerpop.gremlin.structure.Transaction transaction = graph.tx()) {
                Vertex lastVertex = null;

                for (int i = 1; i <= PropertiesHelper.getNumberOfVertices(); i++) {
                    Vertex vertex = graph.addVertex(T.label, "person", "name", "Person Number " + i);

                    if (lastVertex != null) {
                        vertex.addEdge("knows", lastVertex, "since", sdf.format(new Date()));
                    }

                    lastVertex = vertex;
                }

                transaction.commit();
            }
        }
    }

}
