/*
 * Copyright (C) 2016 The Pure Nexus Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.dialer.settings;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.SlimSeekBarPreference;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.android.dialer.R;

import java.util.Arrays;

public class SpeakerSettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String PROXIMITY_AUTO_SPEAKER  = "proximity_auto_speaker";
    private static final String PROXIMITY_AUTO_SPEAKER_DELAY  = "proximity_auto_speaker_delay";
    private static final String PROXIMITY_AUTO_SPEAKER_INCALL_ONLY  = "proximity_auto_speaker_incall_only";
    private static final String BUTTON_PROXIMITY_KEY   = "button_proximity_key";

    private SwitchPreference mProxSpeaker;
    private SlimSeekBarPreference mProxSpeakerDelay;
    private SwitchPreference mProxSpeakerIncallOnly;
    private SwitchPreference mButtonProximity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.speaker_settings);
        final ContentResolver resolver = getActivity().getContentResolver();
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        mProxSpeaker = (SwitchPreference) findPreference(PROXIMITY_AUTO_SPEAKER);
        mProxSpeaker.setChecked(Settings.System.getInt(resolver,
                Settings.System.PROXIMITY_AUTO_SPEAKER, 0) == 1);
        mProxSpeaker.setOnPreferenceChangeListener(this);

        mProxSpeakerDelay = (SlimSeekBarPreference) findPreference(PROXIMITY_AUTO_SPEAKER_DELAY);
        int proxDelay = Settings.System.getInt(resolver,
                Settings.System.PROXIMITY_AUTO_SPEAKER_DELAY, 100);
        mProxSpeakerDelay.setDefault((proxDelay / 100) - 1);
        if (mProxSpeakerDelay != null) {
            mProxSpeakerDelay.setDefault(100);
            mProxSpeakerDelay.isMilliseconds(true);
            mProxSpeakerDelay.setInterval(1);
            mProxSpeakerDelay.minimumValue(100);
            mProxSpeakerDelay.multiplyValue(100);
            mProxSpeakerDelay.setOnPreferenceChangeListener(this);
        }

        mProxSpeakerIncallOnly = (SwitchPreference) findPreference(PROXIMITY_AUTO_SPEAKER_INCALL_ONLY);
        mProxSpeakerIncallOnly.setChecked(Settings.System.getInt(resolver,
                Settings.System.PROXIMITY_AUTO_SPEAKER_INCALL_ONLY, 0) == 1);
        mProxSpeakerIncallOnly.setOnPreferenceChangeListener(this);

        mButtonProximity = (SwitchPreference) findPreference(BUTTON_PROXIMITY_KEY);
        mButtonProximity.setChecked(Settings.System.getInt(resolver,
                Settings.System.BUTTON_PROXIMITY_KEY, 0) == 1);
        mButtonProximity.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mProxSpeaker) {
            Settings.System.putInt(resolver, Settings.System.PROXIMITY_AUTO_SPEAKER,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mProxSpeakerDelay) {
            int proxDelay = Integer.valueOf((String) newValue);
            Settings.System.putInt(resolver, Settings.System.PROXIMITY_AUTO_SPEAKER_DELAY, proxDelay);
            return true;
        } else if (preference == mProxSpeakerIncallOnly) {
            Settings.System.putInt(resolver, Settings.System.PROXIMITY_AUTO_SPEAKER_INCALL_ONLY,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mButtonProximity) {
            Settings.System.putInt(resolver, Settings.System.BUTTON_PROXIMITY_KEY,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        }
        return false;
    }

    private void updateProximityDelaySummary(int value) {
        String summary = getResources().getString(R.string.prox_auto_speaker_delay_summary, value);
        mProxSpeakerDelay.setSummary(summary);
    }
}
