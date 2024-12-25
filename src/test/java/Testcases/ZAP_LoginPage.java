package Testcases;

import Page.LoginPage;
import Page.NavPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.continuumsecurity.proxy.ScanningProxy;
import net.continuumsecurity.proxy.Spider;
import net.continuumsecurity.proxy.ZAProxyScanner;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ZAP_LoginPage {

    private static final String BASE_URL = "http://localhost/NhapKho";
    private static final Logger log = Logger.getLogger(ZAP_LoginPage.class.getName());
    private static final String ZAP_PROXYHOST = "localhost";
    private static final int ZAP_PROXYPORT = 8080;
    private static final String ZAP_APIKEY = "8kfjdm3bhb7kipi6bhpaphnh17";
    private static final String MEDIUM = "MEDIUM";
    private static final String HIGH = "HIGH";

    private ScanningProxy zapScanner;
    private Spider zapSpider;
    private WebDriver driver;
    private LoginPage login;
    private NavPage navPage;
    private int currentScanID;

    @BeforeTest
    public void setup() {
        String proxyStr = ZAP_PROXYHOST + ":" + ZAP_PROXYPORT;
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyStr);
        proxy.setSslProxy(proxyStr);

        ChromeOptions opt = new ChromeOptions();
        opt.setAcceptInsecureCerts(true);
        opt.setProxy(proxy);
        driver = WebDriverManager.chromedriver().capabilities(opt).create();
        login = new LoginPage(driver);

        driver.get(BASE_URL + "/model/login.php");
        try {
            zapScanner = new ZAProxyScanner(ZAP_PROXYHOST, ZAP_PROXYPORT, ZAP_APIKEY);
            log.info("Connected to ZAP Proxy");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to ZAP Proxy", e);
        }


        zapScanner.clear();
        log.info("Started a new session: Scanner");
        zapSpider = (Spider) zapScanner;
        log.info("ZAP API client created");
    }

    @AfterTest
    public void tearDown() throws ClientApiException {

        // Tạo báo cáo trong thư mục chỉ định
        String reportPath = System.getProperty("user.dir") + "/reports/zap-security-report.html";
        generateHtmlReport(reportPath);
        driver.quit();
    }


    private void scanWithZap() {
        log.info("Scanning...");
        zapScanner.scan(BASE_URL);
        currentScanID = zapScanner.getLastScannerScanId();

        try {
            int complete = 0;
            while (complete < 100) {
                // Sử dụng phương thức getScanProgress trả về int
                complete = zapScanner.getScanProgress(currentScanID);
                log.info("Scan is " + complete + "% complete.");
                Thread.sleep(1000);
            }
            log.info("Scanning done.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.severe("Scan was interrupted: " + e.getMessage());
        }
    }

    private void spiderWithZap() {
        zapSpider.setThreadCount(5);
        zapSpider.setMaxDepth(5);
        zapSpider.setPostForms(false);
        zapSpider.spider(BASE_URL);
        try {
            int spiderID = zapSpider.getLastSpiderScanId();
            int complete = 0;
            while (complete < 100) {
                // Sử dụng phương thức getSpiderProgress trả về int
                complete = zapSpider.getSpiderProgress(spiderID);
                log.info("Spider scan is " + complete + "% complete.");
                Thread.sleep(1000);
            }
            // Lấy kết quả spider
            List<String> results = zapSpider.getSpiderResults(spiderID);
            for (String url : results) {
                log.info("Found URL: " + url);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.severe("Spider scan was interrupted: " + e.getMessage());
        }
    }
    private void generateHtmlReport(String reportPath) {
        try {
            log.info("Generating ZAP HTML Report...");

            // Xuất báo cáo HTML từ ZAP
            byte[] report = zapScanner.getHtmlReport();

            // Tạo thư mục nếu nó chưa tồn tại
            File directory = new File(reportPath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Lưu báo cáo HTML vào thư mục mong muốn
            File file = new File(reportPath);
            java.nio.file.Files.write(file.toPath(), report);

            log.info("HTML Report generated successfully at: " + reportPath);
        } catch (Exception e) {
            log.severe("Failed to generate ZAP HTML report: " + e.getMessage());
        }
    }
    @Test(priority = 1)
    public void testSecurityVulnerabilitiesBeforeLogin() {
        log.info("Started spidering");
        spiderWithZap();
        log.info("Ended spidering");

        zapScanner.setEnablePassiveScan(true);

        log.info("Started scanning");
        scanWithZap();
        log.info("Ended scanning");
    }

    @Test(priority = 2)
    public void testSecurityVulnerabilitiesAfterLogin() throws InterruptedException {
        login.doLogin(driver, "user123", "123");
        navPage = new NavPage(driver);
        navPage.navToStockInPage(driver);
        Thread.sleep(4000);
        driver.switchTo().frame("iframe-container");

        log.info("Started spidering");
        spiderWithZap();
        log.info("Ended spidering");

        zapScanner.setEnablePassiveScan(true);

        log.info("Started scanning");
        scanWithZap();
        log.info("Ended scanning");

    }
}
