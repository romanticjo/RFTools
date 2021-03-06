package mcjty.rftools.blocks.logic.wireless;

import mcjty.lib.container.EmptyContainer;
import mcjty.lib.container.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.VerticalLayout;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.TextField;
import mcjty.lib.gui.widgets.ToggleButton;
import mcjty.lib.network.Argument;
import mcjty.rftools.RFTools;
import mcjty.rftools.network.RFToolsMessages;

import java.awt.*;

public class GuiRedstoneTransmitter extends GenericGuiContainer<RedstoneTransmitterTileEntity> {
    public static final int REDSTONE_TRANSMITTER_WIDTH = 168;
    public static final int REDSTONE_TRANSMITTER_HEIGHT = 20;

    private TextField speedField;
    private ToggleButton analog;

    public GuiRedstoneTransmitter(RedstoneTransmitterTileEntity redstoneTransmitterTileEntity, EmptyContainer container) {
        super(RFTools.instance, RFToolsMessages.INSTANCE, redstoneTransmitterTileEntity, container, RFTools.GUI_MANUAL_MAIN, "redtrans");
        xSize = REDSTONE_TRANSMITTER_WIDTH;
        ySize = REDSTONE_TRANSMITTER_HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();

        Panel toplevel = new Panel(mc, this).setFilledRectThickness(2).setLayout(new VerticalLayout());

        analog = new ToggleButton(mc, this).setText("Analog mode").setTooltips("Transmit with the same", "power level as the input,", "instead of always 15").setCheckMarker(true).setDesiredWidth(160).setDesiredHeight(16).setPressed(tileEntity.getAnalog())
                .addButtonEvent(parent -> setAnalog());
        toplevel.addChild(analog);

        toplevel.setBounds(new Rectangle(guiLeft, guiTop, REDSTONE_TRANSMITTER_WIDTH, REDSTONE_TRANSMITTER_HEIGHT));
        window = new Window(this, toplevel);
    }

    private void setAnalog() {
        boolean analog = this.analog.isPressed();
        tileEntity.setAnalog(analog);
        sendServerCommand(RFToolsMessages.INSTANCE, RedstoneTransmitterTileEntity.CMD_SETANALOG, new Argument("analog", analog));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i2) {
        drawWindow();
    }
}
