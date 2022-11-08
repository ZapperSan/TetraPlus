package com.orz.TetraPlus.items.modular.impl;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraItemGroup;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RemoveSchematic;
import se.mickelus.tetra.module.schematic.RepairSchematic;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularKnifeItem extends ItemModularHandheld {
    public final static String bladeKey = "knife/blade";
    public final static String hiltKey = "knife/hilt";

    public final static String guardKey = "knife/guard";

    public static final String identifier = "modular_knife";

    @ObjectHolder(TetraMod.MOD_ID + ":" + identifier)
    public static ModularKnifeItem instance;

    public ModularKnifeItem() {
        super(new Item.Properties().stacksTo(1).tab(TetraItemGroup.instance).fireResistant());

        blockDestroyDamage = 2;

        majorModuleKeys = new String[]{bladeKey, hiltKey};
        minorModuleKeys = new String[]{guardKey};

        requiredModules = new String[]{bladeKey, hiltKey};

        updateConfig(ConfigHandler.honeSwordBase.get(), ConfigHandler.honeSwordIntegrityMultiplier.get());

        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
        RemoveSchematic.registerRemoveSchematics(this, identifier);
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> synergies = DataManager.instance.getSynergyData("knife/"));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            items.add(setupGreatSwordStack("iron", "stick"));
        }
    }

    private ItemStack setupGreatSwordStack(String bladeMaterial, String hiltMaterial) {
        ItemStack itemStack = new ItemStack(this);

        IModularItem.putModuleInSlot(itemStack, bladeKey, "knife/knife_blade", "knife/knife_material", "knife_blade/" + bladeMaterial);
        IModularItem.putModuleInSlot(itemStack, hiltKey, "knife/basic_hilt", "knife/basic_hilt_material", "basic_hilt/" + hiltMaterial);
        IModularItem.putModuleInSlot(itemStack, guardKey, "knife/makeshift_guard", "knife/makeshift_guard_material", "makeshift_guard/" + bladeMaterial);

        IModularItem.updateIdentifier(itemStack);

        return itemStack;
    }
    @Override
    public String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        if (isThrowing(itemStack, entity)) {
            return super.getModelCacheKey(itemStack, entity) + ":throwing";
        }

        if (isBlocking(itemStack, entity)) {
            return super.getModelCacheKey(itemStack, entity) + ":blocking";
        }

        return super.getModelCacheKey(itemStack, entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTransformVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        if (isThrowing(itemStack, entity)) {
            return "throwing";
        }
        if (isBlocking(itemStack, entity)) {
            return "blocking";
        }
        return null;
    }
}
