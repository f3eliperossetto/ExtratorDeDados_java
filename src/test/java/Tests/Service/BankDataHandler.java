package Tests.Service;

import enums.InstanceRegistryHandler;
import abstractions.Handler;
import FileModels.BankDataFile;

public class BankDataHandler extends Handler<BankDataFile> {

    private static final String clientData = "00";
    private static final String financialData = "02";
    private static final String documentData = "01";

    @Override
    public void setBuildingFile() {
        addCommand((line)->line.startsWith(clientData), (line)->registry.fillData(line), InstanceRegistryHandler.CREATE_NEW_REGISTRY_INSTANCE);
        addCommand((line)->line.startsWith(financialData), (line)->registry.addFinancialData(line));
        addCommand((line)->line.startsWith(documentData), (line)->registry.getDocument().fillData(line));
    }

    @Override
    public BankDataFile newInstance() {
        return new BankDataFile();
    }
}
