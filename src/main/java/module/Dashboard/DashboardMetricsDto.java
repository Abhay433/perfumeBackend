package module.Dashboard;

public class DashboardMetricsDto {

    private Long totalFlacons;
    private Long totalInventoryUnits;
    private Long lowStockCount;
    private Long totalFamilies;

    public DashboardMetricsDto() {
    }

    public DashboardMetricsDto(Long totalFlacons, Long totalInventoryUnits, Long lowStockCount, Long totalFamilies) {
        this.totalFlacons = totalFlacons;
        this.totalInventoryUnits = totalInventoryUnits;
        this.lowStockCount = lowStockCount;
        this.totalFamilies = totalFamilies;
    }

    public Long getTotalFlacons() {
        return totalFlacons;
    }

    public void setTotalFlacons(Long totalFlacons) {
        this.totalFlacons = totalFlacons;
    }

    public Long getTotalInventoryUnits() {
        return totalInventoryUnits;
    }

    public void setTotalInventoryUnits(Long totalInventoryUnits) {
        this.totalInventoryUnits = totalInventoryUnits;
    }

    public Long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(Long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public Long getTotalFamilies() {
        return totalFamilies;
    }

    public void setTotalFamilies(Long totalFamilies) {
        this.totalFamilies = totalFamilies;
    }

    @Override
    public String toString() {
        return "DashboardMetricsDto [totalFlacons=" + totalFlacons + ", totalInventoryUnits=" + totalInventoryUnits
                + ", lowStockCount=" + lowStockCount + ", totalFamilies=" + totalFamilies + "]";
    }
}
