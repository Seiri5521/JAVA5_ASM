package com.poly.config;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration // Thêm annotation @Configuration để Spring Boot nhận diện lớp này là lớp cấu hình.
public class PaypalConfig {

    @Value("${paypal.client.id}") // Sửa từ 'paypal.client.app' thành 'paypal.client.id'
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", mode);
        return sdkConfig;
    }

    @Bean
    public OAuthTokenCredential authTokenCredential() {
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        @SuppressWarnings("deprecation")
		APIContext apiContext = new APIContext(authTokenCredential().getAccessToken());
        apiContext.setConfigurationMap(paypalSdkConfig());
        return apiContext;
    }
}

/*
 * @Configuration public class PaypalConfig {
 * 
 * // Sửa tên biến theo @Value annotation để khớp với cấu hình
 * 
 * @Value("${paypal.client.id}") private String clientId;
 * 
 * @Value("${paypal.client.secret}") private String clientSecret;
 * 
 * @Value("${paypal.mode}") private String mode;
 * 
 *//**
	 * Cấu hình cho SDK PayPal
	 * 
	 * @return Map cấu hình cho PayPal SDK
	 */
/*
 * @Bean public Map<String, String> paypalSdkConfig() { Map<String, String>
 * sdkConfig = new HashMap<>(); sdkConfig.put("mode", mode); return sdkConfig; }
 * 
 *//**
	 * Tạo đối tượng OAuthTokenCredential để xác thực ứng dụng
	 * 
	 * @return OAuthTokenCredential với clientId và clientSecret
	 */
/*
 * @Bean public OAuthTokenCredential authTokenCredential() { return new
 * OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig()); }
 * 
 *//**
	 * Tạo đối tượng APIContext để sử dụng cho các hoạt động của PayPal
	 * 
	 * @return APIContext đã được cấu hình với token và cấu hình SDK
	 *//*
		 * @Bean public APIContext apiContext() { APIContext apiContext = null; try { //
		 * Lấy access token từ OAuthTokenCredential và tạo APIContext apiContext = new
		 * APIContext(authTokenCredential().getAccessToken());
		 * apiContext.setConfigurationMap(paypalSdkConfig()); } catch
		 * (PayPalRESTException e) { // Log hoặc xử lý ngoại lệ nếu cần
		 * System.err.println("Error creating APIContext: " + e.getMessage()); } return
		 * apiContext; } }
		 */