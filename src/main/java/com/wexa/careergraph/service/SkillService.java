package com.wexa.careergraph.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

import com.wexa.careergraph.model.Skill;

@Service
public class SkillService {

    private final Driver driver;

    public SkillService(Driver driver) {
        this.driver = driver;
    }

    // =====================================================
    // CREATE skill
    // =====================================================
    public Skill createSkill(Skill skill) {

        String cypher = """
                CREATE (s:Skill {
                    id: $id,
                    name: $name,
                    category: $category
                })
                RETURN s
                """;

        try (Session session = driver.session()) {

            var record = session.run(
                    cypher,
                    Map.of(
                            "id", skill.getId(),
                            "name", skill.getName(),
                            "category", skill.getCategory()
                    )
            ).single();

            var node = record.get("s").asNode();

            return new Skill(
                    node.get("id").asString(),
                    node.get("name").asString(),
                    node.get("category").asString()
            );
        }
    }

    // =====================================================
    // GET all skills
    // =====================================================
    public List<Skill> getAllSkills() {

        String cypher = """
                MATCH (s:Skill)
                RETURN s
                ORDER BY s.name
                """;

        try (Session session = driver.session()) {

            var result = session.run(cypher);

            List<Skill> skills = new ArrayList<>();

            while (result.hasNext()) {

                var record = result.next();
                var node = record.get("s").asNode();

                skills.add(
                        new Skill(
                                node.get("id").asString(),
                                node.get("name").asString(),
                                node.get("category").asString()
                        )
                );
            }

            return skills;
        }
    }

    // =====================================================
    // GET skill by ID
    // =====================================================
    public Optional<Skill> getSkillById(String id) {

        String cypher = """
                MATCH (s:Skill {id: $id})
                RETURN s
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("id", id)
            );

            if (!result.hasNext()) {
                return Optional.empty();
            }

            var record = result.single();
            var node = record.get("s").asNode();

            Skill skill = new Skill(
                    node.get("id").asString(),
                    node.get("name").asString(),
                    node.get("category").asString()
            );

            return Optional.of(skill);
        }
    }

    // =====================================================
    // UPDATE skill
    // =====================================================
    public Optional<Skill> updateSkill(
            String id,
            Skill skill) {

        String cypher = """
                MATCH (s:Skill {id: $id})
                SET s.name = $name,
                    s.category = $category
                RETURN s
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "id", id,
                            "name", skill.getName(),
                            "category", skill.getCategory()
                    )
            );

            if (!result.hasNext()) {
                return Optional.empty();
            }

            var record = result.single();
            var node = record.get("s").asNode();

            Skill updatedSkill = new Skill(
                    node.get("id").asString(),
                    node.get("name").asString(),
                    node.get("category").asString()
            );

            return Optional.of(updatedSkill);
        }
    }

    // =====================================================
    // DELETE skill
    // =====================================================
    public boolean deleteSkill(String id) {

        String cypher = """
                MATCH (s:Skill {id: $id})
                WITH collect(s) AS nodes
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

            var record = result.single();

            return record.get("deleted").asLong() > 0;
        }
    }
}