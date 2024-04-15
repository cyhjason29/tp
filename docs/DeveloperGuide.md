# Developer Guide

* [Acknowledgements](#acknowledgements)
* [Implementation](#implementation)
    * [Results Feature](#results-feature)
    * [Topic Feature](#topics-feature)
    * [Solution Feature](#solution-feature)
    * [Explain Feature](#explain-feature)
    * [Save Feature](#save-feature)
* [Appendix: Requirements](#appendix-requirements)
    * [Product Scope](#appendix-a-product-scope)
    * [User Stories](#appendix-b-user-stories)
    * [Non-Functional Requirements](#appendix-c-non-functional-requirements)
    * [Glossary](#appendix-d-glossary)
* [Appendix: Instructions for manual testing](#appendix-e-instructions-for-manual-testing)
    * [Launch and Shutdown](#launch-and-shutdown)
    * [Choosing a topic](#choosing-a-topic)
    * [Viewing Results](#viewing-results)
    * [Viewing Solutions](#viewing-solutions)
    * [Viewing Explanations](#viewing-explanations)
    * [Selecting Timed Mode](#selecting-timed-mode)
    * [Viewing all topics available](#viewing-all-topics-available)
    * [Viewing Help](#viewing-help)
    * [Pause and Resume GamePlay](#pause-and-resume-gameplay)
    * [Customizing questions](#customizing-questions)
    * [Setting Checkpoints](#setting-checkpoints)
    * [Clearing Progress](#clearing-progress)

## Acknowledgements

*Libraries:*

1. Display formatted tables in the CLI - [ASCII TABLES](https://bethecoder.com/applications/products/asciiTable.action)

2. Topic selection menu and testing mode progress bar - [ProgressBar](https://github.com/ctongfei/progressbar)

## Implementation

### Results Feature

The results feature is facilitated by `ResultsList` and
`AnswerTracker`. Both are used to display the results for
all question sets attempted by the user, including details
such as specific questions and their respective answer inputs
from the user.

Given below is an example usage scenario and how the results
mechanism behaves at each step.

Step 1. The user launches the application for the first time,
and proceeds to start a game with their chosen topic.

The following sequence diagram shows how the `Results` for
one question set is added to the `ResultsList`:

![ResultsList sequence diagram](team/img/DG/Results.png)

> **Note:** The lifeline for Parser and Results should end
> at the destroy marker (X) but due to a limitation of PlantUML,
> the lifeline reaches the end of the diagram.

Similarly, the following sequence diagram shows how the
`AnswerTracker` stores all the user answer inputs:

![AnswerTracker sequence diagram](team/img/DG/AnswerTracker.png)

> **Note:** The lifeline for Parser, allAnswers, and answersCorrectness
> should end at the destroy marker (X) but due to a limitation of PlantUML,
> the lifeline reaches the end of the diagram.

Step 2. The user may repeat Step 1 with other question sets.

The sequence diagram for this step involving `ResultsList`
and `AnswerTracker` are the same as the ones shown in Step 1.

Step 3. The user now wants to view their results by executing
the `results` command.  

![Results command sequence diagram](team/img/DG/ViewResults.png)

> **Note:** If the user uses the results feature before
> attempting any questions, the application will instead
> return an error to the user indicating that there are no
> results.

### Topics Feature

The topics feature comprises `TopicList` and `QuestionListByTopic`.
`TopicList` is the list of topics for the users to attempt.
`QuestionListByTopic` stores the respective question set for each topic in an ArrayList.

Given below is an example usage scenario and how the results
mechanism behaves at each step.

Step 1. The user launches the application for the first time,
and proceeds to start a game with their chosen topic.

The following shows the class diagram for `topicList`:

![TopicList class diagram](team/img/DG/topicList_noC_class_diagram.png)

Step 2. A question from the question set of the chosen topic is displayed.
The user inputs their answer.

Step 3. Step 2 repeats until all the questions in the question set has been asked.
Step 1 executes and process repeats.

The following shows the class diagram for `QuestionListByTopic`:

![QuestionListByTopic](team/img/DG/questionList_class_diagram.png)

### Solution Feature

The solution feature either prints the solution to 1 question or all questions in 1 topic.

The solution feature is facilitated by `Parser#processSolutionCommand`, which is called by `Parser#parseCommand`

> **OVERVIEW:**
> ![Solution sequence diagram](team/img/DG/Solution.png)

> **Note:** The lifeline for Parser and Results should end
> at the destroy marker (X) but due to a limitation of PlantUML,
> the lifeline reaches the end of the diagram.

Step 1: After user runs the program and keys in the user command, the command will be passed to
`Parser#parseCommand`.

> **NOTE:** The command must contain the `solution` keyword.

Step 2a: `Parser#processSolutionCommand` first checks the number of parameters in the user command
by calling `Parser#checkIfTwoParameters`.
The, further processing of parameters is done by calling `Parser#getTopicOrQuestionNum`.
This is facilitated by calling `QuestionsListByTopic#getQuestionSet` to get all questions in the specified topic.

Step 2b: Before getting the solution(s), the program first verifies if the topic has been attempted before.
The program only prints them only if the topic is attempted before.
The topic is selected by calling `TopicList#get` and `TopicList#hasAttempted` returns the attempted status.

Step 3:
Depending on the number of parameters,
if there is 1 parameter (ie get all solutions):
`QuestionsList#getAllSolutions` will get all solutions and `ui#printAllSolutions` will print them.
else if there are 2 parameters (ie get one solution):
`QuestionsList#getOneSolution` will get the specified solution and `ui#printOneSolution` will print it.

### Explain Feature

The explain feature either prints the explanation to 1 question or all questions in 1 topic.

The explain feature is facilitated by `Parser#processExplanationCommand`, which is called by `Parser#parseCommand`

Step 1: After user runs the program and keys in the user command, the command will be passed to
`Parser#parseCommand`.

> **NOTE:** The command must contain the `explain` keyword.
> Sequence diagram for `explain` is similar to the [sequence diagram](#solution-feature) for `solution` feature

Step 2a: `Parser#processExplainCommand` first checks the number of parameters in the user command
by calling `Parser#checkIfTwoParameters`.
The, further processing of parameters is done by calling `Parser#getTopicOrQuestionNum`.
This is facilitated by calling `QuestionsListByTopic#getQuestionSet` to get all questions in the specified topic.

Step 2b: Before getting the explanation(s), the program first verifies if the topic has been attempted before.
The program only prints them only if the topic is attempted before.
The topic is selected by calling `TopicList#get` and `TopicList#hasAttempted` returns the attempted status.

Step 3:
Depending on the number of parameters,
if there is 1 parameter (ie get all explanations):
`QuestionsList#getAllExplanations` will get all explanations and `ui#printAllExplanations` will print them.
else if there are 2 parameters (ie get one explanation):
`QuestionsList#getOneExplanation` will get the specified explanation and `ui#printOneExplanation` will print it.

### Save Feature

The save feature is facilitated by `Storage`. It implements the following operations:
- Storage#initSaveFile()
- Storage#loadProgress()
- Storage#processLine()
- Storage#saveProgress()
- Storage#writeToFile()

Step 1. When the user exits the application for the first time, `Storage` creates a local
text file. Then, it calls `ResultsList#getSessionResults()`, `TopicList#getTopicsChosen()`,
`AnswerTracker#getAllAnswers()`, and `AnswerTracker#getAllCorrectness()` to get all the necessary
game data.

Step 2. `Storage` converts to all the game data into a particular format in order for
the game progress to be recognised when the application is opened the next time. It will then
write all the game data into the aforementioned local text file.

Step 3. The next time the user opens the application, `Storage` will attempt to find the local text
file. If successful, `Storage` will convert the formatted text back into 
the necessary game data and add them back into `ResultsList`, `TopicList` and `AnswerTracker`.

The following is the class diagram:

![Storage Class Diagram](./team/img/DG/Storage.png)

### Custom Feature

The custom feature allows the user to select a topic and generate a subset of random questions from that topic.

This feature is facilitated by `Parser#handleCustomCommand` which is called by `Parser#parseCommand`.

Within `Parser#handleCustomCommand`, Java regular expression packages `Pattern` and `Matcher` are used to ensure that the command is given in valid format. They are also used to extract the relevant parameters.

The following is the overview of the method:

![Custom sequence diagram](team/img/DG/Custom.png)

### Checkpoint Feature

The checkpoint feature allows the user to set a goal to achieve within the study session. Note that this checkpoint feature is only available when practising in custom mode.

This feature is facilitated by `Parser#handleCheckpointCommand` which is called by `Parser#parseCommand`.

Once the checkpoint goal has been reached, the user will be notified that they have achieved their goal for the session.

The following is the overview of the method:

![Checkpoint sequence diagram](team/img/DG/Checkpoint.png)

### Clear Feature

The clear feature allows the user to clear the game progress thus far.

This feature is facilitated by `Parser#handleClearCommand` which is called by `Parser#parseCommand`.

To prevent the user from accidentally clearing their progress, they would get an additional prompt to confirm that they want to clear their progress.

The following is the overview of the method:

![Clear sequence diagram](team/img/DG/Clear.png)


## Appendix: Requirements

### Appendix A: Product scope

**Target user profile:**

* revising Java OOP and Software Engineering concepts taught in CS2113/T
* prefer desktop app over other types
* can type fast
* reasonably comfortable with CLI

**Value proposition:** more accessible than current CS2113/T website as questions are consolidated in dataset. In
addition, able to keep track of progress.

### Appendix B: User Stories

Priorities: High (must-have) - `***`, Medium (good-to-have) - `**`, Low (nice-to-have) - `*`

| Version | Priority | As a ...            | I want to ...                                        | So that I can ...                                          |
|---------|----------|---------------------|------------------------------------------------------|------------------------------------------------------------|
| v1.0    | ***      | new user            | access the tutorial / guide to the game easily       | refer to it if I am unfamiliar with the usage of a command |
| v1.0    | ***      | new user            | choose a topic                                       | choose a specific topic to revise on                       |
| v1.0    | **       | regular user        | take note of the questions answered wrong previously | revisit past mistakes and learn from them                  |
| v1.0    | ***      | student new to Java | receive solutions after answering                   | learn the correct solution for the question                |
| v1.0    | **       | student new to Java | receive explanations after answering                | learn the correct explanation for the solution             |
| v1.0    | **       | regular user        | clear my progress in the game                       | start anew                                                 |
| v1.0    | ***      | busy user           | exit the game                                       | I can do other things in life                              |
| v2.0    | **       | regular user        | see a progress bar when answering MCQs              | track my progress when attempting a question set           |
| v2.0    | **       | regular user        | see a progress bar about all topics in the main menu | track my revision progress for the entire course           |
| v2.0    | ***      | regular user        | access timed modes in the game easily                | train my answering speed in preparation for tests          |
| v2.0    | **       | student new to Java | practice on a random topic                           | avoid memorizing answers if I do a topic repeatedly        |
| v2.0    | ***      | regular user        | come back to my saved points in the game             | continue working from my previous progress                 |
| v2.0    | **       | busy user           | study in smaller chunks by having checkpoints        | play the game during my small slots of free time           |
| TBC     | *        | regular user        | have topics suggestion instead of choosing one      | I can avoid choosing a topic and be lazy                   |
| TBC     | *        | busy user           | have a reminder to do questions                      | I can keep track with the lectures                         |

*[More to be added]*

### Appendix C: Non-Functional Requirements

1. Should work on any *mainstream* OS as long as it has Java `11` or above installed
2. A user with average typing speed should be able to find and answer questions faster than using the CS2113/T course
   website

### Appendix D: Glossary

- *mainstream OS:* Windows, Linux, MacOS

## Appendix E: Instructions for Manual Testing

> **NOTE:**
> These instructions only provide a starting point for testers to work on.
> They are non-exhaustive and testers are encouraged to do more _exploratory_ testing.

### Launch and Shutdown

1. Initial launch
    1. Download the jar file and copy to an empty folder
    2. Open CLI and `cd` to the empty folder
    3. Enter `java -jar jarFileName` to run the program
2. Shutdown
    1. Enter `bye` to exit program.

### Choosing a topic

1. Test case: `topic 1`

   Expected: Topic 1 is chosen. Questions from Topic 1 will start appearing.

2. Test case: `topic`

   Expected: No topic chosen. Invalid command error message shown.

### Viewing Results

1. Test case: `results`

   Expected: Scores for all previous attempts shown

2. Test case: `results details 1`

   Expected: Scores for Attempt 1 shown. Questions and user answers are shown as well.

3. Test case: `result`

   Expected: No results shown. Invalid command error message shown.


### Viewing Solutions

1. Test case: `solution 1 1`

   Expected: Shows solution to Topic 1 Question 1.

2. Test case: `solution`

   Expected: No solution showed. Invalid command format error message shown.


### Viewing Explanations

1. Test case: `explain 1 1`

   Expected: Shows explanation to Topic 1 Question 1.

2. Test case: `explain`

   Expected: No explanation showed. Invalid command format error message shown.


### Selecting Timed Mode

1. Test case: `timed mode 5`, `topic 1`

   Expected: Topic will automatically end after 5 seconds.

2. Test case: `timed mode`

   Expected: No timer will be set. User will be prompted for a time limit.


### Viewing all topics available

1. Test case: `list`

   Expected: All topics will be listed, with the topic names, summary, and attempted status

2. Test case: `topic 1`, `list`

   Expected: All topics will be listed, and attempted status for topic 1 will be updated.


### Viewing Help

1. Test case: `help`

   Expected: All possible commands will be displayed

### Pause and Resume GamePlay

1. Test case: `pause`, then `resume`

   Expected: Game will be paused and resumed

2. Test case: `pause`, then `bye`. Start the program again

   Expected: Game will pause, then exit. Upon start of program, user will be prompted whether to continue with previous
   attempt.


### Customizing questions

1. Test case: `custom 1 10`

   Expected: Game will display 10 questions from Topic 1. Topic 1 status (Attempted/ Unattempted) will remain unchanged.


### Setting Checkpoints

1. Prerequisite: In custom mode

2. Test case: `checkpoint 2`

   Expected: Sets checkpoint to 2.


### Clearing Progress

1. Test case: `clear`

   Expected: Progress cleared

