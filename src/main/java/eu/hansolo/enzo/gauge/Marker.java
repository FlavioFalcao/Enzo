package eu.hansolo.enzo.gauge;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;


/**
 * Created by
 * User: hansolo
 * Date: 03.07.13
 * Time: 08:12
 */
public class Marker {
    private double         _start;
    private DoubleProperty value;
    private String         _text;
    private StringProperty text;


    // ******************** Constructors **************************************
    public Marker() {
        this(0, "");
    }
    public Marker(final double VALUE) {
        this(VALUE, "");
    }
    public Marker(final double VALUE, final String TEXT) {
        _start = VALUE;
        _text  = TEXT;
    }


    // ******************** Methods *******************************************
    public final double getValue() {
        return null == value ? _start : value.get();
    }
    public final void setValue(final double START) {
        if (null == value) {
            _start = START;
        } else {
            value.set(START);
        }
    }
    public final DoubleProperty startProperty() {
        if (null == value) {
            value = new SimpleDoubleProperty(this, "value", _start);
        }
        return value;
    }

    public final String getText() {
        return null == text ? _text : text.get();
    }
    public final void setText(final String TEXT) {
        if (null == text) {
            _text = TEXT;
        } else {
            text.set(TEXT);
        }
    }
    public final StringProperty textProperty() {
        if (null == text) {
            text = new SimpleStringProperty(this, "text", _text);
        }
        return text;
    }


    // ******************** Event handling ************************************
    public final ObjectProperty<EventHandler<MarkerEvent>> onMarkerExceededProperty() { return onMarkerExceeded; }
    public final void setOnMarkerExceeded(EventHandler<MarkerEvent> value) { onMarkerExceededProperty().set(value); }
    public final EventHandler<MarkerEvent> getOnMarkerExceeded() { return onMarkerExceededProperty().get(); }
    private ObjectProperty<EventHandler<MarkerEvent>> onMarkerExceeded = new ObjectPropertyBase<EventHandler<MarkerEvent>>() {
        @Override public Object getBean() { return this; }
        @Override public String getName() { return "onMarkerExceeded";}
    };

    public final ObjectProperty<EventHandler<MarkerEvent>> onMarkerUnderrunProperty() { return onMarkerUnderrun; }
    public final void setOnMarkerUnderrun(EventHandler<MarkerEvent> value) { onMarkerUnderrunProperty().set(value); }
    public final EventHandler<MarkerEvent> getOnMarkerUnderrun() { return onMarkerUnderrunProperty().get(); }
    private ObjectProperty<EventHandler<MarkerEvent>> onMarkerUnderrun = new ObjectPropertyBase<EventHandler<MarkerEvent>>() {
        @Override public Object getBean() { return this; }
        @Override public String getName() { return "onMarkerUnderrun";}
    };

    public void fireMarkerEvent(final MarkerEvent EVENT) {
        final EventHandler<MarkerEvent> HANDLER;
        final EventType TYPE = EVENT.getEventType();
        if (MarkerEvent.MARKER_EXCEEDED == TYPE) {
            HANDLER = getOnMarkerExceeded();
        } else if (MarkerEvent.MARKER_UNDERRUN == TYPE) {
            HANDLER = getOnMarkerUnderrun();
        } else {
            HANDLER = null;
        }

        if (null == HANDLER) return;

        HANDLER.handle(EVENT);
    }
    
    
    public boolean equals(final Marker MARKER) {
        return (Double.compare(MARKER.getValue(), getValue()) == 0 &&
            MARKER.getText().equals(getText()));
    }

    @Override public String toString() {
        final StringBuilder NAME = new StringBuilder();
        NAME.append("Section: ").append("\n");
        NAME.append("text   : ").append(text.get()).append("\n");
        NAME.append("value  : ").append(value.get()).append("\n");
        return NAME.toString();
    }

    
    // ******************** Inner Classes *************************************
    public static class MarkerEvent extends Event {
        public static final EventType<MarkerEvent> MARKER_EXCEEDED = new EventType(ANY, "markerExceeded");
        public static final EventType<MarkerEvent> MARKER_UNDERRUN = new EventType(ANY, "markerUnderrun");
    
    
        // ******************** Constructors **************************************
        public MarkerEvent(final Object SOURCE, final EventTarget TARGET, EventType<MarkerEvent> TYPE) {
            super(SOURCE, TARGET, TYPE);
        }
    }
}