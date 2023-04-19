package com.example.foodapp.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.activity.CountryMealActivity
import com.example.foodapp.activity.MainActivity
import com.example.foodapp.adapter.CountryAdapter
import com.example.foodapp.databinding.FragmentListNationBinding
import com.example.foodapp.viewmodel.HomeViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF


class ListAreaFragment : Fragment(), OnChartValueSelectedListener {
    val number : Long = 500
    private lateinit var binding : FragmentListNationBinding
    private lateinit var countryAdapter : CountryAdapter
    private lateinit var viewModel : HomeViewModel
    val arrayList = arrayOf<String>("American","British","Canadian","Chinese","Croatian","Dutch","Egyptian","French","Greek","Indian","Irish","Italian","Jamaican","Japanese","Kenyan","Malaysian","Mexican","Moroccan","Polish","Portuguese","Russian","Spanish","Thai","Tunisian","Turkish","Unknown","Vietnamese")
    val arrayCount = arrayOf<Float>(32f,57f,13f,12f,8f,4f,8f,28f,8f,11f,8f,19f,8f,9f,2f,8f,5f,7f,8f,8f,1f,3f,3f,8f,2f,3f,2f)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListNationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCountry()
        prepareRecyclerView()
        observerCountryLiveData()
        onClickCountry()
        setDataChart(arrayCount)
        binding.pieChart.setOnChartValueSelectedListener(this)
    }
    private fun sumAll(arrayCount: Array<Float>):Float{
        var sum = 0f
        for (i in arrayCount.indices){
            sum += arrayCount[i]
        }
        return sum
    }
    private fun setDataChart(arrayCount: Array<Float>) {
        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        binding.pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        binding.pieChart.setTransparentCircleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        binding.pieChart.holeRadius = 58f
        binding.pieChart.transparentCircleRadius = 61f

        // on below line we are setting center text
        binding.pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        binding.pieChart.rotationAngle = 0f

        // enable rotation of the pieChart by touch
        binding.pieChart.isRotationEnabled = true
        binding.pieChart.isHighlightPerTapEnabled = true

        // on below line we are setting animation for our pie chart
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.setEntryLabelColor(Color.WHITE)
        binding.pieChart.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()
       for(i in arrayCount.indices){
           val name = arrayList[i]
           val percent = (arrayCount[i] / sumAll(arrayCount)) * 100f
           entries.add(PieEntry(percent,"$name : $percent"))
       }
        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 1f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple_200))
        colors.add(resources.getColor(R.color.slate_blue3))
        colors.add(resources.getColor(R.color.accent))
        colors.add(resources.getColor(R.color.teal_200))
        colors.add(resources.getColor(R.color.teal_700))
        colors.add(resources.getColor(R.color.black))
        colors.add(resources.getColor(R.color.cyan1))
        colors.add(resources.getColor(R.color.g_black))
        colors.add(resources.getColor(R.color.cyan2))
        colors.add(resources.getColor(R.color.grey))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.red))
        colors.add(resources.getColor(R.color.royal_blue1))
        colors.add(resources.getColor(R.color.royal_blue2))
        colors.add(resources.getColor(R.color.royal_blue3))
        colors.add(resources.getColor(R.color.deep_sky_blue))
        colors.add(resources.getColor(R.color.deep_sky_blue2))
        colors.add(resources.getColor(R.color.slate_blue))
        colors.add(resources.getColor(R.color.deep_sky_blue3))
        colors.add(resources.getColor(R.color.deep_sky_blue4))
        colors.add(resources.getColor(R.color.dodger_blue))
        colors.add(resources.getColor(R.color.dodger_blue2))
        colors.add(resources.getColor(R.color.dodger_blue3))
        colors.add(resources.getColor(R.color.dodger_blue4))
        colors.add(resources.getColor(R.color.steel_blue1))
        colors.add(resources.getColor(R.color.steel_blue2))
        colors.add(resources.getColor(R.color.steel_blue3))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(0f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        binding.pieChart.data = data
        binding.pieChart.setDrawEntryLabels(false)
        binding.pieChart.description.isEnabled = false
        val l = binding.pieChart.legend
        binding.pieChart.legend.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT // position
        l.formToTextSpace = 4f
        l.form = Legend.LegendForm.LINE // form type : line, square, circle ..
        l.textSize = 10f
        l.orientation = Legend.LegendOrientation.VERTICAL // side by side or bottom to bottom

        // undo all highlights
        binding.pieChart.highlightValues(null)
        // loading chart
        binding.pieChart.invalidate()

    }
    private fun onClickCountry() {
        countryAdapter.onItemClick = {
           val intent = Intent(activity,CountryMealActivity::class.java)
            intent.putExtra(HomeFragment.COUNTRY_NAME,it.strArea)
            startActivity(intent)
        }
    }
    private fun observerCountryLiveData() {
        viewModel.observerCountryLiveData().observe(viewLifecycleOwner){ list->
            countryAdapter.setCountryList(list)
        }
    }
    private fun prepareRecyclerView() {
        countryAdapter = CountryAdapter()
        binding.rvNation.apply {
            adapter = countryAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val pieEntry = e as PieEntry
        val label: String = pieEntry.label
        Toast.makeText(context, "$label", Toast.LENGTH_SHORT).show()

    }

    override fun onNothingSelected() {
    }
}