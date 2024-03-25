package org.hospitalmanager.repository;

// class containing exceptions for the AuthRepository
public class AuthRepositoryException extends Exception {
    static public class UserNotFoundException extends Exception {
        public UserNotFoundException(String message) {
            super("User not found: " + message);
        }
    }

    static public class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String message) {
            super("User already exists: " + message);
        }
    }

    static public class InvalidTokenException extends Exception {
        public InvalidTokenException(String message) {
            super("Invalid token: " + message);
        }
    }
}
