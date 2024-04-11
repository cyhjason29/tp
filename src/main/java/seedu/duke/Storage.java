//@@author cyhjason29
package seedu.duke;

import seedu.duke.exceptions.CustomException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with loading from the save file and saving in that file, as well as containing
 * the game questions.
 */
public class Storage {
    private static final String RESULTS_HEADER = "result";
    private static final String TOPIC_HEADER = "topic";
    private static final String ANSWER_HEADER = "answer";
    private static final String CORRECTNESS_HEADER = "correctness";
    private static final String PAUSE_HEADER = "pause";
    private static final String TEMP_RESULT_HEADER = "temp_result";
    private static final String TEMP_ANSWER_HEADER = "temp_answer";
    private static final String TEMP_CORRECTNESS_HEADER = "temp_correctness";
    private static final int STARTING_INDEX_TOPIC = 6;
    private static final int STARTING_INDEX_RESULT = 7;
    private static final int STARTING_INDEX_ANSWER = 7;
    private static final int STARTING_INDEX_CORRECTNESS = 12;
    private static final int STARTING_INDEX_TEMP_RESULT = 12;
    private static final int STARTING_INDEX_TEMP_ANSWER = 12;
    private static final int STARTING_INDEX_TEMP_CORRECTNESS = 17;
    private static final int STARTING_INDEX_TOPIC_NUM = 6;
    private static final int FIRST_ANSWER = 0;
    private static final int RESULTS_INDEX = 0;
    private static final int TOPIC_NUMBER_INDEX = 1;
    private static final int INDEX_NUMBER_OF_CORRECT_ANSWERS = 0;
    private static final int INDEX_TOTAL_NUMBER_OF_QUESTIONS = 1;
    private static final int INDEX_SCORE = 2;
    private static final int INDEX_TOPIC_NUM = 0;
    private static final int INDEX_INDEX = 1;
    private static final int TWO_PARAMETERS = 2;
    private static final String RESULTS_SEPARATOR = "\\+";
    private static final String ARG_SEPARATOR = "\\|";
    private static final String SAVE_PAUSE_HEADER = "pause ";
    private static final String SAVE_ARG_SEPARATOR = " | ";
    private static final String FILE_PATH = "data/player2113.txt";
    private static final String FOLDER_NAME = "data";
    private static final String MESSAGE_ERROR_INIT = "There was an error initiating the save file.";
    private static final String MESSAGE_ERROR_WRITING = "There was an error writing to the save file.";
    private static final String TEMP_RESULT = "temp_result ";
    private static final String TEMP_ANSWER = "temp_answer ";
    private static final String TEMP_CORRECTNESS = "temp_correctness ";
    private static final String SAVE_RESULT_HEADER = "result ";
    private static final String SAVE_RESULTS_SEPARATOR = " + ";
    private static final String SAVE_TOPIC_HEADER = "topic ";
    private static final String SAVE_ANSWER_HEADER = "answer ";
    private static final String SAVE_CORRECTNESS_HEADER = "correctness ";

    private static boolean isPaused;


    /**
     * Constructs the Storage object.
     */
    public Storage() {
        isPaused = false;
    }

