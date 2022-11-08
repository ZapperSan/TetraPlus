package com.orz.TetraPlus;

import com.orz.TetraPlus.items.modular.impl.ModularKnifeItem;
import net.minecraftforge.eventbus.api.IEventBus;
import se.mickelus.tetra.TetraRegistries;


public class TetraPlusRegistries extends TetraRegistries {

    public static void init(IEventBus bus) {
        bus.register(TetraRegistries.class);

        items.register(bus);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // ITEMS
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        items.register(ModularKnifeItem.identifier, ModularKnifeItem::new);

    }

}