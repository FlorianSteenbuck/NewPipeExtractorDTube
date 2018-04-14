/*
 *MIT License
 *
 *Copyright (c) 2018 Florian Steenbuck
 *
 *Permission is hereby granted, free of charge, to any person obtaining a copy
 *of this software and associated documentation files (the "Software"), to deal
 *in the Software without restriction, including without limitation the rights
 *to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *copies of the Software, and to permit persons to whom the Software is
 *furnished to do so, subject to the following conditions:
 *
 *The above copyright notice and this permission notice shall be included in all
 *copies or substantial portions of the Software.
 *
 *THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *SOFTWARE.
 */
package org.schabi.newpipe.extractor.settings.model.settings;

import org.schabi.newpipe.extractor.settings.exceptions.UnsupportedSettingValueException;
import org.schabi.newpipe.extractor.settings.exceptions.WrongSettingsDataException;
import org.schabi.newpipe.extractor.settings.model.provider.SettingProvider;
import org.schabi.newpipe.extractor.settings.model.settings.interfaces.WriteableSettings;

import java.util.Map;

public class PreStoredSettings implements WriteableSettings {
    protected Map<String, SettingProvider> providers;

    public PreStoredSettings(Map<String, SettingProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Map<String, SettingProvider> getSettingProviders() {
        return providers;
    }

    @Override
    public boolean has(String id) {
        return providers.containsKey(id);
    }

    @Override
    public void set(String id, Object settingValue) throws UnsupportedSettingValueException {
        if (settingValue instanceof String) {
            setString(id, (String) settingValue);
        } else if (settingValue instanceof Number) {
            setNumber(id, (Number) settingValue);
        } else if (settingValue instanceof Boolean) {
            setBoolean(id, (Boolean) settingValue);
        } else {
            throw new UnsupportedSettingValueException(settingValue.getClass().getName()+" is unsupported");
        }
    }

    @Override
    public void setString(String id, String settingValue) {
        SettingProvider<String> provider = providers.get(id);
        try {
            provider.setData(settingValue);
        } catch (WrongSettingsDataException e) {
            // TODO better error handling
        }
        providers.put(id, provider);
    }

    @Override
    public void setNumber(String id, Number settingValue) {
        SettingProvider<Number> provider = providers.get(id);
        try {
            provider.setData(settingValue);
        } catch (WrongSettingsDataException e) {
            // TODO better error handling
        }
        providers.put(id, provider);
    }

    @Override
    public void setBoolean(String id, Boolean settingValue) {
        SettingProvider<Boolean> provider = providers.get(id);
        try {
            provider.setData(settingValue);
        } catch (WrongSettingsDataException e) {
            // TODO better error handling
        }
        providers.put(id, provider);
    }

    @Override
    public Object get(String id) {
        return providers.get(id).getData();
    }

    @Override
    public String getString(String id) {
        return ((SettingProvider<String>) providers.get(id)).getData();
    }

    @Override
    public Number getNumber(String id) {
        return ((SettingProvider<Number>) providers.get(id)).getData();
    }

    @Override
    public Boolean getBoolean(String id) {
        return ((SettingProvider<Boolean>) providers.get(id)).getData();
    }
}