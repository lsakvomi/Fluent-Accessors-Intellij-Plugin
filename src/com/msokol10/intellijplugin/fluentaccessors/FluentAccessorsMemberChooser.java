package com.msokol10.intellijplugin.fluentaccessors;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

public class FluentAccessorsMemberChooser extends MemberChooser<PsiFieldMember> {

    private AdvanceOptionPanel advanceOptionPanel;


    FluentAccessorsMemberChooser (PsiFieldMember[] elements,
                                  @org.jetbrains.annotations.NotNull
                                          Project project) {
        super(elements, false, true, project);
        setCopyJavadocVisible(false);
    }


    @Override
    protected JComponent createSouthPanel() {
        advanceOptionPanel = new AdvanceOptionPanel();
        advanceOptionPanel.add(super.createSouthPanel(),
                               new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                                                      JBUI.insetsTop(10), 0, 0));
        setOKActionEnabled(true);
        return advanceOptionPanel;
    }

    String getSetterPrefix () {
        return this.advanceOptionPanel.getSetterPrefix();
    }

    String getGetterPrefix () {
        return this.advanceOptionPanel.getGetterPrefix();
    }

    String getFluentPrefix () {
        return this.advanceOptionPanel.getFluentPrefix();
    }

    boolean generateSetters () {
        return this.advanceOptionPanel.generateSetters();
    }

    boolean generateGetters () {
        return this.advanceOptionPanel.generateGetters();
    }

    boolean generateFluent () {
        return this.advanceOptionPanel.generateFluent();
    }


    protected class AdvanceOptionPanel extends JPanel {
        private final JTextField setterPrefix;
        private final JTextField getterPrefix;
        private final JTextField fluentPrefix;
        private final JCheckBox generateSetters;
        private final JCheckBox generateGetters;
        private final JCheckBox generateFluent;

        String getSetterPrefix () {
            return setterPrefix.getText().replaceAll("\\s", "");
        }

        String getGetterPrefix () {
            return getterPrefix.getText().replaceAll("\\s", "");
        }

        String getFluentPrefix () {
            return fluentPrefix.getText().replaceAll("\\s", "");
        }

        boolean generateSetters () {
            return this.generateSetters.isSelected();
        }
        boolean generateGetters () {
            return this.generateGetters.isSelected();
        }
        boolean generateFluent () {
            return this.generateFluent.isSelected();
        }

        AdvanceOptionPanel () {
            super(new GridBagLayout());

            FluentAccessorsApplicationComponent applicationComponent
                    = ApplicationManager.getApplication()
                    .getComponent(FluentAccessorsApplicationComponent.class);

            setterPrefix = new JTextField();
            getterPrefix = new JTextField();
            fluentPrefix = new JTextField();

            JLabel setterLabel = new JLabel("setters prefix:");
            setterLabel.setDisplayedMnemonic(KeyEvent.VK_P);
            setterLabel.setLabelFor(setterPrefix);
            setterPrefix.setText(applicationComponent.getSetterPrefix());
            setterPrefix.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(final FocusEvent e) {
                    setterPrefix.selectAll();
                }

                @Override
                public void focusLost(final FocusEvent e) {
                }
            });

            JLabel getterLabel = new JLabel("getters prefix:");
            getterLabel.setDisplayedMnemonic(KeyEvent.VK_P);
            getterLabel.setLabelFor(getterPrefix);
            getterPrefix.setText(applicationComponent.getGetterPrefix());
            getterPrefix.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(final FocusEvent e) {
                    getterPrefix.selectAll();
                }

                @Override
                public void focusLost(final FocusEvent e) {
                }
            });

            JLabel fluentLabel = new JLabel("fluent prefix:");
            fluentLabel.setDisplayedMnemonic(KeyEvent.VK_P);
            fluentLabel.setLabelFor(getterPrefix);
            fluentPrefix.setText(applicationComponent.getFluentPrefix());
            fluentPrefix.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(final FocusEvent e) {
                    fluentPrefix.selectAll();
                }

                @Override
                public void focusLost(final FocusEvent e) {
                }
            });

            generateSetters = new JCheckBox("generate setters");
            generateSetters.setMnemonic(KeyEvent.VK_G);
            generateSetters.setSelected(applicationComponent.isGeneratingSetters());

            generateGetters = new JCheckBox("generate getters");
            generateGetters.setMnemonic(KeyEvent.VK_G);
            generateGetters.setSelected(applicationComponent.isGeneratingGetters());

            generateFluent = new JCheckBox("generate fluent");
            generateFluent.setMnemonic(KeyEvent.VK_G);
            generateFluent.setSelected(applicationComponent.isGeneratingFluent());

            final Insets insets = JBUI.insetsTop(5);

            add(generateSetters, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.1, 0,
                                                        GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
            add(setterLabel, new GridBagConstraints(1, 0, 1, 1, 0.1, 0,
                                                    GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
            add(setterPrefix, new GridBagConstraints(2, 0, 1, 1, 0.8, 0,
                                                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 20, 0));

            add(generateGetters, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.1, 0,
                                                        GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
            add(getterLabel, new GridBagConstraints(1, 1, 1, 1, 0.1, 0,
                                                    GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
            add(getterPrefix, new GridBagConstraints(2, 1, 1, 1, 0.8, 0,
                                                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 20, 0));

            add(generateFluent, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.1, 0,
                                                       GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
            add(fluentLabel, new GridBagConstraints(1, 2, 1, 1, 0.1, 0,
                                                    GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
            add(fluentPrefix, new GridBagConstraints(2, 2, 1, 1, 0.8, 0,
                                                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, insets, 20, 0));
        }
    }
}
