package com.testimx.qaquiz.controller;

import com.testimx.qaquiz.dto.ProgressSummaryResponse;
import com.testimx.qaquiz.model.User;
import com.testimx.qaquiz.repository.UserRepository;
import com.testimx.qaquiz.security.services.UserDetailsImpl;
import com.testimx.qaquiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes aggregated progress and history information for the
 * authenticated user.
 */
@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ProgressSummaryResponse> getProgress() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(quizService.getProgress(currentUser));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new RuntimeException("User is not authenticated");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
    }
}