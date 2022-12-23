package demo

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.io.AnsiColor.*
import scala.io.Source
import scala.io.StdIn.readLine
import scala.util.Random

object Demo {
  trait person {
    val name: String
  }

  class Producer(val name: String) extends person

  class Writer(val name: String) extends person

  class Artist(val name: String) extends person

  class Song(val title: String, val description: String, val appear: String, val artist: String, val writer: String, val producer: String, val releaseDate: String, val streak: Int, val position: String) {
    val songTitle: String = title
    val songDescription: String = description
    val songAlbum: String = appear
    val songArtist = new Artist(artist)
    val songWriter = new Writer(writer)
    val songProducer = new Producer(producer)
    val releasedDate: String = releaseDate
    val songStreak: Int = streak
    var songMaxPosition: String = position
  }

  class summary(val title: String, val artistName: String) {
    val songTitle: String = title
    val songArtist = new Artist(artistName)
  }

  def main(args: Array[String]): Unit = {

    // Get absolute path from csv file
    val csvFilename = "CSV/dataset.csv"

    // Retrieve data from csv file
    val data = Source.fromFile(csvFilename).getLines.toList
    val columnNames = data.head.split(";")

    // Map the columns with the CSV
    var allSong = data.map(s => {
      new Song(s.split(";")(columnNames.indexOf("Title")),
        s.split(";")(columnNames.indexOf("Description")),
        s.split(";")(columnNames.indexOf("Appears on")),
        s.split(";")(columnNames.indexOf("Artist")),
        s.split(";")(columnNames.indexOf("Writers")),
        s.split(";")(columnNames.indexOf("Producer")),
        s.split(";")(columnNames.indexOf("Released")),
        0,
        ""
      )
    }
    )
    // Call the user menu
    panel(allSong)
  }

  /**
   * Panel that displays all possible actions for the user
   *
   * @param allSong list of songs
   */
  def panel(allSong: List[Song]): Unit = {

    var continue = true;
    var tmpList = allSong

    // Get the user choice and list all possible actions
    val userChoice = readLine(s"\n\n\n${BOLD}Choose the action that you want to execute:\n${RESET}" +
      "1) All songs made by a singer / group\n" +
      "2) Sort the list by streak size\n" +
      "3) Print Basic information about each song\n" +
      "4) Group by artist name\n" +
      "5) Get all songs that went out after 2000\n" +
      "6) Get information about length of each song (sentences, words and char)\n" +
      "7) Get the number of song made by each artist / group\n" +
      "8) Find a word in the text of one of the song\n" +
      "9) Find song that have been sung and written by the same person\n" +
      "10) Add a new song to the list\n" +
      "11) Edit the streak of a song\n" +
      "12) Remove song from the list (based on the title)\n" +
      "13) Play multiple song\n" +
      s"Type quit to finish the program\n"
    )

    // Match case
    userChoice match {
      case "1" => getAllSongForAnArtist(allSong)
      case "2" => sortByStreakSize(allSong)
      case "3" => printBasicInfo(allSong)
      case "4" => groupSongByArtist(allSong)
      case "5" => sortSongAfterADate(allSong)
      case "6" => getSizeOfEachSong(allSong)
      case "7" => numberOfSongPerArtist(allSong)
      case "8" => findWordInDescription(allSong)
      case "9" => findSongWithSameArtistAndWriter(allSong)
      case "10" => tmpList = addNewSong(allSong)
      case "11" => tmpList = editBestPosition(allSong)
      case "12" => tmpList = removeSongByTitle(allSong)
      case "13" => playMultipleSong(allSong)
      case "quit" => continue = false
      case "Quit" => continue = false
      case _ => println(s"${RED}Chose an option between 1 and 10${RESET}")
    }

    // Recursive function
    if (continue) {
      panel(tmpList)
    }
  }

  /**
   *
   * @param allSong
   */
  def playASong(song: Song) = {

    //simulate the duration of the song
    var random = new Random()
    println("Start play " + song.songTitle)
    // Simulate the play of the song
    Thread.sleep(random.nextInt())
  }

  /**
   *
   * @param allSong
   */
  def playMultipleSong(allSong: List[Song]): Unit = {

    // create the 4 song that will be played together
    val rand = new Random()
    val s1 = allSong(rand.nextInt(allSong.length))
    val s2 = allSong(rand.nextInt(allSong.length))
    val s3 = allSong(rand.nextInt(allSong.length))
    val s4 = allSong(rand.nextInt(allSong.length))

    // play all song at the same time and wait until they all finish
    val t1 = Future(playASong(s1))
    val t2 = Future(playASong(s2))
    val t3 = Future(playASong(s3))
    val t4 = Future(playASong(s4))

    // wait until all song finish
    Thread.sleep(10000L)

  }

