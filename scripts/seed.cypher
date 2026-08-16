MERGE (d:Developer {id:'DEV003'}) SET d.name='Demo Developer';
MERGE (java:Skill {id:'SKILL001'}) SET java.name='Java', java.category='Backend';
MERGE (spring:Skill {id:'SKILL002'}) SET spring.name='Spring Boot', spring.category='Backend';
MERGE (r:CareerRole {id:'ROLE001'}) SET r.title='Java Backend Developer', r.level='Mid-Level';
MERGE (d)-[:HAS_SKILL]->(java);
MERGE (d)-[:HAS_SKILL]->(spring);
MERGE (d)-[:TARGETS]->(r);
MERGE (r)-[:REQUIRES_SKILL]->(java);
MERGE (r)-[:REQUIRES_SKILL]->(spring);

MATCH (d:Developer {id:'DEV003'})-[:HAS_SKILL]->(s:Skill)<-[:REQUIRES_SKILL]-(r:CareerRole)
RETURN d.id AS developerId,s.name AS skill,r.title AS careerRole;
