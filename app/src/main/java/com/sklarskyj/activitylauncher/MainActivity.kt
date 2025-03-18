package com.sklarskyj.activitylauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import com.sklarskyj.activitylauncher.databinding.ActivityMainBinding
import com.sklarskyj.activitylauncher.services.SettingsService
import com.sklarskyj.activitylauncher.ui.ActionBarSearch
import com.sklarskyj.activitylauncher.ui.DisclaimerDialogFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActionBarSearch {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var isAutomotive = false

    @Inject
    internal lateinit var settingsService: SettingsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if running on Android Automotive OS
        isAutomotive = packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        settingsService.applyLocaleConfiguration(baseContext)
        if (!settingsService.disclaimerAccepted) {
            DisclaimerDialogFragment().show(supportFragmentManager, "DisclaimerDialogFragment")
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Apply automotive-specific UI adjustments if needed
        if (isAutomotive) {
            applyAutomotiveUiAdjustments()
        }
    }

    /**
     * Apply UI adjustments for automotive displays:
     * - Larger touch targets
     * - Simplified UI for driving safety
     */
    private fun applyAutomotiveUiAdjustments() {
        // Use larger padding for better touch targets in vehicles
        binding.toolbar.setPadding(
            binding.toolbar.paddingLeft,
            binding.toolbar.paddingTop + 8,
            binding.toolbar.paddingRight,
            binding.toolbar.paddingBottom + 8
        )

        // Automotive-specific adjustments can be expanded here
        // This could include:
        // - Adjusting text sizes
        // - Increasing button sizes
        // - Simplifying UI for driving safety
    }

    override var onActionBarSearchListener: ((String) -> Unit)? = null
    private var actionBarSearchView: SearchView? = null
    override var actionBarSearchText: String
        get() = actionBarSearchView?.query?.toString().orEmpty()
        set(value) {
            actionBarSearchView?.setQuery(value, false)
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        actionBarSearchView = searchView
        searchView.queryHint = getText(R.string.filter_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onActionBarSearchListener?.invoke(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                onActionBarSearchListener?.invoke(query.orEmpty())
                return true
            }
        })

        // If running on automotive, we might want to adjust the search UI for better usability
        if (isAutomotive) {
            searchView.maxWidth = resources.displayMetrics.widthPixels / 2
            // Make the search icon larger for easier touch target in automotive
            val searchItem = menu.findItem(R.id.search)
            searchItem.icon?.setBounds(0, 0, 48, 48)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
