package com.stevewedig.blog.digraph;

import static com.stevewedig.blog.translate.FormatLib.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.*;
import com.stevewedig.blog.digraph.id_graph.*;
import com.stevewedig.blog.errors.NotThrown;

// example dag containing diamond (a, b, c, d)
//
// id -> parentIds...
// a ->
// b -> a
// c -> a
// d -> b, c
// e -> d
//
// childIds <- id...
// b, c <- a
// d <- b
// d <- c
// e <- d
// <- e
public class TestSampleIdDag {

  // ===========================================================================
  // ids
  // ===========================================================================

  private static ImmutableSet<String> idSet = parseSet("a, b, c, d, e");

  // ===========================================================================
  // graphs
  // ===========================================================================

  private static IdDag<String> idDagFromParentMap() {

    return IdDagLib.fromParentMap(idSet, getParentMap());
  }

  private static Multimap<String, String> getParentMap() {

    Multimap<String, String> id__parentIds = HashMultimap.create();

    // a ->
    // b -> a
    // c -> a
    // d -> b, c
    // e -> d
    id__parentIds.put("b", "a");
    id__parentIds.put("c", "a");
    id__parentIds.put("d", "b");
    id__parentIds.put("d", "c");
    id__parentIds.put("e", "d");

    return id__parentIds;
  }

  // ===================================

  private static IdDag<String> idDagFromChildMap() {

    return IdDagLib.fromChildMap(idSet, getChildMap());
  }

  private static Multimap<String, String> getChildMap() {

    Multimap<String, String> id__childIds = HashMultimap.create();

    // b, c <- a
    // d <- b
    // d <- c
    // e <- d
    // <- e
    id__childIds.put("a", "b");
    id__childIds.put("a", "c");
    id__childIds.put("b", "d");
    id__childIds.put("c", "d");
    id__childIds.put("d", "e");

    return id__childIds;
  }

  // ===========================================================================
  // tests
  // ===========================================================================

  @Test
  public void testIdDagFromParentMap() {

    verifyIdDag(idDagFromParentMap());
  }

  @Test
  public void testIdDagFromChildMap() {

    verifyIdDag(idDagFromChildMap());
  }

  // ===========================================================================
  // verify dag
  // ===========================================================================

