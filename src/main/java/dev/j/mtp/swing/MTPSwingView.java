/*
 * Copyright (c) 2014. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package dev.j.mtp.swing;

import javax.swing.*;

/**
 * Created by Jason on 5/12/2014.
 */
public class MTPSwingView extends JFrame {
    private JPanel rootContent;
    private JComboBox listDevices;

    public MTPSwingView() {
        super("MTP File Sync");
        setContentPane(rootContent);
    }
}
