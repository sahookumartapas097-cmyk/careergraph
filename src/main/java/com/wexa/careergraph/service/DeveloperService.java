package com.wexa.careergraph.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.springframework.stereotype.Service;

import com.wexa.careergraph.model.Developer;

@Service
public class DeveloperService {

    private final Driver driver;

    public DeveloperService(Driver driver) {
        this.driver = driver;
    }

    // =====================================================
    // CREATE DEVELOPER
    // =====================================================

    public Developer createDeveloper(Developer developer) {

        String cypher = """
                CREATE (d:Developer {
                    id: $id,
                    name: $name,
                    email: $email,
                    experience: $experience
                })
                RETURN d
                """;

        try (Session session = driver.session()) {

            var record = session.run(
                    cypher,
                    Map.of(
                            "id", safe(developer.getId()),
                            "name", safe(developer.getName()),
                            "email", safe(developer.getEmail()),
                            "experience", safe(developer.getExperience())
                    )
            ).single();

            return mapDeveloper(record.get("d").asNode());
        }
    }

    // =====================================================
    // GET ALL DEVELOPERS
    // =====================================================

    public List<Developer> getAllDevelopers() {

        String cypher = """
                MATCH (d:Developer)
                RETURN d
                ORDER BY d.name
                """;

        try (Session session = driver.session()) {

            var result = session.run(cypher);

            List<Developer> developers = new ArrayList<>();

            while (result.hasNext()) {

                var record = result.next();

                Node node = record.get("d").asNode();

                developers.add(mapDeveloper(node));
            }

            return developers;
        }
    }

    // =====================================================
    // GET DEVELOPER BY ID
    // =====================================================

    public Optional<Developer> getDeveloperById(String id) {

        String cypher = """
                MATCH (d:Developer {id: $id})
                RETURN d
                LIMIT 1
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("id", safe(id))
            );

            if (!result.hasNext()) {
                return Optional.empty();
            }

            var record = result.next();

            Node node = record.get("d").asNode();

            return Optional.of(mapDeveloper(node));
        }
    }

    // =====================================================
    // UPDATE DEVELOPER
    // =====================================================

    public Optional<Developer> updateDeveloper(
            String id,
            Developer developer) {

        String cypher = """
                MATCH (d:Developer {id: $id})
                SET d.name = $name,
                    d.email = $email,
                    d.experience = $experience
                RETURN d
                LIMIT 1
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "id", safe(id),
                            "name", safe(developer.getName()),
                            "email", safe(developer.getEmail()),
                            "experience", safe(developer.getExperience())
                    )
            );

            if (!result.hasNext()) {
                return Optional.empty();
            }

            var record = result.next();

            Node node = record.get("d").asNode();

            return Optional.of(mapDeveloper(node));
        }
    }

    // =====================================================
    // DELETE DEVELOPER
    // =====================================================

    public boolean deleteDeveloper(String id) {

        String cypher = """
                MATCH (d:Developer {id: $id})
                WITH collect(d) AS nodes
                FOREACH (node IN nodes |
                    DETACH DELETE node
                )
                RETURN size(nodes) AS deleted
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("id", safe(id))
            );

            if (!result.hasNext()) {
                return false;
            }

            var record = result.next();

            return record.get("deleted").asLong() > 0;
        }
    }

    // =====================================================
    // MAP NEO4J NODE -> DEVELOPER
    // =====================================================

    private Developer mapDeveloper(Node node) {

        String id = getString(node, "id");
        String name = getString(node, "name");
        String email = getString(node, "email");
        String experience = getString(node, "experience");

        return new Developer(
                id,
                name,
                email,
                experience
        );
    }

    // =====================================================
    // SAFE NODE PROPERTY READER
    // =====================================================

    private String getString(Node node, String property) {

        if (!node.containsKey(property)) {
            return "";
        }

        return node.get(property).asString("");
    }

    // =====================================================
    // SAFE REQUEST VALUE
    // =====================================================

    private String safe(String value) {

        return value == null ? "" : value;
    }
}