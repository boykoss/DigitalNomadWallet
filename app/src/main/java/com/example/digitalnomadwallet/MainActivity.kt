package com.example.digitalnomadwallet

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.digitalnomadwallet.databinding.ActivityMainBinding
import com.example.digitalnomadwallet.db.TransactionsDatabase
import com.example.digitalnomadwallet.repository.TransactionsRepository
import com.example.digitalnomadwallet.viewmodel.TransactionsViewModel
import com.example.digitalnomadwallet.viewmodel.TransactionsViewModelProvFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var viewModel: TransactionsViewModel
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = TransactionsRepository(TransactionsDatabase.getInstance(this))

        val viewModelProviderFactory = TransactionsViewModelProvFactory(repository, application)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[TransactionsViewModel::class.java]
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        drawerLayout = binding.drawerLayout
        navView = binding.drawerNavView
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(
            navController.graph, drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, drawerLayout)


    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}