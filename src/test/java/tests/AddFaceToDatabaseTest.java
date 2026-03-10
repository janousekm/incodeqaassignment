package tests;

import base.BaseTest;
import enums.MenuItem;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;
import pages.Identities.IdentitiesPage;
import pages.Identities.SingleIdentityPage;
import pages.Sessions.SingleSessionDetailsPage;
import pages.Sessions.SessionsPage;
import utils.ConfigReader;

public class AddFaceToDatabaseTest extends BaseTest {

    private boolean faceAdded = false;
    private String name;

    @Test(groups = {"smoke", "sessions"})
    public void testAddFaceAppearsInIdentitiesTab() {
        HomePage homePage = new HomePage(getPage());
        SessionsPage sessionsPage = new SessionsPage(getPage());

        homePage.navigateTo(MenuItem.SESSIONS);
        sessionsPage.filterBySessionID(ConfigReader.get("sessionID"));
        name = sessionsPage.getFilteredSessionName();
        SingleSessionDetailsPage detailsPage = sessionsPage.openFirstFilteredSession();
        detailsPage.clickAddFaceToDatabaseButton()
                .assertSuccessfulMessageAppeared();
        faceAdded = true;
    }

    @Override
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (faceAdded) {
                HomePage homePage = new HomePage(getPage());
                IdentitiesPage identitiesPage = new IdentitiesPage(getPage());
                SingleIdentityPage singleIdentityPage = new SingleIdentityPage(getPage());

                homePage.navigateTo(MenuItem.IDENTITIES);
                identitiesPage.filterByName(name)
                        .openFirstIdentityFromGrid();
                singleIdentityPage.deleteIdentity();
                faceAdded = false;
            }
        } catch (Exception e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        } finally {
            super.tearDown(result);
        }
    }
}

