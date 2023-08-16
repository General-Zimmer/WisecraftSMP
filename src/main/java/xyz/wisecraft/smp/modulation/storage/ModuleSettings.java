package xyz.wisecraft.smp.modulation.storage;

public enum ModuleSettings {


    ENABLED("enabled"),ID("id"), PATH("modules");

    private final String string;
    ModuleSettings(String string) {
        this.string = string;
    }
    @Override
    public String toString () {
        return string;
    }
}
