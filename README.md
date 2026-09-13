# Project-1
# 🎯 Number Guessing Game with Timer & Difficulty Levels – Java

## 📌 Project Overview

**Number Guessing Game** is an interactive console-based Java application developed to demonstrate fundamental Java programming concepts in a simple and enjoyable way. The game allows the player to enter their name, select a difficulty level, and try to guess a randomly generated number within a limited number of attempts and a specific time limit.

The project uses Java's `Random` class to generate the secret number and the `Scanner` class to accept input from the player. Based on the selected difficulty, the game generates numbers from different ranges and provides different time limits.

The game also provides helpful hints after every incorrect guess, such as **"Too Low!"** or **"Too High!"**, helping the player move closer to the correct answer.

---

## 🎮 Game Features

### 👤 Player Name

At the beginning of the game, the player enters their name. The name is displayed during the game and when the player successfully guesses the number.

### 🎚️ Difficulty Levels

The game provides three difficulty levels:

* **Easy:** Guess a number between **1 and 100** with a **60-second** time limit.
* **Medium:** Guess a number between **100 and 200** with a **45-second** time limit.
* **Hard:** Guess a number between **300 and 400** with a **30-second** time limit.

### 🔢 Random Number Generation

A random number is automatically generated according to the selected difficulty level. The player must identify the randomly generated number.

### ❤️ Multiple Attempts

The player receives a maximum of **10 chances** to guess the correct number.

The game displays the current attempt, for example:

```text
Chance 3/10
```

It also displays the number of remaining chances after an incorrect guess.

### ⏱️ Time Limit

Each difficulty level has a different time limit. The program uses Java's `System.currentTimeMillis()` to calculate the elapsed time and determine the remaining time.

If the player exceeds the time limit, the game ends automatically.

### 💡 Guessing Hints

After every incorrect guess, the game provides a hint:

```text
Too Low!
Try a higher number.
```

or

```text
Too High!
Try a lower number.
```

These hints help the player improve their next guess.

### 🏆 Winning Message

When the player guesses the correct number, the program displays:

* Congratulations message
* Number of attempts used
* Time used to guess the number

### 🔄 Play Again

After completing a game, the player can choose whether to play another round.

```text
Do you want to play again?
1. Yes
2. No
```

### ❌ Game Over

If the player uses all 10 attempts or the time limit expires, the game displays the correct number and ends the current round.

---

## 🛠️ Technologies Used

| Technology                     | Purpose                                |
| ------------------------------ | -------------------------------------- |
| **Java**                       | Main programming language              |
| **Scanner**                    | Taking user input                      |
| **Random**                     | Generating random numbers              |
| **System.currentTimeMillis()** | Calculating game time                  |
| **if-else**                    | Difficulty and guessing decisions      |
| **while loop**                 | Managing attempts                      |
| **do-while loop**              | Playing multiple rounds                |
| **Boolean**                    | Tracking whether the answer is correct |

---

## 🧠 Java Concepts Demonstrated

This project demonstrates several important Java programming concepts:

* Variables and data types
* User input using `Scanner`
* Random number generation
* Conditional statements
* `if`, `else if`, and `else`
* `while` loop
* `do-while` loop
* Boolean variables
* Arithmetic and comparison operators
* Increment operators
* Methods from Java library classes
* Time calculation
* Break and continue statements
* String concatenation
* Basic exception-free input handling

---

## 🔄 Game Flow

```text
Start Game
    ↓
Enter Player Name
    ↓
Choose Difficulty
    ↓
Generate Random Number
    ↓
Set Attempts & Time Limit
    ↓
Start Timer
    ↓
Enter Guess
    ↓
Check Time
    ↓
Compare Guess with Random Number
    ↓
 ┌───────────────┬─────────────────┐
 ↓               ↓                 ↓
Correct        Too Low          Too High
 ↓               ↓                 ↓
Win          Give Hint          Give Hint
 ↓               └────────┬────────┘
 ↓                        ↓
Play Again? ←──── Continue Guessing
 ↓
Yes / No
 ↓
End Game
```

---

## 🖥️ Example Output

```text
Enter the Name: Himani

================================
      Number Guessing Game
================================

Enter Your Name: Himani

Choose Difficulty:
1. Easy
2. Medium
3. Hard

Enter the choice: 1

Easy Level
Guess the Number between 1 to 100

You have 10 chances.
You have 60 seconds.

----------------------------
Time Remaining: 60 seconds
Chance 1/10

Enter The Number: 40

Too Low!
Try a higher number.

Chances remaining: 9

----------------------------
Time Remaining: 52 seconds
Chance 2/10

Enter The Number: 70

Too High!
Try a lower number.

Chances remaining: 8

----------------------------
Time Remaining: 45 seconds
Chance 3/10

Enter The Number: 55

Congratulations Himani!
You guessed the correct number.
Number of attempts: 3
Time used: 15 seconds
```

---

## 📂 Project Structure

```text
Number-Guessing-Game/
│
├── game.java
├── README.md
└── screenshots/
    └── output.png
```

---

## ▶️ How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/[your-username]/[your-repository].git
```

### 2. Open the Project

Open the project in:

* VS Code
* IntelliJ IDEA
* Eclipse
* NetBeans

### 3. Compile the Program

```bash
javac game.java
```

### 4. Run the Program

```bash
java game
```

### 5. Play the Game

Enter your name, select a difficulty level, and start guessing the randomly generated number.

---

## 🎯 Project Objectives

The main objectives of this project are:

1. To develop a simple interactive Java application.
2. To understand user input using the `Scanner` class.
3. To practice conditional statements and loops.
4. To understand random number generation.
5. To implement a timer-based programming concept.
6. To practice problem-solving and logical thinking.
7. To create an interactive console-based game.
8. To understand how multiple Java concepts work together in one project.

---

## 🌟 Advantages

* Simple and easy to use.
* Beginner-friendly Java project.
* Includes three difficulty levels.
* Provides multiple attempts.
* Includes a time limit.
* Gives hints after incorrect guesses.
* Allows the player to play multiple rounds.
* Demonstrates important Java programming concepts.

---

## 🚀 Future Enhancements

The project can be improved in the future by adding:

* 🏆 Score calculation
* 🥇 High-score system
* 📊 Player statistics
* 👥 Multiplayer mode
* 🎨 Graphical User Interface (GUI)
* 🔊 Sound effects
* 💾 Save game history
* 🌐 Online leaderboard
* 🔐 Player login system
* 📱 Mobile version

---

## ⚠️ Limitations

* The current version is console-based.
* Player scores are not permanently stored.
* There is no graphical interface.
* The game supports one player at a time.
* User input is expected to be numeric when a number is requested.

---

## 🏅 What I'm Most Proud Of

I am proud of developing this project because it helped me understand how different Java programming concepts can be combined to create a complete interactive application.

The most interesting part of the project is the combination of **random number generation, difficulty levels, multiple attempts, guessing hints, and a time limit**. This project improved my logical thinking and gave me practical experience in Java programming.

---

## 👨‍💻 Author

**[Your Name]**

Java Programming Project
B.Sc. IT Student

---

## 📜 License

This project is created for **educational and learning purposes**.

