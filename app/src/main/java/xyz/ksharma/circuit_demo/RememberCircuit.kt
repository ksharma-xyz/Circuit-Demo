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
        .addPresenterFactory(DemoPresenter.Factory(DemoRepository()))
        .addUi<DemoScreen, DemoScreen.State> { state, modifier -> DemoScreenUI(state, modifier) }
        .build()

    val backStack = rememberSaveableBackStack(DemoScreen("Hello"))
    val navigator = rememberCircuitNavigator(backStack)
    CircuitCompositionLocals(circuit) {
        NavigableCircuitContent(navigator = navigator, backStack = backStack)
    }
}
