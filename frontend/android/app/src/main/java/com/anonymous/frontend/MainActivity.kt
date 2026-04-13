package com.wave.app

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
  private lateinit var pageDashboard: View
  private lateinit var pageDecks: View
  private lateinit var pagePads: View
  private lateinit var pageSetlist: View
  private lateinit var navDashboard: TextView
  private lateinit var navDecks: TextView
  private lateinit var navPads: TextView
  private lateinit var navSetlist: TextView
  private lateinit var dashboardHits: TextView
  private lateinit var dashboardTracks: TextView
  private lateinit var dashboardMix: TextView
  private lateinit var dashboardCue: TextView
  private lateinit var deckABpm: TextView
  private lateinit var deckBBpm: TextView
  private lateinit var crossfaderLabel: TextView
  private lateinit var padHitsLabel: TextView
  private lateinit var lastPadLabel: TextView
  private lateinit var trackInput: EditText
  private lateinit var notesInput: EditText
  private lateinit var notesStatus: TextView
  private lateinit var setlistContainer: LinearLayout
  private lateinit var mixModeWarm: Button
  private lateinit var mixModePeak: Button
  private lateinit var mixModeLate: Button

  private val tracks = mutableListOf<String>()
  private val padNames = listOf("Kick", "Snare", "Clap", "Hat", "Bass", "Vox", "FX", "Loop")
  private val modeButtons by lazy { listOf(mixModeWarm, mixModePeak, mixModeLate) }
  private val preferences by lazy { getSharedPreferences("wave_dj_state", Context.MODE_PRIVATE) }
  private var sessionHits = 0
  private var mixMode = "Warm Up"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    bindViews()
    loadState()
    setupNavigation()
    setupDeckControls()
    setupPads()
    setupSetlist()
    setupActions()
    updateDeckDisplays()
    updateMixModeButtons()
    updateDashboard()
    updatePadSummary(null)
    renderTracks()
    notesInput.setText(preferences.getString(KEY_NOTES, ""))
    showPage(pageDashboard, navDashboard)
  }

  private fun bindViews() {
    pageDashboard = findViewById(R.id.page_dashboard)
    pageDecks = findViewById(R.id.page_decks)
    pagePads = findViewById(R.id.page_pads)
    pageSetlist = findViewById(R.id.page_setlist)
    navDashboard = findViewById(R.id.nav_dashboard)
    navDecks = findViewById(R.id.nav_decks)
    navPads = findViewById(R.id.nav_pads)
    navSetlist = findViewById(R.id.nav_setlist)
    dashboardHits = findViewById(R.id.stat_hits_value)
    dashboardTracks = findViewById(R.id.stat_tracks_value)
    dashboardMix = findViewById(R.id.stat_mix_value)
    dashboardCue = findViewById(R.id.dashboard_cue_text)
    deckABpm = findViewById(R.id.deck_a_bpm)
    deckBBpm = findViewById(R.id.deck_b_bpm)
    crossfaderLabel = findViewById(R.id.crossfader_value)
    padHitsLabel = findViewById(R.id.pad_hits_value)
    lastPadLabel = findViewById(R.id.last_pad_value)
    trackInput = findViewById(R.id.track_input)
    notesInput = findViewById(R.id.notes_input)
    notesStatus = findViewById(R.id.notes_status)
    setlistContainer = findViewById(R.id.setlist_container)
    mixModeWarm = findViewById(R.id.mode_warm)
    mixModePeak = findViewById(R.id.mode_peak)
    mixModeLate = findViewById(R.id.mode_late)
  }

  private fun loadState() {
    sessionHits = preferences.getInt(KEY_SESSION_HITS, 0)
    mixMode = preferences.getString(KEY_MIX_MODE, "Warm Up") ?: "Warm Up"
    val storedTracks = preferences.getString(KEY_TRACKS, "[]") ?: "[]"
    val json = JSONArray(storedTracks)
    repeat(json.length()) { index -> tracks.add(json.optString(index)) }
  }

  private fun setupNavigation() {
    navDashboard.setOnClickListener { showPage(pageDashboard, navDashboard) }
    navDecks.setOnClickListener { showPage(pageDecks, navDecks) }
    navPads.setOnClickListener { showPage(pagePads, navPads) }
    navSetlist.setOnClickListener { showPage(pageSetlist, navSetlist) }
  }

  private fun setupDeckControls() {
    findViewById<Button>(R.id.deck_a_minus).setOnClickListener { changeBpm(KEY_DECK_A_BPM, -1) }
    findViewById<Button>(R.id.deck_a_plus).setOnClickListener { changeBpm(KEY_DECK_A_BPM, 1) }
    findViewById<Button>(R.id.deck_b_minus).setOnClickListener { changeBpm(KEY_DECK_B_BPM, -1) }
    findViewById<Button>(R.id.deck_b_plus).setOnClickListener { changeBpm(KEY_DECK_B_BPM, 1) }

    val crossfader = findViewById<SeekBar>(R.id.crossfader_seek)
    crossfader.progress = preferences.getInt(KEY_CROSSFADER, 50)
    crossfader.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        preferences.edit().putInt(KEY_CROSSFADER, progress).apply()
        updateCrossfaderLabel(progress)
      }

      override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
      override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    })
    updateCrossfaderLabel(crossfader.progress)

    listOf(mixModeWarm, mixModePeak, mixModeLate).forEach { button ->
      button.setOnClickListener {
        mixMode = button.text.toString()
        preferences.edit().putString(KEY_MIX_MODE, mixMode).apply()
        updateMixModeButtons()
        updateDashboard()
      }
    }
  }

  private fun setupPads() {
    val ids = listOf(
      R.id.pad_1, R.id.pad_2, R.id.pad_3, R.id.pad_4,
      R.id.pad_5, R.id.pad_6, R.id.pad_7, R.id.pad_8,
    )
    ids.forEachIndexed { index, id ->
      findViewById<Button>(id).setOnClickListener { view ->
        sessionHits += 1
        preferences.edit().putInt(KEY_SESSION_HITS, sessionHits).apply()
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        view.animate().scaleX(0.96f).scaleY(0.96f).setDuration(60).withEndAction {
          view.animate().scaleX(1f).scaleY(1f).setDuration(60).start()
        }.start()
        updatePadSummary(padNames[index])
        updateDashboard()
      }
    }
  }

  private fun setupSetlist() {
    findViewById<Button>(R.id.add_track_button).setOnClickListener {
      val track = trackInput.text.toString().trim()
      if (track.isNotEmpty()) {
        tracks.add(0, track)
        trackInput.text = null
        persistTracks()
        renderTracks()
        updateDashboard()
      }
    }

    findViewById<Button>(R.id.clear_setlist_button).setOnClickListener {
      tracks.clear()
      persistTracks()
      renderTracks()
      updateDashboard()
    }

    findViewById<Button>(R.id.save_notes_button).setOnClickListener {
      preferences.edit().putString(KEY_NOTES, notesInput.text.toString()).apply()
      notesStatus.text = getString(R.string.wave_notes_saved)
    }

    notesInput.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        notesStatus.text = getString(R.string.wave_notes_unsaved)
      }
      override fun afterTextChanged(s: Editable?) = Unit
    })
  }

  private fun setupActions() {
    findViewById<Button>(R.id.open_decks_button).setOnClickListener { showPage(pageDecks, navDecks) }
    findViewById<Button>(R.id.open_pads_button).setOnClickListener { showPage(pagePads, navPads) }
    findViewById<Button>(R.id.open_setlist_button).setOnClickListener { showPage(pageSetlist, navSetlist) }
    findViewById<Button>(R.id.reset_session_button).setOnClickListener {
      sessionHits = 0
      mixMode = "Warm Up"
      preferences.edit()
        .putInt(KEY_SESSION_HITS, 0)
        .putString(KEY_MIX_MODE, mixMode)
        .putInt(KEY_DECK_A_BPM, 124)
        .putInt(KEY_DECK_B_BPM, 128)
        .putInt(KEY_CROSSFADER, 50)
        .apply()
      updateDeckDisplays()
      updateMixModeButtons()
      updatePadSummary(null)
      updateDashboard()
      showPage(pageDashboard, navDashboard)
    }
  }

  private fun showPage(page: View, nav: TextView) {
    listOf(pageDashboard, pageDecks, pagePads, pageSetlist).forEach { it.visibility = View.GONE }
    page.visibility = View.VISIBLE
    listOf(navDashboard, navDecks, navPads, navSetlist).forEach { button ->
      button.setBackgroundResource(if (button == nav) R.drawable.nav_active_background else R.drawable.nav_inactive_background)
      button.setTextColor(getColor(if (button == nav) android.R.color.white else R.color.wave_nav_inactive))
    }
  }

  private fun changeBpm(key: String, delta: Int) {
    val current = preferences.getInt(key, if (key == KEY_DECK_A_BPM) 124 else 128)
    val updated = (current + delta).coerceIn(90, 160)
    preferences.edit().putInt(key, updated).apply()
    updateDeckDisplays()
    updateDashboard()
  }

  private fun updateDeckDisplays() {
    deckABpm.text = preferences.getInt(KEY_DECK_A_BPM, 124).toString()
    deckBBpm.text = preferences.getInt(KEY_DECK_B_BPM, 128).toString()
    updateCrossfaderLabel(preferences.getInt(KEY_CROSSFADER, 50))
  }

  private fun updateCrossfaderLabel(progress: Int) {
    val label = when {
      progress < 45 -> "Deck A +${50 - progress}%"
      progress > 55 -> "Deck B +${progress - 50}%"
      else -> "Balanced blend"
    }
    crossfaderLabel.text = label
  }

  private fun updateMixModeButtons() {
    modeButtons.forEach { button ->
      val selected = button.text.toString() == mixMode
      button.setBackgroundResource(if (selected) R.drawable.nav_active_background else R.drawable.nav_inactive_background)
      button.setTextColor(getColor(if (selected) android.R.color.white else R.color.wave_nav_inactive))
    }
  }

  private fun updatePadSummary(lastPad: String?) {
    padHitsLabel.text = sessionHits.toString()
    lastPadLabel.text = lastPad ?: getString(R.string.wave_no_pad_yet)
  }

  private fun updateDashboard() {
    dashboardHits.text = sessionHits.toString()
    dashboardTracks.text = tracks.size.toString()
    dashboardMix.text = mixMode
    dashboardCue.text = if (tracks.isEmpty()) getString(R.string.wave_empty_setlist) else tracks.first()
  }

  private fun renderTracks() {
    setlistContainer.removeAllViews()
    if (tracks.isEmpty()) {
      val empty = TextView(this)
      empty.text = getString(R.string.wave_setlist_hint)
      empty.setTextColor(getColor(R.color.wave_nav_inactive))
      empty.textSize = 14f
      setlistContainer.addView(empty)
      return
    }

    tracks.forEachIndexed { index, track ->
      val row = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        setBackgroundResource(R.drawable.surface_card)
        setPadding(24, 22, 24, 22)
      }
      val nameView = TextView(this).apply {
        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        text = track
        textSize = 15f
        setTextColor(getColor(android.R.color.white))
      }
      val removeButton = Button(this).apply {
        text = getString(R.string.wave_remove)
        setBackgroundResource(R.drawable.subtle_button)
        setTextColor(getColor(android.R.color.white))
        setOnClickListener {
          tracks.removeAt(index)
          persistTracks()
          renderTracks()
          updateDashboard()
        }
      }
      row.addView(nameView)
      row.addView(removeButton)
      val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT,
      ).apply { bottomMargin = 18 }
      setlistContainer.addView(row, params)
    }
  }

  private fun persistTracks() {
    val json = JSONArray()
    tracks.forEach { json.put(it) }
    preferences.edit().putString(KEY_TRACKS, json.toString()).apply()
  }

  companion object {
    private const val KEY_DECK_A_BPM = "deck_a_bpm"
    private const val KEY_DECK_B_BPM = "deck_b_bpm"
    private const val KEY_CROSSFADER = "crossfader"
    private const val KEY_TRACKS = "tracks"
    private const val KEY_NOTES = "notes"
    private const val KEY_SESSION_HITS = "session_hits"
    private const val KEY_MIX_MODE = "mix_mode"
  }
}
