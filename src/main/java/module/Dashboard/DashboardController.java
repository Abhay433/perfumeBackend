package module.Dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comman.response.ApiResponse;

@RestController
@RequestMapping({ "/api/admin/dashboard", "/api/dashboard" })
public class DashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    @GetMapping({ "", "/metrics" })
    public ResponseEntity<ApiResponse<DashboardMetricsDto>> getMetrics() {
        LOGGER.info("Fetching dashboard metrics");
        DashboardMetricsDto metrics = dashboardService.getMetrics();
        return ResponseEntity.ok(ApiResponse.success("Dashboard metrics retrieved successfully", metrics));
    }
}
