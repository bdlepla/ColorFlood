package com.lepla.bryan.colorflood2

sealed class StartGame
object NewGame : StartGame()
object RestartGame : StartGame()
object ResetStats : StartGame()
