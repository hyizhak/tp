---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# FinClient Developer Guide

This document provides a guide for developers who want to contribute to the project.

## Table of contents

- [Acknowledgements](#acknowledgements)
- [Setting up, getting started](#setting-up-getting-started)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI component](#ui-component)
    - [Logic component](#logic-component)
    - [Model component](#model-component)
    - [Storage component](#storage-component)
    - [Common classes](#common-classes)
- [Implementation](#implementation)
    - [Order function and Call Auction Calculator](#order-function-and-call-auction-calculator)
    - [Accessing Help Message](#accessing-help-message)
    - [Proposed: Undo/redo feature](#proposed-undo-redo-feature)
- [Documentation, logging, testing, configuration, dev-ops](#documentation-logging-testing-configuration-dev-ops)
- [Appendix: Requirements](#appendix-requirements)
    - [Product scope](#product-scope)
    - [User stories](#user-stories)
    - [Use cases](#use-cases)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Glossary](#glossary)
- [Appendix: Planned Enhancements](#appendix-planned-enhancements)
- [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* [Code](https://github.com/se-edu/addressbook-level4) from [AB4](https://se-education.org/addressbook-level4/), a SE-EDU project, has been reused and adapted in FinClient for GUI testing purposes.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture
<div class="text-center">
  <puml src="diagrams/ArchitectureDiagram.puml" width="300"></puml>
  <figcaption><strong>Figure 1:</strong> Architecture Diagram.</figcaption>
</div>
<br>

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

<div class="text-center">
  <puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574"></puml>
  <figcaption><strong>Figure 2:</strong> Architecture Sequence Diagram.</figcaption>
</div>
<br>

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<div class="text-center">
  <puml src="diagrams/ComponentManagers.puml" width="300"></puml>
  <figcaption><strong>Figure 3:</strong> Component Managers Diagram.</figcaption>
</div>
<br>

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/ui/Ui.java)

<div class="text-center">
  <puml src="diagrams/UiClassDiagram.puml" width="800"></puml>
  <figcaption><strong>Figure 4:</strong> UI Class Diagram.</figcaption>
</div>
<br>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/logic/Logic.java)

<div class="text-center">
  <puml src="diagrams/LogicClassDiagram.puml" width="600"></puml>
  <figcaption><strong>Figure 5:</strong> Logic Class Diagram.</figcaption>
</div>
<br>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<div class="text-center">
  <puml src="diagrams/DeleteSequenceDiagram.puml" width="600"></puml>
  <figcaption><strong>Figure 6:</strong> Delete Sequence Diagram.</figcaption>
</div>
<br>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `FinClientParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<div class="text-center">
  <puml src="diagrams/ParserClasses.puml" width="600"></puml>
  <figcaption><strong>Figure 7:</strong> Parser Classes Diagram.</figcaption>
</div>
<br>

How the parsing works:
* When called upon to parse a user command, the `FinClientParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `FinClientParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/model/Model.java)

<div class="text-center">
  <puml src="diagrams/ModelClassDiagram.puml" width="800"></puml>
  <figcaption><strong>Figure 8:</strong> Model Class Diagram.</figcaption>
</div>
<br>

The `Model` component,

* stores the finclient data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">

:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `FinClient`, which `Person` references. This allows `FinClient` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<div class="text-center">
  <puml src="diagrams/BetterModelClassDiagram.puml" width="450"></puml>
  <figcaption><strong>Figure 9:</strong> Better Model Class Diagram.</figcaption>
</div>

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/finclient/storage/Storage.java)

<div class="text-center">
  <puml src="diagrams/StorageClassDiagram.puml" width="550"></puml>
  <figcaption><strong>Figure 10:</strong> Storage Class Diagram.</figcaption>
</div>
<br>

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `FinClientStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.finclient.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Order function and Call Auction Calculator

This feature extends the Person class by adding an order field to record the amount and price for each client’s order. Additionally, a call auction mechanism is integrated within the Model to compute a clearing price based on the complete order book.


#### Implementation

Order Update:

When a user issues an `order` command, the application updates the corresponding `Person` object in the `Model` with the new order details. This update is handled by the `OrderCommand`, which modifies the Model accordingly.

Call Auction Calculator Integration:

The Call Auction Calculator is embedded within the `Model`. All orders are maintained in the `Model`, and the `Model` exposes a method, `model#calculateClearingPrice()`, which utilizes the internal `CallAuctionCalculator` to aggregate the order data and compute the clearing price. The computation determines the price level at which the maximum transaction volume is achieved.

Clearing Price Query:

The clearing price is retrieved on demand by the `UI`. When needed, the `UI` calls `Logic#getClearingPrice()`, which in turn calls `Model#calculateClearingPrice()` and returns the computed clearing price back to the `UI` for display.

<div class="text-center">
  <puml src="diagrams/OrderSequenceDiagram.puml" width="1000"></puml>
  <figcaption><strong>Figure 11:</strong> Order Sequence Diagram.</figcaption>
</div>
<br>

#### Design Considerations

* **Separation of Concerns:**
The `order` command is solely responsible for updating the `Model`, while the clearing price calculation is a distinct operation invoked by the `UI`. This separation ensures that the processes for updating orders and calculating the auction clearing price remain decoupled.
* **Data Consistency:**
All order data is maintained within the `Model`. The integrated call auction calculator accesses the current order book directly from the Model, ensuring that the most up-to-date information is used for computing the clearing price.
* **Performance:**
The clearing price computation is optimized to handle a large volume of orders efficiently. Future enhancements might include caching strategies to further improve performance.
* **Extensibility:**
The `CallAuctionCalculator` is implemented as a modular component within the Model. This design allows for easy modifications or extensions of the auction mechanism without impacting other parts of the system.
* **Error Handling:**
If the order data is incomplete or invalid, the `Logic` will trigger appropriate error messages during the order parsing phase. The clearing price is computed only when the Model’s state is consistent, ensuring accurate auction results.

### Accessing Help Message
The feature allows the user to obtain relevant information regarding the commands available while also giving them the choice to copy a URL to our User Guide when scrolled to the bottom of the help message.

#### Implementation

Accessing Help:

When a user issues a `help` command, or presses a shortcut of `Fn + F1`, the application will run the command through the `Logic` component which then interacts with the `UI` components of `MainWindow` and `HelpWindow`.

Closing Help:

When a user has finished using the help function, the user can click on the `X` to close it or alternatively, press on the `ESCAPE` key to close the help message. This action is registered via the `HelpWindow`.

<div class="text-center">
  <puml src="diagrams/HelpSequenceDiagram.puml" width="800"></puml>
  <figcaption><strong>Figure 12:</strong> Help Sequence Diagram.</figcaption>
</div>
<br>

<div class="text-center">
  <puml src="diagrams/HelpActivityDiagram.puml" width="800"></puml>
  <figcaption><strong>Figure 13:</strong> Help Activity Diagram.</figcaption>
</div>
<br>

### \[Proposed\] Undo\/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedFinClient`. It extends `FinClient` with an undo/redo history, stored internally as an `FinClientStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedFinClient#commit()` — Saves the current address book state in its history.
* `VersionedFinClient#undo()` — Restores the previous address book state from its history.
* `VersionedFinClient#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitFinClient()`, `Model#undoFinClient()` and `Model#redoFinClient()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedFinClient` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<div class="text-center">
  <puml src="diagrams/UndoRedoState0.puml" width="500"></puml>
  <figcaption><strong>Figure 14:</strong> UndoRedo State 0.</figcaption>
</div>
<br>

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitFinClient()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `FinClientStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<div class="text-center">
  <puml src="diagrams/UndoRedoState1.puml" width="500"></puml>
  <figcaption><strong>Figure 15:</strong> UndoRedo State 1.</figcaption>
</div>
<br>

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitFinClient()`, causing another modified address book state to be saved into the `FinClientStateList`.

<div class="text-center">
  <puml src="diagrams/UndoRedoState2.puml" width="500"></puml>
  <figcaption><strong>Figure 16:</strong> UndoRedo State 2.</figcaption>
</div>
<br>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** If a command fails its execution, it will not call `Model#commitFinClient()`, so the address book state will not be saved into the `FinClientStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoFinClient()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<div class="text-center">
  <puml src="diagrams/UndoRedoState3.puml" width="500"></puml>
  <figcaption><strong>Figure 17:</strong> UndoRedo State 3.</figcaption>
</div>
<br>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial FinClient state, then there are no previous FinClient states to restore. The `undo` command uses `Model#canUndoFinClient()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<div class="text-center">
  <img src="images/UndoSequenceDiagram-Logic.png" alt="Undo Sequence Diagram (Logic)" />
  <figcaption><strong>Figure 21:</strong> Undo Sequence Diagram (Logic).</figcaption>
</div>
<br>

<div markdown="span" class="alert alert-info">

:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

<div class="text-center">
  <img src="images/UndoSequenceDiagram-Model.png" alt="Undo Sequence Diagram (Model)" />
  <figcaption><strong>Figure 22:</strong> Undo Sequence Diagram (Model).</figcaption>
</div>
<br>

The `redo` command does the opposite — it calls `Model#redoFinClient()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">

:information_source: **Note:** If the `currentStatePointer` is at index `FinClientStateList.size() - 1`, pointing to the latest address book state, then there are no undone FinClient states to restore. The `redo` command uses `Model#canRedoFinClient()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitFinClient()`, `Model#undoFinClient()` or `Model#redoFinClient()`. Thus, the `FinClientStateList` remains unchanged.

<div class="text-center">
  <puml src="diagrams/UndoRedoState4.puml" width="500"></puml>
  <figcaption><strong>Figure 18:</strong> UndoRedo State 4.</figcaption>
</div>
<br>

Step 6. The user executes `clear`, which calls `Model#commitFinClient()`. Since the `currentStatePointer` is not pointing at the end of the `FinClientStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<div class="text-center">
  <puml src="diagrams/UndoRedoState5.puml" width="500"></puml>
  <figcaption><strong>Figure 19:</strong> UndoRedo State 5.</figcaption>
</div>
<br>

The following activity diagram summarizes what happens when a user executes a new command:

<div class="text-center">
  <puml src="diagrams/CommitActivityDiagram.puml" width="250"></puml>
  <figcaption><strong>Figure 20:</strong> Commit Activity Diagram.</figcaption>
</div>
<br>

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* 💼 Financial advisors
* 🖥️ Tech-savvy
* 📇 has a need to manage a significant number of contacts
* 📝 has a need to add notes to keep track of their clients’ preferences
* ⌨️ prefer typing
* 🖱️ is reasonably comfortable using CLI apps

**Value proposition**:

* 🚀 Provide fast access to client details
* ✨ Easy adding of new clients and required data
* ⌨️ Optimized for users who prefer CLI
* 📋 Allows tracking of multiple details such as clients hobbies, preferences, status etc


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                            | So that I can…​                                                        |
|--------|--------------------------------------------|---------------------------------------------------------|------------------------------------------------------------------------|
| `* * *` | new user                                   | see usage instructions                                  | refer to instructions when I forget how to use the App                 |
| `* * *` | user                                       | add a new person                                        | add new clients that I need to manage                                  |
| `* * *` | user                                       | delete a person                                         | remove entries that I no longer need                                   |
| `* * *` | user                                       | read details about my clients                           | I can tell what my clients have                                        |
| `* * *` | user                                       | add notes to a person                                   | record important details about my business dealings with them          |
| `* * *` | user                                       | search for clients contacts                             | I can immediately get the data I require of my client                  |
| `* * *` | user                                       | store multiple phone numbers and an email for a contact | I can reach them through different channels                            |
| `* * *` | user                                       | find a person by name                                   | locate details of persons without having to go through the entire list |
| `* * *` | user                                       | hide private contact details                            | minimize chance of someone else seeing them by accident                |
| `* *`  | user with many persons in the address book | sort persons by name                                    | locate a person easily                                                 |
| `* *`  | user with things to remember               | add timestamps                                          | remember to do something for a client                                  |
| `* *`  | user                                       | add multiple numbers                                    | keep track of all my client's phone numbers                            |
| `* *`  | forgetful user                             | add more fields to the clients                          | remember who is who at a quick glance                                  |
| `*`    | pro user                                   | see buying and selling prices of each client            | keep track of who wants to buy or sell at what price quickly           |
| `*`    | pro user                                   | have shortcuts                                          | do my work even faster                                                 |

### Use cases

(For all use cases below, the **System** is the `FinClient` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete a person**

**MSS**

1.  User requests to list persons.
2.  FinClient shows a list of persons.
3.  User requests to delete a specific person in the list.
4.  FinClient deletes the person.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. FinClient shows an error message.

      Use case resumes at step 2.

**Use case: Add a person**

**MSS**

1. User requests to add a person.
2. FinClient prompts for the person’s details.
3. User provides the person’s details.
4. FinClient adds the person.

    Use case ends.

**Extensions**

* 3a. User provides an invalid detail.
    * 3a1. FinClient shows an error message.
      
      Use case resumes at step 2.
    * 3a2. User provides a duplicate detail.
      * 3a2.1. FinClient shows an error message.
        
        Use case resumes at step 2.

* 3b. User provides no phone numbers or invalid numbers.
    * 3b1. FinClient shows an error message.
      
      Use case resumes at step 2.

* 3c. User provides more than the accepted number of 3 phone numbers.
    * 3c1. FinClient shows an error message.
      
      Use case resumes at step 2.

**Use case: Edit a person**

**MSS**

1.  User requests to list persons.
2.  FinClient shows a list of persons.
3.  User requests to edit a specific person in the list.
4.  FinClient edits the person's detail while keeping everything else the same.

   Use case ends.

**Extensions**

* 3a. User provides a prefix without any text.
    * 3a1. FinClient shows an error message.

      Use case resumes at step 2.

* 3b. User wants to remove an optional field.
    * 3b1. User provides prefix and delete option.
    * 3b2. FinClient edits the person's detail to remove the optional field.

      Use case ends.

**Use case: Find a person**

**MSS**

1.  User requests to find a person by name.
2.  FinClient shows the person’s details.

    Use case ends.

**Extensions**

* 2a. The person is not found.

  * 2a1. FinClient shows an error message.
  
    Use case resumes at step 1.

**Use case: Hide a person's details**

**MSS**

1. User requests to hide a person's detail by name.
2. FinClient obscures the person’s details.

   Use case ends.

**Use case: Reveal a person's details**

**MSS**

1. User requests to reveal a person's detail by name.
2. FinClient reveals the person’s details.

   Use case ends.

**Use case: Track orders for a person**

**MSS**

1. User requests to track orders for a specific contact.
2. FinClient updates the contact’s order.
3. FinClient calculates and show the call auction clearing price based on the updated order book.

   Use case ends.

**Extensions**

* 1a. The person is not found.

    * 1a1. FinClient shows an error message.

      Use case ends.

* 1b. No order information is provided.

    * 1b1. FinClient empties given person's order.

      Use case resumes at step 3.

**Use case: Add remarks to a person**

**MSS**

1. User requests to add remarks to a person.
2. FinClient prompts for the remarks.
3. User provides the remarks.

    Use case ends.

**Extensions**

* 3a. User provides an empty remark.

    * 3a1. FinClient shows an error message.

      Use case resumes at step 2.

**Use case: Access help message**

**MSS**
1. User requests to access or open help message.
2. FinClient opens help message.
3. User reads help message.
4. User closes help message.

    Use case ends.

**Extensions**

* 2a. User accidentally minimizes help message.
  * 2a1. User manually opens help message.

    Use case resumes at step 3.

* 2b. User overlays help message.
  * User focuses help message.

    Use case resumes at step 3.


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Stock Platform**: App platform that is used to trade with stocks
* **Index**: Number beside the contact's name that is currently displayed, used to specify which contact is to be modified/deleted
--------------------------------------------------------------------------------------------------------------------
## **Appendix: Planned Enhancements**

Team Size: 5

1. **Include shortcuts to make GUI scrollable without a mouse**: Currently, some components in FinClient can only be utilized or accessed via scrolling with a mouse. 
We plan to make it such that these components are able to be accessed via keyboard shortcuts that represents scrolling such as the arrow keys.
2. **Improve on add command accepted inputs**: Currently, the add command uses s/ prefix as a way to add in the stock platform used by the users. We plan to change it in a future update so that user who have clients that have names which contain 's/o' is allowed to be added as a contact with their full name.  
3. **Make the UI remain on found contacts after editing**: Currently, the UI will return to the full list of contacts after the user edits a contact. We plan to make it such that the UI will remain on the found contacts after the user edits a contact.
4. **Allowing tags to contain spacing**.  Currently, tags are not allowed to contain spacing. We plan to make it such that tags are allowed to contain spacing.
5. **Emails are not checked for validity**: Currently, the add and edit command does not check for the validity of the email. We plan to make it such that both of the commands will check for the validity of the email, such as whether they contain '.org' or '.com' etc .
6. **Prevent the acceptance of duplicate inputs for certain parameters**: Currently, duplicated inputs for certain parameters are not checked but are accepted, with only the last input being displayed. We plan to make it such that the duplicated inputs will be checked for so that the commands will not be accepted if there are duplicated inputs for certain parameters.




## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">

:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch.
   1. Download the jar file and copy into an empty folder.
   1. Open a terminal and navigate to the folder with the downloaded jar file.
   1. Run the jar file by typing the command `java -jar finclient.jar` into the terminal.
   1. Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.
<br><br>
1. Saving window preferences.
   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   1. Re-launch the app by typing the command `java -jar finclient.jar` into the terminal. <br>
       Expected: The most recent window size and location is retained.
<br><br>
1. Missing data on startup.
   1. Delete finclient.json located in /data/.
   2. Re-launch the app. <br>
       Expected: App should be repopulated with default values and work again.

### Adding a person

1. Adding a person.
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.
   2. Test case: ```add n/Alice Tan p/91234567 e/alice@example.com a/123 Wonderland Ave t/client c/AlphaCo j/Manager s/IBKR $/1000000```
   <br>Expected: The contact "Alice Tan" is added successfully, with details shown in the contact list and an updated status message.
   3. Test case: Use same command given in ii.
   <br>Expected: A duplicate error message is shown, and no new contact is added.
   4. Test case: `add n/Bob`
   <br>Expected: Error message indicating missing field
   5. Test case: `add e/bob@example.com a/Nowhere`
      <br>Expected: Error message indicating missing field

### Editing a person

1. Editing a person while all persons are being shown.
    1. Prerequisites: List all persons using the `list` command. There is at least 1 person in the list.
    2. Test case: `edit 1 e/newalice@example.com p/99887766`
    <br>Expected: The email and phone number for the first contact are updated accordingly.
    3. Test case: `edit 0 e/newalice@example.com p/99887766`<br>
    Expected: No person is edited. Error details shown in the status message.
    4. Other incorrect delete commands to try: `edit`, `edit x`, `...` (where x is larger than the list size).<br>
      Expected: Similar to previous.
<br><br>
2. Removing optional fields.
   1. Prerequisites: List all persons using the `list` command. There is at least 1 person with the optional fields in the contact.
   2. Test case: `edit 1 c/delete`
   <br>Expected: The company field for the first contact is removed.
   3. Test case: `edit 1 j/delete`
      <br>Expected: The job field for the first contact is removed.
   4. Test case: `edit 1 s/delete`
      <br>Expected: The stock platform field for the first contact is removed.
   5. Test case: `edit 1 $/delete`
      <br>Expected: The net worth field for the first contact is removed.
   6. Test case: `edit 1 r/`
      <br>Expected: The remark for the first contact is removed.
   7. Test case: `edit 1 t/`
      <br>Expected: All tags for the first contact is removed.
   8. Test case: `edit 1 r/ t/ c/delete j/delete s/delete $/delete`
   <br>Expected: All optional fields are removed.

### Deleting a person

1. Deleting a person while all persons are being shown.
   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.
   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. 
   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. 
   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size).<br>
      Expected: Similar to previous.

### Finding a person

1. Searching by name
   1. Prerequisite: One contact with the name "Alice Tan" exists in FinClient.
   2. Test case: `find Alice`<br>
       Expected: Contacts matching the search criteria are displayed. If no match is found, an empty list is shown.
   3. Test case: `find Tan Alice`<br>
       Expected: The contact "Alice Tan" is returned regardless of the keyword order.

### Listing all contacts

1. Listing all contacts
   1. Prerequisite: Have at least one contact in the FinClient
   2. Test case: `list`<br>
       Expected: All contacts are displayed.

### Hiding and revealing contacts

The syntax for both hiding and revealing contacts is analogous, so testers can use similar command formats to verify that the contacts’ sensitive details are either concealed or restored.


1. Hiding all contacts
    1. Test case: `hide all`<br>
      Expected: All contacts’ details (e.g., phone numbers, emails, addresses, etc.) should be hidden from the contact list view. Only minimal information (like names) is visible.
<br><br>

2. Hiding a specific contact by index
   1. Prerequisite: Have at least one contact in the FinClient
   2. Test case: `hide 1`<br>
       Expected: The contact at index 1 is hidden. The contact list should reflect this change.
<br><br>

3. Hiding contacts by matching name keywords
   1. Prerequisite: There should be contacts whose names include “alice” or “bob”.
   2. Test case: `hide alice bob`<br>
      Expected: All contacts with names matching the keyword "alice" or "bob" are hidden. The contact list should reflect this change.
<br><br>

4. Revealing all contacts
   1. Test case: `reveal all`<br>
      Expected: All hidden contacts’ details are revealed in the contact list view.
<br><br>

5. Revealing a specific contact by index 
   1. Prerequisite: Have at least two contacts in the FinClient and the second one is hidden.
   2. Test case: `reveal 2`<br>
      Expected: The contact at index 2 is revealed. The contact list should reflect this change.
<br><br>

6. Revealing contacts by matching name keywords
   1. Prerequisite: There should be contacts whose names include “charlie”.
   2. Test case: `reveal charlie`<br>
      Expected: All contacts with names matching the keyword "charlie" are revealed. The contact list should reflect this change.
<br><br>

### Limit Orders & Call Auction Calculator

This feature gathers all limit price orders to determine the call auction clearing price. Below are concrete examples for six contacts with corresponding order commands and an explanation of the expected outcome.

1. Sample Orders for 6 Contacts
   1. Prerequisite: Have at least six contacts in FinClient. Besides the six contacts, other contacts have no orders.
   2. Test case: 
      ```
      order 1 o/BUY am/100 at/10.00
      order 2 o/BUY am/150 at/10.00
      order 3 o/SELL am/120 at/10.20
      order 4 o/SELL am/130 at/10.00
      order 5 o/BUY am/80 at/10.10
      order 6 o/SELL am/100 at/9.90
      ```
        Expected: The orders are successfully added to the respective contacts. The call auction clearing price is calculated based on the aggregated orders.
   3. Expected Call Auction Clearing Price Calculation
      1. Aggregated BUY Orders:
        - Person 1: 100 shares at 10.00
        - Person 2: 150 shares at 10.00
        - Person 5: 80 shares at 10.10
        - Total Buy Volume: 330 shares (only orders with a limit price ≥ clearing price will be valid)
      2. Aggregated SELL Orders:
        - Person 3: 120 shares at 10.20 (only valid if the clearing price is ≥ 10.20, which may not be optimal)
        - Person 4: 130 shares at 10.00
        - Person 6: 100 shares at 9.90
        - Total Sell Volume (at clearing price = 10.00):
          - Person 4: 130 shares (10.00 ≤ 10.00)
          - Person 6: 100 shares (9.90 ≤ 10.00)
          - Total: 230 shares
      3. Clearing Price Determination:
        When evaluating candidate prices:
        - At 10.00:
          - Valid BUY orders: Persons 1, 2, and 5 (all with prices ≥ 10.00) contribute a total of 330 shares.
          - Valid SELL orders: Persons 4 and 6 (with prices ≤ 10.00) contribute a total of 230 shares.
          - Executed Volume: The trade can occur for min(330, 230) = 230 shares.
        - At other candidate prices (for example, 9.90 or 10.10), the executed volume is lower.
      4. Final Expected Outcome:
      The system should calculate the call auction clearing price as 10.00 with an executed trade volume of 230 shares. The status message should confirm that the order was generated successfully and reflect this clearing price calculation.

### Sorting Contacts

1. Sorting Contacts
   - Prerequisite: Have at least two contacts in FinClient.
   - Test case: `sort name`<br>
     Expected: Contacts are sorted alphabetically by name.
   - Test case: `sort networth`<br>
     Expected: Contacts are sorted according to their networth.

### Clearing all contacts

1. Clearing all contacts
   1. Prerequisite: Have at least one contact in the FinClient
   2. Test case: `clear`<br>
       Expected: All contacts are deleted, and a status message confirms the deletion.

### Saving data

1. Dealing with missing/corrupted data files.
   1. Prerequisite: Requires a copy of working finclient.json.
   1. Test case: Delete finclient.json. <br>
       Expected: App should be repopulated with a copy of sample data and work again, without any presence of old data and a fresh copy of finclient.json is saved.
   2. Test case: Use any `add/edit/delete/hide/reveal/order` <br>
       Expected: App should automatically save data upon use of the above commands.
