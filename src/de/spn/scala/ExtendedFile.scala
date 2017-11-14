package de.spn.scala

import java.io.File

class ExtendedFile(path: String) extends File(path) {
  private val nameParts: List[String] = getName.split(".").toList
  val fileName: String                = nameParts.dropRight(1).mkString(".")
  val fileExtension: Option[String]   = {
    if (isFile) {
      Some("." + nameParts.takeRight(1).mkString(""))
    } else {
      None
    }
  }
  val fileNameWithExtension = fileName + fileExtension

  def parentDirExists: Boolean = {
     return getParentFile.exists
  }

  def listExtendedFiles: List[ExtendedFile] = {
    val files = listFiles map{ file: File => ExtendedFile.create(file.getAbsolutePath) }
    return files.toList
  }
}

object ExtendedFile {
  def create(path: String): ExtendedFile = new ExtendedFile(path)

  def concatPaths(path1: String, path2: String): String = {
    def charIsSeperator(char: String): Boolean = {
      return char == "/" || char == "\\"
    }

    val path1LastChar: String  = path1.charAt(path1.length).toString
    val path2FirstChar: String = path2.charAt(0).toString
    if (charIsSeperator(path1LastChar) || charIsSeperator(path2FirstChar)) {
      return path1 + path2
    } else {
      return path1 + File.separator + path2
    }
  }
}