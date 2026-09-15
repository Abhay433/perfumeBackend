package module.Order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserIdOrderByIdDesc(Long userId);

    Optional<OrderEntity> findByIdAndUserId(Long id, Long userId);

    List<OrderEntity> findAllByOrderByIdDesc();

}
