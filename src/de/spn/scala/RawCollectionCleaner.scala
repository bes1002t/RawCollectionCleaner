package de.spn.scala

import java.io.File
import java.nio.file.{Files, Path, StandardCopyOption}

object RawCollectionCleaner {
  val preparedJpgDir = "prepared"
  val rawsToCleanDir = "trash"
  
  def main(args: Array[String]) = {
    if (args.length == 0) {
      println("Please enter the directory path!")
    } else {
      getListOfRaws(args(0))
    }
  }

  def getListOfRaws(dir: String): Unit = {
    val files: List[ExtendedFile] = getListOfFiles(dir).map{ file: File => ExtendedFile.create(file) }
    val raws:  List[ExtendedFile] = files filter{ _.fileExtension == ".ARW" }
    val jpgs:  List[ExtendedFile] = files filter{ _.fileExtension == ".JPG" }

    for (raw <- raws) {
      val condition = jpgs exists{ jpg: ExtendedFile => jpg.fileName == raw.fileName }
      if (!condition) {
        val targetRaw = createChildTarget(raw, rawsToCleanDir)
        moveFile(raw, targetRaw)
      }
    }

    for (jpg <- jpgs) {
      val condition = raws exists{ raw: ExtendedFile => raw.fileName == jpg.fileName }
      if (!condition) {
        val targetJpg = createChildTarget(jpg, preparedJpgDir)
        moveFile(jpg, targetJpg)
      }
    }
  }

  def createChildTarget(sourceFile: ExtendedFile, childDir: String): ExtendedFile = {
    val targetPath: String = childDir + "/" + sourceFile.fileNameWithExtension
    val targetFile: File   = new File(sourceFile.absolutePath, targetPath)

    return ExtendedFile.create(targetFile)
  }

  def moveFile(sourceFile: ExtendedFile, targetFile: ExtendedFile) {
    Files.move(sourceFile.path, targetFile.path, StandardCopyOption.ATOMIC_MOVE)
  }

  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
        return d.listFiles.filter(_.isFile).toList
    } else {
        return List[File]()
    }
  }
}