package we.rashchenko.chnn.environment

import we.rashchenko.chnn.neuron.Activity
import we.rashchenko.utility.Ticking
import we.rashchenko.utility.graph.NodesCollection

interface Environment<ActivationType> : NodesCollection<Activity<ActivationType>>, Ticking

