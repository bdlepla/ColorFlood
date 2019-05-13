package com.lepla.bryan.colorflood2

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.SerialDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class ColorFloodViewModel : ViewModel() {
    private val subscriptions = CompositeDisposable()
    private val gameWonLostSubscription = SerialDisposable()

    private val defaultData = GameStatsData(23, 23,
        StackKt(defaultGame(10, 10, 6)), 0, 0)

    // created a publish subject here so I can update the data from this controller
    private val dataChangedPublishSubject: BehaviorSubject<GameStatsData>  = BehaviorSubject.createDefault(defaultData)
    private val sharableDataChanged = dataChangedPublishSubject
    fun whenDataChanged(): Observable<GameStatsData> = sharableDataChanged.share()
        //.doOnNext{ Log.d(TAG, "whenDataChanged() got sharableDataChanged")}

    private val theBoardHasChanged: Observable<String> = whenDataChanged().map{it.undoBoards.peek()}
    //.doOnNext{ Log.d(TAG, "theBoardHasChanged got dataChangedPublishSubject()")}
    private val sharableWhenBoardChanged = theBoardHasChanged.share()
    //    .doOnNext{ Log.d(TAG, "sharableWhenBoardChanged got theBoardHasChanged") }
    fun whenBoardChanged(): Observable<String> = sharableWhenBoardChanged
    private val whenGameIsWon = whenBoardChanged()
        .doOnNext{ Log.d(TAG, "whenGameIsWon got whenBoardChanged") }
        .filter(ColorFloodModel::isCompleted).map{true}
        .doOnNext{ Log.d(TAG, "game won")}

    // This subject is because this data is initialized before Activity gets a change to subscribe to
    // the on step count observable. So, the initial value never gets to the activity.
    private val publishStepCountChanged: BehaviorSubject<Int> = BehaviorSubject.createDefault(0)
    private val sharableStepCountChanged = publishStepCountChanged.share()
    fun whenStepCountChanged(): Observable<Int> = sharableStepCountChanged
    private val whenGameIsLost = whenStepCountChanged()
        .doOnNext{ Log.d(TAG, "whenGameIsLost got whenStepCountChanged(): $it")}
        .filter{it == 0}.map{false}
        .doOnNext{ Log.d(TAG, "game lost")}

    private var data : GameStatsData = defaultData
    fun setSavedGame(savedData: String) = dataChangedPublishSubject.onNext(GameStatsSingleton.initialize(savedData, defaultData))

    fun init(
        whenColorClicked: Observable<Int>,
        whenStartGame: Observable<Pair<Boolean, StartGame>>
    ) {
        subscriptions.add(whenColorClicked
            .filter{it != data.firstColor}
            .map{ColorFloodModel.applyColor(data.undoBoards.peek(), it)}
            .subscribe{
                data.undoBoards.push(it)
                dataChangedPublishSubject.onNext(data.copy(currentStepCount = data.currentStepCount-1))
            })

        subscriptions.add(whenDataChanged().subscribe{ data = it } )
        whenDataChanged().map{it.currentStepCount}
        //    .doOnNext {Log.d(TAG, "stepCountChanged: $it")}
            .subscribe(publishStepCountChanged)
        // start the initial/continued game for this session

        subscriptions.add(gameWonLostSubscription)
        subscriptions.add(whenStartGame.subscribe(::howToRestart))
        setWonLostObservable()
    }

    private fun howToRestart(p:Pair<Boolean, StartGame>) {
        setWonLostObservable()
        when (p.second) {
            is NewGame -> startNewGame(p.first, 22)
            is RestartGame -> restartGame()
            is ResetStats -> resetStats()
        }
    }

    private fun setWonLostObservable() {
        gameWonLostSubscription.set(whenGameIsLost.ambWith(whenGameIsWon).subscribe(::completeGame))
    }

    private fun completeGame(win: Boolean) {
        val totalGames = data.totalGames + 1
        val totalWins = data.totalWins + if (win) 1 else 0
        val pct = if (totalGames > 0) totalWins * 100 / totalGames else 0
        val text = """
            |# of Games: $totalGames
            |# of Wins:  $totalWins
            |Percentage: $pct
            """.trimMargin()
        gameCompletedSubject.onNext(win to text)
    }

    private val gameCompletedSubject : PublishSubject<Pair<Boolean, String>> = PublishSubject.create()
    fun whenGameCompleted() : Observable<Pair<Boolean, String>> = gameCompletedSubject

    private fun startNewGame(win: Boolean, previousStartingStepCount: Int) {
        val nextStartingStepCount = previousStartingStepCount + if (win) -1 else 1
        val nextTotalGames = data.totalGames+1
        val nextWins = data.totalWins + if (win) 1 else 0
        dataChangedPublishSubject.onNext(GameStatsData(nextStartingStepCount,
            nextStartingStepCount,
            StackKt(defaultGame(10, 10, 6)), nextTotalGames, nextWins))
    }

    @Suppress("UNUSED_PARAMETER")
    private fun restartGame() {
        val firstBoard = StackKt(data.undoBoards.toList().first())

        dataChangedPublishSubject.onNext(GameStatsData(data.startingStepCount, data.startingStepCount,
            firstBoard, data.totalGames+1, data.totalWins))
    }

    @Suppress("UNUSED_PARAMETER")
    private fun resetStats() {
        // reset stats
        startNewGame(false, 22)
    }

    override fun onCleared() {
        subscriptions.dispose()
        super.onCleared()
    }

    companion object {
        private fun defaultGame(rows: Int, cols: Int, colors: Int) =
            encode((1..rows).map{(1..cols).map{(Math.random()*colors.toFloat()).toInt()}}, colors)
        private val TAG = ColorFloodViewModel::class.java.simpleName
    }
}