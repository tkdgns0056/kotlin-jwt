package com.project.kotlin_jwt.member.repository

import com.project.kotlin_jwt.member.entity.MemberRole
import com.project.kotlin_jwt.member.entity.Members
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Members, Long> {
    fun findByLoginId(loginId: String): Members?
}

interface MemberRoleRepository : JpaRepository<MemberRole, Long>