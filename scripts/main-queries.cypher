// 2+ hop graph traversal
MATCH (d:Developer {id:$developerId})-[:HAS_SKILL]->(s:Skill)<-[:REQUIRES_SKILL]-(r:CareerRole)
RETURN d.id AS developerId, collect(DISTINCT s.name) AS skills, r.id AS roleId, r.title AS roleTitle;

// Career role skills
MATCH (r:CareerRole {id:$careerRoleId})-[:REQUIRES_SKILL]->(s:Skill)
RETURN s.id AS id,s.name AS name,s.category AS category ORDER BY s.name;
