package we.rashchenko.base

import we.rashchenko.networks.NeuralNetwork
import we.rashchenko.environments.Environment

/**
 * If some Collection<[Activity]> (for example [NeuralNetwork] or [Environment] want to make some if these activities
 *  public it may implement these interface to allow other instances to observe these activities.
 */
interface ObservableActivities {
	val activities: Collection<Activity>
}