package com.example.taskflow.server.project

import com.example.taskflow.server.task.Task
import com.example.taskflow.server.user.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "projects")
class Project(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false,
    )
    @JoinColumn(
        name = "user_id",
        nullable = false,
    )
    val user: User,

    @OneToMany(
        mappedBy = "project",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    val tasks: MutableList<Task> = mutableListOf(),
)