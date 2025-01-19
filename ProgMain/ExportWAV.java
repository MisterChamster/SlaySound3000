import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ExportWAV extends JFrame {
    JTextField durationField;
    JComboBox<String> dropdown1, dropdown2, dropdown3, dropdown4;
    JButton cancelButton, exportButton;
    UserInterface parentFrame;

    public ExportWAV(UserInterface parentFrame) {
        super("Export to WAV");
        this.parentFrame = parentFrame;
        initialize();
        addListeners();
    }

    private void initialize() {
        String[] tempArray = new String[0];
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(parentFrame); 

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.insets = new Insets(9, 10, 9, 10); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Basic notes:"), gbc);

        gbc.gridx = 1;
        tempArray = getNoteNamesArrWithEmpty("basic");
        dropdown1 = new JComboBox<>(tempArray);
        mainPanel.add(dropdown1, gbc);

        gbc.insets = new Insets(9, 10, 9, 10); 
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("User notes:"), gbc);

        gbc.gridx = 1;
        tempArray = getNoteNamesArrWithEmpty("user");
        dropdown2 = new JComboBox<>(tempArray);
        mainPanel.add(dropdown2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Basic chords:"), gbc);

        gbc.gridx = 1;
        tempArray = getNotesetNamesArrWithEmpty("basic");
        dropdown3 = new JComboBox<>(tempArray);
        mainPanel.add(dropdown3, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("User chords:"), gbc);

        gbc.gridx = 1;
        tempArray = getNotesetNamesArrWithEmpty("user");
        dropdown4 = new JComboBox<>(tempArray);
        mainPanel.add(dropdown4, gbc);

        gbc.insets = new Insets(9, 10, 3, 10); 
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Duration:"), gbc);

        gbc.gridx = 1;
        durationField = new JTextField(20);
        mainPanel.add(durationField, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        cancelButton = new JButton("Cancel");
        exportButton = new JButton("Export");
        cancelButton.setPreferredSize(new Dimension(100, 40));
        exportButton.setPreferredSize(new Dimension(100, 40));
        buttonPanel.add(cancelButton);
        buttonPanel.add(exportButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        cancelButton.addActionListener(e -> dispose());
        exportButton.addActionListener(e -> {exportButtonListenFunction();});
        dropdown1.addActionListener(e -> {
            if (dropdown1.getSelectedItem() != " --empty-- ") {
                if (dropdown2.getSelectedItem() != " --empty-- ") dropdown2.setSelectedIndex(0); 
                if (dropdown3.getSelectedItem() != " --empty-- ") dropdown3.setSelectedIndex(0); 
                if (dropdown4.getSelectedItem() != " --empty-- ") dropdown4.setSelectedIndex(0);};});
        dropdown2.addActionListener(e -> {
            if (dropdown2.getSelectedItem() != " --empty-- ") {
                if (dropdown1.getSelectedItem() != " --empty-- ") dropdown1.setSelectedIndex(0); 
                if (dropdown3.getSelectedItem() != " --empty-- ") dropdown3.setSelectedIndex(0); 
                if (dropdown4.getSelectedItem() != " --empty-- ") dropdown4.setSelectedIndex(0);};});
        dropdown3.addActionListener(e -> {
            if (dropdown3.getSelectedItem() != " --empty-- ") {
                if (dropdown1.getSelectedItem() != " --empty-- ") dropdown1.setSelectedIndex(0); 
                if (dropdown2.getSelectedItem() != " --empty-- ") dropdown2.setSelectedIndex(0); 
                if (dropdown4.getSelectedItem() != " --empty-- ") dropdown4.setSelectedIndex(0);};});
        dropdown4.addActionListener(e -> {
            if (dropdown4.getSelectedItem() != " --empty-- ") {
                if (dropdown1.getSelectedItem() != " --empty-- ") dropdown1.setSelectedIndex(0); 
                if (dropdown2.getSelectedItem() != " --empty-- ") dropdown2.setSelectedIndex(0); 
                if (dropdown3.getSelectedItem() != " --empty-- ") dropdown3.setSelectedIndex(0);};});
    }

    private void exportButtonListenFunction() {
        if(durationField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Duration field is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String durationText = durationField.getText();

        try {
            double duration = Double.parseDouble(durationText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Duration must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String basicNote = (String) dropdown1.getSelectedItem();
        if (!basicNote.equals(" --empty-- ")) {
            return;
        }
        String userNote = (String) dropdown2.getSelectedItem();
        if (!userNote.equals(" --empty-- ")) {
            return;
        }


        String basicChord = (String) dropdown3.getSelectedItem();
        if (!basicChord.equals(" --empty-- ")) {
            return;
        }
        String userChord = (String) dropdown4.getSelectedItem();
        if (!userChord.equals(" --empty-- ")) {
            return;
        }



        JOptionPane.showMessageDialog(null, "Exporting sound with selected options!", "Export", JOptionPane.INFORMATION_MESSAGE);
    }

    private String[] getNoteNamesArrWithEmpty(String arg) {
        String[] loadArr = new String[0];
        if (arg.equals("basic")) loadArr = parentFrame.getBasicNoteArray();
        if (arg.equals("user")) loadArr = parentFrame.getUserNoteArray();

        String[] tempArr = new String[loadArr.length + 1];
        tempArr[0] = " --empty-- ";
        for (int i = 0; i < loadArr.length; i++) {
            tempArr[i + 1] = String.join(" ", Arrays.copyOf(loadArr[i].split(" "), loadArr[i].split(" ").length-1));
        }
        return tempArr;
    }

    private String[] getNotesetNamesArrWithEmpty(String arg) {
        String[] loadArr = new String[0];
        if (arg.equals("basic")) loadArr = parentFrame.getBasicNotesetArray();
        if (arg.equals("user")) loadArr = parentFrame.getUserNotesetArray();

        String[] tempArr = new String[loadArr.length + 1];
        tempArr[0] = " --empty-- ";
        for (int i = 0; i < loadArr.length; i++) {
            tempArr[i + 1] = loadArr[i].split(", ")[0];
        }
        return tempArr;
    }
}