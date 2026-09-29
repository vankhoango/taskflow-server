package com.example.taskflow.server.auth.exception

class UsernameAlreadyExistsException(
    username: String,
) : RuntimeException("Username already exists: $username")