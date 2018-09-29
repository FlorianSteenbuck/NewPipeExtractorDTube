package org.schabi.newpipe.extractor;

import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;

import java.util.HashMap;

public class SettingsOrganisator {
    protected int startId = 0;
    protected HashMap<Integer, Settings> settingsStorage = new HashMap<Integer, Settings>();
    protected static final SettingsOrganisator instance = new SettingsOrganisator();

    public static SettingsOrganisator getInstance() {
        return instance;
    }

    public synchronized int generateId() {
        startId++;
        return startId;
    }

    public synchronized void addSettings(int id, Settings settings) {
        if (id > startId) {
            throw new NullPointerException("This settings can not be added caused by invalid id we only support <= "+startId+" ids");
        }
        settingsStorage.put(id, settings);
    }
}
