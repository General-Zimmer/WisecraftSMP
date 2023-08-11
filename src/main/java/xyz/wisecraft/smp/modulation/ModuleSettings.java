package xyz.wisecraft.smp.modulation;

public enum ModuleSettings {


    ENABLED("enabled"),ID("id");

    private final String string;
    ModuleSettings(String string) {
        this.string = string;
    }
    @Override
    public String toString () {
        return string;
    }
}
