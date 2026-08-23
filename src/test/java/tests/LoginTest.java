package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";

    @Test(description = "Valid user can log in and land on the inventory page")
    public void validLoginShouldNavigateToInventory() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/inventory.html"),
                "Expected inventory page after valid login, but URL was: " + driver.getCurrentUrl());
    }
}
