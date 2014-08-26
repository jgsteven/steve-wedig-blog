package com.stevewedig.blog.symbol;

import java.util.Map;

import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.stevewedig.blog.value_objects.HasObjectHelper;

// can't extend Map because SymbolMap.get() is more restrictive
/**
 * A type safe mapping from symbols to values of matching types.
 */
public interface SymbolMap extends Iterable<Entry<Symbol<?>, Object>>, HasObjectHelper {

  // ===========================================================================
  // SymbolMap
  // ===========================================================================

  /**
   * 
   * @return type safe value associated with symbol
   */
  <Value> Value get(Symbol<Value> symbol);

  // ===================================
  // convenience getters
  // ===================================

  <Value> Value getDefault(Symbol<Value> symbol, Value defaultValue, boolean adaptNull);

  <Value> Value getDefault(Symbol<Value> symbol, Value defaultValue);

  <Value> Optional<Value> getOptional(Symbol<Value> symbol, boolean adaptNull);

  <Value> Optional<Value> getOptional(Symbol<Value> symbol);

  <Value> Value getNullable(Symbol<Value> symbol);

  // ===================================
  // symbols
  // ===================================

  /**
   * @return Whether the symbol is contained in the SymbolMap.
   */
  boolean contains(Symbol<?> symbol);

  /**
   * @return The symbols in the SymbolMap.
   */
  ImmutableSet<Symbol<?>> symbols();

  // ===================================
  // copies
  // ===================================

  /**
   * @return A SymbolMap.Fluid copy.
   */
  SymbolMap.Fluid fluid();

  /**
   * @return A SymbolMap.Solid copy.
   */
  SymbolMap.Solid solid();

  /**
   * 
   * @return A Map copy.
   */
  Map<Symbol<?>, Object> stateCopy();

  // ===========================================================================
  // SymbolMap.Solid
  // ===========================================================================

  /**
   * A SymbolMap that is immutable and a value object.
   */
  interface Solid extends SymbolMap {
  }

  // ===========================================================================
  // SymbolMap.Fluid
  // ===========================================================================

  /**
   * A SymbolMap that is mutable and an entity object.
   */
  interface Fluid extends SymbolMap {

    // fluid interface allows us to build immutable maps via mutable ones
    <Value> SymbolMap.Fluid put(Symbol<Value> symbol, Value value);

    SymbolMap.Fluid putAll(SymbolMap updates);

  }

}
