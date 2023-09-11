package org.labs;

import lombok.Getter;

import java.util.List;

public class Context {
    public static final MemoryGrid memoryGrid = new MemoryGrid();
    public static final Photo photo = new Photo();
    public static final Equation equation = new Equation();
    public static final List<Displayable> displayables = List.of(
      memoryGrid,
      photo,
      equation
    );

}
