package mcjty.rftools.blocks.itemfilter;

import mcjty.lib.container.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.events.ChoiceEvent;
import mcjty.lib.gui.events.TextEvent;
import mcjty.lib.gui.layout.HorizontalLayout;
import mcjty.lib.gui.layout.PositionalLayout;
import mcjty.lib.gui.widgets.*;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.TextField;
import mcjty.lib.network.Argument;
import mcjty.rftools.RFTools;
import mcjty.rftools.items.dimlets.DimletType;
import mcjty.rftools.network.RFToolsMessages;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import java.awt.*;

public class GuiDimletFilter extends GenericGuiContainer<DimletFilterTileEntity> {
    public static final int DIMLETFILTER_WIDTH = 211;
    public static final int DIMLETFILTER_HEIGHT = 212;

    private static final ResourceLocation iconLocation = new ResourceLocation(RFTools.MODID, "textures/gui/dimletfilter.png");
    private static final ResourceLocation iconGuiElements = new ResourceLocation(RFTools.MODID, "textures/gui/guielements.png");

    private ImageChoiceLabel[] bits = new ImageChoiceLabel[6];
    private TextField[] minText = new TextField[6];
    private TextField[] maxText = new TextField[6];
    private ChoiceLabel[] types = new ChoiceLabel[6];
    private ChoiceLabel[] craftable = new ChoiceLabel[6];

