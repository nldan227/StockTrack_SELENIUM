package Utils;

import SetUp.Setup;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.qameta.allure.Allure;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class utils {
    public static void closeAd(WebElement element){
        element.click();
    }
    public static void scrollDown(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, document.body.scrollHeight)");
    }
    public static void waitForElement(WebDriver driver, WebElement element, int TIME_UNIT_SECONDS) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIME_UNIT_SECONDS));
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    public static String generateValidEmail() {
        String alias = generateText(4, 6); // Tạo alias ngẫu nhiên từ 4 đến 5 ký tự
        return "nguyenlinhdan227+" + alias + "@gmail.com";
    }
    public static String generateInvalidEmail() {
        String alias = generateText(10, 15); // Tạo alias ngẫu nhiên từ 4 đến 5 ký tự
        return alias + "@gmail.com";
    }
    public static String generateUsername(){
        String randomText = generateText(2,4);
        return "user" + randomText;
    }
    public static String getCurrentDate() {
        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        // Định dạng ngày theo kiểu dd-MM-yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        // Trả về ngày hiện tại theo định dạng mong muốn
        return currentDate.format(formatter);
    }
    public static String generateText(int minLength, int maxLength) {
        Random random = new Random();
        int length = minLength + random.nextInt(maxLength - minLength + 1); // Độ dài ngẫu nhiên trong khoảng [minLength, maxLength]
        StringBuilder text = new StringBuilder(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Các ký tự có thể dùng trong alias
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            text.append(characters.charAt(index));
        }
        return text.toString();
    }
    // Hàm tạo số ngẫu nhiên với số chữ số tùy chọn
    public static long randomNumber(int digit) {
        if (digit <= 0) {
            throw new IllegalArgumentException("Số chữ số phải lớn hơn 0");
        }
        Random random = new Random();
        if (digit == 1) {
            // Sinh số ngẫu nhiên từ 1 đến 9 (không bao gồm 0)
            return 1 + random.nextInt(9);  // nextInt(9) sinh số từ 0 đến 8, cộng 1 để có số từ 1 đến 9
        }
        long min = (long) Math.pow(10, digit - 1);  // Giá trị nhỏ nhất có 'digit' chữ số
        long max = (long) Math.pow(10, digit) - 1;  // Giá trị lớn nhất có 'digit' chữ số
        return min + (long) (random.nextDouble() * (max - min));  // Sinh số ngẫu nhiên trong khoảng [min, max]
    }
    public static Map<String, List<String>> readCSV(String filePath) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        String[] header = reader.readNext(); // Đọc dòng tiêu đề, nhưng không làm gì với nó
        List<String[]> records = reader.readAll();
        reader.close();

        Map<String, List<String>> supplierProductMap = new HashMap<>();
        for (String[] record : records){
            String supplier = record[0].trim();
            String product = record[1].trim();

            if(!supplierProductMap.containsKey(supplier)){
                supplierProductMap.put(supplier, new ArrayList<>());
            }
            supplierProductMap.get(supplier).add(product);
        }
        return supplierProductMap;
    }

    public static void checkAlertMessage(SoftAssert softAssert, WebDriver driver, String expectedMessage) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Kiểm tra sự xuất hiện của Alert
            wait.until(ExpectedConditions.alertIsPresent());

            // Chuyển đến Alert và lấy thông báo
            Alert alert = driver.switchTo().alert();
            String actualMessage = alert.getText();

            // Kiểm tra và log thông báo Alert
            verifyAndLog(driver,"Alert", actualMessage, expectedMessage, softAssert);

            // Đóng Alert
            Allure.step("Đóng Alert", () -> {
                alert.dismiss();
                Allure.step("Alert đã được đóng thành công.");
            });

        } catch (org.openqa.selenium.TimeoutException e) {
            // Xử lý trường hợp Alert không xuất hiện
            verifyAndLog(driver, "Alert", "Không tìm thấy Alert", expectedMessage, softAssert);
        }
    }

    // Phương thức kiểm tra giá trị và log vào Allure
    private static void verifyAndLog(WebDriver driver, String fieldName, String actualValue, String expectedValue, SoftAssert softAssert) {
        Allure.step("Kiểm tra " + fieldName, () -> {
            if (!expectedValue.equals(actualValue)) {
                // Khi giá trị không khớp
                AllureTestListener.saveScreenshotPNG(driver);
                Allure.addAttachment("Thông báo lỗi " + fieldName, "Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            } else {
                // Khi giá trị khớp
                Allure.step("Thông báo " + fieldName + " khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
            }
            // Thêm vào kiểm tra với SoftAssert
            softAssert.assertTrue(expectedValue.equals(actualValue),
                    "Thông báo lỗi " + fieldName + " không khớp. Mong đợi: " + expectedValue + " | Thực tế: " + actualValue);
        });
    }

}
