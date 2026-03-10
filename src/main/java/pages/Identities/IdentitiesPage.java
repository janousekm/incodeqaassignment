package pages.Identities;

import com.microsoft.playwright.Page;
import utils.BasePage;
import utils.DataGridHelper;

public class IdentitiesPage extends BasePage {
    private final DataGridHelper dataGrid;

    public IdentitiesPage(Page page) {
        super(page);
        this.dataGrid = new DataGridHelper(page);
    }


    //Business logic
    public IdentitiesPage openFirstIdentityFromGrid()
    {
        dataGrid.waitForRow(1, 10);
        dataGrid.clickRow(1);
        logStep("DataGrid is filtered successfully!");
        return this;
    }

    public IdentitiesPage filterByName(String name) {
        logStep("Filtering sessions by name: " + name);
        dataGrid.waitForRow(1, 5);
        dataGrid.clickAddFilterButton();
        dataGrid.clickFilterOption("Name");
        dataGrid.fillFilterByNameInput(name);
        dataGrid.clickApplyFilterButton();
        return this;
    }


}
