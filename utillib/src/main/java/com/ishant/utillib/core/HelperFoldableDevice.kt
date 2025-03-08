package com.ishant.utillib.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo

object HelperFoldableDevice {

    @SuppressLint("RestrictedApi")
    @Composable
    fun WindowConfigLayouts(
        landscapeUi: @Composable (() -> Unit)? = null,
        portraitUi: @Composable (() -> Unit)? = null,
        foldableUi: @Composable (() -> Unit)? = null,
        dualScreenUi: @Composable (() -> Unit)? = null,
        normalUi: @Composable (() -> Unit)
    ) {
        val context = LocalContext.current
        val windowInfoTracker = remember { WindowInfoTracker.getOrCreate(context) }
        val windowLayoutInfoFlow = windowInfoTracker.windowLayoutInfo(context as Activity)
        val windowLayoutInfo by windowLayoutInfoFlow.collectAsState(
            initial = WindowLayoutInfo(
                emptyList()
            )
        )
        val configuration = LocalConfiguration.current

        when {
            isFolded(windowLayoutInfo) -> {
                if (foldableUi == null) {
                    normalUi()
                } else {
                    foldableUi()
                }
            }

            isDualScreen(windowLayoutInfo) -> {
                if (dualScreenUi == null) {
                    normalUi()
                } else {
                    dualScreenUi()
                }
            }

            else -> {
                when (configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        if (landscapeUi == null) {
                            normalUi()
                        } else {
                            landscapeUi()
                        }

                    }

                    Configuration.ORIENTATION_PORTRAIT -> {
                        if (portraitUi == null) {
                            normalUi()
                        } else {
                            portraitUi()
                        }

                    }

                    Configuration.ORIENTATION_SQUARE -> {
                        if (landscapeUi == null) {
                            normalUi()
                        } else {
                            landscapeUi()
                        }

                    }

                    Configuration.ORIENTATION_UNDEFINED -> {
                        normalUi()
                    }
                }
            }
        }
    }

    private fun isFolded(info: WindowLayoutInfo): Boolean {
        return info.displayFeatures.any {
            it is FoldingFeature && it.state == FoldingFeature.State.HALF_OPENED
        }
    }

    private fun isDualScreen(info: WindowLayoutInfo): Boolean {
        return info.displayFeatures.any {
            it is FoldingFeature && it.state == FoldingFeature.State.FLAT
        }
    }
}