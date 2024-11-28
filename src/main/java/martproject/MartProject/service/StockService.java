package martproject.MartProject.service;

import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.stock.Stock;
import martproject.MartProject.domain.stock.StockDto;
import martproject.MartProject.repository.StockRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public String stockJoin(StockDto.RequestStockDto dto){
        Stock stock = dto.toEntity();
        stockRepository.save(stock);
        log.info("DB에 재고 저장 성공");

        return stock.getStock_num().toString();
    }

    public String updateStock(StockDto.RequestStockDto dto){
        Stock stock = dto.toEntity();
        stockRepository.save(stock);
        log.info("재고정보 변경 완료");

        return stock.getStock_num().toString();
    }
}
