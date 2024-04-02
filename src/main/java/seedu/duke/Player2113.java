package seedu.duke;

import seedu.duke.exceptions.CustomException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Player2113 {
    public static final String SOME_FILE_PATH = "something";
    private static final String FILE_PATH_STORAGE = "data/player2113.txt";
    private static final String MESSAGE_FILE_ERROR = "There was an error locating the save file.";

    private Ui ui;
    private QuestionsList questionsList1;
    private QuestionsList questionsList2;
    private TopicList topicList;
    private QuestionListByTopic questionListByTopic;
    private ResultsList allResults;
    private AnswerTracker userAnswers;
    private Storage storage;
    private final Helper helper;

    public Player2113(String someFilePath) {
        questionsList1 = new QuestionsList();
        questionsList2 = new QuestionsList();
        questionListByTopic = new QuestionListByTopic();
        topicList = new TopicList();
        allResults = new ResultsList();
        userAnswers = new AnswerTracker();
        helper = new Helper();
        storage = new Storage();
        ui = new Ui();

        if (someFilePath.contentEquals("something")) {
            // TODO: load data from file
            // Add dummy data (for now)
            Question question1 = new Question("question1", "solution1", "explanation1");
            Question question2 = new Question("question2", "solution2", "explanation2");
            questionsList1.addQuestion(question1);
            questionsList1.addQuestion(question2);
            questionListByTopic.addQuestionSet(questionsList1);

            Question question3 = new Question("question3", "solution3", "explanation3");
            Question question4 = new Question("question4", "solution4", "explanation4");
            questionsList2.addQuestion(question3);
            questionsList2.addQuestion(question4);
            questionListByTopic.addQuestionSet(questionsList2);

            Topic topic1 = new Topic(questionsList1,"topic1", false, "Covers topic 1 notions mentioned in lecture 1-2");
            Topic topic2 = new Topic(questionsList2,"topic2", false, "Covers topic 2 notions mentioned in lecture 3-4");
            topicList.addTopic(topic1);
            topicList.addTopic(topic2);
        }


    }
  
    public void run() {

        ui.sayHi();
        File saveFile = new File(FILE_PATH_STORAGE);
        boolean isPaused = false;
        try {
            isPaused = storage.loadProgress(saveFile, allResults, topicList, userAnswers);
        } catch (FileNotFoundException e) {
            try {
                storage.initSaveFile(saveFile);
            } catch (CustomException exception) {
                ui.handleException(exception);
            }
        }
        if (isPaused) {
            try {
                loadQuestion(saveFile);
                ui.printTopicCompleted();
            } catch (CustomException e) {
                ui.handleException(e);
            }
        }

        ui.printTopicList(topicList, ui);

        while (ui.isPlaying) {
            ui.readCommands(ui, topicList, questionListByTopic, allResults, helper, userAnswers, storage);
        }

    }

    private void loadQuestion(File saveFile) throws CustomException {
        Results topicResults = new Results();
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<Boolean> correctness = new ArrayList<>();
        int[] pausedQuestion;
        try {
            pausedQuestion = storage.resumeGame(saveFile, topicResults, answers, correctness);
        } catch (FileNotFoundException e) {
            throw new CustomException(MESSAGE_FILE_ERROR);
        }
        
        ui.resumeTopic(pausedQuestion, topicList, questionListByTopic, allResults, userAnswers, storage, ui,
                answers, correctness, topicResults);
    }

    public static void main(String[] args) {
        new Player2113(SOME_FILE_PATH).run();
    }
}
