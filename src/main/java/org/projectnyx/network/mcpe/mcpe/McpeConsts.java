/*
 * Nyx - Server software for Minecraft: PE and more
 * Copyright © boredphoton 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projectnyx.network.mcpe.mcpe;

public final class McpeConsts {
    private McpeConsts() {
    }

    public final static byte APPARENT_PROTOCOL_MAJOR = 82;
    public final static byte APPARENT_PROTOCOL_MINOR = 82;
    public final static String APPARENT_VERSION = "0.15.4";

    public final static int LoginPacket = 1;
    public final static int PlayStatusPacket = 2;
    public final static int ServerToClientHandshakePacket = 3;
    public final static int ClientToServerHandshakePacket = 4;
    public final static int DisconnectPacket = 5;
    public final static int BatchPacket = 6;
    public final static int TextPacket = 7;
    public final static int SetTimePacket = 8;
    public final static int StartGamePacket = 9;
    public final static int AddPlayerPacket = 10;
    public final static int AddEntityPacket = 11;
    public final static int RemoveEntityPacket = 12;
    public final static int AddItemEntityPacket = 13;
    public final static int TakeItemEntityPacket = 14;
    public final static int MoveEntityPacket = 15;
    public final static int MovePlayerPacket = 16;
    public final static int RiderJumpPacket = 17;
    public final static int RemoveBlockPacket = 18;
    public final static int UpdateBlockPacket = 19;
    public final static int AddPaintingPacket = 20;
    public final static int ExplodePacket = 21;
    public final static int LevelEventPacket = 22;
    public final static int BlockEventPacket = 23;
    public final static int EntityEventPacket = 24;
    public final static int MobEffectPacket = 25;
    public final static int UpdateAttributesPacket = 26;
    public final static int MobEquipmentPacket = 27;
    public final static int MobArmorEquipmentPacket = 28;
    public final static int InteractPacket = 30;
    public final static int UseItemPacket = 31;
    public final static int PlayerActionPacket = 32;
    public final static int HurtArmorPacket = 33;
    public final static int SetEntityDataPacket = 34;
    public final static int SetEntityMotionPacket = 35;
    public final static int SetEntityLinkPacket = 36;
    public final static int SetHealthPacket = 37;
    public final static int SetSpawnPositionPacket = 38;
    public final static int AnimatePacket = 39;
    public final static int RespawnPacket = 40;
    public final static int DropItemPacket = 41;
    public final static int ContainerOpenPacket = 42;
    public final static int ContainerClosePacket = 43;
    public final static int ContainerSetSlotPacket = 44;
    public final static int ContainerSetDataPacket = 45;
    public final static int ContainerSetContentPacket = 46;
    public final static int CraftingDataPacket = 47;
    public final static int CraftingEventPacket = 48;
    public final static int AdventureSettingsPacket = 49;
    public final static int BlockEntityDataPacket = 50;
    public final static int PlayerInputPacket = 51;
    public final static int FullChunkDataPacket = 52;
    public final static int SetDifficultyPacket = 53;
    public final static int ChangeDimensionPacket = 54;
    public final static int SetPlayerGameTypePacket = 55;
    public final static int PlayerListPacket = 56;
    public final static int TelemetryEventPacket = 57;
    public final static int SpawnExperienceOrbPacket = 58;
    public final static int ClientboundMapItemDataPacket = 59;
    public final static int MapInfoRequestPacket = 60;
    public final static int RequestChunkRadiusPacket = 61;
    public final static int ChunkRadiusUpdatedPacket = 62;
    public final static int ItemFrameDropItemPacket = 63;
    public final static int ReplaceSelectedItemPacket = 64;
    public final static int AddItemPacket = 65;
}
