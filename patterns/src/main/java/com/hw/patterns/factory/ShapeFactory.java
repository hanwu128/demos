package com.hw.patterns.factory;

import com.hw.patterns.abstract_factory.AbstractFactory;
import com.hw.patterns.abstract_factory.Circle;
import com.hw.patterns.abstract_factory.Color;
import com.hw.patterns.abstract_factory.Rectangle;
import com.hw.patterns.abstract_factory.Shape;
import com.hw.patterns.abstract_factory.Square;

public class ShapeFactory extends AbstractFactory {

    @Override
    public Shape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        if (shapeType.equalsIgnoreCase("CIRCLE")) {
            return new Circle();
        } else if (shapeType.equalsIgnoreCase("RECTANGLE")) {
            return new Rectangle();
        } else if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new Square();
        }
        return null;
    }

    @Override
    public Color getColor(String color) {
        return null;
    }
}
