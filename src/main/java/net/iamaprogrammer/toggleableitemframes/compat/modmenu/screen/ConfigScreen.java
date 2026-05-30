package net.iamaprogrammer.toggleableitemframes.compat.modmenu.screen;

import net.iamaprogrammer.toggleableitemframes.ToggleableItemFrames;
import net.iamaprogrammer.toggleableitemframes.compat.modmenu.widgets.IntSliderWidget;
import net.iamaprogrammer.toggleableitemframes.config.core.Config;
import net.iamaprogrammer.toggleableitemframes.config.core.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigScreen<C extends Config> extends Screen {
    private static final int MARGIN_TOP = 10;
    private static final int SPACE_FOR_TEXT = 10;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 150;

    private final Screen parentScreen;
    private final SaveCallback<C> saveCallback;
    private final C config;
    private final Class<C> configClass;
    private final List<ConfigItem> widgets;

    protected ConfigScreen(Screen parentScreen, SaveCallback<C> saveCallback, C config, Class<C> configClass, Component title, List<ConfigItem> widgets) {
        super(title);
        this.parentScreen = parentScreen;
        this.saveCallback = saveCallback;
        this.config = config;
        this.configClass = configClass;
        this.widgets = widgets;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(this.saveButton());
        this.addRenderableWidget(this.cancelButton());
        for (int i = 0; i < this.widgets.size(); i++) {
            ConfigItem configItem = this.widgets.get(i);
            AbstractWidget widget = configItem.getWidget();

            int posX = (this.width/2) - (BUTTON_WIDTH/2);
            int posY = ((BUTTON_HEIGHT*2) * i) + SPACE_FOR_TEXT + MARGIN_TOP;
            widget.setPosition(posX, posY);
            this.addRenderableWidget(widget);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        for (int i = 0; i < this.widgets.size(); i++) {

            int posX = (this.width/2) - (BUTTON_WIDTH/2);
            int posY = ((BUTTON_HEIGHT*2) * i) + MARGIN_TOP;

            Component text = this.widgets.get(i).getLabel();
            graphics.text(this.font, text, posX, posY, ARGB.white(1.0f), true);
        }
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    private Button saveButton() {
        return Button.builder(Component.translatable("toggleableitemframes.option.save"), button -> {
            this.closeScreen();
            if (!ConfigUtil.save(this.config)) {
                ToggleableItemFrames.LOGGER.error("Could not save config.");
            } else {
                Path path = Path.of(ConfigUtil.CONFIG_PATH.toString(), this.config.fileName()+".json");
                this.saveCallback.onSave(ConfigUtil.load(path, this.configClass));
            }
        }).size(BUTTON_WIDTH, BUTTON_HEIGHT).pos((((this.width/2) - BUTTON_WIDTH) / 2) + (this.width/2), this.height-BUTTON_HEIGHT).build();
    }

    private Button cancelButton() {
        return Button.builder(Component.translatable("toggleableitemframes.option.cancel"),
                button -> this.closeScreen()
        ).size(BUTTON_WIDTH, BUTTON_HEIGHT).pos(((this.width/2) - BUTTON_WIDTH) / 2, this.height-BUTTON_HEIGHT).build();
    }
    private void closeScreen() {
        this.onClose();
        Minecraft.getInstance().setScreen(this.parentScreen);
    }
    public static <C extends Config> Builder<C> builder(Screen parentScreen, C config, Class<C> configClass, SaveCallback<C> saveCallback) {
        return new Builder<>(parentScreen, config, configClass, saveCallback);
    }

    public static class Builder<C extends Config> {
        private final Screen parentScreen;
        private final C config;
        private final Class<C> configClass;
        private final SaveCallback<C> saveCallback;
        private final List<ConfigItem> widgets = new ArrayList<>();

        public Builder(Screen parentScreen, C config, Class<C> configClass, SaveCallback<C> saveCallback) {
            this.parentScreen = parentScreen;
            this.config = config;
            this.configClass = configClass;
            this.saveCallback = saveCallback;
        }

        public ConfigScreen<C> build() {
            return new ConfigScreen<>(this.parentScreen, this.saveCallback, this.config, this.configClass, Component.literal("Config"), this.widgets);
        }

        public <T> Builder<C> addCyclingButtonWidget(Component desc, List<T> values, UpdateCallback<C, T> callback, LoadCallback<C, T> loadCallback) {
            CycleButton<T> widget = CycleButton.<T>builder(
                (val) -> Component.nullToEmpty(String.valueOf(val)),
                values.getFirst()
            )
            .withValues(values)
            .create(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("toggleableitemframes.option.value"),
                (button, value) -> callback.modifyConfigCallback(this.config, value));
            widget.setValue(loadCallback.onLoad(this.config));
            this.widgets.add(new ConfigItem(desc, widget));
            return this;
        }

        public Builder<C> addSliderWidget(Component desc, int min, int max, UpdateCallback<C, Integer> callback, LoadCallback<C, Integer> loadCallback) {
            IntSliderWidget<C> widget = IntSliderWidget.builder(this.config)
                    .setBounds(min, max)
                    .setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
                    .defaultValue(loadCallback.onLoad(this.config))
                    .build(callback);

            this.widgets.add(new ConfigItem(desc, widget));
            return this;
        }
    }

    public interface UpdateCallback<C, T> {
        void modifyConfigCallback(C config, T value);
    }
    public interface LoadCallback<C, T> {
        T onLoad(C config);
    }
    public interface SaveCallback<C> {
        void onSave(C config);
    }
}
