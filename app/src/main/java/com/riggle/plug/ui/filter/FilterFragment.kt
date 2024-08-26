package com.riggle.plug.ui.filter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.kal.rackmonthpicker.RackMonthPicker
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.api.Constants
import com.riggle.plug.data.model.BrandVerifyOtp
import com.riggle.plug.data.model.ChannelPartner
import com.riggle.plug.data.model.District
import com.riggle.plug.data.model.SKUs
import com.riggle.plug.data.model.SalesPerson
import com.riggle.plug.data.model.State
import com.riggle.plug.databinding.Example4CalendarDayBinding
import com.riggle.plug.databinding.Example4CalendarHeaderBinding
import com.riggle.plug.databinding.FragmentFilterBinding
import com.riggle.plug.databinding.HolderBrandsFiltersBinding
import com.riggle.plug.databinding.HolderCityFiltersBinding
import com.riggle.plug.databinding.HolderCpFiltersBinding
import com.riggle.plug.databinding.HolderMainHeadingFiltersBinding
import com.riggle.plug.databinding.HolderSalesPersonFiltersBinding
import com.riggle.plug.databinding.HolderStateFiltersBinding
import com.riggle.plug.ui.base.BaseFragment
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.riggle.plug.ui.home.HomeActivity
import com.riggle.plug.utils.ContinuousSelectionHelper.getSelection
import com.riggle.plug.utils.ContinuousSelectionHelper.isInDateBetweenSelection
import com.riggle.plug.utils.ContinuousSelectionHelper.isOutDateBetweenSelection
import com.riggle.plug.utils.DateSelection
import com.riggle.plug.utils.ImageBindingAdapter
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(), OnSelectDateListener {

    private lateinit var homeActivity: HomeActivity
    private val viewModel: FilterFragmentVM by viewModels()
    private var brandsList = ArrayList<BrandVerifyOtp>()
    private var stateList = ArrayList<State>()
    private var cityList = ArrayList<District>()
    private var cpList = ArrayList<ChannelPartner>()
    private var spList = ArrayList<SalesPerson>()
    private var brandsId = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObservers()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_filter
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    val listener = object : OnSelectDateListener {
        override fun onSelect(calendar: List<Calendar>) {
            for (i in 0..calendar.size - 1) {
                Log.e("RangeDates", calendar[i].time.date.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun DayOfWeek.displayText(uppercase: Boolean = false): String {
        return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
            if (uppercase) value.uppercase(Locale.ENGLISH) else value
        }
    }

    fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

    fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        homeActivity = activity as HomeActivity

        val c = android.icu.util.Calendar.getInstance()
        val year = c.get(android.icu.util.Calendar.YEAR)

        val dateFormat = SimpleDateFormat("MM")
        val dateFormat1 = SimpleDateFormat("MMMM")
        val date1 = Date()

        selectedYear = year.toString()
        selectedMonth = dateFormat.format(date1)
        val selectedMonthName = dateFormat1.format(date1)

        binding.tvSelectedMonthForLeaderBoard.text = "Selected Month : $selectedMonthName"
        if (Constants.selectedTab == "4") {
            binding.cvMonthDate.visibility = View.GONE
            binding.view1.visibility = View.GONE
            binding.view2.visibility = View.GONE
            binding.legendLayout.root.visibility = View.GONE
            binding.tvSelectedMonthForLeaderBoard.visibility = View.VISIBLE
        }

        val daysOfWeek = daysOfWeek()
        binding.legendLayout.root.children.forEachIndexed { index, child ->
            (child as TextView).apply {
                text = daysOfWeek[index].displayText()
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                setTextColorRes(R.color.dark_grey_txt_color)
            }
        }
        configureBinders()
        val sMonth = YearMonth.now()
        val asd = sMonth.minusMonths(7)
        val currentMonth = YearMonth.now()
        binding.cvMonthDate.setup(
            asd,
            asd.plusMonths(1200),
            daysOfWeek.first(),
        )
        binding.cvMonthDate.scrollToMonth(currentMonth)


        if (sharedPrefManager.getStartDate() != null && sharedPrefManager.getEndDate() != null) {
            var sYear = ""
            var sMonth = ""
            var sDay = ""

            val nf1 = "yyyy"
            val nf11 = "MM"
            val nf111 = "dd"

            var sdf = SimpleDateFormat("yyyy-MM-dd")
            val sYear1 = sharedPrefManager.getStartDate()?.let { sdf.parse(it) }
            val sMonth1 = sharedPrefManager.getStartDate()?.let { sdf.parse(it) }
            val sDay1 = sharedPrefManager.getStartDate()?.let { sdf.parse(it) }
            sdf.applyPattern(nf1)
            sYear = sdf.format(sYear1)
            sdf.applyPattern(nf11)
            sMonth = sdf.format(sMonth1)
            sdf.applyPattern(nf111)
            sDay = sdf.format(sDay1)

            sdf = SimpleDateFormat("yyyy-MM-dd")
            var eYear = ""
            var eMonth = ""
            var eDay = ""
            val sYear11 = sharedPrefManager.getEndDate()?.let { sdf.parse(it) }
            val sMonth11 = sharedPrefManager.getEndDate()?.let { sdf.parse(it) }
            val sDay11 = sharedPrefManager.getEndDate()?.let { sdf.parse(it) }
            sdf.applyPattern(nf1)
            eYear = sdf.format(sYear11)
            sdf.applyPattern(nf11)
            eMonth = sdf.format(sMonth11)
            sdf.applyPattern(nf111)
            eDay = sdf.format(sDay11)

            val ssDate = LocalDate.of(sYear.toInt(), sMonth.toInt(), sDay.toInt())
            val eeDate = LocalDate.of(eYear.toInt(), eMonth.toInt(), eDay.toInt())


            selection = getSelection(
                clickedDate = ssDate,
                dateSelection = selection,
            )

            selection = getSelection(
                clickedDate = eeDate,
                dateSelection = selection,
            )
        }

        brandsList.clear()
        brandsList = sharedPrefManager.gateBrandsList() as ArrayList<BrandVerifyOtp>
        initAdapter()
        initBrandsAdapter(brandsList)

        brandsId = ""
        if (brandsList.size != 0) {
            for (i in brandsList.indices) {
                brandsId += if (brandsList.size - 1 != i) {
                    brandsList[i].id.toString() + ","
                } else {
                    brandsList[i].id.toString()
                }
            }
        }
        getFiltersData(brandsId)

        binding.etvName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                filterBrands(editable.toString())
            }
        })

        binding.etvSearchState.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                filterState(editable.toString())
            }
        })

        binding.etvSearchCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                filterCity(editable.toString())
            }
        })

        binding.etvSearchChannelPartner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                filterChannelPartner(editable.toString())
            }
        })

        binding.etvSearchSales.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                filterSalesPerson(editable.toString())
            }
        })

        if (sharedPrefManager.getOrderType()!! == "primary") {
            binding.ivPrimary.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.check
                )
            )
            binding.ivSecondary.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.filter_border
                )
            )
        }

        if (sharedPrefManager.getOrderType()!! == "secondary") {
            binding.ivPrimary.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.filter_border
                )
            )
            binding.ivSecondary.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.check
                )
            )
        }

    }

    private fun Context.getDrawableCompat(@DrawableRes drawable: Int): Drawable =
        requireNotNull(ContextCompat.getDrawable(this, drawable))


    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private var selection = DateSelection()
    private fun configureBinders() {
        val clipLevelHalf = 5000
        val ctx = requireContext()
        val rangeStartBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_start).also {
                it.level = clipLevelHalf // Used by ClipDrawable
            }
        val rangeEndBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_end).also {
                it.level = clipLevelHalf // Used by ClipDrawable
            }
        val rangeMiddleBackground =
            ctx.getDrawableCompat(R.drawable.example_4_continuous_selected_bg_middle)
        val singleBackground = ctx.getDrawableCompat(R.drawable.example_4_single_selected_bg)
        val todayBackground = ctx.getDrawableCompat(R.drawable.example_4_today_bg)

        @RequiresApi(Build.VERSION_CODES.O)
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = Example4CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate /*&& (day.date == today || day.date.isAfter(
                            today
                        ))*/) {
                        selection = getSelection(
                            clickedDate = day.date,
                            dateSelection = selection,
                        )
                        this@FilterFragment.binding.cvMonthDate.notifyCalendarChanged()
                        bindSummaryViews()
                    }
                }
            }
        }




        binding.cvMonthDate.dayBinder = object : MonthDayBinder<DayViewContainer> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun create(view: View) = DayViewContainer(view)

            @RequiresApi(Build.VERSION_CODES.O)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.binding.exFourDayText
                val roundBgView = container.binding.exFourRoundBackgroundView
                val continuousBgView = container.binding.exFourContinuousBackgroundView

                textView.text = null
                roundBgView.makeInVisible()
                continuousBgView.makeInVisible()

                val (startDate, endDate) = selection

                when (data.position) {
                    DayPosition.MonthDate -> {
                        textView.text = data.date.dayOfMonth.toString()/*if (data.date.isBefore(today)) {
                            textView.setTextColorRes(R.color.dark_grey_txt_color_80)
                        } else {*/
                        when {
                            startDate == data.date && endDate == null -> {
                                textView.setTextColorRes(R.color.white)
                                roundBgView.applyBackground(singleBackground)
                            }

                            data.date == startDate -> {
                                textView.setTextColorRes(R.color.white)
                                continuousBgView.applyBackground(rangeStartBackground)
                                roundBgView.applyBackground(singleBackground)
                            }

                            startDate != null && endDate != null && (data.date > startDate && data.date < endDate) -> {
                                textView.setTextColorRes(R.color.dark_grey_txt_color)
                                continuousBgView.applyBackground(rangeMiddleBackground)
                            }

                            data.date == endDate -> {
                                textView.setTextColorRes(R.color.white)
                                continuousBgView.applyBackground(rangeEndBackground)
                                roundBgView.applyBackground(singleBackground)
                            }

                            data.date == today -> {
                                textView.setTextColorRes(R.color.dark_grey_txt_color)
                                roundBgView.applyBackground(todayBackground)
                            }

                            else -> textView.setTextColorRes(R.color.dark_grey_txt_color)
                        }/*
                        }
*/
                    }
                    // Make the coloured selection background continuous on the
                    // invisible in and out dates across various months.
                    DayPosition.InDate -> if (startDate != null && endDate != null && isInDateBetweenSelection(
                            data.date, startDate, endDate
                        )
                    ) {
                        continuousBgView.applyBackground(rangeMiddleBackground)
                    }

                    DayPosition.OutDate -> if (startDate != null && endDate != null && isOutDateBetweenSelection(
                            data.date, startDate, endDate
                        )
                    ) {
                        continuousBgView.applyBackground(rangeMiddleBackground)
                    }
                }
            }

            private fun View.applyBackground(drawable: Drawable) {
                makeVisible()
                background = drawable
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = Example4CalendarHeaderBinding.bind(view).exFourHeaderText
        }
        binding.cvMonthDate.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)

                @RequiresApi(Build.VERSION_CODES.O)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    container.textView.text = data.yearMonth.displayText()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val headerDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var sDate = ""
    var eDate = ""

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindSummaryViews() {
        if (selection.startDate != null) {
            sDate = headerDateFormatter.format(selection.startDate)
        }
        if (selection.endDate != null) {
            eDate = headerDateFormatter.format(selection.endDate)
            binding.tvGetDate.apply {
                text = "$sDate/$eDate"
                setTextColorRes(R.color.white)
                // binding.tvGetDate.visibility = View.VISIBLE

                val rangeDate = binding.tvGetDate.text.split("/")
                val sDate = rangeDate[0]
                val eDate = rangeDate[1]
                sharedPrefManager.saveStartDate(sDate)
                sharedPrefManager.saveEndDate(eDate)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun YearMonth.displayText(short: Boolean = false): String {
        return "${this.month.displayText(short = short)} ${this.year}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Month.displayText(short: Boolean = true): String {
        val style = if (short) TextStyle.SHORT else TextStyle.FULL
        return getDisplayName(style, Locale.ENGLISH)
    }

    fun View.makeVisible() {
        visibility = View.VISIBLE
    }

    fun View.makeInVisible() {
        visibility = View.INVISIBLE
    }

    fun View.makeGone() {
        visibility = View.GONE
    }


    private fun filterSalesPerson(text: String) {
        val fl = ArrayList<SalesPerson>()
        for (item in spList) {
            val name = item.first_name + " " + item.last_name
            if (name.lowercase().contains(text.lowercase())) {
                fl.add(item)
            } else {
                salesAdapter.clearList()
            }
        }
        salesAdapter.filterList(fl)
    }

    private fun filterChannelPartner(text: String) {
        val fl = ArrayList<ChannelPartner>()
        for (item in cpList) {
            if (item.name.lowercase().contains(text.lowercase())) {
                fl.add(item)
            } else {
                cpAdapter.clearList()
            }
        }
        cpAdapter.filterList(fl)
    }

    private fun filterCity(text: String) {
        val fl = ArrayList<District>()
        for (item in cityList) {
            if (item.district_name.lowercase().contains(text.lowercase())) {
                fl.add(item)
            } else {
                cityAdapter.clearList()
            }
        }
        cityAdapter.filterList(fl)
    }

    private fun filterState(text: String) {
        val fl = ArrayList<State>()
        for (item in stateList) {
            if (item.state_name.lowercase().contains(text.lowercase())) {
                fl.add(item)
            } else {
                statesAdapter.clearList()
            }
        }
        statesAdapter.filterList(fl)
    }

    private fun filterBrands(text: String) {
        val fl = ArrayList<BrandVerifyOtp>()
        for (item in brandsList) {
            if (item.name.lowercase().contains(text.lowercase())) {
                fl.add(item)
            } else {
                brandsAdapter.clearList()
            }
        }
        brandsAdapter.filterList(fl)
    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner) {
            when (it?.id) {
                R.id.tvSelectedMonthForLeaderBoard -> {
                    showDatePicker()
                }

                R.id.tvGetDate -> {

                }

                R.id.iv2 -> {
                    homeActivity.onBackPressed()
                }

                R.id.tv2 -> { // clear filter

                    // Start & End Date Filter
                    val calendar = Calendar.getInstance(TimeZone.getDefault())
                    val currentYear: Int = calendar.get(Calendar.YEAR)
                    val currentMonth: Int = calendar.get(Calendar.MONTH) + 1
                    val currentDay: Int = calendar.get(Calendar.DAY_OF_MONTH)

                    sharedPrefManager.saveStartDate("$currentYear-$currentMonth-01")
                    sharedPrefManager.saveEndDate("$currentYear-$currentMonth-$currentDay")

                    sharedPrefManager.saveOrderType("primary")
                    sharedPrefManager.saveSalesPerson("")
                    sharedPrefManager.saveCP("")

                    val brandsList = sharedPrefManager.getCurrentUser()?.user?.brands
                    var brandsId = ""
                    if (!brandsList.isNullOrEmpty()) {
                        for (i in brandsList.indices) {
                            brandsList[i].isSelected = true
                        }
                        sharedPrefManager.saveBrandsList(brandsList)
                        if (brandsList.isNotEmpty()) {
                            for (i in 0..brandsList.size - 1) {
                                if (brandsList[i].isSelected) {
                                    if (brandsList.size - 1 != i) {
                                        brandsId += brandsList[i].id.toString() + ","
                                    } else {
                                        brandsId += brandsList[i].id.toString()
                                    }
                                }
                            }
                            sharedPrefManager.saveBrandsIdString(brandsId)
                        }
                    } else {
                        sharedPrefManager.saveBrandsIdString("")
                    }

                    homeActivity.onBackPressed()
                }

                R.id.llSecondaryOrderType -> {
                    binding.ivPrimary.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.filter_border
                        )
                    )
                    binding.ivSecondary.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.check
                        )
                    )
                    sharedPrefManager.saveOrderType("secondary")

                }

                R.id.llPrimaryOrderType -> {
                    binding.ivSecondary.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.filter_border
                        )
                    )
                    binding.ivPrimary.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.check
                        )
                    )
                    sharedPrefManager.saveOrderType("primary")
                }
            }
        }
    }

    private fun getFiltersData(brands: String) {
        val query = HashMap<String, String>()
        query["brands"] = brands
        query["state"] = ""
        query["city"] = ""
        query["filters"] = "true"
        viewModel.getFiltersData(
            sharedPrefManager.getSessionId()!!,
            sharedPrefManager.getCurrentUser()?.user?.company?.id!!.toInt(),
            query
        )
    }

    private fun initObservers() {

        viewModel.obrFiltersData.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    showHideLoader(false)
                    if (it.data != null) {
                        stateList = it.data.states as ArrayList<State>
                        cityList = it.data.districts as ArrayList<District>
                        cpList = it.data.channel_partners as ArrayList<ChannelPartner>
                        spList = it.data.sales_person as ArrayList<SalesPerson>
                        cityList = it.data.districts
                        initStatesAdapter(it.data.states)
                        initCityAdapter(it.data.districts)
                        initCPAdapter(it.data.channel_partners)
                        initSalesPersonAdapter(it.data.sales_person)
                    }
                }

                Status.ERROR -> {
                    showHideLoader(false)
                    showErrorToast(it.message.toString())
                }

                else -> {}
            }
        }
    }

    private var selectedMonth = ""
    private var selectedYear = ""
    private lateinit var dialog: RackMonthPicker
    private fun showDatePicker() {
        dialog = RackMonthPicker(requireContext()).setSelectedMonth(selectedMonth.toInt())
            .setLocale(Locale.US)
            .setColorTheme(ContextCompat.getColor(requireContext(), R.color.line_color))
            .setSelectedYear(selectedYear.toInt())
            .setPositiveButton { month, startDate, endDate, year, monthLabel ->
                val date = ("$year-$month-$startDate")
                val selectedMonthName = ImageBindingAdapter.getMonthName1(date)
                binding.tvSelectedMonthForLeaderBoard.text = "Selected Month : $selectedMonthName"
                selectedMonth = month.toString()
                selectedYear = year.toString()
                Constants.selectedYear = selectedYear
                Constants.selectedMonth = month.toString()

            }.setNegativeButton {
                dialog.dismiss()
            }
        dialog.show()
    }

    private lateinit var adapter: SimpleRecyclerViewAdapter<SKUs, HolderMainHeadingFiltersBinding>
    private fun initAdapter() {
        adapter = SimpleRecyclerViewAdapter(
            R.layout.holder_main_heading_filters, BR.bean
        ) { v, m, pos ->
            val listUpdated = prepareList()
            for (i in 0 until adapter.list.size) {
                if (pos == i) {
                    if (!listUpdated[i].isSelected) {
                        listUpdated[i].isSelected = true
                    }
                } else {
                    listUpdated[i].isSelected = false
                }
            }
            adapter.list = listUpdated
            adapter.notifyDataSetChanged()

            when (pos) {
                0 -> { // Month Date
                    if (Constants.selectedTab == "4") {
                        binding.tvSelectedMonthForLeaderBoard.visibility = View.VISIBLE
                        showDatePicker()
                        binding.cvMonthDate.visibility = View.GONE
                        binding.llFilterBrands.visibility = View.GONE
                        binding.llFilterOrderType.visibility = View.GONE
                        binding.llFilterTags.visibility = View.GONE
                        binding.llFilterState.visibility = View.GONE
                        binding.llFilterCity.visibility = View.GONE
                        binding.llFilterChannelPartner.visibility = View.GONE
                        binding.llFilterSalesPerson.visibility = View.GONE
                    } else {
                        if (binding.cvMonthDate.visibility != View.VISIBLE) {
                            binding.cvMonthDate.visibility = View.VISIBLE
                            binding.view1.visibility = View.VISIBLE
                            binding.view2.visibility = View.VISIBLE
                            binding.legendLayout.root.visibility = View.VISIBLE
                            binding.tvSelectedMonthForLeaderBoard.visibility = View.GONE
                            binding.cvMonthDate.visibility = View.VISIBLE
                            binding.llFilterBrands.visibility = View.GONE
                            binding.llFilterOrderType.visibility = View.GONE
                            binding.llFilterTags.visibility = View.GONE
                            binding.llFilterState.visibility = View.GONE
                            binding.llFilterCity.visibility = View.GONE
                            binding.llFilterChannelPartner.visibility = View.GONE
                            binding.llFilterSalesPerson.visibility = View.GONE
                        }
                    }
                }

                1 -> { // Brand
                    if (binding.llFilterBrands.visibility != View.VISIBLE) {
                        binding.llFilterBrands.visibility = View.VISIBLE
                        binding.cvMonthDate.visibility = View.GONE
                        binding.llFilterOrderType.visibility = View.GONE
                        binding.llFilterTags.visibility = View.GONE
                        binding.llFilterState.visibility = View.GONE
                        binding.llFilterCity.visibility = View.GONE
                        binding.llFilterChannelPartner.visibility = View.GONE
                        binding.llFilterSalesPerson.visibility = View.GONE
                        binding.tvSelectedMonthForLeaderBoard.visibility = View.GONE

                    }
                }

                2 -> { // Order Type
                    if (binding.llFilterOrderType.visibility != View.VISIBLE) {
                        binding.llFilterOrderType.visibility = View.VISIBLE
                        binding.cvMonthDate.visibility = View.GONE
                        binding.llFilterBrands.visibility = View.GONE
                        binding.llFilterTags.visibility = View.GONE
                        binding.llFilterState.visibility = View.GONE
                        binding.llFilterCity.visibility = View.GONE
                        binding.llFilterChannelPartner.visibility = View.GONE
                        binding.llFilterSalesPerson.visibility = View.GONE
                        binding.tvSelectedMonthForLeaderBoard.visibility = View.GONE

                    }
                }

                3 -> { // Tags
                    if (binding.llFilterTags.visibility != View.VISIBLE) {
                        binding.llFilterTags.visibility = View.VISIBLE
                        binding.cvMonthDate.visibility = View.GONE
                        binding.llFilterBrands.visibility = View.GONE
                        binding.llFilterOrderType.visibility = View.GONE
                        binding.llFilterState.visibility = View.GONE
                        binding.llFilterCity.visibility = View.GONE
                        binding.llFilterChannelPartner.visibility = View.GONE
                        binding.llFilterSalesPerson.visibility = View.GONE
                        binding.tvSelectedMonthForLeaderBoard.visibility = View.GONE

                    }
                }

                4 -> { // State
                    if (binding.llFilterState.visibility != View.VISIBLE) {
                        binding.llFilterState.visibility = View.VISIBLE
                        binding.cvMonthDate.visibility = View.GONE
                        binding.llFilterBrands.visibility = View.GONE
                        binding.llFilterOrderType.visibility = View.GONE
                        binding.llFilterTags.visibility = View.GONE
                        binding.llFilterCity.visibility = View.GONE
                        binding.llFilterChannelPartner.visibility = View.GONE
                        binding.llFilterSalesPerson.visibility = View.GONE
                        binding.tvSelectedMonthForLeaderBoard.visibility = View.GONE

                    }
                }

                5 -> { // City
                    if (binding.llFilterCity.visibility != View.VISIBLE) {
                        binding.llFilterCity.visibility = View.VISIBLE
                        binding.cvMonthDate.visibility = View.GONE
                        binding.llFilterBrands.visibility = View.GONE
                        binding.llFilterOrderType.visibility = View.GONE
                        binding.llFilterTags.visibility = View.GONE
                        binding.llFilterState.visibility = View.GONE
                        binding.llFilterChannelPartner.visibility = View.GONE
                        binding.llFilterSalesPerson.visibility = View.GONE
                        binding.tvSelectedMonthForLeaderBoard.visibility = View.GONE

                    }
                }

                6 -> { // Channel Partner
                    if (binding.llFilterChannelPartner.visibility != View.VISIBLE) {
                        binding.llFilterChannelPartner.visibility = View.VISIBLE
                        binding.cvMonthDate.visibility = View.GONE
                        binding.llFilterBrands.visibility = View.GONE
                        binding.llFilterOrderType.visibility = View.GONE
                        binding.llFilterTags.visibility = View.GONE
                        binding.llFilterState.visibility = View.GONE
                        binding.llFilterCity.visibility = View.GONE
                        binding.llFilterSalesPerson.visibility = View.GONE
                        binding.tvSelectedMonthForLeaderBoard.visibility = View.GONE

                    }
                }

                7 -> { // SalesPerson
                    if (binding.llFilterSalesPerson.visibility != View.VISIBLE) {
                        binding.llFilterSalesPerson.visibility = View.VISIBLE
                        binding.cvMonthDate.visibility = View.GONE
                        binding.llFilterBrands.visibility = View.GONE
                        binding.llFilterOrderType.visibility = View.GONE
                        binding.llFilterTags.visibility = View.GONE
                        binding.llFilterState.visibility = View.GONE
                        binding.llFilterCity.visibility = View.GONE
                        binding.llFilterChannelPartner.visibility = View.GONE
                        binding.tvSelectedMonthForLeaderBoard.visibility = View.GONE

                    }
                }
            }
        }
        binding.rvMainHeadings.adapter = adapter
        adapter.list = prepareList()
    }

    private lateinit var brandsAdapter: SimpleRecyclerViewAdapter<BrandVerifyOtp, HolderBrandsFiltersBinding>
    private fun initBrandsAdapter(brands: List<BrandVerifyOtp>?) {
        brandsAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_brands_filters, BR.bean
        ) { v, m, pos ->
            m.isSelected = !m.isSelected
            brandsAdapter.notifyItemChanged(pos)

            if (brandsList.size > 0) {
                for (i in 0 until brandsList.size) {
                    brandsList[pos].isSelected = !brandsList[pos].isSelected
                }
                sharedPrefManager.saveBrandsList(brandsList)
            } else {
                brandsList = sharedPrefManager.gateBrandsList() as ArrayList<BrandVerifyOtp>
            }

            brandsId = ""

            if (brandsList.size != 0) {
                for (i in brandsList.indices) {
                    if (brandsList[i].isSelected) {
                        if (brandsList.size - 1 != i) {
                            brandsId += brandsList[i].id.toString() + ","
                        } else {
                            brandsId += brandsList[i].id.toString()
                        }
                    }
                }
                brandsId = brandsId.replace(",$".toRegex(), "")
                sharedPrefManager.saveBrandsIdString(brandsId)
            } else {
                sharedPrefManager.saveBrandsIdString("")
            }

            Log.e("ItemClickCheck", brandsId)
            //  Log.e("ItemClickListCheck",brandsList.toString())
        }
        binding.rvBrands.adapter = brandsAdapter
        brandsAdapter.list = brands
    }

    private lateinit var statesAdapter: SimpleRecyclerViewAdapter<State, HolderStateFiltersBinding>
    private fun initStatesAdapter(states: List<State>?) {
        statesAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_state_filters, BR.bean
        ) { v, m, pos ->

        }
        binding.rvStates.adapter = statesAdapter
        statesAdapter.list = states
    }

    private lateinit var cityAdapter: SimpleRecyclerViewAdapter<District, HolderCityFiltersBinding>
    private fun initCityAdapter(cities: List<District>?) {
        cityAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_city_filters, BR.bean
        ) { v, m, pos ->

        }
        binding.rvCity.adapter = cityAdapter
        cityAdapter.list = cities
    }

    private lateinit var cpAdapter: SimpleRecyclerViewAdapter<ChannelPartner, HolderCpFiltersBinding>
    private fun initCPAdapter(cities: List<ChannelPartner>) {
        cpAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_cp_filters, BR.bean
        ) { v, m, pos ->
            for (i in 0..cpList.size - 1) {
                if (pos == i) {
                    if (cpList[i].isSelected) {
                        sharedPrefManager.saveCP("")
                        cpList[i].isSelected = false
                    } else {
                        sharedPrefManager.saveCP(cpList[i].id.toString())
                        cpList[i].isSelected = true
                    }
                } else {
                    cpList[i].isSelected = false
                }
            }
            cpAdapter.list = cpList
        }
        binding.rvChannelPartner.adapter = cpAdapter
        val isCPSelected = sharedPrefManager.getCP()
        if (isCPSelected != null) {
            for (i in 0..cities.size - 1) {
                if (cities[i].id.toString() == isCPSelected) {
                    cities[i].isSelected = true
                    break
                }
            }
        }
        cpAdapter.list = cities
    }

    private var salesPersonList = ArrayList<SalesPerson>()
    private lateinit var salesAdapter: SimpleRecyclerViewAdapter<SalesPerson, HolderSalesPersonFiltersBinding>
    private fun initSalesPersonAdapter(cities: List<SalesPerson>?) {
        salesPersonList = cities as ArrayList<SalesPerson>
        salesAdapter = SimpleRecyclerViewAdapter(
            R.layout.holder_sales_person_filters, BR.bean
        ) { v, m, pos ->
            for (i in 0..salesPersonList.size - 1) {
                if (pos == i) {
                    if (salesPersonList[i].isSelected) {
                        sharedPrefManager.saveSalesPerson("")
                        salesPersonList[i].isSelected = false
                    } else {
                        sharedPrefManager.saveSalesPerson(salesPersonList[i].id.toString())
                        salesPersonList[i].isSelected = true
                    }
                } else {
                    salesPersonList[i].isSelected = false
                }
            }
            salesAdapter.list = salesPersonList
        }
        binding.rvSales.adapter = salesAdapter
        val isSalesSelected = sharedPrefManager.getSalesPerson()
        if (isSalesSelected != null) {
            for (i in 0..cities.size - 1) {
                if (cities[i].id.toString() == isSalesSelected) {
                    cities[i].isSelected = true
                    break
                }
            }
        }
        salesAdapter.list = cities
    }

    private fun prepareList(): ArrayList<SKUs> {
        val list = ArrayList<SKUs>()
        list.add(SKUs("Month/Date", true))
        list.add(SKUs("Brand"))
        list.add(SKUs("Order Type"))
        list.add(SKUs("Tags"))
        list.add(SKUs("State"))
        list.add(SKUs("City"))
        list.add(SKUs("Channel Partner"))
        list.add(SKUs("Salesperson"))
        return list
    }

    override fun onSelect(calendar: List<Calendar>) {
        for (i in 0..calendar.size - 1) {
            Log.e("selectedDates", calendar[i].time.date.toString())
        }
    }
}