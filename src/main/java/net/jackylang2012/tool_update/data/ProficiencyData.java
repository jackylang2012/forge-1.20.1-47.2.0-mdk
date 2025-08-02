package net.jackylang2012.tool_update.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProficiencyData extends SavedData {
    private static final String NAME = "tool_update_proficiencies";
    private final Map<UUID, Map<ResourceLocation, Integer>> data = new HashMap<>();

    public ProficiencyData() {
        super();
    }

    public static ProficiencyData get(ServerPlayer player) {
        DimensionDataStorage storage = player.level().getServer().overworld().getDataStorage();
        return storage.computeIfAbsent(ProficiencyData::load, ProficiencyData::new, NAME);
    }

    public static ProficiencyData load(CompoundTag nbt) {
        ProficiencyData data = new ProficiencyData();
        ListTag playerList = nbt.getList("players", Tag.TAG_COMPOUND);

        for (Tag tag : playerList) {
            CompoundTag playerTag = (CompoundTag) tag;
            UUID playerId = playerTag.getUUID("id");
            Map<ResourceLocation, Integer> toolMap = new HashMap<>();

            ListTag toolList = playerTag.getList("tools", Tag.TAG_COMPOUND);
            for (Tag toolTag : toolList) {
                CompoundTag compound = (CompoundTag) toolTag;
                ResourceLocation toolId = new ResourceLocation(compound.getString("tool"));
                int proficiency = compound.getInt("proficiency");
                toolMap.put(toolId, proficiency);
            }

            data.data.put(playerId, toolMap);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag playerList = new ListTag();

        data.forEach((playerId, toolMap) -> {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("id", playerId);

            ListTag toolList = new ListTag();
            toolMap.forEach((toolId, proficiency) -> {
                CompoundTag toolTag = new CompoundTag();
                toolTag.putString("tool", toolId.toString());
                toolTag.putInt("proficiency", proficiency);
                toolList.add(toolTag);
            });

            playerTag.put("tools", toolList);
            playerList.add(playerTag);
        });

        compound.put("players", playerList);
        return compound;
    }

    public void updateProficiency(UUID playerId, ResourceLocation toolId, int proficiency) {
        data.computeIfAbsent(playerId, k -> new HashMap<>());
        data.get(playerId).put(toolId, proficiency);
        setDirty();
    }

    public int getProficiency(UUID playerId, ResourceLocation toolId) {
        return data.getOrDefault(playerId, new HashMap<>())
                .getOrDefault(toolId, 0);
    }
}