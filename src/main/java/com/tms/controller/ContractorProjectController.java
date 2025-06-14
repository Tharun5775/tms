package com.tms.controller;

import com.tms.dto.ContractorProjectDTO;
import com.tms.service.ContractorProjectService;
import com.tms.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class ContractorProjectController {

    @Autowired
    private ContractorProjectService contractorProjectService;

    @PostMapping("/assign-project")
    public ResponseEntity<?> assignProject(@RequestBody ContractorProjectDTO dto) {
        try {
            String message = contractorProjectService.assignContractorToProject(dto);
            return ResponseEntity.ok().body(message);
        } catch (ResourceNotFoundException ex) {
            log.error("Error assigning project: {}", ex.getMessage());
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}

