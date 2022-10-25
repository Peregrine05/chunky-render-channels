
package io.github.peregrine05.chunkyrenderchannels.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.chunky.renderer.scene.Sky;
import se.llbit.chunky.ui.render.RenderControlsTab;
import se.llbit.chunky.ui.RenderControlsFxController;
import se.llbit.json.Json;

import javax.tools.Tool;

public class ChunkyRenderChannelsTab implements RenderControlsTab {

  private Scene scene = null;

  private boolean renderStateSaved = false;
  private final VBox box = new VBox(10.0);

  private CheckBox enableRenderChannels = new CheckBox("Enable render channels");
  private SimpleBooleanProperty enableRenderChannelsProperty = new SimpleBooleanProperty(false);

  private CheckBox skyChannel = new CheckBox("Sky channel");
  private SimpleBooleanProperty enableSkyChannel = new SimpleBooleanProperty(false);

  private Button saveSkyState = new Button("Save current sky state");

  private CheckBox fogChannel = new CheckBox("Fog channel");
  private SimpleBooleanProperty enableFogChannel = new SimpleBooleanProperty(false);

  private Button saveFogState = new Button("Save current fog state");

  private CheckBox sunChannel = new CheckBox("Sun channel");
  private SimpleBooleanProperty enableSunChannel = new SimpleBooleanProperty(false);

  private CheckBox emittersChannel = new CheckBox("Emitters channel");
  private SimpleBooleanProperty enableEmittersChannel = new SimpleBooleanProperty(false);

