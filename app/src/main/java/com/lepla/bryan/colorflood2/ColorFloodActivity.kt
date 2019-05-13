package com.lepla.bryan.colorflood2

import android.content.Context

import android.os.Bundle
//import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_color_flood.*

class ColorFloodActivity : AppCompatActivity() {

    private lateinit var gameViewModel: ColorFloodViewModel

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val savedGameData = savedInstanceState
            ?.getString(BOARD_STATE)
            .doIfEmpty {
                getPreferences(Context.MODE_PRIVATE)
                    ?.getString(BOARD_STATE, "")
                    ?: ""
            }

        setContentView(R.layout.activity_color_flood)
        gameViewModel = ViewModelProviders.of(this).get(ColorFloodViewModel::class.java)
        gameViewModel.init(colorFloodSelectView.whenColorClicked(), whenStartGame)
        subscriptions.add(gameViewModel.whenStepCountChanged()
        //    .doOnNext { Log.d(TAG, "when step count change: $it") }
            .subscribe(::updateTitleForStepCount))
        subscriptions.add(gameViewModel.whenDataChanged().subscribe(::saveData))
        colorFloodPlayView.initialize(gameViewModel.whenBoardChanged())
        subscriptions.add(gameViewModel.whenGameCompleted().subscribe(::gameCompleted))
        gameViewModel.setSavedGame(savedGameData)
    }

    override fun onDestroy() {
        subscriptions.dispose()
        super.onDestroy()
    }

    private fun updateTitleForStepCount(stepCount: Int) {
        supportActionBar?.let { title = "ColorFlood Steps left: $stepCount" }
    }

    private fun saveData(data: GameStatsData) {
        getPreferences(Context.MODE_PRIVATE)
            .edit()
            .putString(BOARD_STATE, data.toBase64String())
            .apply()
    }

    private val startGameSubject : PublishSubject<Pair<Boolean, StartGame>> = PublishSubject.create()
    private val whenStartGame : Observable<Pair<Boolean, StartGame>> = startGameSubject

    private fun gameCompleted(p : Pair<Boolean, String>) {
        val text = p.second
        val won = p.first
        AlertDialog.Builder(this)
            .setTitle("Play again?")
            .setMessage(text)
            .setPositiveButton("Yes")   { _, _ -> startGame(won, NewGame) }
            .setNegativeButton("Retry") { _, _ -> startGame(won, RestartGame) }
            .setNeutralButton( "Reset") { _, _ -> startGame(won, ResetStats) }
            .setCancelable(false)
            .create()
            .also {it.setCanceledOnTouchOutside(false)}
            .show()
    }

    private fun startGame(won:Boolean, how: StartGame) {
        startGameSubject.onNext(won to how)
    }

    companion object {
        //private val TAG = ColorFloodActivity::class.java.simpleName
        const val BOARD_STATE = "BoardState"
    }
}

