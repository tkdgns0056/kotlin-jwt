package com.project.kotlin_jwt.member.service

import com.project.kotlin_jwt.common.authority.JwtTokenProvider
import com.project.kotlin_jwt.common.authority.TokenInfo
import com.project.kotlin_jwt.common.exception.InvalidInputException
import com.project.kotlin_jwt.common.status.ROLE
import com.project.kotlin_jwt.member.dto.LoginDto
import com.project.kotlin_jwt.member.dto.MemberDtoRequest
import com.project.kotlin_jwt.member.dto.MemberDtoResponse
import com.project.kotlin_jwt.member.entity.MemberRole
import com.project.kotlin_jwt.member.entity.Members
import com.project.kotlin_jwt.member.repository.MemberRepository
import com.project.kotlin_jwt.member.repository.MemberRoleRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    /**
     * 회원가입
     */
    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        // ID 중복 검사
        var member: Members? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if(member != null){
            throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")
        }

         member = memberDtoRequest.toEntity()
         memberRepository.save(member)

//        member = Members(
//            null,
//            memberDtoRequest.loginId,
//            memberDtoRequest.password,
//            memberDtoRequest.name,
//            memberDtoRequest.birthDate,
//            memberDtoRequest.gender,
//            memberDtoRequest.email
//        )

        // 회원가입 시 권한까지 같이 저장
        val memberRole: MemberRole = MemberRole(null, ROLE.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원가입이 완료되었습니다."
    }

    /**
     * 로그인 -> 토큰 발행
     */
    fun login(loginDto: LoginDto): TokenInfo { // 사용자로부터 아이디받고 토큰인포로 발행하여 담아사 돌려줌
        // UsernamePasswordAuthenticationToken 발행
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, loginDto.password)
        // authenticate 실행될때 커스텀유저디테일 서비스에 loadByUser 에 전해주고 비교하여 토큰 발행해줌.
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

    /**
     * 내 정보 조회
     */
    fun searchMyInfo(id: Long): MemberDtoResponse {
        val member: Members = memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "회원번호 (${id}) 가 존재하지 않습니다.")

        return member.toDto();
    }

    /**
     * 내 정보 수정
     */
    fun saveMyInfo(memberDtoRequest: MemberDtoRequest): String {
        val member: Members = memberDtoRequest.toEntity()
        memberRepository.save(member)
        return "수정완료 되었습니다."
    }
}