  public ChunkyRenderChannelsTab() {
    enableRenderChannelsProperty.addListener((observable, oldValue, newValue) -> {
      if (scene != null) {
        if (newValue) {
          if (!renderStateSaved) {
            scene.setAdditionalData("render_channels_saved_sky_mode", Json.of(scene.sky().getSkyMode().name()));
            scene.setAdditionalData("render_channels_saved_draw_sun", Json.of(scene.sun().drawTexture()));
            scene.setAdditionalData("render_channels_saved_fog_density", Json.of(scene.getFogDensity()));
            scene.setAdditionalData("render_channels_saved_enable_sunlight", Json.of(scene.getDirectLight()));
            scene.setAdditionalData("render_channels_saved_enable_emitters", Json.of(scene.getEmittersEnabled()));

            renderStateSaved = true;

            skyChannel.setDisable(false);
            fogChannel.setDisable(false);
            sunChannel.setDisable(false);
            emittersChannel.setDisable(false);
          }

          scene.sky().setSkyMode(Sky.SkyMode.BLACK);
          scene.setFogDensity(0.0);
          scene.setDirectLight(false);
          scene.sun().setDrawTexture(false);
          scene.setEmittersEnabled(false);
        } else {
          scene.sky().setSkyMode(Sky.SkyMode.valueOf(scene.getAdditionalData("render_channels_saved_sky_mode").stringValue(scene.sky().getSkyMode().name())));
          scene.sun().setDrawTexture(scene.getAdditionalData("render_channels_saved_draw_sun").boolValue(scene.sun().drawTexture()));
          scene.setFogDensity(scene.getAdditionalData("render_channels_saved_fog_density").doubleValue(scene.getFogDensity()));
          scene.setDirectLight(scene.getAdditionalData("render_channels_saved_enable_sunlight").boolValue(scene.getDirectLight()));
          scene.setEmittersEnabled(scene.getAdditionalData("render_channels_saved_enable_emitters").boolValue(scene.getEmittersEnabled()));

          renderStateSaved = false;

          skyChannel.setSelected(false);
          fogChannel.setSelected(false);
          sunChannel.setSelected(false);
          emittersChannel.setSelected(false);

          skyChannel.setDisable(true);
          saveSkyState.setDisable(true);
          fogChannel.setDisable(true);
          saveFogState.setDisable(true);
          sunChannel.setDisable(true);
          emittersChannel.setDisable(true);
        }
      }
    });

    enableSkyChannel.addListener((observable, oldValue, newValue) -> {
      if (scene != null) {
        if (newValue) {
          scene.sky().setSkyMode(Sky.SkyMode.valueOf(scene.getAdditionalData("render_channels_saved_sky_mode").stringValue(scene.sky().getSkyMode().name())));
          scene.sun().setDrawTexture(scene.getAdditionalData("render_channels_saved_draw_sun").boolValue(scene.sun().drawTexture()));
          saveSkyState.setDisable(false);
        } else if (renderStateSaved) {
          scene.sky().setSkyMode(Sky.SkyMode.BLACK);
          scene.sun().setDrawTexture(false);
          saveSkyState.setDisable(true);
        }
      }
    });

    enableFogChannel.addListener((observable, oldValue, newValue) -> {
      if (scene != null) {
        if (newValue) {
          scene.setFogDensity(scene.getAdditionalData("render_channels_saved_fog_density").doubleValue(scene.getFogDensity()));
          saveFogState.setDisable(false);
        } else if (renderStateSaved) {
          scene.setFogDensity(0.0);
          saveFogState.setDisable(true);
        }
      }
    });

    enableSunChannel.addListener((observable, oldValue, newValue) -> {
      if (scene != null) {
        if (newValue) {
          scene.setDirectLight(true);
        } else if (renderStateSaved) {
          scene.setDirectLight(false);
        }
      }
    });

    enableEmittersChannel.addListener((observable, oldValue, newValue) -> {
      if (scene != null) {
        if (newValue) {
          scene.setEmittersEnabled(true);
        } else if (renderStateSaved) {
          scene.setEmittersEnabled(false);
        }
      }
    });

    box.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

    enableRenderChannels.selectedProperty().bindBidirectional(enableRenderChannelsProperty);
    enableRenderChannels.setTooltip(new Tooltip("When enabled: Save current scene settings to JSON and disable separate lighting channels.\nWhen disabled: Restore saved render settings from JSON."));
    box.getChildren().add(enableRenderChannels);

    box.getChildren().add(new Separator());

    skyChannel.selectedProperty().bindBidirectional(enableSkyChannel);
    skyChannel.setTooltip(new Tooltip("Enable sky lighting by setting the sky mode and the sun texture state to the saved values."));
    skyChannel.setDisable(true);
    box.getChildren().add(skyChannel);

    saveSkyState.setOnAction(event -> {
      scene.setAdditionalData("render_channels_saved_sky_mode", Json.of(scene.sky().getSkyMode().name()));
      scene.setAdditionalData("render_channels_saved_draw_sun", Json.of(scene.sun().drawTexture()));
    });
    saveSkyState.setTooltip(new Tooltip("Save any changes made to the Sky mode and the sun texture to JSON."));
    saveSkyState.setDisable(true);
    box.getChildren().add(saveSkyState);

    fogChannel.selectedProperty().bindBidirectional(enableFogChannel);
    fogChannel.setDisable(true);
    fogChannel.setTooltip(new Tooltip("Enable fog lighting by setting the Fog density to the saved value."));
    box.getChildren().add(fogChannel);

    saveFogState.setOnAction(event -> {
      scene.setAdditionalData("render_channels_saved_fog_density", Json.of(scene.getFogDensity()));
    });
    saveFogState.setTooltip(new Tooltip("Save any changes made to the Fog density to JSON."));
    saveFogState.setDisable(true);
    box.getChildren().add(saveFogState);

    sunChannel.selectedProperty().bindBidirectional(enableSunChannel);
    sunChannel.setTooltip(new Tooltip("Enable sun lighting."));
    sunChannel.setDisable(true);
    box.getChildren().add(sunChannel);

    emittersChannel.selectedProperty().bindBidirectional(enableEmittersChannel);
    emittersChannel.setDisable(true);
    emittersChannel.setTooltip(new Tooltip("Enable emitter lighting."));
    box.getChildren().add(emittersChannel);
  }

  @Override
  public void update(Scene scene) {
    this.scene = scene;
  }

  @Override
  public String getTabTitle() {
    return "Render Channels Plugin";
  }

  @Override
  public Node getTabContent() {
    return box;
  }
}
