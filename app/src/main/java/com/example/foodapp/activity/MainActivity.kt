package com.example.foodapp.activity

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.foodapp.R
import com.example.foodapp.database.MealDatabase
import com.example.foodapp.databinding.ActivityMainBinding
import com.example.foodapp.viewmodel.HomeViewModel
import com.example.foodapp.viewmodel.factory.HomeViewModelFactory
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var sourceLanguageCode = "en"
    private var targetLanguageCode = "vi"
    private lateinit var translatorOptions : TranslatorOptions
    var connectivity : ConnectivityManager? = null
    var info : NetworkInfo? = null
    private lateinit var  translator : Translator
     val viewModel : HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController= navHostFragment.navController
        binding.botomNav.setupWithNavController(navController)
        checkConnectivity()
    }

     fun translationLanguage(sourceLanguageText : String, textView: TextView) {
        //set up option translation
        translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode)
            .setTargetLanguage(targetLanguageCode)
            .build()
        translator = Translation.getClient(translatorOptions)
        val downloadConditions  = DownloadConditions.Builder()
            .requireWifi()
            .build()
        /// process translation text
        translator.downloadModelIfNeeded(downloadConditions).addOnSuccessListener {
            translator.translate(sourceLanguageText).addOnSuccessListener {translatedText ->
                textView.text = translatedText
                Log.d("translatedata",sourceLanguageText + "--------------------------------------" + translatedText)
            }.addOnFailureListener {
                showToast("${it.message}")
            }
        }.addOnFailureListener {
            showToast("${it.message}")
        }
    }
    fun translationLanguageCollap(sourceLanguageText : String, view: CollapsingToolbarLayout) {
        //set up option translation
        translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode)
            .setTargetLanguage(targetLanguageCode)
            .build()
        translator = Translation.getClient(translatorOptions)
        val downloadConditions  = DownloadConditions.Builder()
            .requireWifi()
            .build()
        /// process translation text
        translator.downloadModelIfNeeded(downloadConditions).addOnSuccessListener {
            translator.translate(sourceLanguageText).addOnSuccessListener {translatedText ->
                view.title = translatedText
            }.addOnFailureListener {
                showToast("${it.message}")
            }
        }.addOnFailureListener {
            showToast("${it.message}")
        }
    }

    private fun checkConnectivity() {
        val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = manager.activeNetworkInfo
        if (null == activeNetwork) {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Make sure that WI-FI or mobile data is turned on, then try again")
                .setCancelable(false)
                .setPositiveButton("Retry") { dialog, id ->
                    recreate()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    finish()
                }
            val alert = dialogBuilder.create()
            alert.setTitle("No Internet Connection")
            alert.show()
        }
    }
    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}