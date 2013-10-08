/*
 * Copyright (c) 2013 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.enzo.onoffswitch.skin;

import eu.hansolo.enzo.common.Util;
import eu.hansolo.enzo.onoffswitch.OnOffSwitch;
import javafx.animation.TranslateTransition;
import javafx.geometry.VPos;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;


/**
 * User: hansolo
 * Date: 08.10.13
 * Time: 07:50
 */
public class OnOffSwitchSkin extends SkinBase<OnOffSwitch> implements Skin<OnOffSwitch> {
    private static final double PREFERRED_WIDTH  = 64;
    private static final double PREFERRED_HEIGHT = 32;
    private static final double MINIMUM_WIDTH    = 16;
    private static final double MINIMUM_HEIGHT   = 8;
    private static final double MAXIMUM_WIDTH    = 1024;
    private static final double MAXIMUM_HEIGHT   = 1024;
    private double              width;
    private double              height;
    private Pane                pane;
    private double              aspectRatio;
    private Region              background;
    private Region              thumb;
    private Text                on;
    private Text                off;
    private Font                font;
    private TranslateTransition moveToSwitchedOff;
    private TranslateTransition moveToSwitchedOn;


    // ******************** Constructors **************************************
    public OnOffSwitchSkin(final OnOffSwitch CONTROL) {
        super(CONTROL);
        aspectRatio = PREFERRED_HEIGHT / PREFERRED_WIDTH;
        init();
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (Double.compare(getSkinnable().getPrefWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getPrefHeight(), 0.0) <= 0 ||
            Double.compare(getSkinnable().getWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getHeight(), 0.0) <= 0) {
            if (getSkinnable().getPrefWidth() > 0 && getSkinnable().getPrefHeight() > 0) {
                getSkinnable().setPrefSize(getSkinnable().getPrefWidth(), getSkinnable().getPrefHeight());
            } else {
                getSkinnable().setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        if (Double.compare(getSkinnable().getMinWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getMinHeight(), 0.0) <= 0) {
            getSkinnable().setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(getSkinnable().getMaxWidth(), 0.0) <= 0 || Double.compare(getSkinnable().getMaxHeight(), 0.0) <= 0) {
            getSkinnable().setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }

        if (getSkinnable().getPrefWidth() != PREFERRED_WIDTH || getSkinnable().getPrefHeight() != PREFERRED_HEIGHT) {
            aspectRatio = getSkinnable().getPrefHeight() / getSkinnable().getPrefWidth();
        }
    }

    private void initGraphics() {
        Font.loadFont(getClass().getResourceAsStream("/eu/hansolo/enzo/fonts/opensans-semibold.ttf"), (0.5 * PREFERRED_HEIGHT)); // "OpenSans"
        font = Font.font("Open Sans", 0.5 * PREFERRED_HEIGHT);

        background = new Region();
        background.getStyleClass().setAll("background");

        on  = new Text("1");
        on.setFont(font);
        on.getStyleClass().setAll("on");

        off = new Text("0");
        off.setFont(font);
        off.getStyleClass().setAll("off");

        thumb = new Region();
        thumb.getStyleClass().setAll("thumb");

        pane = new Pane(background, on, off, thumb);
        pane.getStyleClass().setAll("on-off-switch");

        moveToSwitchedOff = new TranslateTransition(Duration.millis(180), thumb);
        moveToSwitchedOn = new TranslateTransition(Duration.millis(180), thumb);

        // Add all nodes
        getChildren().setAll(pane);
    }

    private void registerListeners() {
        getSkinnable().widthProperty().addListener(observable -> handleControlPropertyChanged("RESIZE") );
        getSkinnable().heightProperty().addListener(observable -> handleControlPropertyChanged("RESIZE") );
        getSkinnable().switchColorProperty().addListener(observable -> handleControlPropertyChanged("SWITCH_COLOR") );
        getSkinnable().textColorOnProperty().addListener(observable -> handleControlPropertyChanged("TEXT_ON_COLOR"));
        getSkinnable().textColorOffProperty().addListener(observable -> handleControlPropertyChanged("TEXT_OFF_COLOR"));
        getSkinnable().thumbColorProperty().addListener(observable -> handleControlPropertyChanged("THUMB_COLOR"));
        getSkinnable().onProperty().addListener(observable -> handleControlPropertyChanged("ON"));
        pane.setOnMouseClicked(mouseEvent -> getSkinnable().setOn(!getSkinnable().isOn()));
    }


    // ******************** Methods *******************************************
    protected void handleControlPropertyChanged(final String PROPERTY) {
        if ("RESIZE".equals(PROPERTY)) {
            resize();
        } else if ("SWITCH_COLOR".equals(PROPERTY)) {
            background.setStyle("-switch-color: " + Util.colorToCss((Color) getSkinnable().getSwitchColor()) + ";");
        } else if ("TEXT_ON_COLOR".equals(PROPERTY)) {
            on.setStyle("-text-color-on: " + Util.colorToCss((Color) getSkinnable().getTextColorOn()) + ";");
        } else if ("TEXT_OFF_COLOR".equals(PROPERTY)) {
            off.setStyle("-text-color-off: " + Util.colorToCss((Color) getSkinnable().getTextColorOff()) + ";");
        } else if ("THUMB_COLOR".equals(PROPERTY)) {
            thumb.setStyle("-thumb-color: " + Util.colorToCss((Color) getSkinnable().getThumbColor()) + ";");
        } else if ("ON".equals(PROPERTY)) {
            if (getSkinnable().isOn()) {
                moveToSwitchedOn.play();
            } else {
                moveToSwitchedOff.play();
            }
        }
    }

    @Override protected double computeMinWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMinWidth(Math.max(MINIMUM_HEIGHT, HEIGHT - TOP_INSET - BOTTOM_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computeMinHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMinHeight(Math.max(MINIMUM_WIDTH, WIDTH - LEFT_INSET - RIGHT_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }

    @Override protected double computeMaxWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMaxWidth(Math.min(MAXIMUM_HEIGHT, HEIGHT - TOP_INSET - BOTTOM_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computeMaxHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        return super.computeMaxHeight(Math.min(MAXIMUM_WIDTH, WIDTH - LEFT_INSET - RIGHT_INSET), TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }

    @Override protected double computePrefWidth(final double HEIGHT, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        double prefHeight = PREFERRED_HEIGHT;
        if (HEIGHT != -1) {
            prefHeight = Math.max(0, HEIGHT - TOP_INSET - BOTTOM_INSET);
        }
        return super.computePrefWidth(prefHeight, TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }
    @Override protected double computePrefHeight(final double WIDTH, double TOP_INSET, double RIGHT_INSET, double BOTTOM_INSET, double LEFT_INSET) {
        double prefWidth = PREFERRED_WIDTH;
        if (WIDTH != -1) {
            prefWidth = Math.max(0, WIDTH - LEFT_INSET - RIGHT_INSET);
        }
        return super.computePrefHeight(prefWidth, TOP_INSET, RIGHT_INSET, BOTTOM_INSET, LEFT_INSET);
    }


    // ******************** Private Methods ***********************************
    private void resize() {
        width  = getSkinnable().getWidth();
        height = getSkinnable().getHeight();

        if (width > 0 && height > 0) {
            if (aspectRatio * width > height) {
                width = 1 / (aspectRatio / height);
            } else if (1 / (aspectRatio / height) > width) {
                height = aspectRatio * width;
            }

            font = Font.font("Open Sans", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 0.5 * height);

            background.setPrefSize(width, height);

            on.setFont(font);
            on.setTextOrigin(VPos.CENTER);
            on.relocate(height * 0.3125, (height - on.getLayoutBounds().getHeight()) * 0.5);

            off.setFont(font);
            off.setTextOrigin(VPos.CENTER);
            off.relocate(width - height * 0.3125 - off.getLayoutBounds().getWidth(), (height - off.getLayoutBounds().getHeight()) * 0.5);

            thumb.setPrefSize((height * 0.75), (height * 0.75));
            thumb.relocate(getSkinnable().isOn() ? width - height * 0.875 - thumb.getTranslateX() : height * 0.125 - thumb.getTranslateX(), height * 0.125);

            //moveToSwitchedOff.setFromX(width * 0.5);
            moveToSwitchedOff.setFromX(width * 0.5);
            if (getSkinnable().isOn()) {
                moveToSwitchedOff.setToX(0 - thumb.getLayoutX() + height * 0.125);
            } else {
                moveToSwitchedOff.setToX(0);
            }

            moveToSwitchedOn.setFromX(0);
            moveToSwitchedOn.setToX(width * 0.5);
        }
    }
}
