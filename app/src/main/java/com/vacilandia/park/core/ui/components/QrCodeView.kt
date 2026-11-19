package com.vacilandia.park.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vacilandia.park.core.ui.theme.BorderDefault
import com.vacilandia.park.core.ui.theme.InkPrimary

@Composable
fun QrCodeView(
    payload: String,
    modifier: Modifier = Modifier,
    size: Dp = 156.dp
) {
    val matrix = remember(payload) { buildQrMatrix(payload) }
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.White)
            .border(1.5.dp, BorderDefault, shape)
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val count = matrix.size
            val cellSize = this.size.width / count

            for (row in 0 until count) {
                for (col in 0 until count) {
                    if (matrix[row][col]) {
                        drawRect(
                            color = InkPrimary,
                            topLeft = Offset(col * cellSize, row * cellSize),
                            size = Size(cellSize + 0.5f, cellSize + 0.5f)
                        )
                    }
                }
            }
        }
    }
}

private fun buildQrMatrix(seedText: String): Array<BooleanArray> {
    val n = 21
    val grid = Array(n) { BooleanArray(n) }

    fun drawFinder(startRow: Int, startCol: Int) {
        for (r in 0 until 7) {
            for (c in 0 until 7) {
                val isBorder = r == 0 || r == 6 || c == 0 || c == 6
                val isCenter = r in 2..4 && c in 2..4
                grid[startRow + r][startCol + c] = isBorder || isCenter
            }
        }
    }

    drawFinder(0, 0)
    drawFinder(0, n - 7)
    drawFinder(n - 7, 0)

    // Patrones de sincronización (timing)
    for (i in 8 until n - 8) {
        grid[6][i] = i % 2 == 0
        grid[i][6] = i % 2 == 0
    }

    // Módulos de datos deterministas según código de reserva
    var hash = seedText.hashCode()
    for (r in 0 until n) {
        for (c in 0 until n) {
            val inFinder = (r < 8 && c < 8) || (r < 8 && c >= n - 8) || (r >= n - 8 && c < 8)
            val inTiming = r == 6 || c == 6
            if (!inFinder && !inTiming) {
                hash = (hash * 1103515245 + 12345 + r * 31 + c * 17)
                grid[r][c] = ((hash ushr 16) and 1) == 1
            }
        }
    }
    return grid
}