  /**
   * 1) Get and print all songs that an artist has made
   *
   * @param allSong list of songs
   */
  def getAllSongForAnArtist(allSong: List[Song]): Unit = {
    // Get the artist name from the user
    val artistName = readLine("Write an artist name: ")
    // Filter the list into a new val
    val artistSongs = allSong.filter(s => s.songArtist.name.equals(artistName))
    if (artistSongs.nonEmpty)
    // Print all songs in the console
      artistSongs.foreach(s => println(s"${BLUE}" + s.songTitle + s"${RESET}"))
    else
      println(s"${RED}No sound found for artist: " + artistName + s"${RESET}")
  }

  /**
   * 2) Print all songs sorted by streak size
   *
   * @param allSong list of songs
   */
  def sortByStreakSize(allSong: List[Song]): Unit = {
    // Sort the list by streak value
    val sortedList = allSong.sortBy(s => s.streak).reverse
    // Print the songs sorted
    sortedList.foreach(s => println(s"${BLUE}" + s.songTitle + " | " + s.streak + s"${RESET}"))
  }

  /**
   * 3) Create a new object called summary and display the information to the user
   *
   * @param allSong list of songs
   */
  def printBasicInfo(allSong: List[Song]): Unit = {
    // Create new object with map function
    // Use only the song title and the artist name to create a "summary" of the song
    val allBasicInfo = allSong.map(s =>
      new summary(s.songTitle, s.artist))
    // Print the basic infos of the first 10 songs
    for (i <- 1 to 10) {
      printInfo(allBasicInfo(i))
    }
  }

  /**
   * 4) Group the list based on the artist that made the song and print the given list
   *
   * @param allSong list of songs
   */
  def groupSongByArtist(allSong: List[Song]): Unit = {
    // Get the song title and artist name created previously with the summary object
    val defaultInfo = getBasicInfo(allSong)
    // Sort the songs by artist name
    val sortedList = defaultInfo.sortBy(_.artistName)
    // Print the result
    sortedList.foreach(s => {
      println(s"${BLUE}" + s.artistName + " | " + s.title + s"${RESET}")
    })
  }


  /**
   * 5) Get and print all songs that went out before a certain date
   *
   * @param allSong list of songs
   */
  def sortSongAfterADate(allSong: List[Song]): Unit = {
    // Filter the list and take only songs appeared after 1990
    val songsFiltered = allSong.filter(s => try {
      s.releaseDate.substring(s.releaseDate.length - 4, s.releaseDate.length).toInt > 2000
    }
      // Catch bad format
    catch {
      case e: NumberFormatException => false
    })

    // Print the songs
    songsFiltered.foreach(s => println(s"${BLUE}" + s.songTitle + " was released after 2000 ( "  + s.releaseDate +  ") " +s"${RESET}"))
  }

  /**
   * 6) Get the size with the number of sentences - words and char of a list of songs and print the infos
   *
   * @param allSong list of songs
   */
  def getSizeOfEachSong(allSong: List[Song]): Unit = {
    // Generate a random numbers between 0 and list length
    val random = Random.nextInt(allSong.length)
    // Use this random to get element of the list of songs
    val selectedSong = allSong(random)
    // Calculate number of char
    val numberOfChar = selectedSong.description.length
    // Calculate number of words
    val allWords = selectedSong.description.split(" ")
    val numberOfWord = allWords.length
    // Calculate number of sentences
    val allSentence = selectedSong.description.split(".")
    val numberOfSentence = allSentence.length
    // Print the result with song title and number of sentences, word and char
    println(s"${BLUE}The description of the song " + selectedSong.songTitle + " has " + numberOfSentence + " sentences, " + numberOfWord + " words, " + numberOfChar + s" characters${RESET}")
  }

  /**
   * 7) Count the number of song that each group has made and print it
   *
   * @param allSong list of songs
   */
  def numberOfSongPerArtist(allSong: List[Song]): Unit = {
    var count = 1

    var sortedlist = allSong.sortBy(_.songArtist.name)
    // Take element 0
    var artistName = sortedlist.head.songArtist.name

    // Count number of songs for the artist
    for (i <- 1 until sortedlist.length) {
      if (artistName.equals(sortedlist(i).songArtist.name)) {
        count += 1
      } else {
        // Print the result
        println(s"${BLUE}" + artistName + " has made " + count + s" song${RESET}")
        // Sort the list by artist name
        artistName = sortedlist(i).songArtist.name
        count = 1
      }
    }
  }

