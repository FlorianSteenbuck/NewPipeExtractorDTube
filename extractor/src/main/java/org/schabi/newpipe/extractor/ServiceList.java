package org.schabi.newpipe.extractor;

import com.github.FlorianSteenbuck.other.settings.exceptions.SettingException;
import com.github.FlorianSteenbuck.other.settings.exceptions.UnsupportedSettingValueException;
import com.github.FlorianSteenbuck.other.settings.exceptions.WrongSettingsDataException;
import com.github.FlorianSteenbuck.other.settings.model.provider.SettingProvider;
import com.github.FlorianSteenbuck.other.settings.model.settings.PreStoredSettings;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.Settings;
import com.github.FlorianSteenbuck.other.settings.model.settings.interfaces.WriteableSettings;
import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.schabi.newpipe.extractor.services.dtube.DTubeService;
import org.schabi.newpipe.extractor.services.soundcloud.SoundcloudService;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * A list of supported services.
 */
public final class ServiceList {
    private static final int VERSION = 2;

    private ServiceList() {
        //no instance
    }

    protected static class CanNotFindStreamingServiceClass extends Exception {
        public CanNotFindStreamingServiceClass(String message, Throwable cause) {
            super(message, cause);
        }
    }

    protected static class StreamingServiceInfo {
        private int id;
        private Settings settings;
        private Class<? extends StreamingService> typ;

        public StreamingServiceInfo(int id, Settings settings, Class<? extends StreamingService> typ) {
            this.id = id;
            this.settings = settings;
            this.typ = typ;
        }

        public StreamingServiceInfo(JsonObject json) throws ClassNotFoundException, IllegalAccessException, InstantiationException, CanNotFindStreamingServiceClass {
            this.id = json.getInt("id");
            try {
                this.typ = (Class<? extends StreamingService>) Class.forName(json.getString("class"));
            } catch (ClassNotFoundException ex) {
                throw new CanNotFindStreamingServiceClass(ex.getMessage(), ex.getCause());
            }
            // Here starts the reflections monster grab a coffee and start reading
            JsonObject settingsRoot = json.getObject("settings");
            JsonObject providers = json.getObject("providers");
            Class settingsClass = Class.forName(settingsRoot.getString("class"));

            boolean writeableSettings = settingsClass == WriteableSettings.class;
            if (!writeableSettings) {
                ArrayList<Class> interfaces = new ArrayList<Class>();
                for (Class mInterface:settingsClass.getInterfaces()) {
                    writeableSettings = mInterface == WriteableSettings.class;
                    if (writeableSettings) {
                        break;
                    }
                }
            }

            if (writeableSettings) {
                WriteableSettings settings = (WriteableSettings) settingsClass.newInstance();
                for (Map.Entry<String, SettingProvider> settingProviderEntry:settings.getSettingProviders().entrySet()) {
                    String key = settingProviderEntry.getKey();
                    if (providers.containsKey(key)) {
                        JsonObject provider = providers.getObject(key);
                        Class providerClass = Class.forName(provider.getString("class"));
                        if (providerClass != settingProviderEntry.getValue().getClass()) {
                            writeableSettings = false;
                            break;
                        }

                        try {
                            settings.set(key, getValueFromSetting(provider.getObject("value")));
                        } catch (UnsupportedSettingValueException e) {
                            // TODO better error handling
                        } catch (WrongSettingsDataException e) {
                            writeableSettings = false;
                            break;
                        }
                    }
                }

                if (writeableSettings) {
                    this.settings = settings;
                    return;
                }
            }

            Map<String, SettingProvider> settings = new HashMap<String, SettingProvider>();
            for (Map.Entry<String, Object> providerEntry:providers.entrySet()) {
                Object obj = providerEntry.getValue();
                if (!(obj instanceof JsonObject)) {
                    continue;
                }
                JsonObject providerJson = (JsonObject) obj;

                Object value = getValueFromSetting(providerJson.getObject("value"));
                String key = providerEntry.getKey();

                Class<? extends SettingProvider> providerClass = (Class<? extends SettingProvider>) Class.forName(providerJson.getString("class"));

                boolean gotPut = false;
                Constructor[] providerConstructors = providerClass.getConstructors();
                for (Constructor providerConstructor:providerConstructors) {
                    Class[] providerConstructParameters = providerConstructor.getParameterTypes();
                    if (providerConstructParameters.length == 1 && providerConstructParameters[0] == value.getClass()) {
                        try {
                            SettingProvider settingProvider = (SettingProvider) providerConstructor.newInstance(value);
                            settings.put(key, settingProvider);
                            break;
                        } catch (Exception ex) {
                            // TODO better error handling
                        }
                    }
                }
                if (!gotPut) {
                    throw new NullPointerException("Can not find matching constructor("+value.getClass().getName()+" value) for init SettingProvider for "+key);
                }
            }

            this.settings = new PreStoredSettings(settings);
        }

