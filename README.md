\# CareerGraph



CareerGraph is a graph-based web application that helps developers understand their skills and discover suitable career roles.



The application uses CognoDB as the graph database and provides career recommendations based on the relationship between developers, skills, and career roles.



\## Features



\- Developer management

\- Skill management

\- Career role management

\- Assign skills to developers

\- Assign career targets to developers

\- Define required skills for career roles

\- Career recommendations based on skill matching

\- Match percentage calculation

\- REST APIs

\- Interactive web interface

\- Graph-based data modeling using CognoDB



\## Technology Stack



\- Java 21

\- Spring Boot

\- REST API

\- CognoDB

\- OpenCypher

\- Neo4j Java Driver

\- HTML5

\- CSS3

\- JavaScript

\- Maven

\- Git / GitHub



\## Why a Graph Database?



CareerGraph is based on relationships between developers, skills, and career roles.



A developer can have multiple skills, a developer can target multiple career roles, and each career role can require multiple skills.



A graph database makes these relationships easy to represent and traverse.



For example:



Developer → HAS\_SKILL → Skill



Developer → TARGETS → CareerRole



CareerRole → REQUIRES\_SKILL → Skill



This makes graph traversal useful for finding suitable career roles based on a developer's existing skills.



\## Data Model



The main nodes are:



\- Developer

\- Skill

\- CareerRole



The main relationships are:



\- HAS\_SKILL

\- TARGETS

\- REQUIRES\_SKILL



### Graph Structure

```text
Developer
    │
    ├── HAS_SKILL ────────> Skill
    │
    └── TARGETS ──────────> CareerRole
                                │
                                │ REQUIRES_SKILL
                                ↓
                              Skill
```


This relationship structure allows CareerGraph to compare developer skills with the skills required by career roles.


\## Career Recommendation



The application calculates a match percentage between a developer's skills and the skills required by each career role.



For example:



Developer skills:



\- Java

\- Spring Boot



Career role:



Java Backend Developer



Required skills:



\- Java

\- Spring Boot



Result:



100% match



The application also displays the matched skill names.



\## Project Structure



```text

careergraph/

├── backend/

├── frontend/

├── scripts/

│   ├── seed.cypher

│   └── main-queries.cypher

├── src/

│   ├── main/

│   │   ├── java/

│   │   └── resources/

│   └── test/

├── .env.example

├── .gitignore

├── pom.xml

├── mvnw

└── mvnw.cmd

## Setup and Installation

### Prerequisites

- Java 21
- Maven
- CognoDB account
- Git

### Environment Variables

Create a `.env` file in the project root:

```text
COGNODB_URI=your-cognodb-uri
COGNODB_USERNAME=your-cognodb-username
COGNODB_PASSWORD=your-cognodb-password
```

Do not commit the `.env` file to GitHub.

### Run the Application

```bash
mvnw.cmd spring-boot:run
```

The application will start on:

http://localhost:8080

### Open the Web Interface

Open the following URL in your browser:

http://localhost:8080