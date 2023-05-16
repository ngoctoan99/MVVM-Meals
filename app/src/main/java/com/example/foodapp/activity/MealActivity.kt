package com.example.foodapp.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.database.MealDatabase
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.fragment.HomeFragment
import com.example.foodapp.model.Meal
import com.example.foodapp.viewmodel.MealViewModel
import com.example.foodapp.viewmodel.factory.MealViewModelFactory
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener


class MealActivity : AppCompatActivity()  {
    private val TAG: String = "toantest"
    private lateinit var binding : ActivityMealBinding
    private lateinit var mealId : String
    private lateinit var mealName : String
    private lateinit var mealThumb : String
    private var mealSave : String=""
    private lateinit var youtubeLink : String
    private lateinit var description : String
    private lateinit var mealViewModel : MealViewModel
    private  var ingredientString : String = "\r"
    private var mealToSave:Meal?=null
    private var  count = 0;
    private lateinit var main : MainActivity
    private lateinit var gestureScanner : GestureDetector
    private var countYoutube :Int = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealDatabase  =MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]
        main = MainActivity()
        getMealInformationFromIntent()
        loadingCase()
        setInformationInViews()
        mealViewModel.getDetail(mealId)
        observerMealDetailLiveData()
        setDataIngredient()
        actionClick()
        setUpPlayerVideo(this)
    }

    private fun setUpPlayerVideo(context : Context) {
        lifecycle.addObserver(binding.youtubePlayer)
        binding.youtubePlayer.getPlayerUiController()
        binding.youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // loading the selected video into the YouTube Player
                if(youtubeLink.isNotEmpty() ){
                    val stringArray = youtubeLink.split("=")
                    youTubePlayer.loadVideo(stringArray[1], 0F)

                    /// pause video when start
                    youTubePlayer.pause()

                    /// action click button video
                    onYoutubeImageClick(youTubePlayer)
                }else {
                    binding.btnVideo.setOnClickListener{
                        Toast.makeText(context, "Have error about this video", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                // this method is called if video has ended,
                super.onStateChange(youTubePlayer, state)
            }
        })
    }

    private fun checkSave() {
        if (mealSave == "saved"){
            binding.btnAddFavorite.visibility = View.INVISIBLE
        }else {
            binding.btnAddFavorite.visibility = View.VISIBLE
        }
    }
    // action click two button
    private fun actionClick() {
        onFavoriteClick()
        onClickTranslate()
    }

    private fun onClickTranslate() {

       binding.btnTranslate.setOnClickListener{
           Log.d("datatranslate",ingredientString)
           count ++
           if(count % 2 != 0){
               main.translationLanguage(binding.tvCategory.text.toString().trim(),binding.tvCategory)
               main.translationLanguage(binding.tvArea.text.toString().trim(),binding.tvArea)
               main.translationLanguage(binding.tv1.text.toString().trim(),binding.tv1)
               main.translationLanguage(binding.tv2.text.toString().trim(),binding.tv2)
               main.translationLanguage(ingredientString,binding.tvDetailIngredient)
               main.translationLanguage(description,binding.tvDescription)
               main.translationLanguageCollap(binding.collapsingToolbar.title.toString().trim(),binding.collapsingToolbar)
           }else {
               binding.tvDetailIngredient.text = ""
               setDataIngredient()
               observerMealDetailLiveData()
               binding.tv1.text = "Ingredient :"
               binding.tv2.text = "Instruction :"
               setInformationInViews()
           }
       }
    }

    // get data ingredient to arrange it to display
    private fun setDataIngredient() {
        // array ingredient item
       mealViewModel.observerMealDetailLiveData().observe(this){ mealToSave->
           ingredientString = " "
           val arrayIngredient = arrayOf<String>("${mealToSave?.strIngredient1.toString()} : ${mealToSave?.strMeasure1.toString()}",
               "${mealToSave?.strIngredient2} : ${mealToSave?.strMeasure2}","${mealToSave?.strIngredient3} : ${mealToSave?.strMeasure3}",
               "${mealToSave?.strIngredient4} : ${mealToSave?.strMeasure4}","${mealToSave?.strIngredient5} : ${mealToSave?.strMeasure5}",
               "${mealToSave?.strIngredient6} : ${mealToSave?.strMeasure6}","${mealToSave?.strIngredient7} : ${mealToSave?.strMeasure7}",
               "${mealToSave?.strIngredient8} : ${mealToSave?.strMeasure8}","${mealToSave?.strIngredient9} : ${mealToSave?.strMeasure9}",
               "${mealToSave?.strIngredient10} : ${mealToSave?.strMeasure10}","${mealToSave?.strIngredient11} : ${mealToSave?.strMeasure11}",
               "${mealToSave?.strIngredient12} : ${mealToSave?.strMeasure12}","${mealToSave?.strIngredient13} : ${mealToSave?.strMeasure13}",
               "${mealToSave?.strIngredient14} : ${mealToSave?.strMeasure14}", "${mealToSave?.strIngredient15} : ${mealToSave?.strMeasure15}",
               "${mealToSave?.strIngredient16} : ${mealToSave?.strMeasure16}","${mealToSave?.strIngredient17} : ${mealToSave?.strMeasure17}",
               "${mealToSave?.strIngredient18} : ${mealToSave?.strMeasure18}","${mealToSave?.strIngredient19} : ${mealToSave?.strMeasure19}",
               "${mealToSave?.strIngredient20} : ${mealToSave?.strMeasure20}")
           for (i in 0..19){
               if(arrayIngredient[i].contains("null")|| arrayIngredient[i] == " : " ||arrayIngredient[i] == "  :  " ||arrayIngredient[i]== " :  "){
                   ingredientString +=""
               }else {
                   ingredientString  = ingredientString +" - "+ arrayIngredient[i] + " \r\n\r"
               }
           }
           Log.d("dataingradient",ingredientString.toString())
           Log.d("datanew",mealToSave?.strMeal.toString())
           binding.tvDetailIngredient.text = ingredientString
       }

    }

    private fun onFavoriteClick() {
        binding.btnAddFavorite.setOnClickListener {
            mealToSave?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(this,"Meal Save",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick(youTubePlayer: YouTubePlayer) {

        binding.btnVideo.setOnClickListener {
//               val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
//               startActivity(intent)
               countYoutube ++
               if(countYoutube % 2 != 0){
                   // display and play video
                   binding.youtubePlayer.visibility = View.VISIBLE
                   youTubePlayer.play()
               }else {
                   /// hide and pause video
                   binding.youtubePlayer.visibility = View.GONE
                   youTubePlayer.pause()
               }
        }
    }
    // get data MVVM meal
    @SuppressLint("SetTextI18n")
    private fun observerMealDetailLiveData() {
        mealViewModel.observerMealDetailLiveData().observe(this
        ) { t ->
            onResponseCase()
            mealToSave = t
            binding.tvCategory.text = "Category : ${t!!.strCategory}"
            binding.tvArea.text = "Area : ${t.strArea}"
            binding.tvDescription.text = t.strInstructions
            youtubeLink = t.strYoutube.toString()
            description = t.strInstructions.toString()
        }
    }
    // set up view in toolbar and add image view
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }
    //get data to intent from activity before
    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
        if(intent.getStringExtra(HomeFragment.SAVE) != null){
            mealSave = intent.getStringExtra(HomeFragment.SAVE)!!
        }
    }
    // view when loading data
    private fun loadingCase(){
        binding.progressCircular.visibility = View.VISIBLE
        binding.tvDescription.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.btnVideo.visibility = View.INVISIBLE
    }
    // view when finish loading data
    private fun onResponseCase(){
        binding.progressCircular.visibility = View.INVISIBLE
        binding.tvDescription.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.btnVideo.visibility = View.VISIBLE
    }

}