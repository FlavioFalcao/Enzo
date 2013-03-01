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

package eu.hansolo.enzo.clock.skin;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import eu.hansolo.enzo.clock.Clock;
import eu.hansolo.enzo.clock.behavior.ClockBehavior;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.effect.InnerShadowBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * User: hansolo
 * Date: 31.10.12
 * Time: 14:18
 */
public class ClockSkin extends BehaviorSkinBase<Clock, ClockBehavior> {
    private static final long   INTERVAL       = 20_000_000l;
    private static final int    PREFERRED_SIZE = 200;
    private static final double MINIMUM_SIZE   = 50;
    private static final double MAXIMUM_SIZE   = 1024;
    private Clock               control;
    private Pane                pane;
    private String              nightDayStyleClass;
    private Region              background;
    private Region              hourPointer;
    private Region              hourPointerFlour;
    private Region              minutePointer;
    private Region              minutePointerFlour;
    private Region              secondPointer;
    private Region              centerKnob;
    private Region              foreground;
    private double              size;
    private double              hourPointerWidthFactor;
    private double              hourPointerHeightFactor;
    private double              minutePointerWidthFactor;
    private double              minutePointerHeightFactor;
    private double              secondPointerWidthFactor;
    private double              secondPointerHeightFactor;
    private double              majorTickWidthFactor;
    private double              majorTickHeightFactor;
    private double              minorTickWidthFactor;
    private double              minorTickHeightFactor;
    private double              majorTickOffset;
    private double              minorTickOffset;
    private Rotate              hourAngle;
    private Rotate              minuteAngle;
    private Rotate              secondAngle;
    private List<Region>        ticks;
    private List<Text>          tickLabels;
    private Group               tickMarkGroup;
    private Group               tickLabelGroup;
    private Group               pointerGroup;
    private Group               secondPointerGroup;
    private Font                tickLabelFont;
    private DoubleProperty      currentMinuteAngle;
    private DoubleProperty      minute;
    private Timeline            timeline;
    private long                lastTimerCall;
    private AnimationTimer      timer;


