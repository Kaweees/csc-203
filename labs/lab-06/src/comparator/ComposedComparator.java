package comparator;

import java.util.Comparator;

public class ComposedComparator implements Comparator<Song> {
  private final Comparator<Song> c1;
  private final Comparator<Song> c2;

  public ComposedComparator(Comparator<Song> c1, Comparator<Song> c2) {
    this.c1 = c1;
    this.c2 = c2;
  }

  @Override
  public int compare(Song song1, Song song2) {
    int result = c1.compare(song1, song2);
    if (result == 0) {
      return c2.compare(song1, song2);
    }
    return result;
  }
}