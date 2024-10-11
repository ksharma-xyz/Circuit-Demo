package xyz.ksharma.circuit_demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.parcelize.Parcelize

@Parcelize
data object CityListScreen : Screen {
    sealed interface State : CircuitUiState {

        data object Loading : State

        data object NoData : State

        data class Success(val cities: ImmutableList<String>, val eventSink: (Event) -> Unit) :
            State

        sealed interface Event : CircuitUiEvent {
            data object BackClicked : Event
            data object CityClicked : Event
        }
    }
}

class CityListPresenter(
    private val screen: CityListScreen,
    private val navigator: Navigator,
    private val cityListRepository: CityListRepository,
) : Presenter<CityListScreen.State> {
    @Composable
    override fun present(): CityListScreen.State {

        val cities by produceState<ImmutableList<String>?>(initialValue = null) {
            delay(2000)
            value = persistentListOf()
            delay(2000)
            value = cityListRepository.getAllCities().toImmutableList()
        }

        return when {
            cities == null -> CityListScreen.State.Loading

            cities?.isEmpty() == true -> CityListScreen.State.NoData

            else -> CityListScreen.State.Success(cities!!) { event ->
                when (event) {
                    CityListScreen.State.Event.BackClicked -> {
                        navigator.pop()
                    }

                    CityListScreen.State.Event.CityClicked -> {
                        //navigator.goTo(CityDetailScreen)
                    }
                }
            }
        }
    }

    class Factory(private val cityListRepository: CityListRepository) : Presenter.Factory {
        override fun create(
            screen: Screen,
            navigator: Navigator,
            context: CircuitContext,
        ): Presenter<*>? {
            return when (screen) {
                is CityListScreen -> return CityListPresenter(screen, navigator, cityListRepository)
                else -> null
            }
        }
    }
}

@Composable
fun CityListScreen(state: CityListScreen.State, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(color = when(state){
                CityListScreen.State.Loading -> Color.Yellow.copy(alpha = 0.5f)
                CityListScreen.State.NoData -> Color.Blue.copy(alpha = 0.5f)
                is CityListScreen.State.Success -> Color.Green.copy(alpha = 0.5f)
            })
            .verticalScroll(state = rememberScrollState()),
    ) {
        Text(
            text = "Cities",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
        )

        when (state) {
            CityListScreen.State.Loading -> {
                Text(
                    text = "Loading...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            CityListScreen.State.NoData -> {
                Text(
                    text = "No data found",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            is CityListScreen.State.Success -> {
                state.cities.forEach {
                    Text(text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                state.eventSink(CityListScreen.State.Event.CityClicked)
                            }
                    )
                }
            }
        }

    }
}
