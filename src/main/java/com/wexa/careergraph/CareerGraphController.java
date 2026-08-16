package com.wexa.careergraph;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wexa.careergraph.model.CareerRole;
import com.wexa.careergraph.model.Developer;
import com.wexa.careergraph.model.Skill;
import com.wexa.careergraph.service.CareerGraphService;
import com.wexa.careergraph.service.CareerRoleService;
import com.wexa.careergraph.service.DeveloperService;
import com.wexa.careergraph.service.SkillService;

@RestController
@RequestMapping("/api")
public class CareerGraphController {

    private final DeveloperService developerService;
    private final CareerRoleService careerRoleService;
    private final SkillService skillService;
    private final CareerGraphService careerGraphService;

    public CareerGraphController(
            DeveloperService developerService,
            CareerRoleService careerRoleService,
            SkillService skillService,
            CareerGraphService careerGraphService) {

        this.developerService = developerService;
        this.careerRoleService = careerRoleService;
        this.skillService = skillService;
        this.careerGraphService = careerGraphService;
    }


    // =====================================================
    // HOME
    // =====================================================

    @GetMapping("/")
    public String home() {
        return "Wexa Career Graph API is running!";
    }


    // =====================================================
    // HEALTH
    // =====================================================

    @GetMapping("/health")
    public String health() {
        return "Career Graph API is healthy!";
    }


    // =====================================================
    // INFO
    // =====================================================

    @GetMapping("/info")
    public String info() {
        return "Wexa Career Graph API - Developer Skill and Career Graph";
    }


    // =====================================================
    // DEVELOPER APIs
    // =====================================================

    // CREATE DEVELOPER
    @PostMapping("/developers")
    public Developer createDeveloper(
            @RequestBody Developer developer) {

        return developerService.createDeveloper(developer);
    }


    // GET ALL DEVELOPERS
    @GetMapping("/developers")
    public List<Developer> getAllDevelopers() {

        return developerService.getAllDevelopers();
    }


