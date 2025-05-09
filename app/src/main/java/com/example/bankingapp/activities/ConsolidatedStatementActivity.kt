package com.example.bankingapp.activities

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.DocumentAdapter
import com.example.bankingapp.classes.Document
import com.example.bankingapp.databinding.ActivityConsolidatedStatementBinding

class ConsolidatedStatementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsolidatedStatementBinding
    private lateinit var adapter: DocumentAdapter
    private var documents = mutableListOf<Document>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConsolidatedStatementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        listeners()

    }

    private fun initialisers() {
        setupToolbar()
        setupYearSpinner()
        setupRecyclerView()
    }

    private fun listeners() {

    }

    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.profileButton.setOnClickListener {
            // Handle profile button click
        }
    }

    private fun setupYearSpinner() {
        val years = resources.getStringArray(R.array.financial_years)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.yearSpinner.adapter = spinnerAdapter
        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedYear = years[position]
                updateDocumentsList(selectedYear)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = DocumentAdapter(documents) { position ->
            handleDocumentClick(position)
        }

        binding.documentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConsolidatedStatementActivity)
            adapter = this@ConsolidatedStatementActivity.adapter

            // Apply layout animation
            val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
                this@ConsolidatedStatementActivity,
                R.anim.layout_animation
            )
            layoutAnimation = animation
        }

        // Initialize with first year
        val firstYear = resources.getStringArray(R.array.financial_years)[0]
        updateDocumentsList(firstYear)
    }

    private fun updateDocumentsList(year: String) {
        // In a real app, you would fetch documents for the selected year from a database or API
        documents.clear()

        // For demo purposes, add some sample documents
        if (year == "FY 23-24") {
            documents.add(Document(year, "208012"))
            documents.add(Document(year, "208012"))
        } else if (year == "FY 22-23") {
            documents.add(Document(year, "195634"))
            documents.add(Document(year, "195635"))
            documents.add(Document(year, "195636"))
        } else {
            documents.add(Document(year, "Sample ID"))
        }

        adapter.notifyDataSetChanged()
        binding.documentsRecyclerView.scheduleLayoutAnimation()
    }

    private fun handleDocumentClick(position: Int) {
        val document = documents[position]
        // Handle document click, e.g., open PDF viewer
        // For example: startActivity(Intent(this, PdfViewerActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}