package martproject.MartProject.Kakaopay;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoPayClient {
    private final WebClient webClient;

    @Value("${kakao.pay.api.url}")
    private String kakaoPayApiUrl;

    @Value("${kakao.pay.api.admin-key}")
    private String adminKey;

    @Value("${kakao.pay.api.cid}")
    private String cid;

    @Value("${kakao.pay.api.approval-url}")
    private String approvalUrl;

    @Value("${kakao.pay.api.cancel-url}")
    private String cancelUrl;

    @Value("${kakao.pay.api.fail-url}")
    private String failUrl;

    public KakaoPayClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(kakaoPayApiUrl).build();
    }

    public Mono<String> requestPayment(String itemName, int quantity, int totalAmount) {
        String requestBody = buildRequestBody(itemName, quantity, totalAmount);

        // Logging request details
        System.out.println("Request URL: " + kakaoPayApiUrl + "/online/v1/payment/ready");
        System.out.println("Request Headers: Authorization=KakaoAK " + adminKey + ", Content-Type=application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("Request Body: " + requestBody);

        return webClient.post()
                .uri("/online/v1/payment/ready")
                .header("Authorization", "KakaoAK " + adminKey)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Response from KakaoPay: " + response))
                .doOnError(error -> {
                    System.err.println("Error during KakaoPay request: " + error.getMessage());
                    error.printStackTrace();
                });
    }

    private String buildRequestBody(String itemName, int quantity, int totalAmount) {
        return "cid=" + cid +
                "&partner_order_id=partner_order_id" +
                "&partner_user_id=partner_user_id" +
                "&item_name=" + itemName +
                "&quantity=" + quantity +
                "&total_amount=" + totalAmount +
                "&vat_amount=" + (totalAmount / 10) +
                "&tax_free_amount=0" +
                "&approval_url=" + approvalUrl +
                "&cancel_url=" + cancelUrl +
                "&fail_url=" + failUrl;
    }
}