    /**
     * Loads in all the save data from the save file.
     *
     * @param f Save file.
     * @param results List of all results.
     * @param topics List of all topics.
     * @param answers List of all user answers to questions.
     * @return If the user previously paused and exited the game.
     * @throws FileNotFoundException If there was an error finding the save file.
     */
    public boolean loadProgress(File f, ResultsList results, TopicList topics, AnswerTracker answers)
            throws FileNotFoundException {
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String line = s.nextLine();
            processLine(line, results, topics, answers);
        }
        return isPaused;
    }

    /**
     * Process each line within the save file.
     *
     * @param line The line read in from the save file.
     * @param results List of all results.
     * @param topics List of all topics.
     * @param answers List of all user answers to questions.
     */
    private static void processLine(String line, ResultsList results, TopicList topics, AnswerTracker answers) {
        if (line.startsWith(RESULTS_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_RESULT).split(ARG_SEPARATOR);

            String result = processedLine[RESULTS_INDEX].trim();
            Results temp = createResults(result);
            results.addResults(temp);

            int topicNum = Integer.parseInt(processedLine[TOPIC_NUMBER_INDEX].trim());
            results.addQuestions(topicNum);
        } else if (line.startsWith(TOPIC_HEADER)) {
            String topicName = line.substring(STARTING_INDEX_TOPIC).trim();
            ArrayList<Topic> topicList = topics.getTopicList();
            for (Topic topic : topicList) {
                if (topic.getTopicName().equals(topicName)) {
                    topic.markAsAttempted();
                    break;
                }
            }
        } else if (line.startsWith(ANSWER_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_ANSWER).split(ARG_SEPARATOR);
            ArrayList<String> userAnswers = new ArrayList<>();
            for (String answer : processedLine) {
                userAnswers.add(answer.trim());
            }
            answers.addUserAnswers(userAnswers);
        } else if (line.startsWith(CORRECTNESS_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_CORRECTNESS).split(ARG_SEPARATOR);
            ArrayList<Boolean> answerCorrectness = new ArrayList<>();
            for (String correctness : processedLine) {
                answerCorrectness.add(Boolean.parseBoolean(correctness.trim()));
            }
            answers.addUserCorrectness(answerCorrectness);
        } else if (line.startsWith(PAUSE_HEADER)) {
            isPaused = true;
        }
    }

    /**
     * Resumes the game if the user previously paused and exited the game.
     *
     * @param file The save file
     * @param topicResults User results within the current attempt.
     * @param userAnswers User answers within the current attempt.
     * @param correctness User answer correctness within the current attempt.
     * @return The topic number and question number that the user previously paused on.
     * @throws FileNotFoundException If there was an error locating the save file.
     */
    public int[] resumeGame(File file, Results topicResults, ArrayList<String> userAnswers,
                            ArrayList<Boolean> correctness)
            throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int[] pausedQuestion = new int[TWO_PARAMETERS];
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith(PAUSE_HEADER)) {
                pausedQuestion = getPausedQuestion(line.substring(STARTING_INDEX_TOPIC_NUM).trim());
            } else if (line.startsWith(TEMP_RESULT_HEADER)) {
                createTempResult(topicResults, line.substring(STARTING_INDEX_TEMP_RESULT).trim());
            } else if (line.startsWith(TEMP_ANSWER_HEADER)) {
                createTempAnswers(userAnswers, line.substring(STARTING_INDEX_TEMP_ANSWER).trim());
            } else if (line.startsWith(TEMP_CORRECTNESS_HEADER)) {
                createTempCorrectness(correctness, line.substring(STARTING_INDEX_TEMP_CORRECTNESS).trim());
            }
        }
        return pausedQuestion;
    }

    /**
     * Retrieves the topic number and question number which the user previously paused on.
     *
     * @param line The line read in from the save file.
     * @return An array containing the topic number and question number.
     */
    private static int[] getPausedQuestion(String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        int[] pausedQuestion = new int[TWO_PARAMETERS];
        pausedQuestion[INDEX_TOPIC_NUM] = Integer.parseInt(processedLine[INDEX_TOPIC_NUM].trim());
        pausedQuestion[INDEX_INDEX] = Integer.parseInt(processedLine[INDEX_INDEX].trim());
        return pausedQuestion;
    }

    /**
     * Create a temporary result for the paused attempt.
     *
     * @param topicResults User result within the current attempt.
     * @param line The line read in from the save file.
     */
    private static void createTempResult(Results topicResults, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        int numberOfCorrectAnswers = Integer.parseInt(processedLine[INDEX_NUMBER_OF_CORRECT_ANSWERS].trim());
        topicResults.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        int totalNumberOfQuestions = Integer.parseInt(processedLine[INDEX_TOTAL_NUMBER_OF_QUESTIONS].trim());
        topicResults.setTotalNumberOfQuestions(totalNumberOfQuestions);
    }

    /**
     * Create a temporary list of user answers for the paused attempt.
     *
     * @param userAnswers User answers within the current attempt.
     * @param line The line read in from the save file.
     */
    private static void createTempAnswers(ArrayList<String> userAnswers, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        for (String answer : processedLine) {
            answer = answer.trim();
            userAnswers.add(answer);
        }
    }

    /**
     * Create a temporary list of user answer correctness for the paused attempt.
     *
     * @param correctness User answer correctness within the current attempt.
     * @param line The line read in from the save file.
     */
    private static void createTempCorrectness(ArrayList<Boolean> correctness, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        for (String answerCorrectness : processedLine) {
            boolean isCorrect = Boolean.parseBoolean(answerCorrectness.trim());
            correctness.add(isCorrect);
        }
    }

    /**
     * Create the user result loaded in from the save file.
     *
     * @param result User result from a previous attempt.
     * @return User results with the necessary information.
     */
    private static Results createResults(String result) {
        String[] processedResult = result.split(RESULTS_SEPARATOR);
        Results temp = new Results();
        int numberOfCorrectAnswers = Integer.parseInt(processedResult[INDEX_NUMBER_OF_CORRECT_ANSWERS].trim());
        temp.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        int totalNumberOfQuestions = Integer.parseInt(processedResult[INDEX_TOTAL_NUMBER_OF_QUESTIONS].trim());
        temp.setTotalNumberOfQuestions(totalNumberOfQuestions);
        String score = processedResult[INDEX_SCORE].trim();
        temp.setScore(score);
        return temp;
    }

    /**
     * Initialises the save file.
     *
     * @param file The save file.
     * @throws CustomException If there was an error writing to the save file.
     */
    public void initSaveFile(File file) throws CustomException {
        try {
            new File(FOLDER_NAME).mkdir();
            file.createNewFile();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_INIT);
        }
    }

    /**
     * Saves the game data.
     *
     * @param results List of all results.
     * @param topics List of all topics.
     * @param answers List of all user answers to questions.
     * @throws CustomException If there was an error writing to the save file.
     */
    public void saveProgress(ResultsList results, TopicList topics, AnswerTracker answers)
            throws CustomException {
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            writeToFile(results, topics, answers, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_WRITING);
        }
    }

    /**
     * Saves the game data when the user is exiting from a paused game.
     *
     * @param results List of all results.
     * @param topics List of all topics.
     * @param answers List of all user answers to questions.
     * @param allAnswers User answers within the current attempt.
     * @param answersCorrectness User answer correctness within the current attempt.
     * @param topicResults User results within the current attempt.
     * @param topicNum The topic number.
     * @param index The question number.
     * @throws CustomException If there was an error writing to the save file.
     */
    public void pauseGame(ResultsList results, TopicList topics, AnswerTracker answers, ArrayList<String> allAnswers,
                          ArrayList<Boolean> answersCorrectness, Results topicResults, int topicNum, int index)
            throws CustomException {
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            savePoint(allAnswers, answersCorrectness, topicResults, fileWriter, topicNum, index);
            writeToFile(results, topics, answers, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_WRITING);
        }
    }

    /**
     * Writes to the file a save point in the current attempt.
     *
     * @param allAnswers User answers within the current attempt.
     * @param answersCorrectness User answers correctness within the current attempt.
     * @param topicResults User results within the current attempt.
     * @param fileWriter File writer to write to the save file.
     * @param topicNum The topic number.
     * @param index The question number.
     * @throws IOException If there was an error writing to the save file.
     */
    private static void savePoint(ArrayList<String> allAnswers, ArrayList<Boolean> answersCorrectness,
                                  Results topicResults, FileWriter fileWriter, int topicNum, int index)
            throws IOException {

        fileWriter.write(SAVE_PAUSE_HEADER + topicNum + SAVE_ARG_SEPARATOR + index + System.lineSeparator());

        StringBuilder listOfAnswers = new StringBuilder();
        StringBuilder listOfCorrectness = new StringBuilder();

        if (!allAnswers.isEmpty()) {
            listOfAnswers.append(allAnswers.get(FIRST_ANSWER));
            listOfCorrectness.append(answersCorrectness.get(FIRST_ANSWER));
        }

        for (int i = 1; i < allAnswers.size(); i++) {
            String answer = allAnswers.get(i);
            boolean correctness = answersCorrectness.get(i);
            listOfAnswers.append(SAVE_ARG_SEPARATOR).append(answer);
            listOfCorrectness.append(SAVE_ARG_SEPARATOR).append(correctness);
        }

        int numberOfCorrectAnswers = topicResults.getNumberOfCorrectAnswers();
        int totalNumberOfQuestions = topicResults.getTotalNumberOfQuestions();

        fileWriter.write(TEMP_RESULT + numberOfCorrectAnswers + SAVE_ARG_SEPARATOR + totalNumberOfQuestions
                + System.lineSeparator());
        fileWriter.write(TEMP_ANSWER + listOfAnswers + System.lineSeparator());
        fileWriter.write(TEMP_CORRECTNESS + listOfCorrectness + System.lineSeparator());
    }

    /**
     * Writes to the save file current game data.
     *
     * @param results List of all results.
     * @param topics List of all topics.
     * @param answers List of all user answers to questions.
     * @param fileWriter File writer to write to the save file.
     * @throws IOException If there was an error writing to the save file.
     */
    private static void writeToFile(ResultsList results, TopicList topics, AnswerTracker answers,
                                    FileWriter fileWriter)
            throws IOException {

        ArrayList<Results> resultList = results.getSessionResults();
        ArrayList<Integer> topicsChosen = results.getTopicsChosen();
        for (int i = 0; i < resultList.size(); i++) {
            Results result = resultList.get(i);
            int numberOfCorrectAnswers = result.getNumberOfCorrectAnswers();
            int totalNumberOfQuestions = result.getTotalNumberOfQuestions();
            String score = result.getScore();
            int topicNum = topicsChosen.get(i);
            fileWriter.write(SAVE_RESULT_HEADER + numberOfCorrectAnswers + SAVE_RESULTS_SEPARATOR
                    + totalNumberOfQuestions + SAVE_RESULTS_SEPARATOR + score + SAVE_ARG_SEPARATOR + topicNum
                    + System.lineSeparator());
        }

        ArrayList<Topic> topicList = topics.getTopicList();
        for (Topic topic : topicList) {
            if (topic.hasAttempted()) {
                fileWriter.write(SAVE_TOPIC_HEADER + topic.getTopicName() + System.lineSeparator());
            }
        }

        ArrayList<ArrayList<String>> userAnswers = answers.getAllAnswers();
        ArrayList<ArrayList<Boolean>> isCorrect = answers.getAllCorrectness();
        for (int i = 0; i < userAnswers.size(); i++) {
            StringBuilder listOfAnswers = new StringBuilder();
            StringBuilder listOfCorrectness = new StringBuilder();

            listOfAnswers.append(userAnswers.get(i).get(FIRST_ANSWER));
            listOfCorrectness.append(isCorrect.get(i).get(FIRST_ANSWER));
            for (int j = 1; j < userAnswers.get(i).size(); j++) {
                String answer = userAnswers.get(i).get(j);
                boolean correctness = isCorrect.get(i).get(j);
                listOfAnswers.append(SAVE_ARG_SEPARATOR).append(answer);
                listOfCorrectness.append(SAVE_ARG_SEPARATOR).append(correctness);
            }
            fileWriter.write(SAVE_ANSWER_HEADER + listOfAnswers + System.lineSeparator());
            fileWriter.write(SAVE_CORRECTNESS_HEADER + listOfCorrectness + System.lineSeparator());
        }
    }
    //@@author

    // creates question list1,2 etc
    public void updateQuestionList(int questionListIndex, QuestionsList questionList) throws CustomException {
        int questionListNum = questionListIndex + 1; // +1 coz 0 index
        // to add a questionList, create a function for each questionList
        switch (questionListNum) {
        case (1):
            updateQuestionList1(questionList);
            break;
        case (2):
            updateQuestionList2(questionList);
            break;
        default:
            throw new CustomException("unable to create questionList" + questionListNum);
        }
    }

    private void updateQuestionList1(QuestionsList questionList) {
        Question question1 = new Question(
                "What language does CS2113 use?",
                "a",
                "CS2113 teaches OOP in Java",
                new String[]{"Java",
                             "C++",
                             "C",
                             "Python"});

        Question question2 = new Question(
                "As per the textbook, brown-field projects are usually harder than green-field projects.",
                "a",
                "Brown-field projects refers to a product to replace/ update an existing software, " +
                        "while green-field projects refers to a totally new system from scratch",
                new String[]{"True",
                             "False",
                             "Only got grass fields",
                             "There is no textbook"});

        Question question3 = new Question(
                "What is NFR?",
                "c",
                "Requirements can be divided as functional and non-functional requirements",
                new String[]{"No-such-thing, For Real",
                             "Nets For Rent",
                             "Non-Functional Requirements",
                             "NUS Forest Registry"});

        Question question4 = new Question(
                "What is an example of a NFR?",
                "c",
                "Non-functional requirements (NFR) specify the constraints " +
                        "under which the system is developed and operated",
                new String[]{"Your imagination",
                             "Fishing nets",
                             "should work on 32 and 64 bit systems",
                             "Recycle bins"});

        Question question5 = new Question(
                "Requirements should be as close to implementation as possible, " +
                        "so as to minimize errors in implementing it",
                "c",
                "In contrast, requirements should be implementation-free.",
                new String[]{"True",
                             "True sometimes",
                             "False",
                             "I give up!"});

        Question question6 = new Question(
                "Ideally, a requirement should not be divisible any further",
                "d",
                "True, textbook uses the term 'atomic' to describe this quality",
                new String[]{"False: a requirement is not a number",
                             "NA: not possible in real life",
                             "Idk",
                             "True"});

        Question question7 = new Question(
                "What does SLAP stands for?",
                "d",
                "See the course website!",
                new String[]{"when you feel pain on your face",
                             "when another person feels pain on their face",
                             "Studying-lit Acceleration Program",
                             "Single Level of Abstraction Per method"});

        questionList.addQuestion(question1);
        questionList.addQuestion(question2);
        questionList.addQuestion(question3);
        questionList.addQuestion(question4);
        questionList.addQuestion(question5);
        questionList.addQuestion(question6);
        questionList.addQuestion(question7);
    }

    private void updateQuestionList2(QuestionsList questionsList) {
        Question question1 = new Question(
                "One should never prioritize efficiency or performance over readability",
                "a",
                "From textbook: there are cases when optimizing takes priority over other things",
                new String[]{"False: not always the case",
                             "False: efficiency == readability",
                             "True: readability is always more important",
                             "True: the compiler will optimize for you"});
        Question question2 = new Question(
                "As per the KISS principle, one should always prefer the simpler solution over clever ones",
                "b",
                "False: not always. Rather, one should not discard the simple solutions just because there " +
                        "is a more 'clever' solution. Instead, 'clever' solution should only be chosen only if " +
                        " the additional cost of complexity is justifiable",
                new String[]{"True: simple == elegant",
                             "False: sometimes we want the clever solutions",
                             "Both are irrelevant to KISS",
                             "I give up!"});
        Question question3 = new Question(
                "Which of the following follows the correct coding standard?",
                "d",
                "Constants should be named using ALL_CAPS_LIKE_THIS",
                new String[]{"int my_var = 2;",
                             "int my_var = 2;",
                             "int MyVar = 2;",
                             "final static int MY_VAR = 2;"});
        Question question4 = new Question(
                "Constants often follow this naming convention: ALL_CAPS_LIKE_THIS. What is it called?",
                "a",
                "Constants should be named using ALL_CAPS_LIKE_THIS",
                new String[]{"SCREAMING_SNAKE_CASE",
                             "camelCase1",
                             "PascalCase",
                             "Train-Case"});

        questionsList.addQuestion(question1);
        questionsList.addQuestion(question2);
        questionsList.addQuestion(question3);
        questionsList.addQuestion(question4);
    }
}
