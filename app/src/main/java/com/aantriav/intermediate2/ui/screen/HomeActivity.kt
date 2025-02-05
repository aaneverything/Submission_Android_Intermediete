package com.aantriav.intermediate2.ui.screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.aantriav.intermediate2.R
import com.aantriav.intermediate2.data.pref.UserPreferences
import com.aantriav.intermediate2.databinding.ActivityHomeBinding
import com.aantriav.intermediate2.ui.MainActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userPreferences: UserPreferences
    private var doubleBackToExitPressed = false

    private var selectedFragmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        if (savedInstanceState != null) {
            selectedFragmentIndex = savedInstanceState.getInt("selectedFragmentIndex", 0)
        }

        binding.mainBottomBar.itemActiveIndex = selectedFragmentIndex
        loadFragment(selectedFragmentIndex)
        setupUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedFragmentIndex", selectedFragmentIndex)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.bottom_menu, it)
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }

            R.id.action_logout -> {
                userPreferences.clearToken()
                Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }, 1000)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupUI() {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressed) {
                    finishAffinity()
                    return
                }

                doubleBackToExitPressed = true
                Toast.makeText(this@HomeActivity, getString(R.string.press_again_for_exit), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressed = false
                }, 3000)
            }
        })

        binding.mainBottomBar.setOnItemSelectedListener { position ->
            if (position != selectedFragmentIndex) {
                selectedFragmentIndex = position
                loadFragment(position)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun loadFragment(index: Int) {
        val selectedFragment = when (index) {
            0 -> StoryFragment()
            1 -> StoryMapsFragment()
            else -> LogoutFragment()
        }
        replaceFragment(selectedFragment)
    }

}
