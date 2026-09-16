package module.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    @Query("SELECT COALESCE(SUM(p.stock_quantity), 0) FROM ProductEntity p")
    Long sumTotalInventoryUnits();

    @Query("SELECT COUNT(p) FROM ProductEntity p WHERE p.stock_quantity > 0 AND p.stock_quantity <= 20")
    Long countLowStockReserves();
}
