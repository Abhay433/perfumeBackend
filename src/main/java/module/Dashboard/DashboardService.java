package module.Dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import module.Category.CategoryRepository;
import module.Product.ProductRepository;

@Service
public class DashboardService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public DashboardMetricsDto getMetrics() {
        Long totalFlacons = productRepository.count();
        Long totalInventoryUnits = productRepository.sumTotalInventoryUnits();
        Long lowStockCount = productRepository.countLowStockReserves();
        Long totalFamilies = categoryRepository.count();

        return new DashboardMetricsDto(
                totalFlacons != null ? totalFlacons : 0L,
                totalInventoryUnits != null ? totalInventoryUnits : 0L,
                lowStockCount != null ? lowStockCount : 0L,
                totalFamilies != null ? totalFamilies : 0L);
    }
}
