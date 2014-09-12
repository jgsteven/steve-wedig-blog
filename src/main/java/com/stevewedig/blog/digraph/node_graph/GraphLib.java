package com.stevewedig.blog.digraph.node_graph;

import java.util.Set;

import com.google.common.collect.*;
import com.stevewedig.blog.digraph.id_graph.IdGraph;
import com.stevewedig.blog.digraph.node.*;
import com.stevewedig.blog.util.SetLib;

/**
 * A library for creating Graphs.
 */
public abstract class GraphLib {

  // ===========================================================================
  // graph from state
  // ===========================================================================

  public static <Id, Node> Graph<Id, Node> graph(IdGraph<Id> idGraph,
      ImmutableBiMap<Id, Node> id__node) {

    return new GraphClass<>(idGraph, id__node, false);
  }

  // ===========================================================================
  // graph from up nodes
  // ===========================================================================

  public static <Id, Node extends UpNode<Id>> Graph<Id, Node> up(ImmutableSet<Node> nodeSet) {

    IdGraph<Id> idGraph = UpNodeLib.nodes__idGraph(nodeSet);

    ImmutableBiMap<Id, Node> id__node = UpNodeLib.nodes__nodeMap(nodeSet);

    return graph(idGraph, id__node);
  }

  public static <Id, Node extends UpNode<Id>> Graph<Id, Node> up(Iterable<Node> nodeIter) {
    return up(ImmutableSet.copyOf(nodeIter));
  }

  @SafeVarargs
  public static <Id, Node extends UpNode<Id>> Graph<Id, Node> up(Node... nodeArray) {
    return up(ImmutableSet.copyOf(nodeArray));
  }

  @SafeVarargs
  public static <Id, Node extends UpNode<Id>> Graph<Id, Node> up(Set<Node>... graphs) {
    return up(SetLib.union(graphs));
  }

  // ===========================================================================
  // graph from down nodes
  // ===========================================================================

  public static <Id, Node extends DownNode<Id>> Graph<Id, Node> down(ImmutableSet<Node> nodeSet) {

    IdGraph<Id> idGraph = DownNodeLib.nodes__idGraph(nodeSet);

    ImmutableBiMap<Id, Node> id__node = DownNodeLib.nodes__nodeMap(nodeSet);

    return graph(idGraph, id__node);
  }

  public static <Id, Node extends DownNode<Id>> Graph<Id, Node> down(Iterable<Node> nodeIter) {
    return down(ImmutableSet.copyOf(nodeIter));
  }

  @SafeVarargs
  public static <Id, Node extends DownNode<Id>> Graph<Id, Node> down(Node... nodeArray) {
    return down(ImmutableSet.copyOf(nodeArray));
  }

  @SafeVarargs
  public static <Id, Node extends DownNode<Id>> Graph<Id, Node> down(Set<Node>... graphs) {
    return down(SetLib.union(graphs));
  }

}
