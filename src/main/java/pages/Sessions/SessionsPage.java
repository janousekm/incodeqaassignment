package pages.Sessions;

import com.microsoft.playwright.Page;
import utils.BasePage;
import utils.DataGridHelper;

public class SessionsPage extends BasePage {

    private final DataGridHelper dataGrid;

    public SessionsPage(Page page) {
        super(page);
        this.dataGrid = new DataGridHelper(page);
    }


    //Business logic
    public String getFilteredSessionName() {
        String cellValue = dataGrid.getCellValue(1, "Name").toLowerCase();
        logStep("Retrieved session name from table: " + cellValue);
        return cellValue;
    }

    public SingleSessionDetailsPage openFirstFilteredSession() {
        logStep("Opening first filtered session");
        dataGrid.clickRow(1);
        return new SingleSessionDetailsPage(page);
    }

    public SessionsPage filterBySessionID(String sessionId) {
        logStep("Filtering sessions by session ID: " + sessionId);
        dataGrid.clickAddFilterButton();
        dataGrid.clickFilterOption("Session Data", "Session ID");
        dataGrid.fillFilterBySessionIDInput(sessionId);
        dataGrid.clickApplyFilterButton();
        return this;
    }


}