  public static void verifyIdDag(IdDag<String> dag) {

    // =================================
    // ids
    // =================================

    assertEquals(idSet, dag.idSet());

    assertEquals(idSet.size(), dag.idSize());

    dag.assertIdsEqual(idSet);

    try {
      dag.assertIdsEqual(parseSet("xxx"));
      throw new NotThrown(AssertionError.class);
    } catch (AssertionError e) {
    }

    // =================================
    // parents
    // =================================

    // a ->
    // b -> a
    // c -> a
    // d -> b, c
    // e -> d

    assertEquals(getParentMap(), dag.id__parentIds());

    assertTrue(dag.parentOf("a", "b"));
    assertFalse(dag.parentOf("b", "a"));

    assertEquals(parseSet(""), dag.parentIdSet("a"));
    assertEquals(parseSet("a"), dag.parentIdSet("b"));
    assertEquals(parseSet("a"), dag.parentIdSet("c"));
    assertEquals(parseSet("b, c"), dag.parentIdSet("d"));
    assertEquals(parseSet("d"), dag.parentIdSet("e"));

    // TODO filterParentMap

    // =================================
    // children
    // =================================

    // b, c <- a
    // d <- b
    // d <- c
    // e <- d
    // <- e

    assertEquals(getChildMap(), dag.id__childIds());

    assertFalse(dag.childOf("a", "b"));
    assertTrue(dag.childOf("b", "a"));

    assertEquals(parseSet("b, c"), dag.childIdSet("a"));
    assertEquals(parseSet("d"), dag.childIdSet("b"));
    assertEquals(parseSet("d"), dag.childIdSet("c"));
    assertEquals(parseSet("e"), dag.childIdSet("d"));
    assertEquals(parseSet(""), dag.childIdSet("e"));

    // TODO filterChildMap

    // =================================
    // ancestors
    // =================================

    // a ->
    // b -> a
    // c -> a
    // d -> b, c
    // e -> d

    assertFalse(dag.ancestorOf("e", "a", false));
    assertFalse(dag.ancestorOf("e", "a", true));

    assertTrue(dag.ancestorOf("a", "e", false));
    assertTrue(dag.ancestorOf("a", "e", true));

    assertFalse(dag.ancestorOf("a", "a", false));
    assertTrue(dag.ancestorOf("a", "a", true));

    assertEquals(parseSet(""), dag.ancestorIdSet("a", false));
    assertEquals(parseSet("a"), dag.ancestorIdSet("b", false));
    assertEquals(parseSet("a"), dag.ancestorIdSet("c", false));
    assertEquals(parseSet("a, b, c"), dag.ancestorIdSet("d", false));
    assertEquals(parseSet("a, b, c, d"), dag.ancestorIdSet("e", false));

    assertEquals(parseSet("a"), dag.ancestorIdSet("a", true));
    assertEquals(parseSet("a, b"), dag.ancestorIdSet("b", true));
    assertEquals(parseSet("a, c"), dag.ancestorIdSet("c", true));
    assertEquals(parseSet("a, b, c, d"), dag.ancestorIdSet("d", true));
    assertEquals(parseSet("a, b, c, d, e"), dag.ancestorIdSet("e", true));

    // TODO anc graph

    // =================================
    // descendants
    // =================================

    // b, c <- a
    // d <- b
    // d <- c
    // e <- d
    // <- e

    assertTrue(dag.descendantOf("e", "a", false));
    assertTrue(dag.descendantOf("e", "a", true));

    assertFalse(dag.descendantOf("a", "e", false));
    assertFalse(dag.descendantOf("a", "e", true));

    assertFalse(dag.descendantOf("a", "a", false));
    assertTrue(dag.descendantOf("a", "a", true));

    assertEquals(parseSet("b, c, d, e"), dag.descendantIdSet("a", false));
    assertEquals(parseSet("d, e"), dag.descendantIdSet("b", false));
    assertEquals(parseSet("d, e"), dag.descendantIdSet("c", false));
    assertEquals(parseSet("e"), dag.descendantIdSet("d", false));
    assertEquals(parseSet(""), dag.descendantIdSet("e", false));

    assertEquals(parseSet("a, b, c, d, e"), dag.descendantIdSet("a", true));
    assertEquals(parseSet("b, d, e"), dag.descendantIdSet("b", true));
    assertEquals(parseSet("c, d, e"), dag.descendantIdSet("c", true));
    assertEquals(parseSet("d, e"), dag.descendantIdSet("d", true));
    assertEquals(parseSet("e"), dag.descendantIdSet("e", true));

    // TODO desc graph

    // =================================
    // roots (sources)
    // =================================

    assertFalse(dag.isRootId("e"));
    assertTrue(dag.isRootId("a"));

    assertEquals(parseSet("a"), dag.rootIdSet());

    // =================================
    // leaves (sinks)
    // =================================

    assertFalse(dag.isLeafId("a"));
    assertTrue(dag.isLeafId("e"));

    assertEquals(parseSet("e"), dag.leafIdSet());

    // =================================
    // topological sort
    // =================================

    assertFalse(dag.containsCycle());

    ImmutableSet<ImmutableList<String>> topsortIdLists =
        ImmutableSet.of(parseList("a, b, c, d, e"), parseList("a, c, b, d, e"));

    assertTrue(topsortIdLists.contains(dag.topsortIdList()));

    // same instance
    assertTrue(dag.topsortIdList() == dag.optionalTopsortIdList().get());

    // =================================
    // depth first
    // =================================

    ImmutableSet<ImmutableList<String>> depthIdLists =
        ImmutableSet.of(parseList("a, b, d, e, c"), parseList("a, c, d, e, b"));

    assertTrue(depthIdLists.contains(dag.depthIdList()));

    // =================================
    // breadth first
    // =================================

    ImmutableSet<ImmutableList<String>> breadthIdLists =
        ImmutableSet.of(parseList("a, b, c, d, e"), parseList("a, c, b, d, e"));

    assertTrue(breadthIdLists.contains(dag.breadthIdList()));

  }

}
