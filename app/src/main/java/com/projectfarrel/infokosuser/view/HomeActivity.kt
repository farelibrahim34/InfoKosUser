package com.projectfarrel.infokosuser.view

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.projectfarrel.infokosuser.adapter.AdapterDataKos
import com.projectfarrel.infokosuser.adapter.AdapterDataKosPi
import com.projectfarrel.infokosuser.adapter.ViewPagerFragmentAdapter
import com.projectfarrel.infokosuser.databinding.ActivityHomeBinding
import com.projectfarrel.infokosuser.view.wrapper.FragmentVpHomeOne
import com.projectfarrel.infokosuser.view.wrapper.FragmentVpHomeThree
import com.projectfarrel.infokosuser.view.wrapper.FragmentVpHomeTwo
import com.projectfarrel.infokosuser.viewmodel.ViewModelDataKos
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator3
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    private lateinit var viewModel : ViewModelDataKos
    private var fragmentList = ArrayList<Fragment>()
    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: CircleIndicator3

    private lateinit var  handler: Handler
    private lateinit var runnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataKos()
        bannerHome()
        dataKosPi()

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable{
            var index = 0
            override fun run() {
                if (index == fragmentList.size)
                    index = 0
                Log.e("Runnable, ","$index")
                binding.viewPagerHome.setCurrentItem(index)
                index++
                handler.postDelayed(this,3000)
            }

        }
        handler.post(runnable)
    }
    fun dataKos(){
        viewModel = ViewModelProvider(this).get(ViewModelDataKos::class.java)
        viewModel.getDataKos().observe(this){
            if (it != null){
                binding.rvPutra.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL,false)
                val adapter = AdapterDataKos(it)
                binding.rvPutra.adapter = adapter

            }
        }
        viewModel.callApiDataKos()
    }
    fun dataKosPi(){
        viewModel.getDataKosPi().observe(this){
            if (it != null){
                binding.progressBar.visibility = View.GONE
                binding.rvPutri.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL,false)
                val adapter = AdapterDataKosPi(it)
                binding.rvPutri.adapter = adapter
            }
        }
        viewModel.callApiDataKosPi()
    }
    fun bannerHome(){
        castView()

        fragmentList.add(FragmentVpHomeOne())
        fragmentList.add(FragmentVpHomeTwo())
        fragmentList.add(FragmentVpHomeThree())

        viewPager.adapter = ViewPagerFragmentAdapter(this,fragmentList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        indicator.setViewPager(viewPager)

    }
    private fun castView() {
        viewPager = binding.viewPagerHome
        indicator = binding.indicatorBanner
    }
    override fun onBackPressed() {

        AlertDialog.Builder(this)
            .setTitle("Tutup Aplikasi")
            .setMessage("Yakin tutup dari aplikasi?")
            .setPositiveButton("Ya"){ dialogInterface: DialogInterface, i: Int ->
                finishAffinity()
            }
            .setNegativeButton("Tidak"){ dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .show()


    }
}