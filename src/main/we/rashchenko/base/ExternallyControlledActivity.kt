package we.rashchenko.base

/**
 * [Activity] that can be set from outside.
 */
class ExternallyControlledActivity : Activity {
	override var active: Boolean = false
}