package com.marvins.adventure1;

import javax.swing.*;

public class ToolWalkBox extends JPanel {

    public static void main(String[] args) {
        WalkBoxEditor walkBoxEditor = new WalkBoxEditor();
        walkBoxEditor.init();
        walkBoxEditor.run();
        walkBoxEditor.stop();
    }
}