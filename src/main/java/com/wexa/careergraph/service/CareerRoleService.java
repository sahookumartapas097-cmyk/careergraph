package com.wexa.careergraph.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

import com.wexa.careergraph.model.CareerRole;

@Service
public class CareerRoleService {

    private final Driver driver;

    public CareerRoleService(Driver driver) {
        this.driver = driver;
    }

    // =====================================================
    // CREATE CAREER ROLE
    // =====================================================

    public CareerRole createCareerRole(CareerRole role) {

        String cypher = """
                CREATE (r:CareerRole {
                    id: $id,
                    title: $title,
                    level: $level
                })
                RETURN r
                """;

        try (Session session = driver.session()) {

            var record = session.run(
                    cypher,
                    Map.of(
                            "id", role.getId(),
                            "title", role.getTitle(),
                            "level", role.getLevel()
                    )
            ).single();

            var node = record.get("r").asNode();

            return new CareerRole(
                    node.get("id").asString(),
                    node.get("title").asString(),
                    node.get("level").asString()
            );
        }
    }

    // =====================================================
    // GET ALL CAREER ROLES
    // =====================================================

    public List<CareerRole> getAllCareerRoles() {

        String cypher = """
                MATCH (r:CareerRole)
                RETURN r
                ORDER BY r.title
                """;

        try (Session session = driver.session()) {

            var result = session.run(cypher);

            List<CareerRole> roles = new ArrayList<>();

            while (result.hasNext()) {

                var record = result.next();
                var node = record.get("r").asNode();

                roles.add(
                        new CareerRole(
                                node.get("id").asString(),
                                node.get("title").asString(),
                                node.get("level").asString()
                        )
                );
            }

            return roles;
        }
    }

    // =====================================================
    // GET CAREER ROLE BY ID
    // =====================================================

    public Optional<CareerRole> getCareerRoleById(String id) {

        String cypher = """
                MATCH (r:CareerRole {id: $id})
                RETURN r
                LIMIT 1
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("id", id)
            );

            // IMPORTANT:
            // Use next() instead of single()
            // because duplicate ROLE IDs currently exist.

            if (!result.hasNext()) {
                return Optional.empty();
            }

            var record = result.next();

            var node = record.get("r").asNode();

            CareerRole role = new CareerRole(
                    node.get("id").asString(),
                    node.get("title").asString(),
                    node.get("level").asString()
            );

            return Optional.of(role);
        }
    }

    // =====================================================
    // UPDATE CAREER ROLE
    // =====================================================

    public Optional<CareerRole> updateCareerRole(
            String id,
            CareerRole role) {

        String cypher = """
                MATCH (r:CareerRole {id: $id})
                SET r.title = $title,
                    r.level = $level
                RETURN r
                LIMIT 1
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "id", id,
                            "title", role.getTitle(),
                            "level", role.getLevel()
                    )
            );

            if (!result.hasNext()) {
                return Optional.empty();
            }

            var record = result.next();

            var node = record.get("r").asNode();

            CareerRole updatedRole = new CareerRole(
                    node.get("id").asString(),
                    node.get("title").asString(),
                    node.get("level").asString()
            );

            return Optional.of(updatedRole);
        }
    }

    // =====================================================
    // DELETE CAREER ROLE
    // =====================================================

    public boolean deleteCareerRole(String id) {

        String cypher = """
                MATCH (r:CareerRole {id: $id})
                WITH collect(r) AS nodes
                FOREACH (node IN nodes | DETACH DELETE node)
                RETURN size(nodes) AS deleted
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("id", id)
            );

            if (!result.hasNext()) {
                return false;
            }

            var record = result.next();

            return record.get("deleted").asLong() > 0;
        }
    }
}