package we.rashchenko.aang.environment

import com.badlogic.gdx.math.Vector2
import we.rashchenko.chnn.environment.Environment

interface Environment2d<ActivationType>: Environment<ActivationType> {
    fun getPositions(): List<Vector2>
}