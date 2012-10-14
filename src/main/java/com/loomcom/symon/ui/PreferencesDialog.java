package com.loomcom.symon.ui;

import com.loomcom.symon.Preferences;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

public class PreferencesDialog extends Observable implements Preferences {

    private final JDialog dialog;

    private JTextField aciaAddressField;
    private JTextField programLoadAddressField;
    private JTextField borderWidthField;

    private int programLoadAddress = DEFAULT_PROGRAM_LOAD_ADDRESS;
    private int aciaAddress = DEFAULT_ACIA_ADDRESS;
    private int borderWidth = DEFAULT_BORDER_WIDTH;

    public PreferencesDialog(Frame parent, boolean modal) {
        this.dialog = new JDialog(parent, modal);
        initComponents();
        updateUi();
    }

    public JDialog getDialog() {
        return dialog;
    }

    /**
     * TODO: Validation of input.
     */
    private void initComponents() {
        dialog.setTitle("Preferences");
        Container contents = dialog.getContentPane();

        JPanel settingsContainer = new JPanel();
        JPanel buttonsContainer = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        settingsContainer.setLayout(layout);

        final JLabel aciaAddressLabel = new JLabel("ACIA Address");
        final JLabel programLoadAddressLabel = new JLabel("Program Load Address");
        final JLabel borderWidthLabel = new JLabel("Console Border Width");

        aciaAddressField = new JTextField(8);
        programLoadAddressField = new JTextField(8);
        borderWidthField = new JTextField(8);

        aciaAddressLabel.setLabelFor(aciaAddressField);
        programLoadAddressLabel.setLabelFor(programLoadAddressField);
        borderWidthLabel.setLabelFor(borderWidthField);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        settingsContainer.add(aciaAddressLabel, constraints);

        constraints.gridx = 1;
        settingsContainer.add(aciaAddressField, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        settingsContainer.add(programLoadAddressLabel, constraints);

        constraints.gridx = 1;
        settingsContainer.add(programLoadAddressField, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        settingsContainer.add(borderWidthLabel, constraints);

        constraints.gridx = 1;
        settingsContainer.add(borderWidthField, constraints);

        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");


        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                updateUi();
                dialog.setVisible(false);
            }
        });

        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                programLoadAddress = hexToInt(programLoadAddressField.getText());
                aciaAddress = hexToInt(aciaAddressField.getText());
                borderWidth = Integer.parseInt(borderWidthField.getText());
                updateUi();
                // TODO: Actually check to see if values have changed, don't assume.
                setChanged();
                PreferencesDialog.this.notifyObservers();
                dialog.setVisible(false);
            }
        });

        buttonsContainer.add(applyButton);
        buttonsContainer.add(cancelButton);

        contents.add(settingsContainer, BorderLayout.PAGE_START);
        contents.add(buttonsContainer, BorderLayout.PAGE_END);

        dialog.pack();
    }

    public int getProgramStartAddress() {
        return programLoadAddress;
    }

    public int getAciaAddress() {
        return aciaAddress;
    }

    /**
     * @return The width of the console border, in pixels.
     */
    public int getBorderWidth() {
        return borderWidth;
    }

    public void updateUi() {
        aciaAddressField.setText(intToHex(aciaAddress));
        programLoadAddressField.setText(intToHex(programLoadAddress));
        borderWidthField.setText(Integer.toString(borderWidth));
    }

    private String intToHex(int i) {
        return String.format("%04x", i);
    }

    private int hexToInt(String s) {
        try {
            return Integer.parseInt(s, 16);
        } catch (NumberFormatException ex) {
            // TODO: Handle Displaying error.
            return 0;
        }
    }

}
