package Tests.Service;

import enums.StatusImport;
import services.Extractable;
import models.CommandResult;
import services.ExtractService;
import FileModels.BankDataFile;
import org.junit.Test;


import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class SampleTest {
    public SampleTest() {
        service = new ExtractService<>(new BankCommandHandler());
    }

    private final String path = new File("src/test/resources").getAbsolutePath() + "\\";
    private final Extractable<BankDataFile> service;

    @Test
    public void ShouldExtractAllRecords() {
        CommandResult<BankDataFile> result = service.loadDataFromFile(path + "Success.txt");
        assertNotNull(result);
        assertTrue(result.getResult().getIsReadingDone());
        assertEquals(StatusImport.SUCCESS, result.getResult().getStatus());
    }

    @Test
    public void ShouldExtractAllRecordsAsync() throws InterruptedException, ExecutionException, TimeoutException {
        Future<CommandResult<BankDataFile>> resultFuture = service.loadDataFromFileAsync(path + "Success.txt");
        CommandResult<BankDataFile> result = resultFuture.get(30, TimeUnit.SECONDS);
        assertNotNull(result);
        assertTrue(result.getResult().getIsReadingDone());
        assertEquals(StatusImport.SUCCESS, result.getResult().getStatus());
    }

    @Test
    public void ShouldExtractAllRecordsWithAlerts() {
        CommandResult<BankDataFile> result = service.loadDataFromFile(path + "Alerts.txt");
        assertNotNull(result);
        assertTrue(result.getResult().getIsReadingDone());
        assertFalse(result.getResult().getMessages().isEmpty());
        assertEquals(StatusImport.ALERTS, result.getResult().getStatus());
    }
    @Test
    public void ShouldExtractAllRecordsWithErrors() {
        CommandResult<BankDataFile> result = service.loadDataFromFile(path + "Errors.txt");
        assertNotNull(result);
        assertFalse(result.getResult().getIsReadingDone());
        assertEquals(StatusImport.ERROR, result.getResult().getStatus());
    }

}