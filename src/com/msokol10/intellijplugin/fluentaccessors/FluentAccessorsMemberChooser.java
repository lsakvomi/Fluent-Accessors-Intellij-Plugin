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
                               new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
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

    boolean generateGetters () {
        return this.advanceOptionPanel.generateGetters();
    }

    boolean useSetters () {
        return this.advanceOptionPanel.useSetters();
    }


    protected class AdvanceOptionPanel extends JPanel {
        private final JTextField setterPrefix;
        private final JTextField getterPrefix;
        private final JCheckBox generateGetters;
        private final JCheckBox invokeExistingSetters;

        String getSetterPrefix () {
            return setterPrefix.getText().replaceAll("\\s", "");
        }

        String getGetterPrefix () {
            return getterPrefix.getText().replaceAll("\\s", "");
        }

        boolean generateGetters () {
            return this.generateGetters.isSelected();
        }

        boolean useSetters () {
            return this.invokeExistingSetters.isSelected();
        }

        AdvanceOptionPanel () {
            super(new GridBagLayout());

            FluentAccessorsApplicationComponent applicationComponent
                    = ApplicationManager.getApplication()
                    .getComponent(FluentAccessorsApplicationComponent.class);

            setterPrefix = new JTextField();
            getterPrefix = new JTextField();

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

            generateGetters = new JCheckBox("generate getters");
            generateGetters.setMnemonic(KeyEvent.VK_G);
            generateGetters.setSelected(applicationComponent.isGeneratingGetters());

            invokeExistingSetters = new JCheckBox("invoke existing setters");
            invokeExistingSetters.setMnemonic(KeyEvent.VK_S);
            invokeExistingSetters.setSelected(applicationComponent.isInvokeExistingSetters());

            final Insets insets = JBUI.insetsTop(5);
            add(generateGetters, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.1, 0,
                                                        GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
            add(setterLabel, new GridBagConstraints(1, 0, 1, 1, 0.1, 0, GridBagConstraints.WEST,
                                                    GridBagConstraints.NONE, insets, 0, 0));
            add(setterPrefix, new GridBagConstraints(2, 0, 1, 1, 0.8, 0, GridBagConstraints.WEST,
                                                     GridBagConstraints.HORIZONTAL, insets, 20, 0));
            add(invokeExistingSetters, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.1, 0,
                                                              GridBagConstraints.WEST, GridBagConstraints.NONE, insets, 0, 0));
            add(getterLabel, new GridBagConstraints(1, 1, 1, 1, 0.1, 0, GridBagConstraints.WEST,
                                                    GridBagConstraints.NONE, insets, 0, 0));
            add(getterPrefix, new GridBagConstraints(2, 1, 1, 1, 0.8, 0, GridBagConstraints.WEST,
                                                     GridBagConstraints.HORIZONTAL, insets, 20, 0));
        }
    }
}
