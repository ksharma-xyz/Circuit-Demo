package xyz.ksharma.circuit_demo

import androidx.compose.runtime.Composable
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator

@Composable
fun CreateCircuit() {
    val circuit = Circuit.Builder()
        .addPresenterFactory(CityListPresenter.Factory(CityListRepository()))
        .addUi<CityListScreen, CityListScreen.State> { state, modifier -> CityListScreen(state, modifier) }
        .build()

    val backStack = rememberSaveableBackStack(CityListScreen)
    val navigator = rememberCircuitNavigator(backStack)
    CircuitCompositionLocals(circuit) {
        NavigableCircuitContent(navigator = navigator, backStack = backStack)
    }
}
