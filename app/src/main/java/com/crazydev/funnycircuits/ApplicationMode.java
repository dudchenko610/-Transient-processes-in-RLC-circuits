package com.crazydev.funnycircuits;

public class ApplicationMode {

    enum AppMode {
        EDITING_MODE,
        SIMULATING_MODE
    }

    public static AppMode CURRENT_APP_MODE;

    static {
        CURRENT_APP_MODE = AppMode.EDITING_MODE;
    }

    public static void setEditingMode() {
        CURRENT_APP_MODE = AppMode.EDITING_MODE;
    }

    public static void setSimulatingMode() {
        CURRENT_APP_MODE = AppMode.SIMULATING_MODE;
    }

}
