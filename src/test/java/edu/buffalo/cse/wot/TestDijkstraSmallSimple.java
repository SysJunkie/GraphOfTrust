package edu.buffalo.cse.wot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import edu.buffalo.cse.wot.neo4j.JettyServer;
import edu.buffalo.cse.wot.neo4j.Pair;
import edu.buffalo.cse.wot.neo4j.config.AppConstants;
import edu.buffalo.cse.wot.neo4j.datastore.DataStoreManager;
import edu.buffalo.cse.wot.neo4j.utils.DataUtils;
import edu.buffalo.cse.wot.neo4j.utils.QaRandomDistributor;
import edu.buffalo.cse.wot.neo4j.utils.ScoreUtils;
/**
 *
 * @author varunjai
 *
 */
public class TestDijkstraSmallSimple {

  private static JettyServer server;
  private static Logger logger = LogManager
      .getLogger(TestDijkstraSmallSimple.class);
  static List<Long> uids;

  @BeforeClass
  public static void testPrep() throws Exception {
    server = new JettyServer();
    server.start();

    // load simple graph
    uids = DataUtils.loadSmallGraph();
  }

  @org.junit.Test
  public void testFixedDijkstra1() {

    /*
     * DataStoreManager.getInstance().getSccUids(AppConstants.LABEL_USER,
     * uids.size());
     */

    final long id = 1;
    final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
        .getUnshuffledSplit(uids, 0.75f, uids.size(), id);

    assertTrue(DataStoreManager.getInstance().getShortestStrongestResponse(
        AppConstants.LABEL_USER, String.valueOf(id), distribution));
  }

  @org.junit.Test
  public void testFixedDijkstra2() {

    final long id = 1;
    final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
        .getUnshuffledSplit(uids, 0.5f, uids.size(), id);

    // find the shortest path
    assertTrue(DataStoreManager.getInstance().getShortestStrongestResponse(
        AppConstants.LABEL_USER, String.valueOf(id), distribution));
  }

  /**
   *
   */
  @org.junit.Test
  public void testFixedDijkstra3() {

    final long id = 1;
    final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
        .getUnshuffledSplit(uids, 0.25f, uids.size(), id);

    assertTrue(DataStoreManager.getInstance().getShortestStrongestResponse(
        AppConstants.LABEL_USER, String.valueOf(id), distribution));
  }

  /**
   *
   */
  @org.junit.Test
  public void testFixedDijkstra4() {

    final long id = 1;
    final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
        .getUnshuffledSplit(uids, 0.1f, uids.size(), id);

    assertFalse(DataStoreManager.getInstance().getShortestStrongestResponse(
        AppConstants.LABEL_USER, String.valueOf(id), distribution));
  }

  @org.junit.Test
  public void testFixedDijkstraWithFeedback2() {

    final List<Boolean> expected = new ArrayList<>();
    final List<Boolean> actual = new ArrayList<>();

    final long id = 1;
    for (int i = 0; i < 10; i++) {
      actual.add(true);
      final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
          .getUnshuffledSplit(uids, 0.5f, uids.size(), id);

      expected.add(DataStoreManager.getInstance().getShortestStrongestResponse(
          AppConstants.LABEL_USER, String.valueOf(id), distribution));
      ScoreUtils.processFeedback(AppConstants.LABEL_USER, actual.get(i),
          expected.get(i), actual.get(i) == expected.get(i), distribution);
    }// for

    System.out.println("feedback" + ScoreUtils.getRMSE(expected, actual));
  }

  
  @org.junit.Test
  public void testFixedDijkstraWithFeedback3() {

    final List<Boolean> expected = new ArrayList<>();
    final List<Boolean> actual = new ArrayList<>();

    final long id = 3;
    for (int i = 0; i < 10; i++) {
      actual.add(true);
      final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
          .getUnshuffledSplit(uids, 0.25f, uids.size(), id);

      expected.add(DataStoreManager.getInstance().getShortestStrongestResponse(
          AppConstants.LABEL_USER, String.valueOf(id), distribution));
      ScoreUtils.processFeedback(AppConstants.LABEL_USER, actual.get(i),
          expected.get(i), actual.get(i) == expected.get(i), distribution);
    }// for

    System.out.println("feedback" + ScoreUtils.getRMSE(expected, actual));
  }

  /**
  *
  */
  @org.junit.Test
  public void testFixedDijkstraWithFeedback4() {

    final List<Boolean> expected = new ArrayList<>();
    final List<Boolean> actual = new ArrayList<>();

    final long id = 1;
    for (int i = 0; i < 10; i++) {
      actual.add(true);
      final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
          .getUnshuffledSplit(uids, 0.1f, uids.size(), id);

      expected.add(DataStoreManager.getInstance().getShortestStrongestResponse(
          AppConstants.LABEL_USER, String.valueOf(id), distribution));
      ScoreUtils.processFeedback(AppConstants.LABEL_USER, actual.get(i),
          expected.get(i), actual.get(i) == expected.get(i), distribution);
    }

    System.out.println("feedback" + ScoreUtils.getRMSE(expected, actual));
  }

  @org.junit.Test
  public void testShuffledDijkstra1() {

    final List<Boolean> expected = new ArrayList<>();
    final List<Boolean> actual = new ArrayList<>();

    final long id = 1;
    for (int i = 0; i < 10; i++) {
      actual.add(true);
      final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
          .getShuffled(uids, 0.75f, id);

      expected.add(DataStoreManager.getInstance().getShortestStrongestResponse(
          AppConstants.LABEL_USER, String.valueOf(id), distribution));
    } // for

    System.out.println(ScoreUtils.getRMSE(expected, actual));
  }

  @org.junit.Test
  public void testShuffledDijkstra2() {

    final List<Boolean> expected = new ArrayList<>();
    final List<Boolean> actual = new ArrayList<>();
    final long id = 1;

    for (int i = 0; i < 10; i++) {
      actual.add(true);
      final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
          .getShuffled(uids, 0.5f, id);

      expected.add(DataStoreManager.getInstance().getShortestStrongestResponse(
          AppConstants.LABEL_USER, String.valueOf(id), distribution));
    } // for

    System.out.println(ScoreUtils.getRMSE(expected, actual));
  }

  @org.junit.Test
  public void testShuffledDijkstra3() {

    final List<Boolean> expected = new ArrayList<>();
    final List<Boolean> actual = new ArrayList<>();
    final long id = 1;

    for (int i = 0; i < 10; i++) {
      actual.add(true);
      final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
          .getShuffled(uids, 0.25f, id);

      expected.add(DataStoreManager.getInstance().getShortestStrongestResponse(
          AppConstants.LABEL_USER, String.valueOf(id), distribution));
    } // for

    System.out.println(ScoreUtils.getRMSE(expected, actual));
  }

  @org.junit.Test
  public void testShuffledDijkstra4() {

    final List<Boolean> expected = new ArrayList<>();
    final List<Boolean> actual = new ArrayList<>();
    final long id = 1;
    for (int i = 0; i < 10; i++) {
      actual.add(true);
      final Pair<Set<Long>, Set<Long>> distribution = QaRandomDistributor
          .getShuffled(uids, 0.1f, id);

      expected.add(DataStoreManager.getInstance().getShortestStrongestResponse(
          AppConstants.LABEL_USER, String.valueOf(id), distribution));
    } // for

    System.out.println(ScoreUtils.getRMSE(expected, actual));
  }

  @AfterClass
  public static void testDestroy() throws Exception {
    server.stop();
  }
}
