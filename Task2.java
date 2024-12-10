import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

 class Connections {
    public static Connection getConnection() {
        String conn_url = "jdbc:mysql://localhost:3306/";
        String user = "sandhya";
        String password = "Sandhya@123";
        String database = "quizApp";
        try {
            return DriverManager.getConnection(conn_url + database, user, password);
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
            return null;
        }
    }

    public static List<Question> fetchQuestions() {
        List<Question> questions = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM questions")) {

            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("opa"),
                        rs.getString("opb"),
                        rs.getString("opc"),
                        rs.getString("opd"),
                        rs.getString("ans")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
        return questions;
    }
}
class Question {
    int id;
    String question, opa, opb, opc, opd, ans;

    public Question(int id, String question, String opa, String opb, String opc, String opd, String ans) {
        this.id = id;
        this.question = question;
        this.opa = opa;
        this.opb = opb;
        this.opc = opc;
        this.opd = opd;
        this.ans = ans;
    }

    // Getters
    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return opa;
    }

    public String getOptionB() {
        return opb;
    }

    public String getOptionC() {
        return opc;
    }

    public String getOptionD() {
        return opd;
    }

    public String getAnswer() {
        return ans;
    }
}
class Main {
    private static int currentIndex = 0;
    private static int score = 0;
    private static Timer timer;
    private static int timeLeft = 10; // Track the time left for each question

    public static void main(String[] args) {
        List<Question> questions = Connections.fetchQuestions();
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No questions found in the database!");
            return;
        }

        // Create JFrame
        JFrame frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(null);

        // Add components
        JLabel questionLabel = new JLabel();
        questionLabel.setBounds(50, 50, 500, 30);
        frame.add(questionLabel);

        JRadioButton optionA = new JRadioButton();
        JRadioButton optionB = new JRadioButton();
        JRadioButton optionC = new JRadioButton();
        JRadioButton optionD = new JRadioButton();
        optionA.setBounds(50, 100, 500, 30);
        optionB.setBounds(50, 140, 500, 30);
        optionC.setBounds(50, 180, 500, 30);
        optionD.setBounds(50, 220, 500, 30);
        frame.add(optionA);
        frame.add(optionB);
        frame.add(optionC);
        frame.add(optionD);

        ButtonGroup optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(200, 270, 100, 30);
        frame.add(submitButton);

        JLabel timerLabel = new JLabel("Time Left: " + timeLeft);
        timerLabel.setBounds(400, 270, 100, 30);
        frame.add(timerLabel);

        // Display the first question
        displayQuestion(questions.get(currentIndex), questionLabel, optionA, optionB, optionC, optionD);

        // Timer logic
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerLabel.setText("Time Left: " + timeLeft);
                timeLeft--;
                if (timeLeft < 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(frame, "Time's up for this question!");
                    moveToNextQuestion(frame, questions, questionLabel, optionA, optionB, optionC, optionD, optionsGroup);
                }
            }
        });

        // Submit button logic
        submitButton.addActionListener(e -> {
            timer.stop();
            String selectedOption = null;
            if (optionA.isSelected()) selectedOption = "A";
            else if (optionB.isSelected()) selectedOption = "B";
            else if (optionC.isSelected()) selectedOption = "C";
            else if (optionD.isSelected()) selectedOption = "D";

            if (selectedOption != null && selectedOption.equals(questions.get(currentIndex).getAnswer())) {
                score++;
            }

            moveToNextQuestion(frame, questions, questionLabel, optionA, optionB, optionC, optionD, optionsGroup);
        });

        // Start the timer for the first question
        timer.start();

        frame.setVisible(true);
    }

    private static void displayQuestion(Question question, JLabel questionLabel, JRadioButton optionA,
                                        JRadioButton optionB, JRadioButton optionC, JRadioButton optionD) {
        questionLabel.setText(question.getQuestion());
        optionA.setText("A) " + question.getOptionA());
        optionB.setText("B) " + question.getOptionB());
        optionC.setText("C) " + question.getOptionC());
        optionD.setText("D) " + question.getOptionD());
    }

    private static void moveToNextQuestion(JFrame frame, List<Question> questions, JLabel questionLabel,
                                           JRadioButton optionA, JRadioButton optionB, JRadioButton optionC,
                                           JRadioButton optionD, ButtonGroup optionsGroup) {
        currentIndex++;
        if (currentIndex < questions.size()) {
            optionsGroup.clearSelection();
            displayQuestion(questions.get(currentIndex), questionLabel, optionA, optionB, optionC, optionD);

            // Reset timer for the next question
            timeLeft = 10; // Reset time left to 10 seconds
            timer.restart(); // Restart the timer
        } else {
            JOptionPane.showMessageDialog(frame, "Quiz Over! Your score: " + score + "/" + questions.size());
            frame.dispose();
        }
    }
}
