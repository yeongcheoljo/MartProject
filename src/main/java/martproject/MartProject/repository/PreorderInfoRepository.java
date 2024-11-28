package martproject.MartProject.repository;

import martproject.MartProject.domain.preorderInfo.PreorderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreorderInfoRepository extends JpaRepository<PreorderInfo, Long> {
}
