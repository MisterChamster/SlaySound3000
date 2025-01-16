import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateChord extends JFrame {
    // UI Components
    private UserInterface parentFrame;
    private JTextField chordNameField;
    private JTextField notesUsedField;
    private JPanel inputPanel;
    private JPanel buttonPanel;
    private JButton discardButton;
    private JButton createButton;
    private JButton addNoteButton;
    private JButton deleteNoteButton;

    // Backend variables
    SoundNoteSet createdChord;
    String createdChordName;
    String notesUsed;

    public CreateChord(UserInterface parentFrame) {
        super("Create Chord");
        this.parentFrame = parentFrame;
        initialize();
        addListeners();
    }

    private void initialize() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400); 
        setLayout(new BorderLayout(15, 15)); 
        setLocationRelativeTo(null); 

        inputPanel = new JPanel(new GridBagLayout()); 
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Name:", SwingConstants.CENTER), gbc); 

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        chordNameField = new JTextField(30);
        inputPanel.add(chordNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Notes used:", SwingConstants.CENTER), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        notesUsedField = new JTextField(30);
        notesUsedField.setEditable(false);
        inputPanel.add(notesUsedField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        addNoteButton = new JButton("Add note...");
        deleteNoteButton = new JButton("Delete note...");
        addNoteButton.setPreferredSize(new Dimension(140, 40));
        deleteNoteButton.setPreferredSize(new Dimension(140, 40));

        JPanel notesButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        notesButtonPanel.add(addNoteButton);
        notesButtonPanel.add(deleteNoteButton);
        inputPanel.add(notesButtonPanel, gbc);
        add(inputPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        discardButton = new JButton("Discard");
        createButton = new JButton("Create");
        discardButton.setPreferredSize(new Dimension(140, 40));
        createButton.setPreferredSize(new Dimension(140, 40));
        buttonPanel.add(discardButton);
        buttonPanel.add(createButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (parentFrame != null) {
                    parentFrame.setEnabled(true);
                }
            }
        });

        discardButton.addActionListener(e -> {
            dispose();
            if (parentFrame != null) {
                parentFrame.setEnabled(true);
            }
        });

        createButton.addActionListener(e -> {
            createdChordName = chordNameField.getText();
            notesUsed = notesUsedField.getText();

            if (createdChordName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chord name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (notesUsed.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must add at least one note to create a chord.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // JOptionPane.showMessageDialog(this, "Chord created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (parentFrame != null) {
                parentFrame.setEnabled(true);
            }
        });

        addNoteButton.addActionListener(e -> openAddNoteDialog());
        deleteNoteButton.addActionListener(e -> openDeleteNoteDialog());
    }

    private void openAddNoteDialog() {
        AddNoteDialog addDialog = new AddNoteDialog(this, notesUsedField);
    }

    private void openDeleteNoteDialog() {
        DeleteNoteDialog deleteDialog = new DeleteNoteDialog(this, notesUsedField);
    }

    public String getCreatedChordName() {
        return createdChordName;
    }

    public String getNotesUsed() {
        return notesUsed;
    }
}
