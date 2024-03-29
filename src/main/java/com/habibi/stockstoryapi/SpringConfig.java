package com.habibi.stockstoryapi;

import com.habibi.stockstoryapi.auth.JwtAuthFilter;
import com.habibi.stockstoryapi.auth.JwtUtils;
import com.habibi.stockstoryapi.repository.StockPositionStoryRepository;
import com.habibi.stockstoryapi.repository.StockPurchaseRecordRepository;
import com.habibi.stockstoryapi.repository.StockSellRecordRepository;
import com.habibi.stockstoryapi.repository.UserRepository;
import com.habibi.stockstoryapi.service.*;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@PropertySources({
        @PropertySource("classpath:.env")
})
@EnableWebSecurity
@EnableMethodSecurity
@EnableTransactionManagement
public class SpringConfig {
    private final StockPositionStoryRepository stockPositionStoryRepository;
    private final StockPurchaseRecordRepository stockPurchaseRecordRepository;
    private final StockSellRecordRepository stockSellRecordRepository;
    private final UserRepository userRepository;
    @Autowired
    public SpringConfig(
            StockPositionStoryRepository stockPositionStoryRepository,
            StockPurchaseRecordRepository stockPurchaseRecordRepository,
            StockSellRecordRepository stockSellRecordRepository,
            UserRepository userRepository
    ){
        this.stockPositionStoryRepository = stockPositionStoryRepository;
        this.stockPurchaseRecordRepository = stockPurchaseRecordRepository;
        this.stockSellRecordRepository = stockSellRecordRepository;
        this.userRepository = userRepository;
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
                .cors(withDefaults())
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
        return new StockStoryServiceImpl(stockPurchaseRecordRepository, stockSellRecordRepository, stockPositionStoryRepository, stockCodeToNameMapper(), ownStockService());
    }

    @Bean
    public UserDetailsService userDetailService(){
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(){
        return new JwtAuthFilter(userDetailService(), jwtUtils());
    }

    @Bean JwtUtils jwtUtils(){
        return new JwtUtils();
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Bean
    public JpaTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
