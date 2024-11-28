package martproject.MartProject.Kakaopay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentPageController {
    @GetMapping("/paymentPage")
    public String paymentPage() {
        return "paymentPage";
    }
}
