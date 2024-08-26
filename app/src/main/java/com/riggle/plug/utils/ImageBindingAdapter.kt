package com.riggle.plug.utils

import android.content.Context
import android.graphics.Color
import android.location.Geocoder
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.imageview.ShapeableImageView
import com.riggle.plug.BR
import com.riggle.plug.R
import com.riggle.plug.data.model.Arpr
import com.riggle.plug.data.model.BeatSalesBeat
import com.riggle.plug.data.model.GraphData
import com.riggle.plug.data.model.NetworkCPCount1Item
import com.riggle.plug.data.model.New1
import com.riggle.plug.data.model.OwnOrderDetailsResponseModel
import com.riggle.plug.data.model.Placed
import com.riggle.plug.data.model.Placed2
import com.riggle.plug.data.model.ProductNetworkOrderDetails
import com.riggle.plug.data.model.ProductOwnOrderDetails
import com.riggle.plug.data.model.ResultSalesLocation
import com.riggle.plug.data.model.Retailer
import com.riggle.plug.data.model.SalesNetworkResponseModel
import com.riggle.plug.data.model.SalesmanCityBeats
import com.riggle.plug.data.model.SubCategory1
import com.riggle.plug.data.model.SubCategory2
import com.riggle.plug.data.model.TargetUserData
import com.riggle.plug.databinding.LaySalesBeatItemBinding
import com.riggle.plug.ui.base.SimpleRecyclerViewAdapter
import com.skydoves.progressview.ProgressView
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["setFont"])
    fun setFont(tv: TextView, isSelected: Boolean) {
        if (isSelected) {
            val myTypeface = ResourcesCompat.getFont(tv.context, R.font.poppins_bold)
            tv.typeface = myTypeface
        } else {
            val myTypeface = ResourcesCompat.getFont(tv.context, R.font.poppins_regular)
            tv.typeface = myTypeface
        }
    }

    fun amountSorting(amount: Double): String {
        var updatedAmount = ""
        val formatter = DecimalFormat("#.##")
        if (amount > 999 && amount < 99999) {
            val cal = (amount / 1000)
            updatedAmount = formatter.format(cal).toString() + " K"
        } else if (amount > 99999 && amount < 9999999) {
            val cal = (amount / 100000)
            updatedAmount = formatter.format(cal).toString() + " Lakh"
        } else if (amount > 9999999 && amount < 999999999) {
            val cal = (amount / 10000000)
            updatedAmount = formatter.format(cal).toString() + " Crore"
        } else {
            updatedAmount = amount.toString()
        }
        return updatedAmount
    }


    @JvmStatic
    @BindingAdapter(value = ["amountSorting1"])
    fun amountSorting1(tv: TextView, amount: Int) {
        var updatedAmount = ""
        val formatter = DecimalFormat("#.#")
        if (amount > 999 && amount < 99999) {
            val cal = (amount / 1000)
            updatedAmount = formatter.format(cal).toString() + "K"
        } else if (amount > 99999 && amount < 9999999) {
            val cal = (amount / 100000)
            updatedAmount = formatter.format(cal).toString() + " Lakh"
        } else if (amount > 9999999 && amount < 999999999) {
            val cal = (amount / 10000000)
            updatedAmount = formatter.format(cal).toString() + " Crore"
        } else {
            updatedAmount = amount.toString()
        }
        tv.text = updatedAmount
    }

    @JvmStatic
    @BindingAdapter(value = ["amountSorting4"])
    fun amountSorting4(tv: TextView, amount: Int) {
        var updatedAmount = ""
        val formatter = DecimalFormat("#.##")
        if (amount > 999 && amount < 99999) {
            val cal = (amount / 1000)
            updatedAmount = formatter.format(cal).toString() + "K"
        } else if (amount > 99999 && amount < 9999999) {
            val cal = (amount / 100000)
            updatedAmount = formatter.format(cal).toString() + " L"
        } else if (amount > 9999999 && amount < 999999999) {
            val cal = (amount / 10000000)
            updatedAmount = formatter.format(cal).toString() + " Cr"
        } else {
            updatedAmount = amount.toString()
        }
        tv.text = updatedAmount
    }

    @JvmStatic
    @BindingAdapter(value = ["amountSorting2"])
    fun amountSorting2(tv: TextView, amount: Double) {
        var updatedAmount = ""
        val formatter = DecimalFormat("#.##")
        if (amount > 999 && amount < 99999) {
            val cal = (amount / 1000)
            updatedAmount = formatter.format(cal).toString() + "K"
        } else if (amount > 99999 && amount < 9999999) {
            val cal = (amount / 100000)
            updatedAmount = formatter.format(cal).toString() + " Lakh"
        } else if (amount > 9999999 && amount < 999999999) {
            val cal = (amount / 10000000)
            updatedAmount = formatter.format(cal).toString() + " Crore"
        } else {
            updatedAmount = amount.toString()
        }
        // ₹
        tv.text = "($updatedAmount)"
    }

    @JvmStatic
    @BindingAdapter(value = ["amountSorting6"])
    fun amountSorting6(tv: TextView, amount: Double) {
        val decim = DecimalFormat("#,###.##")
        val asdf = decim.format(amount.toFloat())
        // ₹
        tv.text = "(₹ $asdf)"
    }

    @JvmStatic
    @BindingAdapter(value = ["amountSorting3"])
    fun amountSorting3(tv: TextView, amount: Double) {
        var updatedAmount = ""
        val formatter = DecimalFormat("#.##")
        if (amount > 999 && amount < 99999) {
            val cal = (amount / 1000)
            updatedAmount = formatter.format(cal).toString() + "K"
        } else if (amount > 99999 && amount < 9999999) {
            val cal = (amount / 100000)
            updatedAmount = formatter.format(cal).toString() + " L"
        } else if (amount > 9999999 && amount < 999999999) {
            val cal = (amount / 10000000)
            updatedAmount = formatter.format(cal).toString() + " Cr"
        } else {
            updatedAmount = amount.toString()
        }
        // ₹
        tv.text = updatedAmount
    }

    @JvmStatic
    @BindingAdapter(value = ["amountSorting5"])
    fun amountSorting5(tv: TextView, amount: Double) {
        var updatedAmount = ""
        val formatter = DecimalFormat("#.##")
        if (amount > 999 && amount < 99999) {
            val cal = (amount / 1000)
            updatedAmount = formatter.format(cal).toString() + "K"
        } else if (amount > 99999 && amount < 9999999) {
            val cal = (amount / 100000)
            updatedAmount = formatter.format(cal).toString() + " L"
        } else if (amount > 9999999 && amount < 999999999) {
            val cal = (amount / 10000000)
            updatedAmount = formatter.format(cal).toString() + " Cr"
        } else {
            updatedAmount = amount.toString()
        }
        // ₹
        tv.text = "₹ $updatedAmount"
    }

    @JvmStatic
    @BindingAdapter(value = ["setPrimaryProgress"])
    fun setPrimaryProgress(pb: ProgressBar, response: TargetUserData?) {
        response?.primary_order_value?.let { primary ->
            if (primary > 0) {
                response.primary_target.let {
                    if (it > 0) {
                        val percentage = (primary * 100) / it
                        pb.progress = percentage
                    }
                }
            }
        }
    }

