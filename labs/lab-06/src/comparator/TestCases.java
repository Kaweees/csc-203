package comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

public class TestCases {
  private static final Song[] songs = new Song[] { new Song("Decemberists", "The Mariner's Revenge Song", 2005),
      new Song("Rogue Wave", "Love's Lost Guarantee", 2005), new Song("Avett Brothers", "Talk on Indolence", 2006),
      new Song("Gerry Rafferty", "Baker Street", 1998), new Song("City and Colour", "Sleeping Sickness", 2007),
      new Song("Foo Fighters", "Baker Street", 1997), new Song("Queen", "Bohemian Rhapsody", 1975),
      new Song("Gerry Rafferty", "Baker Street", 1978) };

  @Test
  public void testArtistComparator() {
    ArtistComparator artistComparator = new ArtistComparator();

    // Compare the first and second songs
    int comparisonResult = artistComparator.compare(songs[0], songs[1]);
    assertTrue(comparisonResult < 0);

    // Compare the fourth and fifth songs
    comparisonResult = artistComparator.compare(songs[3], songs[4]);
    assertTrue(comparisonResult > 0);

    // Compare the seventh and eighth songs
    comparisonResult = artistComparator.compare(songs[6], songs[7]);
    assertTrue(comparisonResult > 0);
  }

  @Test
  public void testLambdaTitleComparator() {
    Comparator<Song> titleComparator = (song1, song2) -> song1.getTitle().compareTo(song2.getTitle());

    // Compare the first and second songs
    int comparisonResult = titleComparator.compare(songs[0], songs[1]);
    assertTrue(comparisonResult > 0);

    // Compare the fourth and fifth songs
    comparisonResult = titleComparator.compare(songs[3], songs[4]);
    assertTrue(comparisonResult < 0);

    // Compare the sixth and seventh songs
    comparisonResult = titleComparator.compare(songs[5], songs[6]);
    assertTrue(comparisonResult < 0);
  }

  @Test
  public void testYearExtractorComparator() {
    Comparator<Song> yearComparator = Comparator.comparingInt(Song::getYear);
    Comparator<Song> descendingYearComparator = yearComparator.reversed();

    // Compare the second and third songs
    int comparisonResult = descendingYearComparator.compare(songs[1], songs[2]);
    assertTrue(comparisonResult > 0);

    // Compare the fifth and seventh songs
    comparisonResult = descendingYearComparator.compare(songs[4], songs[6]);
    assertTrue(comparisonResult < 0);

    // Compare the fourth and eighth songs
    comparisonResult = descendingYearComparator.compare(songs[3], songs[7]);
    assertTrue(comparisonResult < 0);
  }

  @Test
  public void testComposedComparator() {
    Comparator<Song> yearComparator = Comparator.comparingInt(Song::getYear);
    Comparator<Song> artistComparator = Comparator.comparing(Song::getArtist);

    ComposedComparator composedComparator = new ComposedComparator(yearComparator, artistComparator);

    // Compare the fourth and eighth songs
    int comparisonResult = composedComparator.compare(songs[3], songs[7]);
    assertTrue(comparisonResult > 0);
  }

  @Test
  public void testThenComparing() {
    Comparator<Song> titleComparator = Comparator.comparing(Song::getTitle);
    Comparator<Song> artistComparator = Comparator.comparing(Song::getArtist);

    Comparator<Song> titleThenArtistComparator = titleComparator.thenComparing(artistComparator);
    Arrays.sort(songs, titleThenArtistComparator);

    // Verify the ordering of songs with the same title and different artists
    int comparisonResult = titleThenArtistComparator.compare(songs[3], songs[5]);
    assertTrue(comparisonResult < 0);
  }

  @Test
  public void runSort() {
    List<Song> songList = new ArrayList<>(Arrays.asList(songs));
    List<Song> expectedList = Arrays.asList(new Song("Avett Brothers", "Talk on Indolence", 2006),
        new Song("City and Colour", "Sleeping Sickness", 2007),
        new Song("Decemberists", "The Mariner's Revenge Song", 2005), new Song("Foo Fighters", "Baker Street", 1997),
        new Song("Gerry Rafferty", "Baker Street", 1978), new Song("Gerry Rafferty", "Baker Street", 1998),
        new Song("Queen", "Bohemian Rhapsody", 1975), new Song("Rogue Wave", "Love's Lost Guarantee", 2005));

    songList.sort(
        // pass comparator here
        Comparator.comparing(Song::getArtist).thenComparing(Song::getTitle).thenComparing(Song::getYear));
    assertEquals(songList, expectedList);
  }
}
