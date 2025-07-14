package dev.yodaforce.skyforce.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.yodaforce.skyforce.config.SkyForceConfigManager;

public class SkyForceModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SkyForceConfigManager::createScreen;
    }
}
