package com.springboot.careassist.config;

import com.springboot.careassist.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyUserDetailsService myUserDetailsService ;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(HttpMethod.POST,"/api/patient/add").permitAll()
                        // login API
                        .requestMatchers(HttpMethod.GET,"/api/auth/login").authenticated()
                        // user details API
                        .requestMatchers(HttpMethod.GET,"/api/auth/user-details").authenticated()
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()

                        // Admin onboard API (permitAll to add) (After addition - denyAll)
                        .requestMatchers(HttpMethod.POST,"/api/admin/add").denyAll()

                        //Insurer APIS
                        .requestMatchers(HttpMethod.POST,"/api/insurer/add").hasAuthority("ADMIN")

                        //Patient APIs
                        .requestMatchers(HttpMethod.PUT,"/api/patient/update").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/api/patient/view").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/api/patient/claim/all").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/api/patient/all").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/patient/{patientId}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/patient/deactive/{patientId}").hasAuthority("ADMIN")

                        //HealthCareProvider APIs
                        .requestMatchers(HttpMethod.POST,"/api/healthcareprovider/add").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/healthcareprovider/update").hasAuthority("HEALTHCAREPROVIDER")
                        .requestMatchers(HttpMethod.GET,"/api/healthcareprovider/claim/all").hasAuthority("HEALTHCAREPROVIDER")
                        .requestMatchers(HttpMethod.GET,"/api/healthcareprovider/view").hasAuthority("HEALTHCAREPROVIDER")
                        .requestMatchers(HttpMethod.GET,"/api/healthcareprovider/all").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/healthcareprovider/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/healthcareprovider/deactive/{providerId}").hasAuthority("ADMIN")

                        //InsurancePlan APIs
                        .requestMatchers(HttpMethod.POST,"/api/insuranceplan/add").hasAuthority("INSURER")
                        .requestMatchers(HttpMethod.PUT,"/api/insuranceplan/update/{planId}").hasAuthority("INSURER")
                        .requestMatchers(HttpMethod.GET,"/api/insuranceplan/all").hasAnyAuthority("PATIENT","INSURER")

                        //Policy APIs
                        .requestMatchers(HttpMethod.POST,"/api/policy/add/{planId}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/api/policy/all").hasAuthority("PATIENT")

                        //Claim APIs
                        .requestMatchers(HttpMethod.POST,"/api/claim/add/cashless/{policyId}").hasAuthority("HEALTHCAREPROVIDER")
                        .requestMatchers(HttpMethod.POST,"/api/claim/add/{policyId}").hasAuthority("PATIENT")
                        .requestMatchers(HttpMethod.GET,"/api/claim/filter").hasAuthority("INSURER")
                        .requestMatchers(HttpMethod.PUT,"/api/claim/approve/{claimId}").hasAuthority("INSURER")
                        .requestMatchers(HttpMethod.GET,"/api/claim/invoice/{claimId}").hasAuthority("INSURER")
                        .requestMatchers(HttpMethod.PUT,"/api/claim/pay/{claimId}").hasAuthority("INSURER")
                        .requestMatchers(HttpMethod.PUT,"/api/claim/reject/{claimId}").hasAuthority("INSURER")


                        //Invoice APIs
                        .requestMatchers(HttpMethod.POST,"/api/invoice/add/{claimId}").hasAuthority("HEALTHCAREPROVIDER")

                        // Claim Document APIs
                        .requestMatchers(HttpMethod.POST, "/api/claim/documents/{claimId}").hasAnyAuthority("PATIENT", "HEALTHCAREPROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/claim/documents/{claimId}").hasAnyAuthority("PATIENT", "HEALTHCAREPROVIDER", "INSURER")
                        .requestMatchers(HttpMethod.GET, "/api/claim/documents/download/{documentId}").hasAnyAuthority("PATIENT", "HEALTHCAREPROVIDER", "INSURER")

                        .anyRequest().permitAll()

                );

        // Add jwt filter before Spring's Auth Filter
        http.addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class);

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public AuthenticationProvider getProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(getEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }
}
