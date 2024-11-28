package martproject.MartProject.Kakaopay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PaymentController {

    @Autowired
    private KakaoPayClient kakaoPayClient;

    @Autowired
    PaymentService paymentService;

    @GetMapping("/pay")
    public Mono<String> pay(@RequestParam String itemName, @RequestParam int quantity, @RequestParam int totalAmount) {
        return kakaoPayClient.requestPayment(itemName, quantity, totalAmount);
    }

    @GetMapping("/approval")
    public String approval(@RequestParam String pg_token, @RequestParam Long paymentId) {
        paymentService.updatePaymentStatus(paymentId, "APPROVED");
        return "Payment approved: " + pg_token;
    }

    @GetMapping("/cancel")
    public String cancel(@RequestParam Long paymentId) {
        paymentService.updatePaymentStatus(paymentId, "CANCELLED");
        return "Payment cancelled";
    }

    @GetMapping("/fail")
    public String fail(@RequestParam Long paymentId) {
        paymentService.updatePaymentStatus(paymentId, "FAILED");
        return "Payment failed";
    }
}
