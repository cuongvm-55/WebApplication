package com.luvsoft.controllers;

public abstract class AbstractController {
    private boolean isSUPMode;
    public boolean isSUPMode() {
        return isSUPMode;
    }

    public void setSUPMode(boolean isSUPMode) {
        this.isSUPMode = isSUPMode;
    }
}
