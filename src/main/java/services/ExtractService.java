package services;

import abstractions.CommandHandler;
import enums.InstanceRegistryHandler;
import enums.StatusImport;
import models.CommandHandlerModel;
import models.CommandResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ExtractService<T> implements Extractable<T> {
    private final CommandHandler<T> commandHandler;
    private String path;

    public ExtractService(CommandHandler<T> commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public Future<CommandResult<T>> loadDataFromFileAsync(String path) {
        this.path = path;
        return CompletableFuture.supplyAsync(this::execute);
    }

    @Override
    public CommandResult<T> loadDataFromFile(String path) {
        this.path = path;
        return execute();
    }

    private CommandResult<T> execute() {
        CommandResult<T> result = new CommandResult<>();
        int cont = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            if (!commandHandler.getAll().isEmpty())
                commandHandler.dispose();

            String lineArchive;

            while ((lineArchive = reader.readLine()) != null) {

                String finalLineArchive = lineArchive;

                Optional<CommandHandlerModel> command = commandHandler.getCommands().stream()
                        .filter(cm -> cm.getCheckLineData().invoke(finalLineArchive)).findFirst();

                if (command.isPresent()) {
                    runCommand(result, cont, lineArchive, command.get());
                } else {
                    result.getMessages().add("Line " + cont + " not mapped to an entity, check if you set the command on package abstractions.buildCommands ");
                    result.setStatus(StatusImport.ALERTS);
                }

                cont++;
            }
        } catch (Exception ex) {
            result.getMessages().add(ex.getMessage());
            result.setReadingDone(false);
            result.setStatus(StatusImport.ERROR);
        }
        result.setData(commandHandler.getAll());
        return result;
    }

    private void runCommand(CommandResult<T> result, int cont, String lineArchive, CommandHandlerModel command) {
        try {
            if (command.getInstanceRegistryHandler() == InstanceRegistryHandler.CREATE_NEW_REGISTRY_INSTANCE) {
                commandHandler.set(commandHandler::getNewInstance);
                commandHandler.add();
            }
            command.getFillObject().invoke(lineArchive);
        } catch (Exception ex) {
            result.setStatus(StatusImport.ALERTS);
            result.getMessages().add("Error to get line: " + cont + " - " + lineArchive + " - " + "Error: " + ex.getMessage());
        }
    }

}
