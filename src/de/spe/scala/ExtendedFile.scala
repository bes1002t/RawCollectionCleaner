package de.spe.scala

import java.io.File

class ExtendedFile(file: File) {
  val (fileName: String, fileExtension: String) = {
    val nameParts     = file.getName.split(".").toList
    val fileName      = nameParts.dropRight(1).mkString(".")
    val fileExtension = "." + nameParts.takeRight(1).mkString(".")

    (fileName, fileExtension)
  }
  val fileNameWithExtension = fileName + fileExtension
  val parentDir             = file.getParent
  val absolutePath          = file.getAbsolutePath
  val path                  = file.toPath
}

object ExtendedFile {
  def create(file: File): ExtendedFile = new ExtendedFile(file)
}