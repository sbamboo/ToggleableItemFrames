package net.iamaprogrammer.toggleableitemframes.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.iamaprogrammer.toggleableitemframes.ToggleableItemFramesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class ClientDisconnectEventHandler implements ClientPlayConnectionEvents.Disconnect {
    @Override
    public void onPlayDisconnect(ClientPacketListener handler, Minecraft client) {
        ToggleableItemFramesClient.ALWAYS_INVISIBLE = true;
    }
}
