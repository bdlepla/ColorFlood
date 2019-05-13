package com.lepla.bryan.colorflood2

import android.util.Base64
import android.util.Base64.DEFAULT

data class GameStatsData(val startingStepCount: Int,
                         val currentStepCount: Int,
                         val undoBoards: StackKt<String>,
                         val totalGames: Int,
                         val totalWins: Int) {
    val firstColor = undoBoards.peek().substring(5).take(1).toInt()
}

fun GameStatsData.toBase64String(): String = Base64.encodeToString(
    GameStatsProto.GameStats.newBuilder()
    .setCurrentStepCount(this.currentStepCount)
    .setStartingStepCount(this.startingStepCount)
    .addAllUndoBoardStack(this.undoBoards.toList())
    .setTotalGames(this.totalGames)
    .setTotalWins(totalWins)
    .build()
    .toByteArray(), DEFAULT)

object GameStatsSingleton {
    fun initialize(base64GameStats: String, defaultGameStats:GameStatsData) =
        decodeOrCreateGameStatsProto(base64GameStats, defaultGameStats)

    private fun decodeOrCreateGameStatsProto(base64GameStats: String, defaultGameStats:GameStatsData): GameStatsData {
        if (base64GameStats.isNotEmpty()) {
            try {
                val byteArray = Base64.decode(base64GameStats, DEFAULT)
                return GameStatsProto.GameStats.parseFrom(byteArray).toGameStatsData()
            } catch (ex:Exception) {} // ignore
        }

        return defaultGameStats
    }

    private fun GameStatsProto.GameStats.toGameStatsData() =
        GameStatsData(startingStepCount, currentStepCount,
            undoBoardStackList.toStack(), totalGames, totalWins)

    private fun <E> List<E>.toStack() = StackKt(this)
}

