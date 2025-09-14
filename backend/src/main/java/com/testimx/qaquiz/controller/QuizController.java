package com.testimx.qaquiz.controller;

import com.testimx.qaquiz.dto.*;
import com.testimx.qaquiz.model.User;
import com.testimx.qaquiz.repository.UserRepository;
import com.testimx.qaquiz.security.services.UserDetailsImpl;
import com.testimx.qaquiz.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller managing quiz sessions.  Provides endpoints to start a
 * session, answer questions and retrieve the summary.
 */
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Starts a quiz session for the authenticated user.
     */
    @PostMapping("/start")
    public ResponseEntity<QuizStartResponse> startQuiz(@Valid @RequestBody QuizStartRequest request) {
        User currentUser = getCurrentUser();
        QuizStartResponse response = quizService.startQuiz(request.getCategoryId(), request.getNumQuestions(), currentUser);
        return ResponseEntity.ok(response);
    }

    /**
     * Submit an answer for a question in the current session.  Returns
     * whether the answer was correct and an explanation if not.
     */
    @PostMapping("/answer")
    public ResponseEntity<QuizAnswerResponse> answerQuestion(@Valid @RequestBody QuizAnswerRequest request) {
        User currentUser = getCurrentUser();
        QuizAnswerResponse response = quizService.answerQuestion(request.getSessionId(), request.getQuestionId(), request.getSelectedLabel(), currentUser);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a summary for a completed quiz session.
     */
    @GetMapping("/{sessionId}/summary")
    public ResponseEntity<QuizSummaryResponse> getSummary(@PathVariable Long sessionId) {
        User currentUser = getCurrentUser();
        QuizSummaryResponse summary = quizService.summariseSession(sessionId, currentUser);
        return ResponseEntity.ok(summary);
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