package xyz.wisecraft.smp.storage;

import lombok.Getter;

/**
 * Storage for the plugin to store data in RAM.
 */
public abstract class OtherStorage {

    /**
     * The name of the server
     * -- GETTER --
     * Get the server name
     */
    @Getter
    private static String server_name;

    /**
     * Set the server name
     * @param server_name The name of the server
     */
    public static void setServer_name(String server_name) {
        OtherStorage.server_name = server_name;
    }
}
