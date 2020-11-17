package com.lahmloon.occupations_isic_code

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lahmloon.occupations_isic_code.occupations.data.Occupations
import com.lahmloon.occupations_isic_code.databinding.ActivityMainBinding
import com.lahmloon.occupations_isic_code.view.OccupationListAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model: MainActivityModel by viewModels()
    private val listAdapter = OccupationListAdapter(this::onOccupationClicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSearch()
        setupList()

        model.occupations.observe(this, Observer { listAdapter.occupations = it })
    }

    private fun onOccupationClicked(occupations: Occupations) {
        Toast.makeText(
            this,
            String.format(
                "Id : ${occupations.id}\n" +
                        "Sub Of : ${occupations.idSubOf}\n" +
                        "Name Tha : ${occupations.nameTh}\n" +
                        "Name Eng : ${occupations.nameEng}"
            ), LENGTH_LONG
        ).show()
    }

    private fun setupSearch() {
        binding.search.doAfterTextChanged {
            model.search(it?.toString() ?: "")
        }
    }

    private fun setupList() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        val list = binding.occupations
        list.layoutManager = layoutManager
        list.adapter = listAdapter
        list.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(
            list.context,
            layoutManager.orientation
        )
        list.addItemDecoration(itemDecoration)
    }
}