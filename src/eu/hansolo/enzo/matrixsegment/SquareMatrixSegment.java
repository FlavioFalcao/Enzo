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

package eu.hansolo.enzo.matrixsegment;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SquareMatrixSegment extends Control {
    public static enum Dot {
        D01, D11, D21, D31, D41, D51, D61,
        D02, D12, D22, D32, D42, D52, D62,
        D03, D13, D23, D33, D43, D53, D63,
        D04, D14, D24, D34, D44, D54, D64,
        D05, D15, D25, D35, D45, D55, D65,
        D06, D16, D26, D36, D46, D56, D66,
        D07, D17, D27, D37, D47, D57, D67
    }
    private Color                                   defaultColor = Color.RED;
    private ObjectProperty<Color>                   color;
    private String                                  defaultCharacter = " ";
    private StringProperty                          character;
    private Map<Integer, List<Dot>>                 mapping;
    private ObjectProperty<Map<Integer, List<Dot>>> customDotMapping;
    private boolean                                 defaultBackgroundVisible = true;
    private BooleanProperty                         backgroundVisible;
    private boolean                                 defaultHighlightsVisible = true;
    private BooleanProperty                         highlightsVisible;
    private boolean                                 defaultGlowEnabled = true;
    private BooleanProperty                         glowEnabled;


    // ******************** Constructors **************************************
    public SquareMatrixSegment() {
        this(" ", Color.RED);
    }

    public SquareMatrixSegment(final String CHARACTER) {
        this(CHARACTER, Color.RED);
    }

    public SquareMatrixSegment(final Color COLOR) {
        this(" ", COLOR);
    }

    public SquareMatrixSegment(final String CHARACTER, final Color COLOR) {
        getStyleClass().add("square-matrix-segment");
        defaultColor      = COLOR;
        defaultCharacter  = CHARACTER;
        mapping           = new HashMap<>(72);

        initMapping();
    }


    // ******************** Initialization ************************************
    private void initMapping() {
        // Space
        mapping.put(20, Arrays.asList(new Dot[] {}));
        // * + , - . / : ; = \ _ < > #
        mapping.put(42, Arrays.asList(new Dot[]{Dot.D32, Dot.D13, Dot.D33, Dot.D53, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D35, Dot.D55, Dot.D36}));
        mapping.put(43, Arrays.asList(new Dot[]{Dot.D32, Dot.D33, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D54, Dot.D35, Dot.D36}));
        mapping.put(44, Arrays.asList(new Dot[]{Dot.D25, Dot.D35, Dot.D36, Dot.D27}));
        mapping.put(45, Arrays.asList(new Dot[]{Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D54}));
        mapping.put(46, Arrays.asList(new Dot[]{Dot.D35, Dot.D36, Dot.D45, Dot.D46}));
        mapping.put(47, Arrays.asList(new Dot[]{Dot.D52, Dot.D43, Dot.D34, Dot.D25, Dot.D16}));
        mapping.put(58, Arrays.asList(new Dot[]{Dot.D22, Dot.D32, Dot.D23, Dot.D33, Dot.D25, Dot.D35, Dot.D26, Dot.D36}));
        mapping.put(59, Arrays.asList(new Dot[]{Dot.D22, Dot.D32, Dot.D23, Dot.D33, Dot.D25, Dot.D35, Dot.D36, Dot.D27}));
        mapping.put(61, Arrays.asList(new Dot[]{Dot.D13, Dot.D23, Dot.D33, Dot.D43, Dot.D53, Dot.D15, Dot.D25, Dot.D35, Dot.D45, Dot.D55}));
        mapping.put(92, Arrays.asList(new Dot[]{Dot.D12, Dot.D23, Dot.D34, Dot.D45, Dot.D56}));
        mapping.put(95, Arrays.asList(new Dot[]{Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(60, Arrays.asList(new Dot[]{Dot.D41, Dot.D32, Dot.D23, Dot.D14, Dot.D25, Dot.D36, Dot.D47}));
        mapping.put(62, Arrays.asList(new Dot[]{Dot.D21, Dot.D32, Dot.D43, Dot.D54, Dot.D45, Dot.D36, Dot.D27}));
        mapping.put(35, Arrays.asList(new Dot[]{Dot.D21, Dot.D41, Dot.D22, Dot.D42, Dot.D13, Dot.D23, Dot.D33, Dot.D43, Dot.D53, Dot.D24, Dot.D44, Dot.D15, Dot.D25, Dot.D35, Dot.D45, Dot.D55, Dot.D26, Dot.D46, Dot.D27, Dot.D47}));
        mapping.put(34, Arrays.asList(new Dot[]{Dot.D21, Dot.D41, Dot.D22, Dot.D42, Dot.D23, Dot.D43}));
        // 0 - 9
        mapping.put(48, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D33, Dot.D53, Dot.D14, Dot.D34, Dot.D54, Dot.D15, Dot.D35, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(49, Arrays.asList(new Dot[]{Dot.D31, Dot.D22, Dot.D32, Dot.D33, Dot.D34, Dot.D35, Dot.D36, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(50, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D53, Dot.D44, Dot.D35, Dot.D26, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(51, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D53, Dot.D34, Dot.D44, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(52, Arrays.asList(new Dot[]{Dot.D14, Dot.D32, Dot.D42, Dot.D23, Dot.D41, Dot.D43, Dot.D44, Dot.D15, Dot.D25, Dot.D35, Dot.D45, Dot.D55, Dot.D46, Dot.D47}));
        mapping.put(53, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D23, Dot.D33, Dot.D43, Dot.D54, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(54, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(55, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D52, Dot.D43, Dot.D34, Dot.D35, Dot.D36, Dot.D37}));
        mapping.put(56, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(57, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D24, Dot.D34, Dot.D44, Dot.D54, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        // ? ! % $ [ ] ( ) { }
        mapping.put(63, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D53, Dot.D34, Dot.D44, Dot.D35, Dot.D37}));
        mapping.put(33, Arrays.asList(new Dot[]{Dot.D31, Dot.D32, Dot.D33, Dot.D34, Dot.D35, Dot.D37}));
        mapping.put(37, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D12, Dot.D22, Dot.D52, Dot.D43, Dot.D34, Dot.D25, Dot.D16, Dot.D46, Dot.D56, Dot.D47, Dot.D57}));
        mapping.put(36, Arrays.asList(new Dot[]{Dot.D31, Dot.D22, Dot.D32, Dot.D42, Dot.D52, Dot.D13, Dot.D33, Dot.D24, Dot.D34, Dot.D44, Dot.D35, Dot.D55, Dot.D16, Dot.D26, Dot.D36, Dot.D46, Dot.D37}));
        mapping.put(91, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D42, Dot.D43, Dot.D44, Dot.D45, Dot.D46, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(93, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D22, Dot.D23, Dot.D24, Dot.D25, Dot.D26, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(40, Arrays.asList(new Dot[]{Dot.D41, Dot.D32, Dot.D23, Dot.D24, Dot.D25, Dot.D36, Dot.D47}));
        mapping.put(41, Arrays.asList(new Dot[]{Dot.D21, Dot.D32, Dot.D43, Dot.D44, Dot.D45, Dot.D36, Dot.D27}));
        mapping.put(123, Arrays.asList(new Dot[]{Dot.D31, Dot.D41, Dot.D22, Dot.D23, Dot.D14, Dot.D25, Dot.D26, Dot.D37, Dot.D47}));
        mapping.put(125, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D42, Dot.D43, Dot.D54, Dot.D45, Dot.D46, Dot.D27, Dot.D37}));
        // A - Z
        mapping.put(65, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D25, Dot.D35, Dot.D45, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(66, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(67, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D14, Dot.D15, Dot.D16, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(68, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(69, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D16, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(70, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D16, Dot.D17}));
        mapping.put(71, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D12, Dot.D13, Dot.D14, Dot.D34, Dot.D44, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(72, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(73, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D32, Dot.D33, Dot.D34, Dot.D35, Dot.D36, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(74, Arrays.asList(new Dot[]{Dot.D51, Dot.D52, Dot.D53, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(75, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D42, Dot.D13, Dot.D33, Dot.D14, Dot.D24, Dot.D15, Dot.D35, Dot.D16, Dot.D46, Dot.D17, Dot.D57}));
        mapping.put(76, Arrays.asList(new Dot[]{Dot.D11, Dot.D12, Dot.D13, Dot.D14, Dot.D15, Dot.D16, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
        mapping.put(77, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D22, Dot.D42, Dot.D52, Dot.D13, Dot.D33, Dot.D53, Dot.D14, Dot.D34, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(78, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D23, Dot.D53, Dot.D14, Dot.D34, Dot.D54, Dot.D15, Dot.D45, Dot.D55, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(79, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(80, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D16, Dot.D17}));
        mapping.put(81, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D35, Dot.D55, Dot.D16, Dot.D46, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(82, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D24, Dot.D34, Dot.D44, Dot.D15, Dot.D35, Dot.D16, Dot.D46, Dot.D17, Dot.D57}));
        mapping.put(83, Arrays.asList(new Dot[]{Dot.D21, Dot.D31, Dot.D41, Dot.D12, Dot.D52, Dot.D13, Dot.D24, Dot.D34, Dot.D44, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(84, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D32, Dot.D33, Dot.D34, Dot.D35, Dot.D36, Dot.D37}));
        mapping.put(85, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D55, Dot.D16, Dot.D56, Dot.D27, Dot.D37, Dot.D47}));
        mapping.put(86, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D54, Dot.D15, Dot.D55, Dot.D26, Dot.D46, Dot.D37}));
        mapping.put(87, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D13, Dot.D53, Dot.D14, Dot.D34, Dot.D54, Dot.D15, Dot.D35, Dot.D55, Dot.D16, Dot.D26, Dot.D46, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(88, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D23, Dot.D43, Dot.D34, Dot.D25, Dot.D45, Dot.D16, Dot.D56, Dot.D17, Dot.D57}));
        mapping.put(89, Arrays.asList(new Dot[]{Dot.D11, Dot.D51, Dot.D12, Dot.D52, Dot.D23, Dot.D43, Dot.D34, Dot.D35, Dot.D36, Dot.D37}));
        mapping.put(90, Arrays.asList(new Dot[]{Dot.D11, Dot.D21, Dot.D31, Dot.D41, Dot.D51, Dot.D52, Dot.D43, Dot.D34, Dot.D25, Dot.D16, Dot.D17, Dot.D27, Dot.D37, Dot.D47, Dot.D57}));
    }


    // ******************** Methods *******************************************
    @Override public boolean isResizable() {
        return true;
    }

    public final Color getColor() {
        return null == color ? defaultColor : color.get();
    }
    public final void setColor(final Color COLOR) {
        if (null == color) {
            defaultColor = COLOR;
        } else {
            color.set(COLOR);
        }
    }
    public final ObjectProperty<Color> colorProperty() {
        if (null == color) {
            color = new SimpleObjectProperty<>(this, "color", defaultColor);
        }
        return color;
    }

    public final String getCharacter() {
        return null == character ? defaultCharacter : character.get();
    }
    public final void setCharacter(final String CHARACTER) {
        if (null == character) {
            defaultCharacter = CHARACTER;
        } else {
            character.set(CHARACTER);
        }
    }
    public final void setCharacter(final Character CHARACTER) {
        if (null == character) {
            defaultCharacter = String.valueOf(CHARACTER);
        } else {
            character.set(String.valueOf(CHARACTER));
        }
    }
    public final StringProperty characterProperty() {
        if (null == character) {
            character = new SimpleStringProperty(this, "character", defaultCharacter);
        }
        return character;
    }

    public final Map<Integer, List<Dot>> getCustomDotMapping() {
        if (customDotMapping == null) {
            customDotMapping = new SimpleObjectProperty<Map<Integer, List<Dot>>>(new HashMap<>());
        }
        return customDotMapping.get();
    }
    public final void setCustomDotMapping(final Map<Integer, List<Dot>> CUSTOM_DOT_MAPPING) {
        if (customDotMapping == null) {
            customDotMapping = new SimpleObjectProperty<Map<Integer, List<Dot>>>(new HashMap<>());
        }
        customDotMapping.get().clear();
        for (int key : CUSTOM_DOT_MAPPING.keySet()) {
            customDotMapping.get().put(key, CUSTOM_DOT_MAPPING.get(key));
        }
    }
    public final ObjectProperty<Map<Integer, List<Dot>>> customDotMappingProperty() {
        if (customDotMapping == null) {
            customDotMapping = new SimpleObjectProperty<Map<Integer, List<Dot>>>(new HashMap<>());
        }
        return customDotMapping;
    }

    /**
     * Returns a Map that contains the default mapping from ascii integers to lcd segments.
     * The segments are defined as follows:
     *
     *        D11 D21 D31 D41 D51
     *        D12 D22 D32 D42 D52
     *        D13 D23 D33 D43 D53
     *        D14 D24 D34 D44 D54
     *        D15 D25 D35 D45 D55
     *        D16 D26 D36 D46 D56
     *        D17 D27 D37 D47 D57
     *
     * If you would like to add a $ sign (ASCII: 36) for example you should add the following code to
     * your custom dot map.
     *
     * customDotMapping.put(36, Arrays.asList(new DotMatrixSegment.Dot[] {
     *     DotMatrixSegment.Dot.D11,
     *     DotMatrixSegment.Dot.A2,
     *     DotMatrixSegment.Dot.F,
     *     DotMatrixSegment.Dot.P,
     *     DotMatrixSegment.Dot.K,
     *     DotMatrixSegment.Dot.C,
     *     DotMatrixSegment.Dot.D2,
     *     DotMatrixSegment.Dot.D1,
     *     DotMatrixSegment.Dot.H,
     *     DotMatrixSegment.Dot.M
     * }));
     *
     * @return a Map that contains the default mapping from ascii integers to segments
     */
    public final Map<Integer, List<Dot>> getDotMapping() {
        HashMap<Integer, List<Dot>> dotMapping = new HashMap<Integer, List<Dot>>(42);
        for (int key : mapping.keySet()) {
            dotMapping.put(key, mapping.get(key));
        }
        return dotMapping;
    }

    public final boolean isBackgroundVisible() {
        return null == backgroundVisible ? defaultBackgroundVisible : backgroundVisible.get();
    }
    public final void setBackgroundVisible(final boolean BACKGROUND_VISIBLE) {
        if (null == backgroundVisible) {
            defaultBackgroundVisible = BACKGROUND_VISIBLE;
        } else {
            backgroundVisible.set(BACKGROUND_VISIBLE);
        }
    }
    public final BooleanProperty backgroundVisibleProperty() {
        if (null == backgroundVisible) {
            backgroundVisible = new SimpleBooleanProperty(this, "backgroundVisible", defaultBackgroundVisible);
        }
        return backgroundVisible;
    }

    public final boolean isHighlightsVisible() {
        return null == highlightsVisible ? defaultHighlightsVisible : highlightsVisible.get();
    }
    public final void setHighlightsVisible(final boolean HIGHLIGHTS_VISIBLE) {
        if (null == highlightsVisible) {
            defaultHighlightsVisible = HIGHLIGHTS_VISIBLE;
        } else {
            highlightsVisible.set(HIGHLIGHTS_VISIBLE);
        }
    }
    public final BooleanProperty highlightsVisibleProperty() {
        if (null == highlightsVisible) {
            highlightsVisible = new SimpleBooleanProperty(this, "highlightsVisible", defaultHighlightsVisible);
        }
        return highlightsVisible;
    }

    public final boolean isGlowEnabled() {
        return null == glowEnabled ? defaultGlowEnabled : glowEnabled.get();
    }
    public final void setGlowEnabled(final boolean GLOW_ENABLED) {
        if (null == glowEnabled) {
            defaultGlowEnabled = GLOW_ENABLED;
        } else {
            glowEnabled.set(GLOW_ENABLED);
        }
    }
    public final BooleanProperty glowEnabledProperty() {
        if (null == glowEnabled) {
            glowEnabled = new SimpleBooleanProperty(this, "glowEnabled", defaultGlowEnabled);
        }
        return glowEnabled;
    }
    

    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("squarematrixsegment.css").toExternalForm();
    }
}

