package we.rashchenko.aang.environment

import com.badlogic.gdx.math.Vector2
import java.util.*

class PingPong(private val boardSize: Int, ballSize: Int) : Environment2d<Boolean> {
    override val size = boardSize * boardSize
    private val ballHalfSize = (ballSize - 1) / 2
    private val random = Random()
    private fun getRandomCoordinate() = ballHalfSize + random.nextInt(boardSize - 2 * ballHalfSize)
    private var ballPosition = getRandomCoordinate() to getRandomCoordinate()
    private var ballVelocity = (if (random.nextBoolean()) -1 else 1) to (if (random.nextBoolean()) -1 else 1)
    private fun isBallTouchBorder(ballCoordinate: Int, ballVelocity: Int) =
        ballCoordinate + ballVelocity !in 0 until boardSize

    override fun tick() {
        if (isBallTouchBorder(ballPosition.first, ballVelocity.first)) {
            ballVelocity = ballVelocity.first * -1 to ballVelocity.second
        }
        if (isBallTouchBorder(ballPosition.second, ballVelocity.second)) {
            ballVelocity = ballVelocity.first to ballVelocity.second * -1
        }
        ballPosition = ballPosition.first + ballVelocity.first to ballPosition.second + ballVelocity.second
    }

    override fun getState(): List<Boolean> {
        return List(size) { i ->
            val x = i / boardSize
            val y = i % boardSize
            x in ballPosition.first - ballHalfSize..ballPosition.first + ballHalfSize &&
                    y in ballPosition.second - ballHalfSize..ballPosition.second + ballHalfSize
        }
    }

    override fun getPositions(): List<Vector2> {
        val xShift = 0.3f
        val yShift = 0.3f
        val xSize = 0.4f
        val ySize = 0.4f
        return List(size) { i ->
            val x = i / boardSize
            val y = i % boardSize
            Vector2(x * xShift + xSize, y * yShift + ySize)
        }
    }
}