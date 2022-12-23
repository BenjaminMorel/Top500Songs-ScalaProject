# Top500Songs-ScalaProject

This project is a Scala application that compiles a list of the top 500 songs based on a CSV file that has been provided. This project was realised during the 635_1_Scala course by Hugo Vouillamoz and Benjamin Morel.

### Description

We implemented different functions that the user can call using the run prompt. When you run the demo.Demo.scala class you have the possibility to chose between 1 to 13, depending on what you want to do:

```
1) All songs made by a singer / group
2) Sort the list by streak size
3) Print Basic information about each song
4) Group by artist name
5) Get all songs that went out after 2000
6) Get information about length of each song (sentences, words and char)
7) Get the number of song made by each artist / group
8) Find a word in the text of one of the song
9) Find song that have been sung and written by the same person
10) Add a new song to the list
11) Edit the streak of a song
12) Remove song from the list (based on the title)
13) Play multiple songs
Type quit to finish the program
```

### SetupInstallation requirements
- java/jdk installed
- scala with sbt
- imports: 
  - scala.concurrent.Future
  - concurrent.ExecutionContext.Implicits.global
  - scala.io.AnsiColor.*
  - scala.io.Source
  - scala.io.StdIn.readLine
  - scala.util.Random
