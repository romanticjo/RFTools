package com.mcjty.rftools.blocks.itemfilter;

import com.mcjty.container.ContainerFactory;
import com.mcjty.container.GenericContainer;
import com.mcjty.container.SlotDefinition;
import com.mcjty.container.SlotType;
import net.minecraft.entity.player.EntityPlayer;

public class ItemFilterContainer extends GenericContainer {

    public static final String CONTAINER_INVENTORY = "container";

    public static final int SLOT_GHOST = 0;
    public static final int GHOST_SIZE = 6;
    public static final int SLOT_BUFFER = 6;
    public static final int BUFFER_SIZE = 6;
    public static final int SLOT_PLAYERINV = GHOST_SIZE + BUFFER_SIZE;

    public static final ContainerFactory factory = new ContainerFactory() {
        @Override
        protected void setup() {
            addSlotBox(new SlotDefinition(SlotType.SLOT_GHOST), CONTAINER_INVENTORY, SLOT_GHOST, 28, 7, 1, 18, 6, 18);
            addSlotBox(new SlotDefinition(SlotType.SLOT_INPUT), CONTAINER_INVENTORY, SLOT_BUFFER, 64, 7, 1, 18, 6, 18);
            layoutPlayerInventorySlots(10, 142);
        }
    };

    public ItemFilterContainer(EntityPlayer player, ItemFilterTileEntity containerInventory) {
        super(factory, player);
        addInventory(CONTAINER_INVENTORY, containerInventory);
        addInventory(ContainerFactory.CONTAINER_PLAYER, player.inventory);
        generateSlots();
    }
}