    // ******************** Constructors **************************************
    public ClockSkin(final Clock CONTROL) {
        super(CONTROL, new ClockBehavior(CONTROL));
        control = CONTROL;
        pane    = new Pane();

        nightDayStyleClass        = control.isNightMode() ? "night-mode" : "day-mode";

        hourPointerWidthFactor    = 0.04;
        hourPointerHeightFactor   = 0.55;
        minutePointerWidthFactor  = 0.04;
        minutePointerHeightFactor = 0.4;
        secondPointerWidthFactor  = 0.075;
        secondPointerHeightFactor = 0.46;

        majorTickWidthFactor  = 0.04;
        majorTickHeightFactor = 0.12;
        minorTickWidthFactor  = 0.01;
        minorTickHeightFactor = 0.05;

        majorTickOffset = 0.018;
        minorTickOffset = 0.05;

        tickLabelFont      = Font.loadFont(getClass().getResourceAsStream("/resources/helvetica.ttf"), 12);

        minute             = new SimpleDoubleProperty(0);
        currentMinuteAngle = new SimpleDoubleProperty(0);

        hourAngle = new Rotate();
        hourAngle.angleProperty().bind(currentMinuteAngle);
        minuteAngle = new Rotate();
        secondAngle = new Rotate();

        ticks      = new ArrayList<>(60);
        tickLabels = new ArrayList<>(12);

        timeline = new Timeline();
        timer = new AnimationTimer() {
            @Override public void handle(final long NOW) {
                if (NOW >= lastTimerCall + INTERVAL) {
                    // SecondsRight
                    if (control.isDiscreteSecond()) {
                        secondAngle.setAngle(Calendar.getInstance().get(Calendar.SECOND) * 6);
                    } else {
                        secondAngle.setAngle(Calendar.getInstance().get(Calendar.SECOND) * 6 + Calendar.getInstance().get(Calendar.MILLISECOND) * 0.006);
                    }
                    // Minutes
                    minute.set((Calendar.getInstance().get(Calendar.MINUTE)) * 6);
                    // Hours
                    minuteAngle.setAngle((Calendar.getInstance().get(Calendar.HOUR)) * 30 + 0.5 * Calendar.getInstance().get(Calendar.MINUTE));
                    lastTimerCall = NOW;
                }
            }
        };
        minute.addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                moveMinutePointer(newValue.doubleValue());
            }
        });

        init();
        initGraphics();
        registerListeners();
        timer.start();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (Double.compare(control.getPrefWidth(), 0.0) <= 0 || Double.compare(control.getPrefHeight(), 0.0) <= 0 ||
            Double.compare(control.getWidth(), 0.0) <= 0 || Double.compare(control.getHeight(), 0.0) <= 0) {
            control.setPrefSize(PREFERRED_SIZE, PREFERRED_SIZE);
        }

        if (Double.compare(control.getMinWidth(), 0.0) <= 0 || Double.compare(control.getMinHeight(), 0.0) <= 0) {
            control.setMinSize(MINIMUM_SIZE, MINIMUM_SIZE);
        }

        if (Double.compare(control.getMaxWidth(), 0.0) <= 0 || Double.compare(control.getMaxHeight(), 0.0) <= 0) {
            control.setMaxSize(MAXIMUM_SIZE, MAXIMUM_SIZE);
        }
    }

    private void initGraphics() {
        background = new Region();
        if (Clock.Design.IOS6 == control.getDesign()) {
            background.getStyleClass().setAll("background-ios6");
        }else if (Clock.Design.DB == control.getDesign()) {
            background.getStyleClass().setAll("background-db");
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            background.getStyleClass().setAll("background-braun");
        }

        String majorTickStyleClass;
        String minorTickStyleClass;
        if (Clock.Design.IOS6 == control.getDesign()) {
            majorTickStyleClass = "major-tick-ios6";
            minorTickStyleClass = "minor-tick-ios6";
        } else if (Clock.Design.DB == control.getDesign()) {
            majorTickStyleClass = "major-tick-db";
            minorTickStyleClass = "minor-tick-db";
        } else {
            majorTickStyleClass = "major-tick-braun";
            minorTickStyleClass = "minor-tick-braun";
        }

        int tickLabelCounter = 1;
        for (double angle = 0 ; angle < 360 ; angle += 6) {
            Region tick = new Region();
            if (angle % 30 == 0) {
                tick.getStyleClass().setAll(majorTickStyleClass);
                Text tickLabel = new Text(Integer.toString(tickLabelCounter));
                tickLabel.getStyleClass().setAll("tick-label-braun");
                tickLabels.add(tickLabel);
                tickLabelCounter++;
            } else {
                tick.getStyleClass().setAll(minorTickStyleClass);
            }
            ticks.add(tick);
        }

        tickMarkGroup = new Group();
        tickMarkGroup.setEffect(DropShadowBuilder.create()
                                             .color(Color.rgb(0, 0, 0, 0.65))
                                             .radius(1.5)
                                             .blurType(BlurType.GAUSSIAN)
                                             .offsetY(1)
                                             .build());
        tickMarkGroup.getChildren().setAll(ticks);

        tickLabelGroup = new Group();
        tickLabelGroup.setEffect(DropShadowBuilder.create()
                                                  .color(Color.rgb(0, 0, 0, 0.65))
                                                  .radius(1.5)
                                                  .blurType(BlurType.GAUSSIAN)
                                                  .offsetY(1)
                                                  .build());
        tickLabelGroup.getChildren().setAll(tickLabels);
        tickLabelGroup.setVisible(Clock.Design.BRAUN == control.getDesign());

        hourPointer = new Region();
        if (Clock.Design.IOS6 == control.getDesign()) {
            hourPointer.getStyleClass().setAll("hour-pointer-ios6");
        } else if (Clock.Design.DB == control.getDesign()) {
            hourPointer.getStyleClass().setAll("hour-pointer-db");
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            hourPointer.getStyleClass().setAll("hour-pointer-braun");
        }
        hourPointer.getTransforms().setAll(hourAngle);

        hourPointerFlour = new Region();
        hourPointerFlour.getStyleClass().setAll("hour-pointer-braun-flour");
        if (Clock.Design.BRAUN == control.getDesign()) {
            hourPointerFlour.setVisible(true);
        } else {
            hourPointerFlour.setVisible(false);
        }
        hourPointerFlour.getTransforms().setAll(hourAngle);

        minutePointer = new Region();
        if (Clock.Design.IOS6 == control.getDesign()) {
            minutePointer.getStyleClass().setAll("minute-pointer-ios6");
        } else if (Clock.Design.DB == control.getDesign()) {
            minutePointer.getStyleClass().setAll("minute-pointer-db");
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            minutePointer.getStyleClass().setAll("minute-pointer-braun");
        }
        minutePointer.getTransforms().setAll(minuteAngle);

        minutePointerFlour = new Region();
        minutePointerFlour.getStyleClass().setAll("minute-pointer-braun-flour");
        if (Clock.Design.BRAUN == control.getDesign()) {
            minutePointerFlour.setVisible(true);
        } else {
            minutePointerFlour.setVisible(false);
        }
        minutePointerFlour.getTransforms().setAll(minuteAngle);

        pointerGroup = new Group();
        pointerGroup.setEffect(DropShadowBuilder.create()
                                                .color(Color.rgb(0, 0, 0, 0.45))
                                                .radius(12)
                                                .blurType(BlurType.GAUSSIAN)
                                                .offsetY(6)
                                                .build());
        pointerGroup.getChildren().setAll(minutePointerFlour, minutePointer, hourPointerFlour, hourPointer);

        secondPointer = new Region();
        if (Clock.Design.IOS6 == control.getDesign()) {
            secondPointer.getStyleClass().setAll("second-pointer-ios6");
        } else if (Clock.Design.DB == control.getDesign()) {
            secondPointer.getStyleClass().setAll("second-pointer-db");
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            secondPointer.getStyleClass().setAll("second-pointer-braun");
        }
        secondPointer.getTransforms().setAll(secondAngle);

        secondPointerGroup = new Group();
        secondPointerGroup.setEffect(DropShadowBuilder.create()
                                                      .color(Color.rgb(0, 0, 0, 0.45))
                                                      .radius(12)
                                                      .blurType(BlurType.GAUSSIAN)
                                                      .offsetY(6)
                                                      .input(InnerShadowBuilder.create()
                                                                               .color(Color.rgb(255, 255, 255, 0.3))
                                                                               .radius(1)
                                                                               .blurType(BlurType.GAUSSIAN)
                                                                               .offsetY(1)
                                                                               .input(InnerShadowBuilder.create()
                                                                                                        .color(Color.rgb(0, 0, 0, 0.3))
                                                                                                        .radius(1)
                                                                                                        .blurType(BlurType.GAUSSIAN)
                                                                                                        .offsetY(-1)
                                                                                                        .build())
                                                                               .build())
                                                      .build());
        secondPointerGroup.getChildren().setAll(secondPointer);
        secondPointerGroup.setVisible(control.isSecondPointerVisible());

        centerKnob = new Region();
        if (Clock.Design.IOS6 == control.getDesign()) {
            centerKnob.getStyleClass().setAll("center-knob-ios6");
        } else if (Clock.Design.DB == control.getDesign()) {
            centerKnob.getStyleClass().setAll("center-knob-db");
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            centerKnob.getStyleClass().setAll("center-knob-braun");
        }

        foreground = new Region();
        if (Clock.Design.IOS6 == control.getDesign()) {
            foreground.getStyleClass().setAll("foreground-ios6");
        } else if (Clock.Design.DB == control.getDesign()) {
            foreground.getStyleClass().setAll("foreground-db");
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            foreground.getStyleClass().setAll("foreground-braun");
        }

        pane.getChildren().setAll(background, tickMarkGroup, tickLabelGroup, pointerGroup, secondPointerGroup, centerKnob, foreground);

        getChildren().setAll(pane);

        updateDesign();
    }

    private void registerListeners() {
        registerChangeListener(control.widthProperty(), "RESIZE");
        registerChangeListener(control.heightProperty(), "RESIZE");
        registerChangeListener(control.secondPointerVisibleProperty(), "SECOND_POINTER_VISIBLE");
        registerChangeListener(control.nightModeProperty(), "DESIGN");
        registerChangeListener(control.designProperty(), "DESIGN");
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("RESIZE".equals(PROPERTY)) {
            resize();
        } else if ("DESIGN".equals(PROPERTY)) {
            updateDesign();
        } else if ("SECOND_POINTER_VISIBLE".equals(PROPERTY)) {
            secondPointerGroup.setVisible(control.isSecondPointerVisible());
        }
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_HEIGHT) {
        double prefHeight = PREFERRED_SIZE;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(control.getMinHeight(), PREF_HEIGHT - control.getInsets().getTop() - control.getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    @Override protected double computePrefHeight(final double PREF_WIDTH) {
        double prefWidth = PREFERRED_SIZE;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(control.getMinWidth(), PREF_WIDTH -control. getInsets().getRight() - control.getInsets().getLeft());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computeMinWidth(final double MIN_HEIGHT) {
        return super.computeMinWidth(Math.max(20, MIN_HEIGHT - control.getInsets().getTop() - control.getInsets().getBottom()));
    }

    @Override protected double computeMinHeight(final double MIN_WIDTH) {
        return super.computeMinHeight(Math.max(20, MIN_WIDTH - control.getInsets().getRight() - control.getInsets().getLeft()));
    }

    @Override protected double computeMaxWidth(final double MAX_HEIGHT) {
        return super.computeMaxWidth(Math.min(1024, MAX_HEIGHT - control.getInsets().getTop() - control.getInsets().getBottom()));
    }

    @Override protected double computeMaxHeight(final double MAX_WIDTH) {
        return super.computeMaxHeight(Math.min(1024, MAX_WIDTH - control.getInsets().getRight() - control.getInsets().getLeft()));
    }

    private void updateDesign() {
        // Set day or night mode
        nightDayStyleClass = control.isNightMode() ? "night-mode" : "day-mode";
        // Set Styles for each component
        if (Clock.Design.IOS6 == control.getDesign()) {
            background.getStyleClass().setAll(nightDayStyleClass, "background-ios6");
            int index = 0;
            for (double angle = 0 ; angle < 360 ; angle += 6) {
                Region tick = ticks.get(index);
                if (angle % 30 == 0) {
                    tick.getStyleClass().setAll(nightDayStyleClass, "major-tick-ios6");
                } else {
                    tick.getStyleClass().setAll(nightDayStyleClass, "minor-tick-ios6");
                }
                ticks.add(tick);
                index++;
            }
            hourPointer.getStyleClass().setAll(nightDayStyleClass, "hour-pointer-ios6");
            minutePointer.getStyleClass().setAll(nightDayStyleClass, "minute-pointer-ios6");
            secondPointer.getStyleClass().setAll(nightDayStyleClass, "second-pointer-ios6");
            centerKnob.getStyleClass().setAll(nightDayStyleClass, "center-knob-ios6");
            foreground.getStyleClass().setAll(nightDayStyleClass, "foreground-ios6");
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            nightDayStyleClass = control.isNightMode() ? "night-mode-braun" : "day-mode-braun";
            background.getStyleClass().setAll(nightDayStyleClass, "background-braun");
            int index = 0;
            for (double angle = 0 ; angle < 360 ; angle += 6) {
                if (angle % 30 == 0) {
                    ticks.get(index).getStyleClass().setAll(nightDayStyleClass, "major-tick-braun");
                } else {
                    ticks.get(index).getStyleClass().setAll(nightDayStyleClass, "minor-tick-braun");
                }
                index++;
            }
            System.out.println(ticks.size());
            index = 0;
            for (index = 0 ; index < 12 ; index++) {
                tickLabels.get(index).getStyleClass().setAll(nightDayStyleClass, "tick-label-braun");
            }
            hourPointer.getStyleClass().setAll(nightDayStyleClass, "hour-pointer-braun");
            minutePointer.getStyleClass().setAll(nightDayStyleClass, "minute-pointer-braun");
            secondPointer.getStyleClass().setAll(nightDayStyleClass, "second-pointer-braun");
            centerKnob.getStyleClass().setAll(nightDayStyleClass, "center-knob-braun");
            foreground.getStyleClass().setAll(nightDayStyleClass, "foreground-braun");
        } else {
            background.getStyleClass().setAll(nightDayStyleClass, "background-db");
            int index = 0;
            for (double angle = 0 ; angle < 360 ; angle += 6) {
                Region tick = ticks.get(index);
                if (angle % 30 == 0) {
                    tick.getStyleClass().setAll(nightDayStyleClass, "major-tick-db");
                } else {
                    tick.getStyleClass().setAll(nightDayStyleClass, "minor-tick-db");
                }
                ticks.add(tick);
                index++;
            }
            hourPointer.getStyleClass().setAll(nightDayStyleClass, "hour-pointer-db");
            minutePointer.getStyleClass().setAll(nightDayStyleClass, "minute-pointer-db");
            secondPointer.getStyleClass().setAll(nightDayStyleClass, "second-pointer-db");
            centerKnob.getStyleClass().setAll(nightDayStyleClass, "center-knob-db");
            foreground.getStyleClass().setAll(nightDayStyleClass, "foreground-db");
        }
        tickLabelGroup.setVisible(Clock.Design.BRAUN == control.getDesign());
        resize();
    }

    private void resize() {
        size = control.getWidth() < control.getHeight() ? control.getWidth() : control.getHeight();

        background.setPrefSize(size, size);

        // TODO: hourPointer and minutePointer have to be vice versa...wrong scaling here !!!

        if (Clock.Design.IOS6 == control.getDesign()) {
            hourPointerWidthFactor    = 0.04;
            hourPointerHeightFactor   = 0.55;
            minutePointerWidthFactor  = 0.04;
            minutePointerHeightFactor = 0.4;
            secondPointerWidthFactor  = 0.075;
            secondPointerHeightFactor = 0.46;
            majorTickWidthFactor      = 0.04;
            majorTickHeightFactor     = 0.12;
            minorTickWidthFactor      = 0.01;
            minorTickHeightFactor     = 0.05;
            majorTickOffset           = 0.018;
            minorTickOffset           = 0.05;
            hourAngle.setPivotX(size * 0.5 * hourPointerWidthFactor);
            hourAngle.setPivotY(size * 0.76 * hourPointerHeightFactor);
            minuteAngle.setPivotX(size * 0.5 * minutePointerWidthFactor);
            minuteAngle.setPivotY(size * 0.66 * minutePointerHeightFactor);
            secondAngle.setPivotX(size * 0.5 * secondPointerWidthFactor);
            secondAngle.setPivotY(size * 0.7341040462 * secondPointerHeightFactor);
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            hourPointerWidthFactor    = 0.105;
            hourPointerHeightFactor   = 0.485;
            minutePointerWidthFactor  = 0.105;
            minutePointerHeightFactor = 0.4;
            secondPointerWidthFactor  = 0.09;
            secondPointerHeightFactor = 0.53;
            majorTickWidthFactor      = 0.015;
            majorTickHeightFactor     = 0.045;
            minorTickWidthFactor      = 0.0075;
            minorTickHeightFactor     = 0.0225;
            majorTickOffset           = 0.012;
            minorTickOffset           = 0.02;
            hourAngle.setPivotX(size * 0.5 * hourPointerWidthFactor);
            hourAngle.setPivotY(size * 0.895 * hourPointerHeightFactor);
            minuteAngle.setPivotX(size * 0.5 * minutePointerWidthFactor);
            minuteAngle.setPivotY(size * 0.87 * minutePointerHeightFactor);
            secondAngle.setPivotX(size * 0.5 * secondPointerWidthFactor);
            secondAngle.setPivotY(size * 0.8125 * secondPointerHeightFactor);
        } else {
            hourPointerWidthFactor    = 0.04;
            hourPointerHeightFactor   = 0.47;
            minutePointerWidthFactor  = 0.055;
            minutePointerHeightFactor = 0.33;
            secondPointerWidthFactor  = 0.1;
            secondPointerHeightFactor = 0.455;
            majorTickWidthFactor      = 0.04;
            majorTickHeightFactor     = 0.12;
            minorTickWidthFactor      = 0.025;
            minorTickHeightFactor     = 0.04;
            majorTickOffset           = 0.018;
            minorTickOffset           = 0.06;
            hourAngle.setPivotX(size * 0.5 * hourPointerWidthFactor);
            hourAngle.setPivotY(size * hourPointerHeightFactor);
            minuteAngle.setPivotX(size * 0.5 * minutePointerWidthFactor);
            minuteAngle.setPivotY(size * minutePointerHeightFactor);
            secondAngle.setPivotX(size * 0.5 * secondPointerWidthFactor);
            secondAngle.setPivotY(size * secondPointerHeightFactor);
        }

        double radius = 0.4;
        double sinValue;
        double cosValue;
        int index = 0;
        for (double angle = 0 ; angle < 360 ; angle += 6) {
            sinValue = Math.sin(Math.toRadians(angle));
            cosValue = Math.cos(Math.toRadians(angle));
            Region tick = ticks.get(index);
            if (angle % 30 == 0) {
                tick.setPrefWidth(size * majorTickWidthFactor);
                tick.setPrefHeight(size * majorTickHeightFactor);
                tick.setTranslateX(size * 0.5 + ((size * (radius + majorTickOffset) * sinValue) - (size * (majorTickWidthFactor) * 0.5)));
                tick.setTranslateY(size * 0.5 + ((size * (radius + majorTickOffset) * cosValue) - (size * (majorTickHeightFactor) * 0.5)));
            } else {
                tick.setPrefWidth(size * minorTickWidthFactor);
                tick.setPrefHeight(size * minorTickHeightFactor);
                tick.setTranslateX(size * 0.5 + ((size * (radius + minorTickOffset) * sinValue) - (size * (minorTickWidthFactor) * 0.5)));
                tick.setTranslateY(size * 0.5 + ((size * (radius + minorTickOffset) * cosValue) - (size * (minorTickHeightFactor) * 0.5)));
            }
            tick.setRotate(-angle);
            index++;
        }

        if (Clock.Design.BRAUN == control.getDesign()) {
            int tickLabelCounter = 0;
            tickLabelFont = Font.loadFont(getClass().getResourceAsStream("/resources/helvetica.ttf"), (0.075 * size));
            for (double angle = 0 ; angle < 360 ; angle += 30.0) {
                double x = 0.31 * size * Math.sin(Math.toRadians(150 - angle));
                double y = 0.31 * size * Math.cos(Math.toRadians(150 - angle));
                tickLabels.get(tickLabelCounter).setFont(tickLabelFont);
                tickLabels.get(tickLabelCounter).setX(size * 0.5 + x - tickLabels.get(tickLabelCounter).getLayoutBounds().getWidth() * 0.5);
                tickLabels.get(tickLabelCounter).setY(size * 0.5 + y);
                tickLabels.get(tickLabelCounter).setTextOrigin(VPos.CENTER);
                tickLabels.get(tickLabelCounter).setTextAlignment(TextAlignment.CENTER);
                tickLabelCounter++;
            }
        }

        hourPointer.setPrefSize(size * hourPointerWidthFactor, size * hourPointerHeightFactor);
        if (Clock.Design.IOS6 == control.getDesign()) {
            hourPointer.setTranslateX(size * 0.5 - (hourPointer.getPrefWidth() * 0.5));
            hourPointer.setTranslateY(size * 0.5 - (hourPointer.getPrefHeight()) + (hourPointer.getPrefHeight() * 0.24));
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            hourPointer.setTranslateX(size * 0.5 - (hourPointer.getPrefWidth() * 0.5));
            hourPointer.setTranslateY(size * 0.5 - (hourPointer.getPrefHeight()) + (hourPointer.getPrefHeight() * 0.108));
            hourPointerFlour.setPrefSize(size * hourPointerWidthFactor, size * hourPointerHeightFactor);
            hourPointerFlour.setTranslateX(size * 0.5 - (hourPointer.getPrefWidth() * 0.5));
            hourPointerFlour.setTranslateY(size * 0.5 - (hourPointer.getPrefHeight()) + (hourPointer.getPrefHeight() * 0.108));
        } else {
            hourPointer.setTranslateX(size * 0.5 - (hourPointer.getPrefWidth() * 0.5));
            hourPointer.setTranslateY(size * 0.5 - hourPointer.getPrefHeight());
        }

        minutePointer.setPrefSize(size * minutePointerWidthFactor, size * minutePointerHeightFactor);
        if (Clock.Design.IOS6 == control.getDesign()) {
            minutePointer.setTranslateX(size * 0.5 - (minutePointer.getPrefWidth() * 0.5));
            minutePointer.setTranslateY(size * 0.5 - (minutePointer.getPrefHeight()) + (minutePointer.getPrefHeight() * 0.34));
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            minutePointer.setTranslateX(size * 0.5 - (minutePointer.getPrefWidth() * 0.5));
            minutePointer.setTranslateY(size * 0.5 - (minutePointer.getPrefHeight()) + (minutePointer.getPrefHeight() * 0.128));
            minutePointerFlour.setPrefSize(size * minutePointerWidthFactor, size * minutePointerHeightFactor);
            minutePointerFlour.setTranslateX(size * 0.5 - (minutePointer.getPrefWidth() * 0.5));
            minutePointerFlour.setTranslateY(size * 0.5 - (minutePointer.getPrefHeight()) + (minutePointer.getPrefHeight() * 0.128));
        } else {
            minutePointer.setTranslateX(size * 0.5 - (minutePointer.getPrefWidth() * 0.5));
            minutePointer.setTranslateY(size * 0.5 - minutePointer.getPrefHeight());
        }

        secondPointer.setPrefSize(size * secondPointerWidthFactor, size * secondPointerHeightFactor);
        if (Clock.Design.IOS6 == control.getDesign()) {
            secondPointer.setTranslateX(size * 0.5 - (secondPointer.getPrefWidth() * 0.5));
            secondPointer.setTranslateY(size * 0.5 - (secondPointer.getPrefHeight()) + (secondPointer.getPrefHeight() * 0.2658959538));
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            secondPointer.setTranslateX(size * 0.5 - (secondPointer.getPrefWidth() * 0.5));
            secondPointer.setTranslateY(size * 0.5 - secondPointer.getPrefHeight() + (secondPointer.getPrefHeight() * 0.189));
        } else {
            secondPointer.setTranslateX(size * 0.5 - (secondPointer.getPrefWidth() * 0.5));
            secondPointer.setTranslateY(size * 0.5 - secondPointer.getPrefHeight());
        }

        if (Clock.Design.IOS6 == control.getDesign()) {
            centerKnob.setPrefSize(size * 0.015, size * 0.015);
        } else if (Clock.Design.BRAUN == control.getDesign()) {
            centerKnob.setPrefSize(size * 0.085, size * 0.085);
        } else {
            centerKnob.setPrefSize(size * 0.1, size * 0.1);
        }
        centerKnob.setTranslateX(size * 0.5 - (centerKnob.getPrefWidth() * 0.5));
        centerKnob.setTranslateY(size * 0.5 - (centerKnob.getPrefHeight() * 0.5));

        foreground.setPrefSize(size * 0.955, size * 0.495);
        foreground.setTranslateX(size * 0.5 - (foreground.getPrefWidth() * 0.5));
        foreground.setTranslateY(size * 0.01);
    }


    // ******************** Drawing related ***********************************
    private void moveMinutePointer(double newAngle) {
        final KeyValue kv = new KeyValue(currentMinuteAngle, newAngle, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
        final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
        timeline  = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }
}