  /**
   * 8) Find a song that has the word as an input
   *
   * @param allSong list of songs
   */
  def findWordInDescription(allSong: List[Song]): Unit = {
    // Ask user to give a word
    val wordToFind = readLine("Input the word you want to find: ")
    // Filter the list with the given word
    allSong.foreach(s => {
      if (s.songDescription.contains(wordToFind)) {
        // Print result
        println(s"${BLUE}" + s.songTitle + " has the word you search \n" + s.songDescription + s"${RESET}")
      }
    })
  }

  /**
   * 9) Find and print a song that has the same artist and writer name
   *
   * @param allSong list of songs
   */
  def findSongWithSameArtistAndWriter(allSong: List[Song]): Unit = {
    for (elem <- allSong) {
      // Test if the artist name is the same than the writer name
      if (elem.songArtist.name.equals(elem.songWriter.name)) {
        // If yes print the result
        println(s"${BLUE}" + elem.songTitle + " was sung and written by " + elem.songArtist.name + s"${RESET}")
      }
    }
  }

  /**
   * 10) Add a new song in the list of songs
   *
   * @param allSong list of songs
   * @return updated list of songs, with the added Song
   */
  def addNewSong(allSong: List[Song]): List[Song] = {
    // Ask the user to enter information about the song he/she wants to create
    val songTitle = readLine("Input song title: ")
    val songDescription = readLine("Input song description: ")
    val songAlbum = readLine("Input song album: ")
    val songArtist = readLine("Input artist name: ")
    val songWriter = readLine("Input writer name: ")
    val songProducer = readLine("Input producer name: ")
    val releasedDate = readLine("Input released date: ")
    val songStreak = readLine("Input streak: ").toInt
    val songMaxPosition = readLine("Input song max position: ")
    // Create new object with values
    val newSong = Song(songTitle, songDescription, songAlbum, songArtist, songWriter, songProducer, releasedDate, songStreak, songMaxPosition)
    // Print the result
    println(s"${BLUE}New song " + songTitle + s" added${RESET}")
    // Add the new song to the list
    allSong ::: newSong :: Nil
  }

  /**
   * 11) Edit the streak of a Song
   *
   * @param allSong list of all songs
   * @return updated list of all songs with new values for the Song that was edited
   */
  def editBestPosition(allSong: List[Song]): List[Song] = {
    val songToEdit = readLine("What is the name of the song: ")
    val bestPosition = readLine("What is the new position: ")

    allSong.foreach(s => {
      if (s.songTitle.equals(songToEdit)) {
        // Store old position to print it later
        val oldPosition = s.songMaxPosition
        // Change the position
        s.songMaxPosition = bestPosition
        // Print the result from old position to the new one
        println(s"${BLUE}Song changed position from " + oldPosition + " to " + bestPosition + s"${RESET}")
      }
    })
    // Change the list of songs with new values
    return allSong
    //    allSong ::: newSong :: Nil
  }

  /**
   * 12) Remove a song
   *
   * @param allSong list of all songs
   * @return updated list of all songs, without the removed Song
   */
  def removeSongByTitle(allSong: List[Song]): List[Song] = {
    val songTitle = readLine("Insert the title of the song you want to remove : ")
    var tempList = allSong.filter(s => !s.songTitle.equals(songTitle))
    println(s"${BLUE}The song " + songTitle + s" has been removed${RESET}")
    return tempList
  }

  /**
   * Get and print all song that a producer has produce
   *
   * @param allSong list of songs
   */
  def getAllSongForAProducer(allSong: List[Song]): Unit = {
    val producerName = readLine("Input name of the producer: ")
    allSong.filter(s => s.songProducer.name.equals(producerName))
    allSong.foreach(s => println(s.songTitle))
  }


  /**
   * Create the list made of summarize instead of full song
   */
  def getBasicInfo(allSong: List[Song]): List[summary] = {
    val allBasicInfo = allSong.map(s =>
      new summary(s.songTitle, s.artist))
    return allBasicInfo;
  }

  /**
   * Function used to print information
   */
  def printInfo(sum: summary): Unit = {
    println(s"${BLUE}" + sum.title + " was made by " + sum.artistName + s"${RESET}")
  }


}