# Ng Xin Zhe Sean - Project Portfolio Page

## Overview

Player2113 is a desktop CLI tool to help CS2113/T students revise Java OOP concepts in a
gamification environment. The user first chooses a topic, answers the questions, and then
reviews the solutions. The application has about 3.6k lines of code and is written in Java.

### Summary of Contributions

* **New Feature:** Added the ability to view solutions: `solution`
    * What it does: allows the user to view all solutions to a previously attempted topic
      or to view the solution to a previously attempted specific question
    * Justification: This feature improves the product significantly as this is part of
      the product's purpose: for users to view solutions to previously attempted questions
    * Highlights: Depending on the number of user inputs, this feature allows the user to
      either view all solutions to a topic or view 1 solution to a specific question. The
      implementation therefore needed additional parsing of user inputs to accurately
      differentiate between displaying all or just one solution.


* **New Feature:** Added the ability to view explanations: `explain`
    * What it does: allows the user to view all explanations to a previously attempted
      topic or to view the explanation to a previously attempted specific question
    * Justification: This feature improves the product significantly as this extends the
      usefulness of the product: for users to view the explanations to solutions if they do
      not understand them.
    * Highlights: Depending on the number of user inputs, this feature allows the user to
      either view all explanations to a topic or view 1 explanation to a specific question.
      The implementation therefore needed additional parsing of user inputs to accurately
      differentiate between displaying all or just one explanation.


* **Feature Enhancement:** Added the ability to choose a random
  topic ([#54](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/54))
    * What it does: allows the user to choose a random topic amongst the list of topics
      displayed
    * Justification: This feature improves the product's gamification aspect: this random
      feature adds variation and challenge to the user experience when using this product.
  
  <div style="page-break-after: always;"></div>
  
    * Highlights: This enhancement closely works with the controller responsible for
      choosing which topic to display questions from. Due to the nature of java's Random
      library, additional checks have to be made to ensure random number generated is not
      invalid like 0 or an out-of-bounds index.


* **Code Contributed:** [RepoSense Link](https://nus-cs2113-ay2324s2.github.io/tp-dashboard/?search=ngxzs&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2024-02-23&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)


* **Enhancements to existing features:**
    * Made GUI more user-friendly with line breaks ([#40](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/40/files))
    * Added Parser class testing to increase line test coverage from 0% to 9% due to code
      regression ([#75](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/75/files), [#82](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/82/files))
    * Added scaffolding for main class code ([#21](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/21/files))

* **UG Contributions:**
    * Added documentation for features `solution`
      and `explain` ([#57](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/86/files))
    * Added links, headings, and standardized format structure for
      UG ([#69](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/69/files), [#161](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/161/files), [#181](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/181/files))


* **DG Contributions:**
    * Added implementation details for `solution`
      and `explain` ([#58](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/58/files))
    * Added links, headings, and standardized format structure for
      UG ([#161](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/161/files))


* **Team-based Tasks Contributions:**
    * PR review (to resolve GitHub checks errors): ([#26](https://github.com/AY2324S2-CS2113-F15-1/tp/issues/26))
    * Enabled assertions, modified setup in gradle
      build ([#21](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/21/files))
    * Code
      Refactoring ([#86](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/86/files), [#67](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/67/files))
    * Added database questions for
      product ([#149](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/149/files), [#167](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/167/files), [#168](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/168/files))
