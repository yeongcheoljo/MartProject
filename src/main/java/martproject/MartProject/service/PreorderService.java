package martproject.MartProject.service;

import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.preorderGoods.PreorderGoods;
import martproject.MartProject.domain.preorderGoods.PreorderGoodsDto;
import martproject.MartProject.domain.preorderInfo.PreorderInfo;
import martproject.MartProject.domain.preorderInfo.PreorderInfoDto;
import martproject.MartProject.repository.PreorderGoodsRepository;
import martproject.MartProject.repository.PreorderInfoRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PreorderService {

    private final PreorderGoodsRepository preorderGoodsRepository;
    private final PreorderInfoRepository preorderInfoRepository;

    public PreorderService(PreorderGoodsRepository preorderGoodsRepository, PreorderInfoRepository preorderInfoRepository) {
        this.preorderGoodsRepository = preorderGoodsRepository;
        this.preorderInfoRepository = preorderInfoRepository;
    }

    public void RegistPreorderStock(PreorderGoodsDto.RequestPreorderGoodsDto Dto){
        PreorderGoods preorderGoods = Dto.toEntity();
        preorderGoodsRepository.save(preorderGoods);
        log.info("예약 상품 등록 완료");
    }

    public void RegistPreorder(PreorderInfoDto.RequestPreorderInfoDto Dto){
        PreorderInfo preorderInfo = Dto.toEntity();
        preorderInfoRepository.save(preorderInfo);
        log.info("예약 완료");
    }
}
