package we.rashchenko.chnn.environment

import we.rashchenko.chnn.node.Activity

interface Environment<ActivationType>{
    fun getActivities(): List<Activity<ActivationType>>
}