/*    @JvmStatic
    @BindingAdapter(value = ["setSecondaryProgress"])
    fun setSecondaryProgress(pb: ProgressBar, response: TargetUserData?) {
        response?.secondary_order_value?.let { primary ->
            if (primary > 0) {
                response.secondary_target.let {
                    if (it > 0) {
                        val percentage = (primary * 100) / it
                        pb.progress = percentage
                    }
                }
            }
        }
    }*/

    @JvmStatic
    @BindingAdapter(value = ["setProgressPercentagePrimary"])
    fun setProgressPercentagePrimary(tv: TextView, response: TargetUserData?) {
        response?.primary_order_value?.let { secondary ->
            if (secondary > 0) {
                response.primary_target.let {
                    if (it > 0) {
                        val decimalFormat = DecimalFormat("#.#")
                        val percentage = (secondary * 100) / it
                        tv.text = decimalFormat.format(percentage) + "%"
                    }
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setProgressPercentageSecondary"])
    fun setProgressPercentageSecondary(tv: TextView, response: TargetUserData?) {
        response?.secondary_order_value?.let { secondary ->
            if (secondary > 0) {
                response.secondary_target.let {
                    if (it > 0) {
                        val decimalFormat = DecimalFormat("#.#")
                        val percentage = (secondary * 100) / it
                        tv.text = decimalFormat.format(percentage) + "%"
                    }
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setAlphabet"])
    fun setAlphabet(tv: TextView, pos: Int?) {
        if (pos != null) {
            tv.text = "Point ${getAlphabet(pos)}"
        }
    }

    fun getMonthName(pos: Int): String {
        val list = ArrayList<String>()
        list.add("Jan")
        list.add("Feb")
        list.add("Mar")
        list.add("Apr")
        list.add("May")
        list.add("Jun")
        list.add("Jul")
        list.add("Aug")
        list.add("Sept")
        list.add("Oct")
        list.add("Nov")
        list.add("Dec")
        return list[pos - 1]
    }

    private fun getAlphabet(pos: Int): String {
        val list = ArrayList<String>()
        list.add("A")
        list.add("B")
        list.add("C")
        list.add("D")
        list.add("E")
        list.add("F")
        list.add("G")
        list.add("H")
        list.add("I")
        list.add("J")
        list.add("K")
        list.add("L")
        list.add("M")
        list.add("N")
        list.add("O")
        list.add("P")
        list.add("Q")
        list.add("R")
        list.add("S")
        list.add("T")
        list.add("U")
        list.add("V")
        list.add("W")
        list.add("X")
        list.add("Y")
        list.add("Z")
        return list[pos]
    }

    @JvmStatic
    @BindingAdapter(value = ["low_stock_status"])
    fun setLowStockStatus(textView: TextView, status: String?) {
        status?.let { key ->
            when (key) {
                "in_stock" -> {
                    textView.text = "In Stock"
                    textView.setTextColor(
                        ContextCompat.getColor(
                            textView.context, R.color.green_color_text
                        )
                    )
                }

                "out_of_stock" -> {
                    textView.text = "Out Of Stock"
                    textView.setTextColor(
                        ContextCompat.getColor(
                            textView.context, R.color.line_color
                        )
                    )
                }

                "low" -> {
                    textView.text = "Low"
                    textView.setTextColor(
                        ContextCompat.getColor(
                            textView.context, R.color.red_color_3
                        )
                    )
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setTitleBarColor"])
    fun setTitleBarColor(tv: TextView, isSelected: Boolean?) {
        if (isSelected != null && isSelected) {
            tv.setTextColor(tv.context.getColor(R.color.line_color))
        } else {
            tv.setTextColor(tv.context.getColor(R.color.dark_grey_txt_color))
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setSalesBeatItem"])
    fun setSalesBeatItem(rv: RecyclerView, bean: List<BeatSalesBeat>?) {
        val adapter: SimpleRecyclerViewAdapter<BeatSalesBeat, LaySalesBeatItemBinding> =
            SimpleRecyclerViewAdapter(
                R.layout.lay_sales_beat_item, BR.bean
            ) { v, m, pos ->

            }
        rv.adapter = adapter
        if (bean != null) {
            adapter.list = bean
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["setACBoxStatus"])
    fun setACBoxStatus(
        acb: ShapeableImageView, isSelected: Boolean?
    ) {
        if (isSelected != null && isSelected) {
            acb.setBackgroundDrawable(ContextCompat.getDrawable(acb.context, R.drawable.check))
        } else {
            acb.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    acb.context,
                    R.drawable.filter_border
                )
            )
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setProgress"])
    fun setProgress(
        pv: com.mikhaellopez.circularprogressbar.CircularProgressBar, percentage: String?
    ) {
        if (percentage != null) {
            pv.progress = percentage.toFloat()
        } else {
            pv.progress = 0f
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["image_tint"])
    fun setImageTint(imageView: ImageView, bean: GraphData?) {
        //ContextCompat.getColor(context, R.color.COLOR_YOUR_COLOR)
        imageView.setColorFilter(
            Color.parseColor(bean?.color), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    @JvmStatic
    @BindingAdapter(value = ["image_tint1"])
    fun setImageTint1(imageView: ImageView, bean: SubCategory1?) {
        //ContextCompat.getColor(context, R.color.COLOR_YOUR_COLOR)
        if (bean?.color != null) {
            imageView.setColorFilter(
                Color.parseColor(bean?.color), android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            imageView.setColorFilter(
                Color.parseColor("#E5E4E2"), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["image_tint2"])
    fun setImageTint2(imageView: ImageView, bean: SubCategory2?) {
        //ContextCompat.getColor(context, R.color.COLOR_YOUR_COLOR)
        if (bean?.color != null) {
            imageView.setColorFilter(
                Color.parseColor(bean?.color), android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            imageView.setColorFilter(
                Color.parseColor("#E5E4E2"), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setLastOrderDate1"])
    fun setLastOrderDate1(tv: TextView, date: String) {
        if (date != null) {
            var nwDate = ""
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val newFormat = "dd/MM/yyyy"
            val newDate: Date = sdf.parse(date)
            sdf.applyPattern(newFormat)
            nwDate = sdf.format(newDate)
            tv.text = nwDate
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setInsightsPieValues"])
    fun setInsightsPieValues(textView: TextView, revenue: Double?) {
        revenue?.let {
            val decim = DecimalFormat("#,###.##")
            val asdf = decim.format(it.toFloat())
            textView.text = "₹ $asdf"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setInsightsPieValues1"])
    fun setInsightsPieValues1(textView: TextView, revenue: Float?) {
        revenue?.let {
            val decim = DecimalFormat("#,###.##")
            val asdf = decim.format(it.toFloat())
            textView.text = "₹ $asdf"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setRemarksTime"])
    fun setRemarksTime(textView: TextView, date: String?) {
        date?.let {
            var nwDate = ""
            //val oldFormat = "yyyy-MM-dd'T'HH:mm:ssz"
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "dd/MM/yyyy, hh:mm a"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
                textView.text = "$nwDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    fun getFormattedNumber(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        return String.format("%.1f %c", count / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
    }

    fun getMonthName(date: String?): String {
        if (date != null) {
            var nwDate = ""
             // 2024-07-01
            val newFormat = "MMM"
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val newDate = sdf.parse(date)
            sdf.applyPattern(newFormat)
            nwDate = sdf.format(newDate)
            return nwDate
        }else{
            return "IV"
        }
    }

    fun getMonthName1(date: String?): String {
        var nwDate = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val newFormat = "MMMM"
        val newDate: Date = sdf.parse(date)
        sdf.applyPattern(newFormat)
        nwDate = sdf.format(newDate)
        return nwDate
    }

    fun getMonthName2(date: String?): String {
        var nwDate = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val newFormat = "dd MMM"
        val newDate: Date = sdf.parse(date)
        sdf.applyPattern(newFormat)
        nwDate = sdf.format(newDate)
        return nwDate
    }

    fun getMonthlyName(date: String?): String {
        if (date != null) {
            var nwDate = ""
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val newFormat = "MMM, yyyy"
            val newDate: Date = sdf.parse(date)
            sdf.applyPattern(newFormat)
            nwDate = sdf.format(newDate)
            return nwDate
        } else {
            return "NA"
        }
    }

    fun getMonthNameWeeklyOrderSummary1(model: Placed?): String {
        if (model != null) {
            if (model.start_of_week != null && model.end_of_week != null) {
                var startWeek = ""
                var sdf = SimpleDateFormat("yyyy-MM-dd")
                val newFormat = "dd MMM"
                val newDate = sdf.parse(model.start_of_week)
                sdf.applyPattern(newFormat)
                startWeek = sdf.format(newDate)

                sdf = SimpleDateFormat("yyyy-MM-dd")
                var endWeek = ""
                val newDate1 = sdf.parse(model.end_of_week)
                sdf.applyPattern(newFormat)
                endWeek = sdf.format(newDate1)
                val final = "$startWeek - $endWeek"
                return final
            } else {
                return "NA - NA"
            }
        } else {
            return "NA - NA"
        }
    }

    fun getMonthNameWeeklyInsightPlacedOrders(model: Placed2?): String {
        if (model != null) {
            if (model.start_of_week != null && model.end_of_week != null) {
                var startWeek = ""
                var sdf = SimpleDateFormat("yyyy-MM-dd")
                val newFormat = "dd MMM"
                val newDate = sdf.parse(model.start_of_week)
                sdf.applyPattern(newFormat)
                startWeek = sdf.format(newDate)

                sdf = SimpleDateFormat("yyyy-MM-dd")
                var endWeek = ""
                val newDate1 = sdf.parse(model.end_of_week)
                sdf.applyPattern(newFormat)
                endWeek = sdf.format(newDate1)
                val final = "$startWeek - $endWeek"
                return final
            } else {
                return "NA - NA"
            }
        } else {
            return "NA - NA"
        }
    }

    fun getMonthNameWeeklyInsightRetailers(model: Retailer?): String {
        if (model != null) {
            if (model.start_of_week != null && model.end_of_week != null) {
                var startWeek = ""
                var sdf = SimpleDateFormat("yyyy-MM-dd")
                val newFormat = "dd MMM"
                val newDate = sdf.parse(model.start_of_week)
                sdf.applyPattern(newFormat)
                startWeek = sdf.format(newDate)

                sdf = SimpleDateFormat("yyyy-MM-dd")
                var endWeek = ""
                val newDate1 = sdf.parse(model.end_of_week)
                sdf.applyPattern(newFormat)
                endWeek = sdf.format(newDate1)
                val final = "$startWeek - $endWeek"
                return final
            } else {
                return "NA - NA"
            }
        } else {
            return "NA - NA"
        }
    }

    fun getMonthNameWeekly(model: New1?): String {
        if (model != null) {
            if (model.start_of_week != null && model.end_of_week != null) {
                var startWeek = ""
                var sdf = SimpleDateFormat("yyyy-MM-dd")
                val newFormat = "dd MMM"
                val newDate = sdf.parse(model.start_of_week)
                sdf.applyPattern(newFormat)
                startWeek = sdf.format(newDate)

                sdf = SimpleDateFormat("yyyy-MM-dd")
                var endWeek = ""
                val newDate1 = sdf.parse(model.end_of_week)
                sdf.applyPattern(newFormat)
                endWeek = sdf.format(newDate1)
                val final = "$startWeek - $endWeek"
                return final
            } else {
                return "NA - NA"
            }
        } else {
            return "NA - NA"
        }
    }

    fun getMonthNameWeeklyARPR(model: Arpr?): String {
        if (model != null) {
            if (model.start_of_week != null && model.end_of_week != null) {
                var startWeek = ""
                var sdf = SimpleDateFormat("yyyy-MM-dd")
                val newFormat = "dd MMM"
                val newDate = sdf.parse(model.start_of_week)
                sdf.applyPattern(newFormat)
                startWeek = sdf.format(newDate)

                sdf = SimpleDateFormat("yyyy-MM-dd")
                var endWeek = ""
                val newDate1 = sdf.parse(model.end_of_week)
                sdf.applyPattern(newFormat)
                endWeek = sdf.format(newDate1)
                val final = "$startWeek - $endWeek"
                return final
            } else {
                return "NA - NA"
            }
        } else {
            return "NA - NA"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setTextCapital"])
    fun setTextCapital(tv: TextView, text: String?) {
        if (text != null) {
            val space = " "
            val splitedStr = text.split(space)
            tv.text = splitedStr.joinToString(space) {
                it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
        } else {
            tv.text = "NA"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setLastOrderDate"])
    fun setLastOrderDate(tv: TextView, date: String?) {
        if (date != null) {
            var nwDate = ""
            //val oldFormat = "yyyy-MM-dd'T'HH:mm:ssz"
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
                tv.text = "$nwDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } else {
            tv.text = "NA"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["logoIcon"])
    fun setLogoIcon(imageView: ImageView, imageUrl: String?) {
        imageUrl?.let {
            Glide.with(imageView.context).load(it).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setSalesPersonName"])
    fun setSalesPersonName(tv: TextView, bean: List<SalesmanCityBeats>?) {

        if (bean != null) {
            val csvList = StringBuilder()
            for (s in bean.indices) {
                csvList.append(bean[s].full_name)
                if (s != bean.size - 1) {
                    csvList.append(", ")
                }
            }
            if (csvList.toString() == "") {
                tv.text = "Salesperson : Not assigned yet"
            } else {
                tv.text = "Salesperson : $csvList"
            }
        } else {
            tv.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setItemSetCountDetails"])
    fun setItemSetCountDetails(tv: TextView, bean: ProductOwnOrderDetails?) {
        if (bean != null) {
            val moq = bean.product_moq
            val orderedQuantity = bean.quantity

            if (moq != null) {
                val set = orderedQuantity / moq
                tv.text = "Set : $set"
            } else {
                tv.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setItemSetCount"])
    fun setItemSetCount(tv: TextView, bean: OwnOrderDetailsResponseModel?) {
        if (bean != null) {
            val moq = bean.products[0].product_moq
            val orderedQuantity = bean.products[0].ordered_quantity

            if (moq != null) {
                val set = orderedQuantity / moq
                tv.text = "Set : $set"
            } else {
                tv.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setItemSetCountNetDetails"])
    fun setItemSetCountNetDetails(tv: TextView, bean: ProductNetworkOrderDetails?) {
        if (bean != null) {
            val moq = bean.product_moq
            val orderedQuantity = bean.quantity

            if (moq != null) {
                val set = orderedQuantity / moq
                tv.text = "Set : $set"
            } else {
                tv.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setItemSetCount1"])
    fun setItemSetCount1(tv: TextView, bean: ProductOwnOrderDetails?) {
        if (bean != null) {
            val moq = bean.product_moq
            val orderedQuantity = bean.ordered_quantity

            if (moq != null) {
                val set = orderedQuantity / moq
                tv.text = "Set : $set"
            } else {
                tv.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["get_last_active"])
    fun getLastActive(textView: TextView, date: String?) {
        date?.let {
            var nwDate = ""
            //val oldFormat = "yyyy-MM-dd'T'HH:mm:ssz"
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "dd MMM"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
                textView.text = "Last log in on $nwDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["set_leave_type"])
    fun setLeaveType(textView: TextView, type: String?) {
        type?.let {
            when (it) {
                "full_day" -> {
                    textView.text = "Full Day"
                }

                "half_day" -> {
                    textView.text = "Half Day"
                }

                "ojt" -> {
                    textView.text = "On Job Training"
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["created_at"])
    fun setCreatedAt(textView: TextView, date: String?) {
        date?.let {
            var nwDate = ""
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "MMM dd,yyyy"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
                textView.text = "$nwDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["getAddress"])
    fun getAddress(textView: TextView, bean: ResultSalesLocation?) {
        bean?.let {
            val latLong = LatLng(bean.latitude, bean.longitude)
            textView.text = addressFromLatLong(textView.context, latLong)
        }
    }

    fun getDaysBetweenDates(startdate: Date, enddate: Date): List<Date> {
        val dates: MutableList<Date> = ArrayList()
        val calendar = Calendar.getInstance()
        calendar.time = startdate
        while (calendar.time.before(enddate)) {
            val result: Date = calendar.time
            dates.add(result)
            calendar.add(Calendar.DATE, 1)
        }
        return dates
    }

    private fun addressFromLatLong(context: Context, location: LatLng): String {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                location.latitude, location.longitude, 1
            )
            return addresses?.get(0)?.getAddressLine(0).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return "Address not available"
        }
        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    }

    @JvmStatic
    @BindingAdapter(value = ["setStartTime"])
    fun setStartTime(tv: TextView, date: String?) {
        if (date != null) {
            var nwDate = ""
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "hh:mm a"
            val sdf = SimpleDateFormat(oldFormat)
            val newDate: Date = sdf.parse(date)
            sdf.applyPattern(newFormat)
            nwDate = sdf.format(newDate)
            tv.text = nwDate
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setLastLoginTime"])
    fun setLastLoginTime(tv: TextView, date: String?) {
        if (date != null) {
            var nwDate = ""
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "dd MMM"
            val sdf = SimpleDateFormat(oldFormat)
            val newDate: Date = sdf.parse(date)
            sdf.applyPattern(newFormat)
            nwDate = sdf.format(newDate)
            tv.text = "Last log in on $nwDate "
        }
    }

    fun startEndTime(date: String?): String {
        date?.let {
            var nwDate = ""
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "hh a"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return nwDate
        } ?: run {
            return "-"
        }
    }

    var formatter = DecimalFormat("00")

    @JvmStatic
    @BindingAdapter(value = ["setOrderDate"])
    fun setOrderDate(textView: TextView, date: String?) {
        date?.let {
            var nwDate = ""
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "EEE, dd MMM yyyy"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
                textView.text = "Order Date: $nwDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setDeliveryDate"])
    fun setDeliveryDate(textView: TextView, date: String?) {
        date?.let {
            var nwDate = ""
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "EEE, dd MMM yyyy"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
                textView.text = "Deliver Date: $nwDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setCollectionDate"])
    fun setCollectionDate(textView: TextView, date: String?) {
        date?.let {
            var nwDate = ""
            val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val newFormat = "EEE dd MMM yyyy"
            val sdf = SimpleDateFormat(oldFormat)
            try {
                val newDate: Date = sdf.parse(date)
                sdf.applyPattern(newFormat)
                nwDate = sdf.format(newDate)
                textView.text = "Collection Date: $nwDate"
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setCPCount1"])
    fun setCPCount1(tv: TextView, count: Int?) {
        if (count != null && count != 0) tv.text = formatter.format(count).toString()
        else tv.text = "-"
    }

    @JvmStatic
    @BindingAdapter(value = ["salesNetworkArrowVisibility"])
    fun salesNetworkArrowVisibility(iv: ImageView, bean: SalesNetworkResponseModel) {
        if (bean.direct_reportee == 0 && bean.indirect_reportee == 0) {
            iv.visibility = View.GONE
        } else {
            iv.visibility = View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["arrowVisibility"])
    fun arrowVisibility(iv: ImageView, bean: NetworkCPCount1Item) {
        if (bean.direct_channels == 0 && bean.indirect_channels == 0) {
            iv.visibility = View.GONE
        } else {
            iv.visibility = View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setCardColor"])
    fun setCardColor(cv: CardView, assigned: Boolean) {
        if (assigned) {
            cv.setCardBackgroundColor(cv.context.getColor(R.color.white))
        } else {
            cv.setCardBackgroundColor(cv.context.getColor(R.color.line_color8_trans))
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["product_image"])
    fun setProductImage(imageView: ImageView, image: Int?) {
        Glide.with(imageView.context).load(image).into(imageView)
    }

    @JvmStatic
    @BindingAdapter(value = ["setImageWithFullUrl"])
    fun setImageWithFullUrl(imageView: ImageView, image: String?) {
        Glide.with(imageView.context).load(image).placeholder(R.drawable.ic_rocket_anim)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter(value = ["setImage1"])
    fun setImage1(imageView: ImageView, image: Int?) {
        Glide.with(imageView.context).load(image)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter(value = ["setImageWithFullUrl1"])
    fun setImageWithFullUrl1(imageView: ImageView, image: String?) {
        Glide.with(imageView.context).load(image).into(imageView)
    }

    @JvmStatic
    @BindingAdapter(value = ["setConstraintBackground"])
    fun setConstraintBackground(cv: ConstraintLayout, position: Int) {
        if (position == 0) {
            cv.background = cv.context.getDrawable(R.drawable.ic_gradient)
        } else {
            cv.background = cv.context.getDrawable(R.drawable.ic_white)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setHomeBarValues"])
    fun setHomeBarValues(pv: ProgressView, bar: Int) {
        pv.progress = bar.toFloat()
        pv.labelText = "$bar lakh"
    }


}