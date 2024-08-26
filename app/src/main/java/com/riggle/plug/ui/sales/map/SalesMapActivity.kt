package com.riggle.plug.ui.sales.map

import android.icu.util.Calendar
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.riggle.plug.R
import com.riggle.plug.data.model.ResultSalesLocation
import com.riggle.plug.databinding.ActivitySalesMapBinding
import com.riggle.plug.ui.base.BaseActivity
import com.riggle.plug.ui.base.BaseViewModel
import com.riggle.plug.ui.home.sales.salesInsights.saleLocations.SalesLocationsFragment
import com.riggle.plug.utils.Status
import com.riggle.plug.utils.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class SalesMapActivity : BaseActivity<ActivitySalesMapBinding>(), OnMapReadyCallback {

    private val viewModel: SalesMapActivityVM by viewModels()
    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    companion object{
        var id1 = ""
    }
    override fun getLayoutResource(): Int {
        return R.layout.activity_sales_map
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getList()
        initObservers()
    }

    private fun getList() {
        val cal = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val query = HashMap<String, String>()
        query["id"] = id1

        /*
        query["date"] = format.format(cal.time)
*/
        query["date"] = "2024-06-03"
        query["expand"] = "user"
        query["fields"] = "created_at,id,latitude,longitude,timestamp,type"

        viewModel.getSalesLocations(
            sharedPrefManager.getSessionId()!!, query
        )
    }

    private var locations = ArrayList<LatLng>()
    private fun initObservers() {
        viewModel.obrSalesLocations.observe(this) {
            when (it?.status) {
                Status.LOADING -> {
                    showHideLoader(true)
                }

                Status.SUCCESS -> {
                    if (it.data?.results?.isNotEmpty() == true) {
                        for (i in 0 until it.data.results.size) {
                            if (it.data.results[i].latitude != null && it.data.results[i].longitude != null){
                            locations.add(LatLng(it.data.results[i].latitude!!,it.data.results[i].longitude!!))
                                }
                            }
                        showHideLoader(false)

                        for (location in locations) {
                            googleMap.addMarker(MarkerOptions().position(location))
                        }
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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

}