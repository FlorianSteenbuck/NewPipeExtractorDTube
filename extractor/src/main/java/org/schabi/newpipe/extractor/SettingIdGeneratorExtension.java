package org.schabi.newpipe.extractor;

import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;

public class SettingIdGeneratorExtension {
    protected SettingsOrganisator organisator;
    protected int id;

    public SettingIdGeneratorExtension(SettingsOrganisator organisator) {
        this.organisator = organisator;
    }

    protected int generateId() {
        return organisator.generateId();
    }

    protected synchronized void setId(int id, Settings settings) {
        organisator.addSettings(id, settings);
        this.id = id;
    }
}
