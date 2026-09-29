package com.example.taskflow.server.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(
        nullable = false,
        unique = true,
    )
    val username: String,

    @Column(
        name = "password_hash",
        nullable = false,
    )
    val passwordHash: String,
)