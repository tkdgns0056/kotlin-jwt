package com.project.kotlin_jwt.member.controller

import com.project.kotlin_jwt.common.authority.TokenInfo
import com.project.kotlin_jwt.common.status.dto.BaseResponse
import com.project.kotlin_jwt.common.status.dto.CustomUser
import com.project.kotlin_jwt.member.dto.LoginDto
import com.project.kotlin_jwt.member.dto.MemberDtoRequest
import com.project.kotlin_jwt.member.dto.MemberDtoResponse
import com.project.kotlin_jwt.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/member")
@RestController
class MemberController(
    private val memberService: MemberService
) {
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    // Unti : 아무것도 없는 값이라고 Unit이락함 void같은 건가 싶음
    fun signUp(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit> {
        val resultMsg: String = memberService.signUp(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }

    /**
     * 내 정보 보기
     */
    @GetMapping("/info")
    fun searchMyInfo(): BaseResponse<MemberDtoResponse> {
        // 토큰에 유저id가 있기 때문에 토큰에서 뽑아옴
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = memberService.searchMyInfo(userId)
        return BaseResponse(data = response)
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/info")
    fun saveMyInfo(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit>{
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        // 처음에 빨가불이 뜬 이유는  memberRequestDto 에서 id 값이 val로 되어있기 떄문이다.
        // val은 변경이 불가능한 변수인데 변경을 시도하기 때문에 빨간불 에러가 난 것.
        // memberRequestDto 에서 val -> var 변경 해야 에러가 안난다.
        memberDtoRequest.id = userId
        val resultMsg: String = memberService.saveMyInfo(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }
}