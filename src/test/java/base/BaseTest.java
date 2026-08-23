package base;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    private static final String APP_URL = "https://www.saucedemo.com/";
    private static final String SAUCE_HUB = "https://ondemand.eu-central-1.saucelabs.com:443/wd/hub";

    protected WebDriver driver;
    private boolean sauceExecution;

    @Parameters("execution")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("local") String execution) {
        sauceExecution = "sauce".equalsIgnoreCase(execution);
        if (sauceExecution) {
            driver = createSauceLabsDriver();
        } else {
            driver = createLocalChromeDriver();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(APP_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (sauceExecution && driver instanceof RemoteWebDriver) {
            String status = result.isSuccess() ? "passed" : "failed";
            ((RemoteWebDriver) driver).executeScript("sauce:job-result=" + status);
        }
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriver createLocalChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        return new org.openqa.selenium.chrome.ChromeDriver(options);
    }

    private WebDriver createSauceLabsDriver() {
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        if (username == null || username.isBlank() || accessKey == null || accessKey.isBlank()) {
            throw new IllegalStateException(
                    "SAUCE_USERNAME and SAUCE_ACCESS_KEY environment variables must be set for Sauce Labs execution.");
        }

        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 11");
        browserOptions.setBrowserVersion("latest");

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", username);
        sauceOptions.put("accessKey", accessKey);
        sauceOptions.put("build", "saucedemo-pom-build");
        sauceOptions.put("name", "SauceDemo Login Test");
        browserOptions.setCapability("sauce:options", sauceOptions);

        try {
            return new RemoteWebDriver(new URL(SAUCE_HUB), browserOptions);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Sauce Labs hub URL: " + SAUCE_HUB, e);
        }
    }

}