        protected static Object getValueFromSetting(JsonObject valueSetting) throws ClassNotFoundException {
            Object value = valueSetting.get("value");
            Class valueClass = Class.forName(valueSetting.getString("class"));
            if (value instanceof JsonObject) {
                try {
                    Method fromJson = valueClass.getMethod("fromJson", JsonObject.class);
                    value = fromJson.invoke(null, value);
                } catch (ReflectiveOperationException ex) {
                }
            }
            return value;
        }

        public Class<? extends StreamingService> getTyp(){
            return typ;
        }

        public int getId() {
            return id;
        }

        public Settings getSettings() {
            return settings;
        }

        public static StreamingServiceInfo fromJson(JsonObject json) throws IllegalAccessException, InstantiationException, ClassNotFoundException, CanNotFindStreamingServiceClass {
            return new StreamingServiceInfo(json);
        }

        public JsonObject json() throws JsonParserException {
            JsonObject rootObject = new JsonObject();
            rootObject.put("id", id);
            rootObject.put("class", typ.getName());

            JsonObject settingsRoot = new JsonObject();
            settingsRoot.put("class", settings.getClass().getName());
            JsonObject settingsProvider = new JsonObject();
            for (Map.Entry<String, SettingProvider> setting:settings.getSettingProviders().entrySet()) {
                String key = setting.getKey();
                Object value = setting.getValue();
                Class valueClass = value.getClass();
                Class typ = setting.getClass();

                if (!(value instanceof JsonObject)) {
                    try {
                        Method jsonConvertMethod = value.getClass().getMethod("json");
                        value = jsonConvertMethod.invoke(value);
                        if (value instanceof String) {
                            value = JsonParser.any().from((String) value);
                        }
                    } catch (ReflectiveOperationException ex) {
                    }
                }
                JsonObject jsonSettingValue = new JsonObject();
                jsonSettingValue.put("value", value);
                jsonSettingValue.put("class", valueClass.getName());

                JsonObject jsonSettingProvider = new JsonObject();
                jsonSettingProvider.put("value", jsonSettingValue);
                jsonSettingProvider.put("class", typ);
                settingsProvider.put(key, jsonSettingProvider);
            }
            settingsRoot.put("providers", settingsProvider);
            rootObject.put("settings", settingsRoot);
            return rootObject;
        }
    }

    public static final YoutubeService YouTube;
    public static final SoundcloudService SoundCloud;
    public static final DTubeService DTube;

    private static final List<StreamingService> FINAL_SERVICES = unmodifiableList(asList(
            YouTube = new YoutubeService(0),
            SoundCloud = new SoundcloudService(1),
            // DailyMotion = new DailyMotionService(2),
            DTube = new DTubeService(3)
    ));

    // Do it like port splitting and hope that this is enough :-)
    private static final int PLUGIN_SERVICES_ID_OFFSET = 9000;
    private static ArrayList<StreamingService> pluginServices = new ArrayList<StreamingService>();

    /**
     * Get all the supported services.
     *
     * @return a unmodifiable list of all the supported services
     */
    public static List<StreamingService> all() {
        ArrayList<StreamingService> services = new ArrayList<StreamingService>();
        services.addAll(FINAL_SERVICES);
        services.addAll(pluginServices);
        return services;
    }

    public static boolean isPluginServiceInfo(StreamingServiceInfo serviceInfo) {
        return isPluginId(serviceInfo.getId());
    }

