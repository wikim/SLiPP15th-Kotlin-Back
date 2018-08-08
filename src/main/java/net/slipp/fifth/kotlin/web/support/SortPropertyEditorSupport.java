package net.slipp.fifth.kotlin.web.support;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class SortPropertyEditorSupport extends PropertyEditorSupport {

    private final String orderProperty;
    private final PropertyValues values;

    public SortPropertyEditorSupport(String orderProperty, PropertyValues values) {
        this.orderProperty = orderProperty;
        this.values = values;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        PropertyValue rawOrder = values.getPropertyValue(orderProperty);
        Direction order = null == rawOrder ? Direction.ASC : Direction.fromString(rawOrder.getValue().toString());

        setValue(new Sort(order, text));
    }

}
