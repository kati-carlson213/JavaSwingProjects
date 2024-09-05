import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

public class Main {
    private JTextArea textArea;
    private JTextArea outputArea;


    Outputs outputClass = new Outputs();

    Main() {
        JFrame frame = new JFrame("Simple Julia Interpreter");

        ImageIcon img = new ImageIcon("C:\\Users\\katie\\IdeaProjects\\simpleJuliaInterpreter\\juliaIcon.jpg");

        frame.setIconImage(img.getImage());




        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(Color.BLACK);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setBackground(Color.decode("#292929"));
        textArea.setForeground(Color.WHITE);

        Font textFont = new Font("Arial", Font.PLAIN, 14);
        textArea.setFont(textFont);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(200, 200));

        panel.add(scrollPane, BorderLayout.NORTH);

        JButton selectFileButton = new JButton("Select File");
        selectFileButton.setPreferredSize(new Dimension(100, 30));
        selectFileButton.setBackground(Color.BLACK);
        selectFileButton.setForeground(Color.WHITE);
        selectFileButton.setBorder(new LineBorder(Color.WHITE));

        JButton submitButton = new JButton("Interpret Code");
        submitButton.setPreferredSize(new Dimension(120, 30));
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(new LineBorder(Color.WHITE));

        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.setBackground(Color.decode("#1a1a1a"));

        submitPanel.add(selectFileButton);
        submitPanel.add(submitButton);
        panel.add(submitPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);

        outputArea.setFont(textFont);


        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setPreferredSize(new Dimension(200, 300));

        panel.add(outputScrollPane, BorderLayout.SOUTH);

        selectFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileDialog fileDialog = new FileDialog(frame, "Select File", FileDialog.LOAD);
                fileDialog.setVisible(true);

                String directory = fileDialog.getDirectory();
                String fileName = fileDialog.getFile();

                if (fileName != null) {
                    String filePath = directory + fileName;

                    try {
                        File inputFile = new File(filePath);
                        String fileInput = "";

                        Scanner scan = new Scanner(inputFile);

                        while (scan.hasNextLine()) {
                            fileInput += scan.nextLine() + "\n";
                        }

                        textArea.setText(fileInput);

                        scan.close();

                    } catch (Exception except) {
                        String exceptionOutput = except.getMessage();
                        outputArea.setText(exceptionOutput);
                    }
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sourceCode = textArea.getText();

                Interpreter interpreter = new Interpreter(outputClass);

                try {
                    interpreter.interpret(sourceCode);
                    String output = outputClass.getOutput();

                    String exceptionStatus = output.substring(0, 5);

                    if (exceptionStatus.equals("true+")) {
                        outputArea.setForeground(Color.decode("#e91640"));
                        output = output.replaceAll("true\\+", "");

                    }
                    else {
                        outputArea.setForeground(Color.WHITE);
                        output = output.replaceAll("false", "");
                    }

                    output = output.replaceAll("java.lang.Exception:", "");

                    outputArea.setText(output);



                } catch (Exception ex) {
                    String errorMessage = String.valueOf(ex);
                    errorMessage = errorMessage.replaceAll("java.lang.Exception:", "");

                    outputArea.setForeground(Color.decode("#e91640"));

                    outputArea.setText(errorMessage);
                }



            }
        });

        frame.getContentPane().add(panel);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
