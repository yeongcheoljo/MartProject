package martproject.MartProject.repository;

import martproject.MartProject.domain.preorderGoods.PreorderGoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreorderGoodsRepository extends JpaRepository<PreorderGoods, Long> {
}
