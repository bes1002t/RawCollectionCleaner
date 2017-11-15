package de.spn.main.scala

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
    val files: List[ExtendedFile] = getListOfFiles(dir)
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
    val targetPath: String = ExtendedFile.concatPaths(sourceFile.getParent, childDir)
    val targetFilePath: String = ExtendedFile.concatPaths(targetPath, sourceFile.fileNameWithExtension)

    return ExtendedFile.create(targetFilePath)
  }

  def moveFile(sourceFile: ExtendedFile, targetFile: ExtendedFile) {
    // first create dir if not exist
    if (!targetFile.parentDirExists) {
      targetFile.getParentFile.createNewFile
    }

    Files.move(sourceFile.toPath, targetFile.toPath, StandardCopyOption.ATOMIC_MOVE)
  }

  def getListOfFiles(sourceDir: String):List[ExtendedFile] = {
    val dir = ExtendedFile.create(sourceDir)
    if (dir.exists && dir.isDirectory) {
        return dir.listExtendedFiles.filter(_.isFile).toList
    } else {
        return List[ExtendedFile]()
    }
  }
}