    public GuiDimletFilter(DimletFilterTileEntity dimletFilterTileEntity, DimletFilterContainer container) {
        super(RFTools.instance, RFToolsMessages.INSTANCE, dimletFilterTileEntity, container, RFTools.GUI_MANUAL_DIMENSION, "filter");

        xSize = DIMLETFILTER_WIDTH;
        ySize = DIMLETFILTER_HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();

        Panel toplevel = new Panel(mc, this).setBackground(iconLocation).setLayout(new PositionalLayout());

        int[] inputMode = tileEntity.getInputMode();
        int[] minRarity = tileEntity.getMinRarity();
        int[] maxRarity = tileEntity.getMaxRarity();
        int[] craftableI = tileEntity.getCraftable();
        DimletType[] dimletTypes = tileEntity.getTypes();

        for (ForgeDirection direction : ForgeDirection.values()) {
            if (!ForgeDirection.UNKNOWN.equals(direction)) {
                final int side = direction.ordinal();
                Panel panel = new Panel(mc, this).setLayout(new HorizontalLayout()).setLayoutHint(new PositionalLayout.PositionalHint(18, 21 + side * 13, 190, 12));
                ImageChoiceLabel choiceLabel = new ImageChoiceLabel(mc, this).setDesiredWidth(12).setDesiredHeight(12).
                        addChoice("0", "Disabled", iconGuiElements, 160, 0).
                        addChoice("1", "Input", iconGuiElements, 96, 16).
                        addChoice("2", "Output", iconGuiElements, 80, 16);
                bits[side] = choiceLabel;
                choiceLabel.setCurrentChoice(inputMode[side]);
                choiceLabel.addChoiceEvent(new ChoiceEvent() {
                    @Override
                    public void choiceChanged(Widget parent, String newChoice) {
                        changeMode(side);
                    }
                });

                Label minLabel = new Label(mc, this).setText("R:");
                minText[side] = new TextField(mc, this).setDesiredHeight(12).setDesiredWidth(24).addTextEvent(new TextEvent() {
                    @Override
                    public void textChanged(Widget parent, String newText) {
                        setMinimumRarity(side);
                    }
                });
                minText[side].setText(Integer.toString(minRarity[side]));
                maxText[side] = new TextField(mc, this).setDesiredHeight(12).setDesiredWidth(24).addTextEvent(new TextEvent() {
                    @Override
                    public void textChanged(Widget parent, String newText) {
                        setMaximumRarity(side);
                    }
                });
                maxText[side].setText(Integer.toString(maxRarity[side]));

                types[side] = new ChoiceLabel(mc, this).setDesiredHeight(12).setDesiredWidth(68).setTooltips("Filter based on type", "of the dimlet");
                types[side].addChoices("*");
                for (DimletType type : DimletType.values()) {
                    types[side].addChoices(type.dimletType.getName());
                }
                if (dimletTypes[side] == null) {
                    types[side].setChoice("*");
                } else {
                    types[side].setChoice(dimletTypes[side].dimletType.getName());
                }
                types[side].addChoiceEvent(new ChoiceEvent() {
                    @Override
                    public void choiceChanged(Widget parent, String newChoice) {
                        setType(side);
                    }
                });


                craftable[side] = new ChoiceLabel(mc, this).setDesiredHeight(12).setDesiredWidth(16).setTooltips("Filter based on craftability", "of the dimlet");
                craftable[side].addChoices("*", "Y", "N");
                if (craftableI[side] == DimletFilterTileEntity.CRAFTABLE_DONTCARE) {
                    craftable[side].setChoice("*");
                } else if (craftableI[side] == DimletFilterTileEntity.CRAFTABLE_YES) {
                    craftable[side].setChoice("Y");
                } else {
                    craftable[side].setChoice("N");
                }
                craftable[side].addChoiceEvent(new ChoiceEvent() {
                    @Override
                    public void choiceChanged(Widget parent, String newChoice) {
                        setCraftable(side);
                    }
                });

                panel.addChild(choiceLabel).addChild(minLabel).addChild(minText[side]).addChild(maxText[side]).addChild(types[side]).addChild(craftable[side]);

                toplevel.addChild(panel);
            }
        }

        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);
    }

    private void setMinimumRarity(int side) {
        int value = 0;
        try {
            value = Integer.parseInt(minText[side].getText());
            if (value < 0) {
                value = 0;
            } else if (value > 6) {
                value = 6;
            }
        } catch (NumberFormatException e) {
            value = 0;
        }
        sendServerCommand(RFToolsMessages.INSTANCE, DimletFilterTileEntity.CMD_SETMINRARITY,
                new Argument("side", side),
                new Argument("value", value));
    }

    private void setMaximumRarity(int side) {
        int value = 0;
        try {
            value = Integer.parseInt(maxText[side].getText());
            if (value < 0) {
                value = 0;
            } else if (value > 6) {
                value = 6;
            }
        } catch (NumberFormatException e) {
            value = 0;
        }
        sendServerCommand(RFToolsMessages.INSTANCE, DimletFilterTileEntity.CMD_SETMAXRARITY,
                new Argument("side", side),
                new Argument("value", value));
    }

    private void setCraftable(int side) {
        String choice = craftable[side].getCurrentChoice();
        int cr;
        if ("*".equals(choice)) {
            cr = DimletFilterTileEntity.CRAFTABLE_DONTCARE;
        } else if ("Y".equals(choice)) {
            cr = DimletFilterTileEntity.CRAFTABLE_YES;
        } else {
            cr = DimletFilterTileEntity.CRAFTABLE_NO;
        }
        sendServerCommand(RFToolsMessages.INSTANCE, DimletFilterTileEntity.CMD_SETCRAFTABLE,
                new Argument("side", side),
                new Argument("craftable", cr));
    }

    private void setType(int side) {
        String choice = types[side].getCurrentChoice();
        int type;
        if ("*".equals(choice)) {
            type = -1;
        } else {
            type = DimletType.getTypeByName(choice).ordinal();
        }
        sendServerCommand(RFToolsMessages.INSTANCE, DimletFilterTileEntity.CMD_SETTYPE,
                new Argument("side", side),
                new Argument("type", type));
    }

    private void changeMode(int side) {
        ImageChoiceLabel choiceLabel = bits[side];
        int input = choiceLabel.getCurrentChoiceIndex();
        sendServerCommand(RFToolsMessages.INSTANCE, DimletFilterTileEntity.CMD_SETMODE,
                new Argument("side", side),
                new Argument("input", input));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i2) {
        drawWindow();
    }
}