    // GET DEVELOPER BY ID
    @GetMapping("/developers/{id}")
    public ResponseEntity<Developer> getDeveloperById(
            @PathVariable("id") String id) {

        return developerService.getDeveloperById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // UPDATE DEVELOPER
    @PutMapping("/developers/{id}")
    public ResponseEntity<Developer> updateDeveloper(
            @PathVariable("id") String id,
            @RequestBody Developer developer) {

        return developerService.updateDeveloper(id, developer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // DELETE DEVELOPER
    @DeleteMapping("/developers/{id}")
    public ResponseEntity<Void> deleteDeveloper(
            @PathVariable("id") String id) {

        boolean deleted = developerService.deleteDeveloper(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }


    // =====================================================
    // CAREER ROLE APIs
    // =====================================================

    // CREATE CAREER ROLE
    @PostMapping("/career-roles")
    public CareerRole createCareerRole(
            @RequestBody CareerRole role) {

        return careerRoleService.createCareerRole(role);
    }


    // GET ALL CAREER ROLES
    @GetMapping("/career-roles")
    public List<CareerRole> getAllCareerRoles() {

        return careerRoleService.getAllCareerRoles();
    }


    // GET CAREER ROLE BY ID
    @GetMapping("/career-roles/{id}")
    public ResponseEntity<CareerRole> getCareerRoleById(
            @PathVariable("id") String id) {

        return careerRoleService.getCareerRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // UPDATE CAREER ROLE
    @PutMapping("/career-roles/{id}")
    public ResponseEntity<CareerRole> updateCareerRole(
            @PathVariable("id") String id,
            @RequestBody CareerRole role) {

        return careerRoleService.updateCareerRole(id, role)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // DELETE CAREER ROLE
    @DeleteMapping("/career-roles/{id}")
    public ResponseEntity<Void> deleteCareerRole(
            @PathVariable("id") String id) {

        boolean deleted = careerRoleService.deleteCareerRole(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }


    // =====================================================
    // SKILL APIs
    // =====================================================

    // CREATE SKILL
    @PostMapping("/skills")
    public Skill createSkill(
            @RequestBody Skill skill) {

        return skillService.createSkill(skill);
    }


    // GET ALL SKILLS
    @GetMapping("/skills")
    public List<Skill> getAllSkills() {

        return skillService.getAllSkills();
    }


    // GET SKILL BY ID
    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkillById(
            @PathVariable("id") String id) {

        return skillService.getSkillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // UPDATE SKILL
    @PutMapping("/skills/{id}")
    public ResponseEntity<Skill> updateSkill(
            @PathVariable("id") String id,
            @RequestBody Skill skill) {

        return skillService.updateSkill(id, skill)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // DELETE SKILL
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(
            @PathVariable("id") String id) {

        boolean deleted = skillService.deleteSkill(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }


    // =====================================================
    // CAREER GRAPH / RELATIONSHIP APIs
    // =====================================================


    // =====================================================
    // DEVELOPER -> SKILL
    // =====================================================

    // ADD SKILL TO DEVELOPER
    @PostMapping("/developers/{developerId}/skills/{skillId}")
    public ResponseEntity<String> addSkillToDeveloper(
            @PathVariable("developerId") String developerId,
            @PathVariable("skillId") String skillId) {

        boolean created =
                careerGraphService.addSkillToDeveloper(
                        developerId,
                        skillId
                );

        if (!created) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Skill assigned to developer successfully"
        );
    }


    // DELETE SKILL FROM DEVELOPER
    @DeleteMapping("/developers/{developerId}/skills/{skillId}")
    public ResponseEntity<String> removeSkillFromDeveloper(
            @PathVariable("developerId") String developerId,
            @PathVariable("skillId") String skillId) {

        boolean deleted =
                careerGraphService.removeSkillFromDeveloper(
                        developerId,
                        skillId
                );

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Skill removed from developer successfully"
        );
    }


    // GET DEVELOPER SKILLS
    @GetMapping("/developers/{developerId}/skills")
    public ResponseEntity<List<Map<String, Object>>> getDeveloperSkills(
            @PathVariable("developerId") String developerId) {

        return ResponseEntity.ok(
                careerGraphService.getDeveloperSkills(
                        developerId
                )
        );
    }


    // =====================================================
    // DEVELOPER -> CAREER ROLE
    // =====================================================

    // ADD CAREER TARGET
    @PostMapping("/developers/{developerId}/career-roles/{careerRoleId}")
    public ResponseEntity<String> addCareerTarget(
            @PathVariable("developerId") String developerId,
            @PathVariable("careerRoleId") String careerRoleId) {

        boolean created =
                careerGraphService.addCareerTarget(
                        developerId,
                        careerRoleId
                );

        if (!created) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Career role assigned to developer successfully"
        );
    }


    // DELETE CAREER TARGET
    @DeleteMapping("/developers/{developerId}/career-roles/{careerRoleId}")
    public ResponseEntity<String> removeCareerTarget(
            @PathVariable("developerId") String developerId,
            @PathVariable("careerRoleId") String careerRoleId) {

        boolean deleted =
                careerGraphService.removeCareerTarget(
                        developerId,
                        careerRoleId
                );

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Career role removed from developer successfully"
        );
    }


    // GET DEVELOPER CAREER ROLES
    @GetMapping("/developers/{developerId}/career-roles")
    public ResponseEntity<List<Map<String, Object>>> getDeveloperCareerRoles(
            @PathVariable("developerId") String developerId) {

        return ResponseEntity.ok(
                careerGraphService.getDeveloperCareerRoles(
                        developerId
                )
        );
    }


    // =====================================================
    // CAREER ROLE -> SKILL
    // =====================================================

    // ADD REQUIRED SKILL
    @PostMapping("/career-roles/{careerRoleId}/skills/{skillId}")
    public ResponseEntity<String> addRequiredSkill(
            @PathVariable("careerRoleId") String careerRoleId,
            @PathVariable("skillId") String skillId) {

        boolean created =
                careerGraphService.addRequiredSkill(
                        careerRoleId,
                        skillId
                );

        if (!created) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Skill assigned to career role successfully"
        );
    }


    // DELETE REQUIRED SKILL
    @DeleteMapping("/career-roles/{careerRoleId}/skills/{skillId}")
    public ResponseEntity<String> removeRequiredSkill(
            @PathVariable("careerRoleId") String careerRoleId,
            @PathVariable("skillId") String skillId) {

        boolean deleted =
                careerGraphService.removeRequiredSkill(
                        careerRoleId,
                        skillId
                );

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                "Skill removed from career role successfully"
        );
    }


    // GET CAREER ROLE REQUIRED SKILLS
    @GetMapping("/career-roles/{careerRoleId}/skills")
    public ResponseEntity<List<Map<String, Object>>> getCareerRoleSkills(
            @PathVariable("careerRoleId") String careerRoleId) {

        return ResponseEntity.ok(
                careerGraphService.getCareerRoleSkills(
                        careerRoleId
                )
        );
    }


    // =====================================================
    // CAREER RECOMMENDATIONS
    // =====================================================

    @GetMapping("/developers/{developerId}/career-recommendations")
    public ResponseEntity<List<Map<String, Object>>> getCareerRecommendations(
            @PathVariable("developerId") String developerId) {

        return ResponseEntity.ok(
                careerGraphService.getCareerRecommendations(
                        developerId
                )
        );
    }


    // =====================================================
    // TEMPORARY TEST APIs
    // =====================================================

    @PostMapping("/test-post")
    public ResponseEntity<String> testPost() {

        return ResponseEntity.ok(
                "POST mapping is working!"
        );
    }


    @PostMapping("/test-career-role-skill")
    public ResponseEntity<String> testCareerRoleSkill() {

        return ResponseEntity.ok(
                "Career Role -> Skill POST mapping is working!"
        );
    }


    @GetMapping("/test-career-skill")
    public String testCareerSkill() {

        return "Career skill mapping is working!";
    }
}