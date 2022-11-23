package demo

import com.github.tototoshi.csv.*

import java.io.File
import scala.io.Source
import scala.io.StdIn.readLine

object Demo {
  def main(args: Array[String]): Unit = {

    trait person {
      val name: String
    }

    class Producer(val name: String) extends person
    class Writer(val name: String) extends person
    class Artist(val name: String) extends person


    class Song(val title: String, val description: String, val appear: String, val artist: String, val writer: String, val producer: String, val releaseDate: String, val streak: String, val position: String) {
      val songTitle = title
      val songDescription = description
      val songAlbum = appear
      val songArtist = new Artist(artist)
      val songWriter = new Writer(writer)
      val songProducer = new Producer(producer)
      val releasedDate = releaseDate
      //      val songStreak = streak
      //      val songMaxPosition = position
    }

    //Get absolute path from csv file
    val csvFilename = "CSV/dataset.csv"

    //Retrieve data from csv file
    val data = Source.fromFile(csvFilename).getLines.toList

    val allSong = new java.util.ArrayList[Song]

    //Print all lines of data
    for (i <- data) {
      // println(i)
      val cols = i.split(";")

      if (cols.length > 8) {
        //      val mySong = new Song(cols(0),cols(1),cols(2),cols(3),cols(4),cols(5),cols(6),cols(7).toInt,cols(8).toInt)
        val mySong = new Song(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6), cols(7), cols(8))
        allSong.add(mySong)
      } else {
        val mySong = new Song(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6), "", "")
        allSong.add(mySong)
      }
    }

    while(true) {
      val userChoice = readLine("Hurry up choose the query you want little bitch \n 1. Fuck you\n2. Fuck you\n3. Fuck you\n4. Fuck you\n5. Fuck you\n6. Fuck you\n7. Fuck you\n8. Fuck you\n9. Fuck you\n10. Fuck you\n");
      if (userChoice.toInt > 0 && userChoice.toInt < 10) {
        println("Your bad choice was " + userChoice.toString)
      } else {
        println("Select a number between 1 and 10 stupid wanier")
      }
    }


    val titles = data.filter({ i => i.charAt(0) == 'L' })
    //    for (i <- titles) {
    //      println(i)
    //    }


    //Using CSV reader
    //      val reader = CSVReader.open(new File(csvFilename))
    //      reader.foreach(fields => println(fields))
    //      reader.foreach(fields => println(fields))
    //      reader.close()

    //TODO
    // 1 get all song for a user


  }
}