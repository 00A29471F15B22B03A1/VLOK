package core.ui;

import core.localization.Language;
import core.localization.Localization;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JFrame {

    public SettingsWindow() {
        setSize(250, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle(Localization.getString("ui.settings"));
        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();

        JComboBox<Language> languagesComboBox = new JComboBox<>(Localization.getLoadedLanguages());
        JLabel languageLabel = new JLabel(Localization.getString("ui.languages"));
        languageLabel.setLabelFor(languagesComboBox);

        gc.gridy = 0;

        gc.gridx = 0;
        add(languageLabel, gc);
        gc.gridx++;
        add(languagesComboBox, gc);

        gc.gridy++;
        gc.gridx = 0;
        JButton cancelButton = new JButton(Localization.getString("ui.cancel"));
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton, gc);

        gc.gridx++;
        JButton saveButton = new JButton(Localization.getString("ui.save"));
        saveButton.addActionListener(e -> {
            Localization.setLanguage((Language) languagesComboBox.getSelectedItem());
            dispose();
        });
        add(saveButton, gc);

        Localization.addLanguageListener(newLanguage -> {
            setTitle(Localization.getString("ui.settings"));
            languageLabel.setText(Localization.getString("ui.languages"));
            cancelButton.setText(Localization.getString("ui.cancel"));
            saveButton.setText(Localization.getString("ui.save"));
        });
    }
}
