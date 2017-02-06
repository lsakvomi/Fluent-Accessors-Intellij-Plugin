package com.msokol10.intellijplugin.fluentaccessors;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import org.jetbrains.annotations.NotNull;

public class FluentAccessorsApplicationComponent implements ApplicationComponent,
        PersistentStateComponent<FluentAccessorsApplicationComponent.State> {


    private State state = new State();

    public FluentAccessorsApplicationComponent() {
    }

    @Override
    public State getState() {
        return state;
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "FluentAccessorsApplicationComponent";
    }

    @Override
    public void loadState(final State o) {
        this.state = o;
    }


    public String getSetterPrefix() {
        return this.state.getSetterPrefix();
    }

    public String getGetterPrefix() {
        return this.state.getGetterPrefix();
    }

    public String getFluentPrefix() {
        return this.state.getFluentPrefix();
    }

    public boolean isGeneratingSetters() {
        return this.state.isGenerateSetters();
    }

    public boolean isGeneratingGetters() {
        return this.state.isGenerateGetters();
    }

    public boolean isGeneratingFluent() {
        return this.state.isGenerateFluent();
    }

    public void updateSetterPrefix(String setterPrefix) {
        this.state.setSetterPrefix(setterPrefix);
    }

    public void updateGetterPrefix(String getterPrefix) {
        this.state.setGetterPrefix(getterPrefix);
    }

    public void updateFluentPrefix(String fluentPrefix) {
        this.state.setFluentPrefix(fluentPrefix);
    }

    public void updateIsGeneratingSetters(boolean b) {
        this.state.setGenerateSetters(b);
    }

    public void updateIsGeneratingGetters(boolean b) {
        this.state.setGenerateGetters(b);
    }
    public void updateIsGeneratingFluent(boolean b) {
        this.state.setGenerateFluent(b);
    }



    public static class State {
        private String setterPrefix;
        private String getterPrefix;
        private String fluentPrefix;
        private boolean generateSetters;
        private boolean generateGetters;
        private boolean generateFluent;

        public String getSetterPrefix() {
            return setterPrefix;
        }

        public void setSetterPrefix(final String setterPrefix) {
            this.setterPrefix = setterPrefix;
        }

        public String getGetterPrefix() {
            return getterPrefix;
        }

        public void setGetterPrefix(final String getterPrefix) {
            this.getterPrefix = getterPrefix;
        }

        public String getFluentPrefix() {
            return fluentPrefix;
        }

        public void setFluentPrefix(final String fluentPrefix) {
            this.fluentPrefix = fluentPrefix;
        }

        public boolean isGenerateSetters() {
            return generateSetters;
        }

        public void setGenerateSetters(final boolean generateSetters) {
            this.generateSetters = generateSetters;
        }

        public boolean isGenerateGetters() {
            return generateGetters;
        }

        public void setGenerateGetters(final boolean generateGetters) {
            this.generateGetters = generateGetters;
        }

        public boolean isGenerateFluent() {
            return generateFluent;
        }

        public void setGenerateFluent(final boolean generateFluent) {
            this.generateFluent = generateFluent;
        }
    }
}
