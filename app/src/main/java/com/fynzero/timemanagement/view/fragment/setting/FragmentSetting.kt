package com.fynzero.timemanagement.view.fragment.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.notification.AlarmReceiver

class FragmentSetting : PreferenceFragmentCompat() {

    private lateinit var switchPreference: SwitchPreference
    private lateinit var language: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference)

        val alarmReceiver = AlarmReceiver()

        switchPreference = findPreference(resources.getString(R.string.notification))!!
        language = findPreference(resources.getString(R.string.translate))!!

        switchPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, _ ->
                if (switchPreference.isChecked) {
                    activity?.let { alarmReceiver.cancelAlarm(it) }
                    Toast.makeText(activity, R.string.reminder_off, Toast.LENGTH_SHORT).show()
                    switchPreference.isChecked = false
                } else {
                    activity?.let { alarmReceiver.setRepeatingAlarm(it) }
                    Toast.makeText(activity, R.string.reminder_on, Toast.LENGTH_SHORT).show()
                    switchPreference.isChecked = true
                }

                false
            }

        language.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            false
        }
    }
}