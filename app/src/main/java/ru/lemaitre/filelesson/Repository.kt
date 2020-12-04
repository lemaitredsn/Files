package ru.lemaitre.filelesson


import android.content.Context
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.lemaitre.filelesson.data.Networking
import java.io.File

class Repository {

    private val myContext = MyApp.context

    private val sharePrefs by lazy {
        myContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private var state:String = "неизвестно"
    fun getState() = state

    suspend fun downloadFileExample(link: String) {
        //проверка есть ли такой же файл
        val savedFile = sharePrefs.all.keys.contains(link)
        Log.d("TAG", "saved=$savedFile link=$link")
        if (savedFile) {
            state = myContext.getString(R.string.file_exists)
            return
        }
        //имя файла timestamp_name.type
        val nameFile = toPatternNameFile(link)
        Log.d("TAG", "$nameFile")
        //скачиваем файл
        withContext(Dispatchers.IO) {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@withContext
            //указываем куда будем сохранять данные
            val testFolder =
                myContext.getExternalFilesDir("readmeFolderTest")
            val testFile = File(testFolder, nameFile)
            //сохраняем данные
            try {
                testFile.outputStream()
                    .use { fileOutputStream ->
                        Networking.api
                            .getFile(link)
                            .byteStream()
                            .use { inputStream ->
                                inputStream.copyTo(fileOutputStream)
                            }
                    }
                    state = myContext.getString(R.string.success_download, nameFile)
            } catch (t: Throwable) {
                //если ошибка файл удаляем
                testFile.delete()
                Log.d("TAG", "ошибка при загрузке, файл удален ", t)
            }
        }
    }

    suspend fun saveInSharedPreference(link: String) {
        withContext(Dispatchers.IO) {
            val textToSave = toPatternNameFile(link)
            sharePrefs.edit()
                .putString(link, textToSave)
                .apply()
        }
    }

    private suspend fun loadLinkFirstLaunch() {
        withContext(Dispatchers.IO) {
            try {
                val link = myContext.resources.assets.open("linkFolder/link.txt")
                    .bufferedReader()
                    .use { it.readLine() }
                Log.d("TAG", link)
                downloadFileExample(link)
                saveInSharedPreference(link)
            } catch (t: Throwable) {
                Log.d("TAG", "ошибка загрузки первого запуска ", t)
            }
        }
    }

    private suspend fun saveFirstLaunch() {
        withContext(Dispatchers.IO) {
            sharePrefs.edit()
                .putBoolean(KEY_FIRST_LAUNCH, true)
                .apply()
        }
    }

    fun checkFirstLaunch(){
        if(sharePrefs.contains(KEY_FIRST_LAUNCH)){
            Log.d("TAG", "существует значение по $KEY_FIRST_LAUNCH")
            //Ничего не делать
        }else {
            Log.d("TAG", "Не существует по ключу $KEY_FIRST_LAUNCH")
            CoroutineScope(Dispatchers.IO).launch {
                saveFirstLaunch()
                loadLinkFirstLaunch()
            }
         }
    }

    private fun toPatternNameFile(link: String): String {
        val index = link.lastIndexOf('/')
        val timeStamp = System.currentTimeMillis().toString()
        return timeStamp + "_" + link.substring(index + 1)
    }

    companion object {
        private const val SHARED_PREFERENCE_NAME = "saved_prefs" //имя файла
        const val KEY_FIRST_LAUNCH = "key_first_launch"
    }
}