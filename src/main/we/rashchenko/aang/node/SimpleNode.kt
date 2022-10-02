package we.rashchenko.aang.node

import we.rashchenko.chnn.node.Node

class SimpleNode: Node<Boolean, Boolean> {
    private var activity: Boolean = false
    private val inhibitingInputIDs = mutableSetOf<Int>()
    private val activatingInputIDs = mutableSetOf<Int>()
    private var lastInputs = emptyMap<Int, Boolean>()
    override fun touch(inputs: Map<Int, Boolean>) {
        error = false
        val activeInputs = inputs.filterValues { it }
        val activation = activeInputs.count{ it.key in activatingInputIDs }
        val inhibition = activeInputs.count{ it.key in inhibitingInputIDs }
        activity = activation > inhibition
        lastInputs = inputs
    }

    private var error: Boolean = false
    override fun reportFeedbacks(feedbacks: List<Boolean>) {
        val positiveFeedback = feedbacks.count { it }
        val negativeFeedbacks = feedbacks.size - positiveFeedback
        error = negativeFeedbacks > positiveFeedback
    }

    override fun addInput(inputId: Int) {
        val targetActivation = if (error) getActivation() else !getActivation()
        if (targetActivation) {
            activatingInputIDs.add(inputId)
        } else {
            inhibitingInputIDs.add(inputId)
        }
    }

    override fun removeInput(inputId: Int) {
        inhibitingInputIDs.remove(inputId)
        activatingInputIDs.remove(inputId)
    }

    override fun getActivation(): Boolean {
        return activity
    }

    override fun getFeedbacks(): Map<Int, Boolean> {
        val activeInputs = lastInputs.filterValues { it }
        val inhibitingInputs = activeInputs.filterKeys { it in inhibitingInputIDs }
        val activatingInputs = activeInputs.filterKeys { it in activatingInputIDs }
        val targetActivation = if (error) getActivation() else !getActivation()
        return if (targetActivation){
            inhibitingInputs.mapValues { false } + activatingInputs.mapValues { true }
        } else{
            inhibitingInputs.mapValues { true } + activatingInputs.mapValues { false }
        }
    }

    override fun isExtraInputRequested(): Boolean {
        return error
    }

    override fun inputsRemoveRequested(): List<Int> {
        return emptyList()
    }
}