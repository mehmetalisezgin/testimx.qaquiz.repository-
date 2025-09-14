package com.testimx.qaquiz.model;

/**
 * Enumeration of all application roles.  Students can take quizzes,
 * whereas administrators can additionally manage categories and
 * questions.  Spring Security makes use of the names prefixed with
 * {@code ROLE_} for simplicity.
 */
public enum ERole {
    ROLE_STUDENT,
    ROLE_ADMIN
}