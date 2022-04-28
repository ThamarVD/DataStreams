import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamGUI extends JFrame{
    private JPanel mainPanel;
    private ArrayList<String> fileData;
    public StreamGUI(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createTextPanel();
        createUserPanel();

        mainPanel.add(textPanel, BorderLayout.NORTH);
        mainPanel.add(userPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel textPanel;
    private JTextArea origArea;
    private JScrollPane origPane;
    private JTextArea searchArea;
    private JScrollPane searchPane;
    private void createTextPanel(){
        textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(1, 2));

        origArea = new JTextArea(15, 15);
        origPane = new JScrollPane(origArea);
        textPanel.add(origPane);

        searchArea = new JTextArea(15, 15);
        searchPane = new JScrollPane(searchArea);
        textPanel.add(searchPane);
    }

    private JPanel userPanel;
    private JTextField fileLocation;
    private JButton loadBtn;
    private JButton searchBtn;
    private JButton quitBtn;
    private void createUserPanel(){
        userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        fileLocation = new JTextField(30);
        JPanel tempPanel = new JPanel();
        loadBtn = new JButton("Load File");
        loadBtn.addActionListener(e -> loadFile());
        searchBtn = new JButton("Search");
        quitBtn = new JButton("Quit");
        quitBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?") == JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });
        tempPanel.add(loadBtn);
        tempPanel.add(searchBtn);
        tempPanel.add(quitBtn);

        userPanel.add(fileLocation, BorderLayout.NORTH);
        userPanel.add(tempPanel, BorderLayout.SOUTH);
    }

    private void searchFile(){
        searchArea.setText("");
        try{
            List<String> results = fileData.stream().filter(str -> str.toLowerCase(Locale.ROOT).contains(fileLocation.getText())).collect(Collectors.toList());

            for(String lines : results){
                searchArea.append(lines + "\n");
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void loadFile(){
        if(origArea.getText() != ""){
            fileData = new ArrayList<String>();
            origArea.setText("");
            try{
                for(Object fileLine : readFile().toArray()){
                    fileData.add(String.valueOf(fileLine));
                    origArea.append(String.valueOf(fileLine) + "\n");
                }
                searchBtn.addActionListener(e -> searchFile());
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Stream readFile(){
        JFileChooser chooser = new JFileChooser();
        File selectedFile;
        Stream lines = null;

        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);
            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                lines = Files.lines(file);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return lines;
    }
}
