package models;

import enums.InstanceRegistryHandler;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class CommandHandler {

    public CommandHandler(Predicate<String> checkLineData, Consumer<String> fillObject, InstanceRegistryHandler registryType) {
        this.checkLineData = checkLineData;
        this.fillObject = fillObject;
        this.registryType = registryType;
    }


    private final Predicate<String> checkLineData;
    private final Consumer<String> fillObject;
    private final InstanceRegistryHandler registryType;

    public Predicate<String> getCheckLineData() {
        return checkLineData;
    }

    public Consumer<String> getFillObject() {
        return fillObject;
    }

    public InstanceRegistryHandler getRegistryType() {
        return registryType;
    }

}
