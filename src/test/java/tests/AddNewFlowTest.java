package tests;

import base.BaseTest;
import enums.MenuItem;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import pages.Flows.FlowsPage;
import pages.Flows.NewFlowPage;

import java.util.List;

public class AddNewFlowTest extends BaseTest {

    private boolean flowCreated = false;
    private FlowsPage flowsPage;

    @DataProvider(name = "flowModules")
    public Object[][] flowModulesProvider() {
        return new Object[][]{
                {List.of("ID Capture", "ID Validation", "Face Capture")}
        };
    }

    @Test(groups = {"regression", "flows"}, dataProvider = "flowModules")
    public void testAddNewFlowWithModules(List<String> desiredModules) {

        HomePage homePage = new HomePage(getPage());
        flowsPage = new FlowsPage(getPage());
        NewFlowPage newFlowPage = new NewFlowPage(getPage());

        homePage.navigateTo(MenuItem.FLOWS);
        flowsPage.clickOnAddNewFlowButton();

        for (String module : desiredModules) {
            newFlowPage.addModuleForFlow(module);
        }

        newFlowPage.editFlowName();

        String newFlowName = newFlowPage.getNewFlowName();

        newFlowPage.clickOnSaveChanges()
                .assertSuccessfulFlowCreationMessageAppeared();

        flowCreated = true;

        homePage.navigateTo(MenuItem.FLOWS);

        List<String> modulesFromFlowsPage = flowsPage
                .filterFlowGrid(newFlowName)
                .getAllFlowModules();
        Assert.assertEquals(modulesFromFlowsPage, desiredModules, "Flow modules are not the same as expected.");
    }

    @Override
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (flowCreated) {
                flowsPage.deleteFlowAndConfirmDeletion();
                flowCreated = false;
            }
        } catch (Exception e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        } finally {
            super.tearDown(result);
        }
    }
}