package ru.lemaitre.filelesson

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

fun main() {
    inputStreamBuffered()
}

fun basicFile() {
    val file = File("C:\\Users\\Pustovalov's\\Desktop\\filename.txt")
    if (!file.exists()) {
        val result = file.createNewFile()
        println("file created =$result")
        println("file length =${file.length()}")
        println("file length =${file.absolutePath}")
        println("file length =${file.name}")

    } else {
        val result = file.delete()
        println("file deleted =$result")
    }
}

fun basicFolder() {
    val folder = File("C:\\Users\\Pustovalov's\\Desktop\\testFolder")
    folder.mkdir() //создать папку

    println("file.isFile = ${folder.isFile}") //проверка на файл
    println("file.isDirectory = ${folder.isDirectory}") //проверка на директорию

}

//относительный путь
fun relativePathExample() {
    val folder =
        File("testFolder\\innerfolder\\exampledir") //от текущей папки где находится проект будет создана папка
//    folder.mkdir() // - метод непозволяет создать несколько вложенных папок(File("testFolder\\innerfolder\\exampledir"))
    folder.mkdirs() // - позволяет сделать
    val file = File(
        folder,
        "filename.txt"
    ) //позволяет создать файл в относительной папке без указания абсолютного пути
    file.createNewFile()
    folder.listFiles()?.forEachIndexed { index, file ->
        println("file $index = ${file.absolutePath}")
    } //позволяет показать все файлы в папке
    println("folder length =${folder.absolutePath}")
}

//работа со stream out/in

fun outputStreamExample() {
    val folder = File("testFolder\\innerfolder\\exampledir")
    folder.mkdirs()
    val file = File(folder, "filename.txt")
    file.createNewFile()

/*    var outputStream: FileOutputStream? = null
    try {
        outputStream = file.outputStream()
        outputStream.write("Test string".toByteArray())
    } catch (exception: IOException) {

    } finally {
        try {
            outputStream?.close()
        } catch (e: Exception) {

        }
    }*/ //так работат неудобно поэтому в котлине есть экстенш метод
    try {
        val outputStream = file.outputStream()
        outputStream.use {
            it.write("Test string".toByteArray())//после окончния поток будет закрыт
        }
    } catch (t: Throwable) {
    }

}

fun outPutBufferedExample() {

    val folder = File("testFolder\\innerfolder\\exampledir")
    folder.mkdirs()
    val file = File(folder, "filename.txt")
    file.createNewFile()
    try {
        val start = System.currentTimeMillis()
        file.outputStream().buffered().use { stream -> //buffered позволяет выполнять код и накапливать данные в буфер при заполнении в буфер
            //данные записываются по умолчанию буфер равен 8кб
            (0..100_000).forEach {
                stream.write("$it\n".toByteArray())
            }
        }
        println("time = ${System.currentTimeMillis()- start}")
    }catch (t:Throwable){}
}

fun inputStreamBuffered(){
    val folder = File("testFolder\\innerfolder\\exampledir")
    folder.mkdirs()
    val file = File(folder, "filename.txt")
   try {
       val text = file.inputStream()
           .bufferedReader()
           .use {
               it.readText()
           }
       println(text)
   }catch (t:Throwable){}
}
