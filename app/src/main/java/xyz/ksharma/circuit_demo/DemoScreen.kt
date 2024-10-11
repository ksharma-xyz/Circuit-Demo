package xyz.ksharma.circuit_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class DemoScreen(val data: String) : Screen {
    data class State(val data: String, val eventSink: (Event) -> Unit) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        data object BackClicked : Event
    }
}

class DemoPresenter(
    private val screen: DemoScreen,
    private val navigator: Navigator,
    private val demoRepository: DemoRepository,
) : Presenter<DemoScreen.State> {
    @Composable
    override fun present(): DemoScreen.State {
        val email = demoRepository.getEmail(screen.data)
        return DemoScreen.State(email) { event ->
            when (event) {
                DemoScreen.Event.BackClicked -> navigator.pop()
            }
        }
    }

    class Factory(private val demoRepository: DemoRepository) : Presenter.Factory {
        override fun create(
            screen: Screen,
            navigator: Navigator,
            context: CircuitContext,
        ): Presenter<*>? {
            return when (screen) {
                is DemoScreen -> return DemoPresenter(screen, navigator, demoRepository)
                else -> null
            }
        }
    }
}

@Composable
fun DemoScreenUI(state: DemoScreen.State, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .background(color = Color.Green.copy(alpha = 0.5f))
    ) {
        Text(text = state.data,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    state.eventSink(DemoScreen.Event.BackClicked)
                }
        )
    }
}
