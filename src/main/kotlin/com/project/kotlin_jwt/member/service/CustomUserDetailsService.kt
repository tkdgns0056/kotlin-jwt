package com.project.kotlin_jwt.member.service

import com.project.kotlin_jwt.common.status.dto.CustomUser
import com.project.kotlin_jwt.member.entity.Members
import com.project.kotlin_jwt.member.repository.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService (
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        memberRepository.findByLoginId(username)
            ?.let { createUserDetails(it) }
            ?: throw UsernameNotFoundException("해당 유저는 찾을 수 없습니다.")

    private fun createUserDetails(member: Members): UserDetails =
        CustomUser(
            member.id!!,
            member.loginId,
            passwordEncoder.encode(member.password),
            member.memberRole!!.map{ SimpleGrantedAuthority("ROLE_${it.role}")}
        )
}