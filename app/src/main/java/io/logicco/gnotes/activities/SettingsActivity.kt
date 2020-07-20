package io.logicco.gnotes.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.logicco.gnotes.R
import io.logicco.gnotes.utils.ThemeProvider

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private val themeProvider by lazy { ThemeProvider(requireContext()) }
        private val themePreference by lazy {
            findPreference<ListPreference>(getString(R.string.theme_preferences_key))
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            setThemePreference()
        }

        private fun setThemePreference() {
            themePreference?.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { _, newValue ->
                        if (newValue is String) {
                            val theme = themeProvider.getTheme(newValue)
                            AppCompatDelegate.setDefaultNightMode(theme)
                        }
                        true
                    }
            themePreference?.summaryProvider = Preference.SummaryProvider<ListPreference> { preference ->
                themeProvider.getThemeDescriptionForPreference(preference.value)
            }
        }
    }

}