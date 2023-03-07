package xyz.wisecraft.smp.advancements.util.exceptions;

public class AdvancementNoExist extends Exception {

    private final String m;

    public AdvancementNoExist(String m) {
        this.m = m;
    }

    public String getadvancement(){return m;}
}
