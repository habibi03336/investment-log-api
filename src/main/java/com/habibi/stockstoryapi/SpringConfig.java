package com.habibi.stockstoryapi;

import com.habibi.stockstoryapi.auth.JwtAuthFilter;
import com.habibi.stockstoryapi.auth.JwtUtils;
import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import com.habibi.stockstoryapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@PropertySources({
        @PropertySource("classpath:.env")
})
@EnableWebSecurity
public class SpringConfig {
    private final StockPositionStoryRepository stockPositionStoryRepository;
    private final StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private final StockSellRecordRepository stockSellRecordRepository;
    @Autowired
    public SpringConfig(
            StockPositionStoryRepository stockPositionStoryRepository,
            StockPurchaseRecordRepository stockPurchaseRecordRepository,
            StockSellRecordRepository stockSellRecordRepository

    ){
        this.stockPositionStoryRepository = stockPositionStoryRepository;
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
                        httpSecurityCsrfConfigurer.disable();
                    }
                })
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers("/api/auth/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public StockCodeToNameMapper stockCodeToNameMapper(){
        return new StockCodeToNameMapperByFinanceAPI();
    }

    @Bean
    public OwnStockService ownStockService() {
        return new OwnStockServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockCodeToNameMapper());
    }

    @Bean
    public RealizedStockService realizedStockService() {
        return new RealizedStockServiceImpl(stockSellRecordRepository, stockCodeToNameMapper());
    }

    @Bean
    public StockRecordService stockRecordService() {
        return new StockRecordServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockCodeToNameMapper());
    }

    @Bean
    public StockStoryService stockStoryService() {
        return new StockStoryServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockPositionStoryRepository, stockCodeToNameMapper());
    }

    @Bean
    public UserDetailsService userDetailService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(){
        return new JwtAuthFilter(userDetailService(), jwtUtils());
    }

    @Bean JwtUtils jwtUtils(){
        return new JwtUtils();
    }

}
