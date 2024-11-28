package martproject.MartProject.service;

import lombok.extern.slf4j.Slf4j;
import martproject.MartProject.domain.disposal.Disposal;
import martproject.MartProject.domain.disposal.DisposalDto;
import martproject.MartProject.repository.DisposalRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DisposalService {

    private final DisposalRepository disposalRepository;

    public DisposalService(DisposalRepository disposalRepository) {
        this.disposalRepository = disposalRepository;
    }

    public void dispose(DisposalDto.RequestDisposalDto Dto) {
        Disposal disposal = Dto.toEntity();
        disposalRepository.save(disposal);
        log.info("폐기처리 완료");
    }
}
