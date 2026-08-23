# SauceDemo Selenium TestNG Framework

A robust, scalable UI Test Automation Framework built using Java, Selenium WebDriver, TestNG, and the Page Object Model (POM) design pattern. Features cross-browser cloud execution support via Sauce Labs.

---

## Tech Stack & Tools

* Language: Java
* Automation Library: Selenium WebDriver (v4.x)
* Test Runner / Framework: TestNG
* Design Pattern: Page Object Model (POM)
* Build Tool: Apache Maven
* Cloud Infrastructure: Sauce Labs (EU Central Hub)

---

## Project Structure

* src/test/java/base/BaseTest.java - Driver initialization, configuration & teardown
* src/test/java/pages/LoginPage.java - Page Object Model web elements & actions
* src/test/java/tests/LoginTest.java - Executable TestNG test scenarios
* testng-sauce.xml - TestNG suite for Sauce Labs cloud runs
* pom.xml - Project dependencies & plugin configurations

---

## Execution Instructions

### Prerequisites
* JDK 11 or higher
* Apache Maven
* Sauce Labs account credentials (SAUCE_USERNAME & SAUCE_ACCESS_KEY)

### Local Execution
To execute tests locally using Chrome:
```bash
mvn test
