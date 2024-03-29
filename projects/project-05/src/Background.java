import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background {
  private final String id;
  private final List<PImage> images;
  private int imageIndex;

  public Background(String id, List<PImage> images) {
    this.id = id;
    this.images = images;
  }

  public List<PImage> getImages() {
    return images;
  }

  public int getImageIndex() {
    return imageIndex;
  }

  public PImage getCurrentImage() {
    return images.get(imageIndex);
  }

    public String getId() {
      return id;
    }
}
