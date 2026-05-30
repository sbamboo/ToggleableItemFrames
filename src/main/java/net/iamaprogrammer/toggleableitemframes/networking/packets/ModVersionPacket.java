package net.iamaprogrammer.toggleableitemframes.networking.packets;

import io.netty.buffer.ByteBuf;
import net.iamaprogrammer.toggleableitemframes.ToggleableItemFrames;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record ModVersionPacket(String version) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ModVersionPacket> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(
            ToggleableItemFrames.MOD_ID, ToggleableItemFrames.MOD_ID + "_version_identifier"));
    public static final StreamCodec<ByteBuf, ModVersionPacket> PACKET_CODEC = ByteBufCodecs.STRING_UTF8.map(ModVersionPacket::new, ModVersionPacket::version).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
