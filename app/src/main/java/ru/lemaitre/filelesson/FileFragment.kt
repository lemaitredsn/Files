package ru.lemaitre.filelesson

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_storage.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lemaitre.filelesson.extensions.showToast

class FileFragment : Fragment(R.layout.fragment_storage) {


    private val viewModel: FileViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.checkOnFirstLaunch()
        viewModel.stateLiveData.observe(viewLifecycleOwner){state-> requireContext().showToast(state)}
/*        isFirstLaunch = viewModel.checkFirstLaunch()
        if(isFirstLaunch){
            viewModel.loadLinkFirstLaunch()
        }
        viewModel.saveFirstLaunch()*/

        storageBtn.setOnClickListener {
            val link = storageEt.text.toString()
            if (!link.isNullOrBlank()) {
                    viewModel.loadingLiveData.observe(viewLifecycleOwner, ::updateIsLoading)
                    viewModel.downloadFile(link)
            } else
                requireContext().showToast("Нет данных!")
        }
    }

    private fun updateIsLoading(isLoading: Boolean) {
        progressBar.isVisible = isLoading
        storageBtn.isEnabled = !isLoading
    }
}