package com.stevewedig.blog.util;

import com.google.common.collect.*;

/**
 * Multimap related utilities.
 */
public abstract class MultimapLib {

  public static <Item> ImmutableSet<Item> keysAndValues(Multimap<Item, Item> key__values) {

    ImmutableSet.Builder<Item> items = ImmutableSet.builder();

    items.addAll(key__values.keySet());

    items.addAll(key__values.values());

    return items.build();
  }

}
