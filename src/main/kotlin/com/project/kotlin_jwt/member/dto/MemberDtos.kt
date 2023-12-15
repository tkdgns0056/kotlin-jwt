package com.project.kotlin_jwt.member.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.project.kotlin_jwt.common.annotation.ValidEnum
import com.project.kotlin_jwt.common.status.Gender
import com.project.kotlin_jwt.member.entity.Members
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MemberDtoRequest(

    var id: Long?, // private을 할 경우 null로 에러가 떨어졌음 이유가 뭘까?

    @field:NotBlank
    @JsonProperty("loginId") // 사용자로부터 _로 받는게 아니기 때문에 _로그인아이디와 로그인 아이디를 연결 시키기 위해서 사용함.
    private val _loginId: String?,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}\$",
        message = "영문, 숫자, 특수문자를 포함한 8-20자리로 입력해주세요"
    )
    @JsonProperty("password")
    private val _password: String?,

    @field:NotBlank
    @JsonProperty("name")
    private val _name: String?,

    @field:NotBlank
    @field:Pattern(
        regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$",
        message = "날짜형식(YYYY-MM-DD)을 확인해주세요"
    )
    @JsonProperty("birthDate")
    private val _birthDate: String?,

    @field:NotBlank
    @field:Email
    @JsonProperty("email")
    private val _email: String?,

    @field:NotBlank
    @field:ValidEnum(enumClass = Gender::class, message = "MAN 이나 WOMAN 중 하나를 선택해주세요.")
    @JsonProperty("gender")
    private val _gender: String?,
){
    val loginId: String
        get() = _loginId!!
    val password: String
        get() = _password!!
    val name: String
        get() = _name!!
    val birthDate: LocalDate
        get() = _birthDate!!.toLocalDate()
    val email: String
        get() = _email!!
    val gender: Gender
        get() = Gender.valueOf(_gender!!)

    // 확장 함수
    private fun String.toLocalDate(): LocalDate =
        LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    fun toEntity(): Members =
        Members(id, loginId, password, name, birthDate, gender, email)
}

data class LoginDto (
    @field:NotBlank
    @JsonProperty("loginId")
    private val _loginId: String?,

    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?,
) {
    val loginId: String
        get() =_loginId!!
    val password: String
        get() = _password!!
}

data class MemberDtoResponse (
    val id: Long,
    val loginId: String,
    val name: String,
    val birthDate: String,
    val gender: String,
    val email: String,
)