    public static boolean isPluginService(StreamingService service) {
        return isPluginId(service.getServiceId());
    }

    public static boolean isPluginId(int id) {
        return id > PLUGIN_SERVICES_ID_OFFSET;
    }

    public synchronized static int addService(StreamingService service, Map<String, Object> initSettings) throws UnsupportedSettingValueException, WrongSettingsDataException {
        if (initSettings != null) {
            Settings settings = service.getSettings();
            if (settings instanceof WriteableSettings) {
                WriteableSettings writeableSettings = (WriteableSettings) settings;
                for (Map.Entry<String, Object> settingEntry : initSettings.entrySet()) {
                    writeableSettings.set(settingEntry.getKey(), settingEntry.getValue());
                }
                service.updateSettings(writeableSettings);
            }
        }
        pluginServices.add(service);
        return pluginServices.size()-1+ PLUGIN_SERVICES_ID_OFFSET;
    }

    public synchronized static int addService(StreamingService service) throws SettingException {
        return addService(service, null);
    }

    public static JsonObject json() throws JsonParserException {
        JsonObject rootObject = new JsonObject();
        rootObject.put("version", VERSION);
        JsonArray services = new JsonArray();
        for (StreamingServiceInfo serviceInfo:export()) {
            services.add(serviceInfo.json());
        }
        rootObject.put("services", services);
        return rootObject;
    }

    public static List<StreamingServiceInfo> export() {
        ArrayList<StreamingServiceInfo> exportDump = new ArrayList<StreamingServiceInfo>();
        for (StreamingService finalService:FINAL_SERVICES) {
            exportDump.add(new StreamingServiceInfo(finalService.getServiceId(), finalService.getSettings(), finalService.getClass()));
        }
        for (StreamingService pluginService:pluginServices) {
            exportDump.add(new StreamingServiceInfo(pluginService.getServiceId(), pluginService.getSettings(), pluginService.getClass()));
        }
        return exportDump;
    }

    public static boolean importJsonServices(JsonObject jsonObject) throws IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, CanNotFindStreamingServiceClass {
        ArrayList<StreamingServiceInfo> serviceInfos = new ArrayList<StreamingServiceInfo>();
        JsonArray services = jsonObject.getArray("services");
        int version = jsonObject.getInt("version");
        jsonObject.remove("version");
        jsonObject.remove("services");
        for (Object service:services) {
            if (!(service instanceof JsonObject)) {
                continue;
            }
            JsonObject jsonService = (JsonObject) service;
            try {
                serviceInfos.add(new StreamingServiceInfo(jsonService));
            } catch (CanNotFindStreamingServiceClass ex) {
                isPluginId(jsonService.getInt("id"));
            }
        }
        return importServices(serviceInfos, version, jsonObject);
    }

    public static boolean importServices(List<StreamingServiceInfo> serviceInfos, int version, @Nullable Map<String, ?> versionSpecifications) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        switch (version) {
            case 2:
                boolean nativ404Error = true;
                boolean plugin404Error = true;
                if (versionSpecifications != null && (!versionSpecifications.isEmpty())) {
                    if (versionSpecifications.containsKey("nativ404Error")) {
                        nativ404Error = ((Boolean) versionSpecifications.get("nativ404Error")).booleanValue();
                    }
                    if (versionSpecifications.containsKey("plugin404Error")) {
                        plugin404Error = ((Boolean) versionSpecifications.get("plugin404Error")).booleanValue();
                    }
                }
                for (StreamingServiceInfo serviceInfo:serviceInfos) {
                    StreamingService service = null;
                    Class<? extends StreamingService> serviceTyp = serviceInfo.getTyp();
                    for (Constructor serviceConstructor:serviceTyp.getConstructors()) {
                        Class[] paramTypes = serviceConstructor.getParameterTypes();
                        if (paramTypes.length <= 0) {
                            service = (StreamingService) serviceConstructor.newInstance();
                            break;
                        } else if (paramTypes.length <= 3) {
                            break;
                        }
                    }

                    if (isPluginServiceInfo(serviceInfo)) {

                    } else {

                    }
                }
                break;
            default:
                // TODO create custom Exception
                throw new NullPointerException("Version of dump is not supported");
        }
        return false;
    }
}
