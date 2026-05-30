package net.iamaprogrammer.toggleableitemframes.compat.modmenu.screen;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class ConfigItem {
    private final Component label;
    private final AbstractWidget widget;

    public ConfigItem(Component label, AbstractWidget widget) {
        this.label = label;
        this.widget = widget;
    }

    public Component getLabel() {
        return label;
    }

    public AbstractWidget getWidget() {
        return widget;
    }
}
