package com.AceboIvanPruebaTec.Agencia.Reserva.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()//csrf is disabled for simplicity
                .authorizeHttpRequests()
                .requestMatchers("/agency/hotels/all").permitAll()
                .requestMatchers("/agency/flights/all").permitAll()
                .requestMatchers("/agency/hotels/rooms/all").permitAll()
                .requestMatchers("/agency/hotels/{id}").permitAll()
                .requestMatchers("/agency/flights/{id}").permitAll()
                .requestMatchers("/agency/hotels/rooms/{id}").permitAll()
                .requestMatchers("/agency/hotels/available-rooms").permitAll()
                .requestMatchers("/agency/hotel-booking/new").permitAll()
                .requestMatchers("/agency/flights/filter").permitAll()
                .requestMatchers("agency/flight-booking/new").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .httpBasic()
                .and()
                .build();
    }

}