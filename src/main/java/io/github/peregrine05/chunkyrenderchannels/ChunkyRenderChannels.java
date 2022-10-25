
package io.github.peregrine05.chunkyrenderchannels;

import se.llbit.chunky.Plugin;
import se.llbit.chunky.main.Chunky;
import se.llbit.chunky.main.ChunkyOptions;
import se.llbit.chunky.ui.ChunkyFx;
import se.llbit.chunky.ui.render.RenderControlsTab;
import se.llbit.chunky.ui.render.RenderControlsTabTransformer;
import io.github.peregrine05.chunkyrenderchannels.ui.ChunkyRenderChannelsTab;

import java.util.ArrayList;
import java.util.List;

public class ChunkyRenderChannels implements Plugin {
  @Override
  public void attach(Chunky chunky) {
    RenderControlsTabTransformer prev = chunky.getRenderControlsTabTransformer();
    chunky.setRenderControlsTabTransformer(tabs -> {
      List<RenderControlsTab> transformed = new ArrayList<>(prev.apply(tabs));
      transformed.add(new ChunkyRenderChannelsTab());
      return transformed;
    });
  }

  public static void main(String[] args) {
    // Start Chunky normally with this plugin attached.
    Chunky.loadDefaultTextures();
    Chunky chunky = new Chunky(ChunkyOptions.getDefaults());
    new ChunkyRenderChannels().attach(chunky);
    ChunkyFx.startChunkyUI(chunky);
  }
}
