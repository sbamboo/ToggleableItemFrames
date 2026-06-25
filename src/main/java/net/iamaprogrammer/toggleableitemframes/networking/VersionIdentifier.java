package net.iamaprogrammer.toggleableitemframes.networking;

import net.fabricmc.loader.api.FabricLoader;
import net.iamaprogrammer.toggleableitemframes.ToggleableItemFrames;

public class VersionIdentifier {
    public static final String MOD_VERSION = FabricLoader.getInstance()
            .getModContainer(ToggleableItemFrames.MOD_ID)
            .map(container -> container.getMetadata().getVersion().getFriendlyString())
            .orElse("unknown");
}
