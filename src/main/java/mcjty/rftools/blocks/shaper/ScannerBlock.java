package mcjty.rftools.blocks.shaper;

import mcjty.lib.container.GenericGuiContainer;
import mcjty.rftools.RFTools;
import mcjty.rftools.blocks.GenericRFToolsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ScannerBlock extends GenericRFToolsBlock<ScannerTileEntity, ScannerContainer> /*, IRedstoneConnectable */ {

    public ScannerBlock() {
        super(Material.IRON, ScannerTileEntity.class, ScannerContainer.class, "scanner", true);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World player, List<String> list, ITooltipFlag whatIsThis) {
        super.addInformation(itemStack, player, list, whatIsThis);
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            list.add(TextFormatting.WHITE + "This block can scan an area and link");
            list.add(TextFormatting.WHITE + "to shape cards for the Builder or Shield.");
            list.add(TextFormatting.WHITE + "The resulting shape card can also be used");
            list.add(TextFormatting.WHITE + "in the Composer");
        } else {
            list.add(TextFormatting.WHITE + RFTools.SHIFT_MESSAGE);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Class<? extends GenericGuiContainer> getGuiClass() {
        return GuiScanner.class;
    }

    @Override
    public int getGuiID() {
        return RFTools.GUI_SCANNER;
    }
}
