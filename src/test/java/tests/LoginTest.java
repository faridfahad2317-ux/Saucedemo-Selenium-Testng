package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;
import utils.ExcelUtils;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtils.readSheet("testdata/loginData.xlsx", "Sheet1");
    }

    @Test(description = "Valid user can log in and land on the inventory page", dataProvider = "loginData")
    public void validLoginShouldNavigateToInventory(String username, String password) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/inventory.html"),
                "Expected inventory page after valid login, but URL was: " + driver.getCurrentUrl());
    }
}
