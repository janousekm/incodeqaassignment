# Incode Assignment

## Tech Stack

| Tool              | Version | Purpose                          |
|-------------------|---------|----------------------------------|
| Java              | 21      | Language                         |
| Playwright        | 1.49.0  | Browser automation               |
| TestNG            | 7.9.0   | Test runner & parallel execution |
| ExtentReports     | 5.1.2   | HTML test reporting              |
| Maven             | 3.x     | Build & dependency management    |

## Setup

### Prerequisites

- Java 21+
- Maven 3.x
- Playwright browsers installed (Should be installed automatically, if it doesn't please run: `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"`)

**Local setup:**

1. Copy the example file:
   ```
   cp src/test/resources/data.local.properties.example src/test/resources/data.local.properties
   ```
2. Fill in your credentials:
   ```properties
   EMAIL=email@example.com
   PASSWORD=password
   ```
3. Or, you can just add variables via PowerShell:
   ```powershell
   # PowerShell
   $env:EMAIL="user@example.com"
   $env:PASSWORD="secret"
   mvn test
   ```

## Running Tests

### Default suite (Chromium, headed)

```bash
mvn test
```

### Cross-browser (Chromium + Firefox + WebKit, headless)

```bash
mvn test "-DsuiteXmlFile=runners/cross-browser.xml"
```

### Run all tests via default browser (Chromium)

```bash
mvn test "-DsuiteXmlFile=runners/testng.xml"
```

## Reports

After a test run, reports are generated at:

- **ExtentReports**: `reports/extent-report.html`
- **Surefire Reports**: `target/surefire-reports/`

Failed tests automatically capture a full-page screenshot embedded in the ExtentReport.