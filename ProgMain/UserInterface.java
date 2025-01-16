import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;

public class UserInterface extends JFrame{
    //UI variables
    JButton createNoteButton = new JButton("Create Note");
    JButton createChordButton = new JButton("Create Chord");
    JButton exportToWavButton = new JButton("Export to .wav");
    JButton deleteNoteButton = new JButton("Delete Note");
    JButton deleteChordButton = new JButton("Delete Chord");
    JLabel sampleSizeLabel = new JLabel("Sample Size: ");
    String[] sampleSizes = {"8-bit", "16-bit"};
    JComboBox<String> sampleSizeDropdown = new JComboBox<>(sampleSizes);

    JLabel sampleRateLabel = new JLabel("Sample Rate: ");
    JTextField sampleRateField = new JTextField(10);
    JButton setBasicSampleRateButton = new JButton("Set Basic");

    //Backend variables
    int mainSampleSize = 8;
    float mainSampleRate = 44100;
    String[] basicNoteArray, userNoteArray;
    String basicNotesPath = "notes/basicNotes.txt";
    String userNotesPath = "notes/userNotes.txt";

    public UserInterface() {
        super("SlaySound 3000");
        initialize();
        addListeners();
    }

    private void initialize(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setSize(800, 600);
        setLayout(new BorderLayout()); 
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER; 
        
        JPanel settingsPanel = new JPanel(new GridLayout(3, 2, 10, 10)); 
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        settingsPanel.add(sampleSizeLabel);
        settingsPanel.add(sampleSizeDropdown);
        settingsPanel.add(sampleRateLabel);
        sampleRateField.setText(String.valueOf(mainSampleRate));
        settingsPanel.add(sampleRateField);
        settingsPanel.add(new JLabel()); 
        settingsPanel.add(setBasicSampleRateButton);
        
        settingsPanel.setPreferredSize(new Dimension(400, 150)); 
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(settingsPanel, gbc);
        
        JPanel actionsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionsPanel.add(createNoteButton);
        actionsPanel.add(createChordButton);
        actionsPanel.add(exportToWavButton);
        actionsPanel.add(deleteNoteButton);
        actionsPanel.add(deleteChordButton);
        
        actionsPanel.setPreferredSize(new Dimension(400, 150)); 
        
        gbc.gridy = 1;
        mainPanel.add(actionsPanel, gbc);
        add(mainPanel, BorderLayout.CENTER);

        updateBasicNoteArray();
        updateUserNoteArray();
    }

    private void addListeners() {
        createNoteButton.addActionListener(e -> {createNoteListenFunction();});

        createChordButton.addActionListener(e -> {createChordListenFunction();});

        exportToWavButton.addActionListener(e -> {exportToWavListenFunction();});

        deleteNoteButton.addActionListener(e -> {deleteNoteListenFunction();});

        deleteChordButton.addActionListener(e -> {deleteChordListenFunction();});
        
        //should recompute all notes and chords
        sampleSizeDropdown.addActionListener(e -> {
            String selectedSize = (String) sampleSizeDropdown.getSelectedItem();
            if(selectedSize.equals("8-bit")) mainSampleSize = 8;
            else mainSampleSize = 16;
            System.out.println("Program sample size: " + mainSampleSize);
        });

        addSampleRateListeners();
    }

    private void createNoteListenFunction() {
        this.setEnabled(false);
        CreateNote createNoteWindow = new CreateNote(this);
        createNoteWindow.setVisible(true);

        createNoteWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (createNoteWindow.getCreatedNote().frequency != 0) {
                    String noteToFile = createNoteWindow.getCreatedNote().soundName + " " + createNoteWindow.getCreatedNote().frequency;
                    System.out.println(noteToFile);

                    try (FileWriter writer = new FileWriter(userNotesPath, true)) {
                        writer.write(noteToFile + "\n");
                        updateUserNoteArray();
                    } catch (IOException ex) {
                        // I don't know how to test this, but should be alright I guess(?)
                        JOptionPane.showMessageDialog(createNoteWindow, "Exception " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
            }
        });
    }

    private void createChordListenFunction() {

            this.setEnabled(false);
            CreateChord createChordWindow = new CreateChord(this);
            createChordWindow.setVisible(true);
        
            createChordWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    setEnabled(true); // Włącz ponownie główne okno po zamknięciu okna CreateChord
                }
            });
        
    }

    private void exportToWavListenFunction() {
        System.out.println("Button 'Export To Wav' clicked");
    }

    private void deleteNoteListenFunction() {
        System.out.println("Button 'Delete Note' clicked");
    }

    private void deleteChordListenFunction() {
        System.out.println("Button 'Delete Chord' clicked");
    }

    private void addSampleRateListeners() {
        sampleRateField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                sampleRateField.addActionListener(enter -> {
                    sampleRateField.transferFocus();
                });
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = sampleRateField.getText();
                try {
                    float newSampleRate = Float.parseFloat(text);
                    if (newSampleRate > 0 && newSampleRate <= 200000) {
                        if (mainSampleRate != newSampleRate) {
                            mainSampleRate = newSampleRate;
                            System.out.println("Sample rate set to: " + mainSampleRate);
                        }
                    } else {
                        sampleRateField.setText(String.valueOf(mainSampleRate));
                        JOptionPane.showMessageDialog(null,
                                "Sample rate must be between 0 and 200000.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    sampleRateField.setText(String.valueOf(mainSampleRate));
                    JOptionPane.showMessageDialog(null,
                            "Sample rate must be a valid float number.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setBasicSampleRateButton.addActionListener(e -> {
            if (mainSampleRate != 44100) {
                mainSampleRate = 44100;
                sampleRateField.setText(String.valueOf(mainSampleRate));
                System.out.println("Sample rate reset to basic: 44100");
            }
        });
    }

    public void updateBasicNoteArray() {
        if (!(new File(basicNotesPath).isFile())) {
            JOptionPane.showMessageDialog(this, "Error: Could not find file: " + basicNotesPath, "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            System.exit(0);
        } else {
            try {
                basicNoteArray = loadNotesFromFile(basicNotesPath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Could not load basic notes. Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                System.exit(0);
            }
        }
    }

    public void updateUserNoteArray() {
        if (!(new File(userNotesPath).isFile())) {
            JOptionPane.showMessageDialog(this, "Error: Could not find file: " + userNotesPath, "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            System.exit(0);
        } else {
            try {
                userNoteArray = loadNotesFromFile(userNotesPath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Could not load user notes. Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                System.exit(0);
            }
        }
    }

    private String[] loadNotesFromFile(String filePath) {
        List<String> notesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                notesList.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
        return notesList.toArray(new String[0]);
    }

    public Boolean isNoteNameInBasicNoteArray(String noteName) {
        for(String name : basicNoteArray) {
            if(noteName.equals(name.split(" ")[0])) return true;
        }
        return false;
    }

    public Boolean isNoteFrequencyInBasicNoteArray(float noteFrequency) {
        for(String freq : basicNoteArray) {
            if(noteFrequency == Float.parseFloat(freq.split(" ")[1])) return true;
        }
        return false;
    }

    public Boolean isNoteNameInUserNoteArray(String noteName) {
        for(String name : userNoteArray) {
            if(noteName.equals(name.split(" ")[0])) return true;
        }
        return false;
    }

    public Boolean isNoteFrequencyInUserNoteArray(float noteFrequency) {
        for(String freq : userNoteArray) {
            if(noteFrequency == Float.parseFloat(freq.split(" ")[1])) return true;
        }
        return false;
    }

}
