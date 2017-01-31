package com.msokol10.intellijplugin.fluentaccessors;

import com.intellij.openapi.editor.actionSystem.EditorAction;


public class FluentAccessorsAction extends EditorAction {

    protected FluentAccessorsAction () {
        super(new FluentAccessorsActionHandler());
    }

}
