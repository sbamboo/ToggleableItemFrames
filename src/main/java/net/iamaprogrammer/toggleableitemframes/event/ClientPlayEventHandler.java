package net.iamaprogrammer.toggleableitemframes.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.iamaprogrammer.toggleableitemframes.ToggleableItemFramesClient;
import net.iamaprogrammer.toggleableitemframes.networking.VersionIdentifier;
import net.iamaprogrammer.toggleableitemframes.networking.packets.ModVersionPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class ClientPlayEventHandler implements ClientPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ClientPacketListener handler, PacketSender sender, Minecraft client) {
        if (client.getSingleplayerServer() != null || !ToggleableItemFramesClient.CONFIG.isInvisibleIfNotSupportedByServer()) {
            ToggleableItemFramesClient.ALWAYS_INVISIBLE = false;
        } else {
            ClientPlayNetworking.send(new ModVersionPacket(VersionIdentifier.MOD_VERSION));
        }
    }
}
