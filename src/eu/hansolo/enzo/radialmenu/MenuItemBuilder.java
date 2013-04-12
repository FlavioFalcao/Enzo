/*
 * Copyright (c) 2013, Gerrit Grunwald
 * All right reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * The names of its contributors may not be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package eu.hansolo.enzo.radialmenu;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 24.09.12
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class MenuItemBuilder implements Builder<MenuItem> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected MenuItemBuilder() {}


    // ******************** Methods *******************************************
    public static final MenuItemBuilder create() {
        return new MenuItemBuilder();
    }

    public final MenuItemBuilder tooltip(final String TOOLTIP) {
        properties.put("TOOLTIP", new SimpleStringProperty(TOOLTIP));
        return this;
    }

    public final MenuItemBuilder size(final double SIZE) {
        properties.put("SIZE", new SimpleDoubleProperty(SIZE));
        return this;
    }

    public final MenuItemBuilder innerColor(final Color INNER_COLOR) {
        properties.put("INNER_COLOR", new SimpleObjectProperty<Color>(INNER_COLOR));
        return this;
    }

    public final MenuItemBuilder frameColor(final Color FRAME_COLOR) {
        properties.put("FRAME_COLOR", new SimpleObjectProperty<Color>(FRAME_COLOR));
        return this;
    }

    public final MenuItemBuilder foregroundColor(final Color FOREGROUND_COLOR) {
        properties.put("FOREGROUND_COLOR", new SimpleObjectProperty<Color>(FOREGROUND_COLOR));
        return this;
    }

    public final MenuItemBuilder symbol(final Symbol.Type SYMBOL_TYPE) {
        properties.put("SYMBOL", new SimpleObjectProperty<Symbol.Type>(SYMBOL_TYPE));
        return this;
    }

    public final MenuItemBuilder thumbnailImageName(final String THUMBNAIL_IMAGE_NAME) {
        properties.put("THUMBNAIL_IMAGE_NAME", new SimpleStringProperty(THUMBNAIL_IMAGE_NAME));
        return this;
    }

    @Override public final MenuItem build() {
        final MenuItem CONTROL = new MenuItem();

        for (String key : properties.keySet()) {
            if ("TOOLTIP".equals(key)) {
                CONTROL.setTooltip(((StringProperty) properties.get(key)).get());
            } else if("SIZE".equals(key)) {
                CONTROL.setSize(((DoubleProperty) properties.get(key)).get());
            } else if ("INNER_COLOR".equals(key)) {
                CONTROL.setInnerColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("FRAME_COLOR".equals(key)) {
                CONTROL.setFrameColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("FOREGROUND_COLOR".equals(key)) {
                CONTROL.setForegroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("SYMBOL".equals(key)) {
                CONTROL.setSymbol(((ObjectProperty<Symbol.Type>) properties.get(key)).get());
            } else if ("THUMBNAIL_IMAGE_NAME".equals(key)) {
                CONTROL.setThumbnailImageName(((StringProperty) properties.get(key)).get());
            }
        }

        return CONTROL;
    }
}

