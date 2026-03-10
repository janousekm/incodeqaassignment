package tests;

import base.BaseTest;
import enums.MenuItem;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.Sessions.SingleSessionDetailsPage;
import pages.Sessions.SessionsPage;
import utils.ConfigReader;

public class VerifyNameWithOCRNameTest extends BaseTest {

    @Test(groups = {"smoke", "sessions"})
    public void testVerifyNameWithOCRName() {

        HomePage homePage = new HomePage(getPage());
        SessionsPage sessionsPage = new SessionsPage(getPage());

        homePage.navigateTo(MenuItem.SESSIONS);
        String nameValueFromTable = sessionsPage
                .filterBySessionID(ConfigReader.get("SESSION_ID"))
                .getFilteredSessionName();
        SingleSessionDetailsPage detailsPage = sessionsPage.openFirstFilteredSession();
        String nameValueFromSession = detailsPage.getOcrFullName();

        Assert.assertEquals(nameValueFromTable, nameValueFromSession, "Assertion failed! Name in table was: " + nameValueFromTable + " but Full Name (OCR) was: " + nameValueFromSession);

    }


}