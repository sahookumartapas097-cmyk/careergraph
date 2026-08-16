package com.wexa.careergraph.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

@Service
public class CareerGraphService {

    private final Driver driver;

    public CareerGraphService(Driver driver) {
        this.driver = driver;
    }


    // =====================================================
    // DEVELOPER -> SKILL
    // =====================================================

    // ADD SKILL TO DEVELOPER
    public boolean addSkillToDeveloper(
            String developerId,
            String skillId) {

        String cypher = """
                MATCH (d:Developer {id: $developerId})
                MATCH (s:Skill {id: $skillId})

                MERGE (d)-[:HAS_SKILL]->(s)

                RETURN d.id AS developerId,
                       s.id AS skillId
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "developerId", developerId,
                            "skillId", skillId
                    )
            );

            if (!result.hasNext()) {
                return false;
            }

            var record = result.single();

            return record.get("developerId")
                    .asString()
                    .equals(developerId)
                    &&
                    record.get("skillId")
                    .asString()
                    .equals(skillId);
        }
    }


    // DELETE SKILL FROM DEVELOPER
    public boolean removeSkillFromDeveloper(
            String developerId,
            String skillId) {

        String cypher = """
                MATCH (d:Developer {id: $developerId})
                      -[rel:HAS_SKILL]->
                      (s:Skill {id: $skillId})

                DELETE rel

                RETURN count(rel) AS deleted
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "developerId", developerId,
                            "skillId", skillId
                    )
            );

            if (!result.hasNext()) {
                return false;
            }

            return result.single()
                    .get("deleted")
                    .asLong() > 0;
        }
    }


    // GET DEVELOPER SKILLS
    public List<Map<String, Object>> getDeveloperSkills(
            String developerId) {

        String cypher = """
                MATCH (d:Developer {id: $developerId})
                      -[:HAS_SKILL]->(s:Skill)

                RETURN DISTINCT
                       s.id AS id,
                       s.name AS name,
                       s.category AS category

                ORDER BY s.name
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("developerId", developerId)
            );

            List<Map<String, Object>> skills =
                    new ArrayList<>();

            while (result.hasNext()) {

                var record = result.next();

                skills.add(
                        Map.of(
                                "id",
                                record.get("id").asString(),

                                "name",
                                record.get("name").asString(),

                                "category",
                                record.get("category").asString()
                        )
                );
            }

            return skills;
        }
    }


    // =====================================================
    // DEVELOPER -> CAREER ROLE
    // =====================================================

    // ADD CAREER TARGET
    public boolean addCareerTarget(
            String developerId,
            String careerRoleId) {

        String cypher = """
                MATCH (d:Developer {id: $developerId})
                MATCH (r:CareerRole {id: $careerRoleId})

                MERGE (d)-[:TARGETS]->(r)

                RETURN d.id AS developerId,
                       r.id AS careerRoleId
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "developerId", developerId,
                            "careerRoleId", careerRoleId
                    )
            );

            if (!result.hasNext()) {
                return false;
            }

            var record = result.single();

            return record.get("developerId")
                    .asString()
                    .equals(developerId)
                    &&
                    record.get("careerRoleId")
                    .asString()
                    .equals(careerRoleId);
        }
    }


    // DELETE CAREER TARGET
    public boolean removeCareerTarget(
            String developerId,
            String careerRoleId) {

        String cypher = """
                MATCH (d:Developer {id: $developerId})
                      -[rel:TARGETS]->
                      (r:CareerRole {id: $careerRoleId})

                DELETE rel

                RETURN count(rel) AS deleted
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "developerId", developerId,
                            "careerRoleId", careerRoleId
                    )
            );

            if (!result.hasNext()) {
                return false;
            }

            return result.single()
                    .get("deleted")
                    .asLong() > 0;
        }
    }


    // GET DEVELOPER CAREER ROLES
    public List<Map<String, Object>> getDeveloperCareerRoles(
            String developerId) {

        String cypher = """
                MATCH (d:Developer {id: $developerId})
                      -[:TARGETS]->(r:CareerRole)

                RETURN DISTINCT
                       r.id AS id,
                       r.title AS title,
                       r.level AS level

                ORDER BY r.id
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("developerId", developerId)
            );

            List<Map<String, Object>> careerRoles =
                    new ArrayList<>();

            while (result.hasNext()) {

                var record = result.next();

                careerRoles.add(
                        Map.of(
                                "id",
                                record.get("id").asString(),

                                "title",
                                record.get("title").asString(),

                                "level",
                                record.get("level").asString()
                        )
                );
            }

            return careerRoles;
        }
    }


    // =====================================================
    // CAREER ROLE -> SKILL
    // =====================================================

    // ADD REQUIRED SKILL
    public boolean addRequiredSkill(
            String careerRoleId,
            String skillId) {

        String cypher = """
                MATCH (r:CareerRole {id: $careerRoleId})
                MATCH (s:Skill {id: $skillId})

                MERGE (r)-[:REQUIRES_SKILL]->(s)

                RETURN r.id AS roleId,
                       s.id AS skillId
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "careerRoleId", careerRoleId,
                            "skillId", skillId
                    )
            );

            if (!result.hasNext()) {
                return false;
            }

            var record = result.single();

            return record.get("roleId")
                    .asString()
                    .equals(careerRoleId)
                    &&
                    record.get("skillId")
                    .asString()
                    .equals(skillId);
        }
    }


    // DELETE REQUIRED SKILL
    public boolean removeRequiredSkill(
            String careerRoleId,
            String skillId) {

        String cypher = """
                MATCH (r:CareerRole {id: $careerRoleId})
                      -[rel:REQUIRES_SKILL]->
                      (s:Skill {id: $skillId})

                DELETE rel

                RETURN count(rel) AS deleted
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of(
                            "careerRoleId", careerRoleId,
                            "skillId", skillId
                    )
            );

            if (!result.hasNext()) {
                return false;
            }

            return result.single()
                    .get("deleted")
                    .asLong() > 0;
        }
    }


    // GET CAREER ROLE REQUIRED SKILLS
    public List<Map<String, Object>> getCareerRoleSkills(
            String careerRoleId) {

        String cypher = """
                MATCH (r:CareerRole {id: $careerRoleId})
                      -[:REQUIRES_SKILL]->(s:Skill)

                RETURN DISTINCT
                       s.id AS id,
                       s.name AS name,
                       s.category AS category

                ORDER BY s.name
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("careerRoleId", careerRoleId)
            );

            List<Map<String, Object>> skills =
                    new ArrayList<>();

            while (result.hasNext()) {

                var record = result.next();

                skills.add(
                        Map.of(
                                "id",
                                record.get("id").asString(),

                                "name",
                                record.get("name").asString(),

                                "category",
                                record.get("category").asString()
                        )
                );
            }

            return skills;
        }
    }


    // =====================================================
    // CAREER RECOMMENDATIONS
    // =====================================================

    public List<Map<String, Object>> getCareerRecommendations(
            String developerId) {

        String cypher = """
                MATCH (d:Developer {id: $developerId})

                MATCH (r:CareerRole)

                OPTIONAL MATCH (r)-[:REQUIRES_SKILL]->(required:Skill)

                WITH d,
                     r,
                     collect(DISTINCT required.id)
                     AS requiredSkillIds

                OPTIONAL MATCH
                    (d)-[:HAS_SKILL]->(developerSkill:Skill)

                WITH r,
                     requiredSkillIds,
                     collect(DISTINCT developerSkill.id)
                     AS developerSkillIds

                WITH r,
                     requiredSkillIds,
                     developerSkillIds,

                     [skillId IN requiredSkillIds
                      WHERE skillId IS NOT NULL
                      AND skillId IN developerSkillIds]
                     AS matchingSkillIds

                OPTIONAL MATCH
                    (r)-[:REQUIRES_SKILL]->(matched:Skill)

                WITH r,
                     requiredSkillIds,
                     matchingSkillIds,
                     collect(
                         DISTINCT
                         CASE
                             WHEN matched.id IN matchingSkillIds
                             THEN matched.name
                             ELSE NULL
                         END
                     ) AS matchedNames

                WITH r,
                     size(requiredSkillIds)
                     AS totalRequiredSkills,

                     size(matchingSkillIds)
                     AS matchedSkills,

                     [name IN matchedNames
                      WHERE name IS NOT NULL]
                     AS matchedSkillNames

                RETURN
                    r.id AS id,
                    r.title AS title,
                    r.level AS level,

                    totalRequiredSkills,

                    matchedSkills,

                    CASE
                        WHEN totalRequiredSkills = 0
                        THEN 0.0
                        ELSE
                            toFloat(matchedSkills)
                            /
                            toFloat(totalRequiredSkills)
                            * 100.0
                    END AS matchPercentage,

                    matchedSkillNames

                ORDER BY matchPercentage DESC,
                         r.title
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("developerId", developerId)
            );

            List<Map<String, Object>> recommendations =
                    new ArrayList<>();

            while (result.hasNext()) {

                var record = result.next();

                Map<String, Object> recommendation =
                        Map.of(
                                "id",
                                record.get("id").asString(),

                                "title",
                                record.get("title").asString(),

                                "level",
                                record.get("level").asString(),

                                "totalRequiredSkills",
                                record.get("totalRequiredSkills")
                                        .asLong(),

                                "matchedSkills",
                                record.get("matchedSkills")
                                        .asLong(),

                                "matchPercentage",
                                record.get("matchPercentage")
                                        .asDouble(),

                                "matchedSkillNames",
                                record.get("matchedSkillNames")
                                        .asList()
                        );

                recommendations.add(recommendation);
            }

            return recommendations;
        }
    }


    // =====================================================
    // CHECK DEVELOPER
    // =====================================================

    public boolean developerExists(String developerId) {

        String cypher = """
                MATCH (d:Developer {id: $developerId})
                RETURN d.id AS id
                LIMIT 1
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("developerId", developerId)
            );

            return result.hasNext();
        }
    }


    // =====================================================
    // CHECK CAREER ROLE
    // =====================================================

    public boolean careerRoleExists(String careerRoleId) {

        String cypher = """
                MATCH (r:CareerRole {id: $careerRoleId})
                RETURN r.id AS id
                LIMIT 1
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("careerRoleId", careerRoleId)
            );

            return result.hasNext();
        }
    }


    // =====================================================
    // CHECK SKILL
    // =====================================================

    public boolean skillExists(String skillId) {

        String cypher = """
                MATCH (s:Skill {id: $skillId})
                RETURN s.id AS id
                LIMIT 1
                """;

        try (Session session = driver.session()) {

            var result = session.run(
                    cypher,
                    Map.of("skillId", skillId)
            );

            return result.hasNext();
        }
    }
}