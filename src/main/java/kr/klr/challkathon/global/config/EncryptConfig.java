package kr.klr.challkathon.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

@Configuration
public class EncryptConfig {
    
    @Value("${spring.aes.password:default-password-for-aes}")
    private String password;
    
    @Value("${spring.aes.salt:deadbeef}")  // hex 형식의 기본값 사용
    private String salt;

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor(password, salt);
    }
}
