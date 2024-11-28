package martproject.MartProject.Controller;

import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.disposal.Disposal;
import martproject.MartProject.domain.disposal.DisposalDto;
import martproject.MartProject.domain.stock.Stock;
import martproject.MartProject.repository.DisposalRepository;
import martproject.MartProject.repository.StockRepository;
import martproject.MartProject.service.DisposalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
public class DisposalController {

    private DisposalService disposalService;
    private final DisposalRepository disposalRepository;
    private final StockRepository stockRepository;

    public DisposalController(DisposalRepository disposalRepository, StockRepository stockRepository, DisposalService disposalService) {
        this.disposalRepository = disposalRepository;
        this.stockRepository = stockRepository;
        this.disposalService = disposalService;
    }

    @GetMapping("disposal")
    public String disposal(Model model) {
        List<Disposal> disposalList = this.disposalRepository.findAll();
        model.addAttribute("disposalList", disposalList);
        return "disposal";
    }

    @PostMapping("disposal")
    public String disposal(@ModelAttribute("disposal") Disposal disposal) {
        LocalDate today = LocalDate.now();

        List<Stock> stockList = stockRepository.findAll();

        for (Stock stock : stockList) {
            if(stock.getStock_expiration_date().isBefore(today)) {
                DisposalDto.RequestDisposalDto disposalDto = DisposalDto.RequestDisposalDto.builder()
                        .goods(stock.getGoods())
                        .disposal_amount(stock.getStock())
                        .disposal_date(today)
                        .disposal_expiration_date(stock.getStock_expiration_date())
                        .build();

                disposalService.dispose(disposalDto);
                stockRepository.delete(stock);
            }
        }

        return "redirect:/disposal";
    }
}
