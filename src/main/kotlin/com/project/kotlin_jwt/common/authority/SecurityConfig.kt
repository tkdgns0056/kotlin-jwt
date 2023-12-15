package com.project.kotlin_jwt.common.authority

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {
    // 시큐리티 필터 설정
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain{
        http
            .httpBasic { it.disable() } // 끈다.
            .csrf { it.disable() } // 끈다.
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // jwt를 사용하기떄문에 세션 사용 안한다는 듯
            .authorizeHttpRequests {
                it.requestMatchers("/api/member/signup", "/api/member/login").anonymous()
                    .requestMatchers("/api/member/**").hasRole("MEMBER")
                    .anyRequest().permitAll() // 위 url를 호출하는 사람은 인증되지 않는 사람은 x 그외에는 아무나 접근 가능
            }
            .addFilterBefore( // 앞에 있는 필터가 뒤웨있는 필터보다 먼저 실행되라는뜻
                JwtAuthenticationFilter(jwtTokenProvider), // 이 필터가 통과하면 뒤에 필터는 실행 x
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }

    // 코드를 암호화 하기 위해서 만듦
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
}