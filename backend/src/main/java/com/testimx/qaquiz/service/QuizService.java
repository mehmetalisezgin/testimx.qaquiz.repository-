package com.testimx.qaquiz.service;

import com.testimx.qaquiz.dto.*;
import com.testimx.qaquiz.model.*;
import com.testimx.qaquiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Encapsulates quiz session logic: starting a quiz, answering
 * questions, summarising results and computing user progress.
 */
@Service
public class QuizService {
    @Autowired
    private QuizSessionRepository sessionRepository;
    @Autowired
    private QuizSessionItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Begins a new quiz session for the given user and category.  The
     * server selects a random subset of questions if a limit is
     * provided.
     */
    @Transactional
    public QuizStartResponse startQuiz(Long categoryId, Integer numQuestions, User user) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        List<Question> allQuestions = questionRepository.findByCategory(category);
        if (allQuestions.isEmpty()) {
            throw new RuntimeException("No questions available for this category");
        }
        // Shuffle and limit
        Collections.shuffle(allQuestions);
        int total = allQuestions.size();
        int limit = (numQuestions == null || numQuestions <= 0 || numQuestions > total) ? total : numQuestions;
        List<Question> selected = allQuestions.subList(0, limit);
        QuizSession session = new QuizSession();
        session.setUser(user);
        session.setCategory(category);
        session.setStartTime(LocalDateTime.now());
        session.setScore(0);
        session.setTotalQuestions(limit);
        sessionRepository.save(session);
        // Convert questions to DTOs (do not send correct flags)
        List<QuestionDto> questionDtos = selected.stream().map(q -> {
            List<OptionDto> options = q.getOptions().stream()
                    .map(opt -> new OptionDto(opt.getLabel(), opt.getText()))
                    .collect(Collectors.toList());
            return new QuestionDto(q.getId(), q.getText(), q.getCategory().getId(), options);
        }).collect(Collectors.toList());
        return new QuizStartResponse(session.getId(), questionDtos);
    }

    /**
     * Processes the answer for a specific question within a session.  If
     * the answer is correct the session's score is incremented and a
     * positive response is returned.  If incorrect the correct
     * explanation is provided.
     */
    @Transactional
    public QuizAnswerResponse answerQuestion(Long sessionId, Long questionId, String selectedLabel, User user) {
        QuizSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Quiz session not found"));
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have permission to answer this session");
        }
        // Check if question belongs to session category
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        if (!question.getCategory().getId().equals(session.getCategory().getId())) {
            throw new RuntimeException("Question does not belong to the session's category");
        }
        // Check if already answered
        Optional<QuizSessionItem> existing = session.getItems().stream()
                .filter(item -> item.getQuestion().getId().equals(questionId))
                .findFirst();
        if (existing.isPresent()) {
            throw new RuntimeException("Question has already been answered in this session");
        }
        boolean correct = false;
        String explanation = null;
        // Determine correctness and find explanation
        for (Option opt : question.getOptions()) {
            if (opt.getLabel().equalsIgnoreCase(selectedLabel)) {
                if (opt.isCorrect()) {
                    correct = true;
                }
            }
            if (opt.isCorrect()) {
                // capture explanation from correct option
                explanation = opt.getExplanation();
            }
        }
        // Create session item
        QuizSessionItem item = new QuizSessionItem(session, question, selectedLabel, correct);
        session.getItems().add(item);
        if (correct) {
            session.setScore(session.getScore() + 1);
        }
        // Persist changes
        sessionRepository.save(session);
        // Explanation only sent when incorrect; otherwise null
        return new QuizAnswerResponse(correct, correct ? null : explanation);
    }

    /**
     * Completes a quiz session and returns a summary of the user's
     * performance.  The summary includes per‑question results and
     * aggregates such as total correct answers and duration.
     */
    @Transactional
    public QuizSummaryResponse summariseSession(Long sessionId, User user) {
        QuizSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Quiz session not found"));
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have permission to view this session");
        }
        if (session.getEndTime() == null) {
            session.setEndTime(LocalDateTime.now());
            sessionRepository.save(session);
        }
        int correctCount = session.getScore() == null ? 0 : session.getScore();
        int total = session.getTotalQuestions() == null ? session.getItems().size() : session.getTotalQuestions();
        double percentage = total == 0 ? 0 : (double) correctCount / total * 100.0;
        Duration duration = Duration.between(session.getStartTime(), session.getEndTime());
        List<QuizItemResultDto> results = session.getItems().stream()
                .map(item -> new QuizItemResultDto(item.getQuestion().getId(), item.isCorrect()))
                .collect(Collectors.toList());
        return new QuizSummaryResponse(session.getId(), correctCount, total, percentage, duration, results);
    }

    /**
     * Calculates aggregated progress statistics and history for the
     * given user.  Returns overall correctness, streak, category
     * breakdown and history of past sessions.
     */
    @Transactional(readOnly = true)
    public ProgressSummaryResponse getProgress(User user) {
        List<QuizSession> sessions = sessionRepository.findByUser(user);
        if (sessions.isEmpty()) {
            return new ProgressSummaryResponse(0, 0, 0, new ArrayList<>(), new ArrayList<>());
        }
        // Compute overall correct ratio
        int totalCorrect = sessions.stream().mapToInt(s -> s.getScore() == null ? 0 : s.getScore()).sum();
        int totalQuestions = sessions.stream().mapToInt(s -> s.getTotalQuestions() == null ? s.getItems().size() : s.getTotalQuestions()).sum();
        double overallPercent = totalQuestions == 0 ? 0.0 : (double) totalCorrect / totalQuestions * 100.0;
        // Compute streak: count consecutive sessions at end of list with >=50% score
        sessions.sort(Comparator.comparing(QuizSession::getStartTime));
        int streak = 0;
        ListIterator<QuizSession> reverseIter = sessions.listIterator(sessions.size());
        while (reverseIter.hasPrevious()) {
            QuizSession session = reverseIter.previous();
            int correct = session.getScore() == null ? 0 : session.getScore();
            int total = session.getTotalQuestions() == null ? session.getItems().size() : session.getTotalQuestions();
            boolean passed = total == 0 ? false : ((double) correct / total) >= 0.5;
            if (passed) {
                streak++;
            } else {
                break;
            }
        }
        // Category breakdown
        Map<Category, List<QuizSession>> sessionsByCategory = sessions.stream()
                .collect(Collectors.groupingBy(QuizSession::getCategory));
        List<CategoryProgressDto> categoryProgress = new ArrayList<>();
        for (Map.Entry<Category, List<QuizSession>> entry : sessionsByCategory.entrySet()) {
            Category cat = entry.getKey();
            List<QuizSession> catSessions = entry.getValue();
            int catCorrect = catSessions.stream().mapToInt(s -> s.getScore() == null ? 0 : s.getScore()).sum();
            int catTotal = catSessions.stream().mapToInt(s -> s.getTotalQuestions() == null ? s.getItems().size() : s.getTotalQuestions()).sum();
            double percent = catTotal == 0 ? 0.0 : (double) catCorrect / catTotal * 100.0;
            categoryProgress.add(new CategoryProgressDto(cat.getId(), cat.getName(), percent));
        }
        // History: show latest 10 sessions sorted descending by start time
        List<QuizHistoryDto> history = sessions.stream()
                .sorted(Comparator.comparing(QuizSession::getStartTime).reversed())
                .limit(10)
                .map(s -> {
                    int correct = s.getScore() == null ? 0 : s.getScore();
                    int total = s.getTotalQuestions() == null ? s.getItems().size() : s.getTotalQuestions();
                    boolean passed = total == 0 ? false : ((double) correct / total) >= 0.5;
                    String scoreStr = total == 0 ? "0/0" : correct + "/" + total;
                    LocalDate date = s.getStartTime() != null ? s.getStartTime().toLocalDate() : LocalDate.now();
                    String formatted = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
                    return new QuizHistoryDto(s.getId(), s.getCategory().getName(), formatted, scoreStr, passed);
                })
                .collect(Collectors.toList());
        return new ProgressSummaryResponse(overallPercent, streak, sessions.size(), categoryProgress, history);
    }
}