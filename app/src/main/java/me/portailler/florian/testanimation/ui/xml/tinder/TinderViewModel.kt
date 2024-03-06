package me.portailler.florian.testanimation.ui.xml.tinder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.portailler.florian.testanimation.ui.xml.tinder.card.TinderCardEntity

class TinderViewModel:ViewModel() {

	private val _entities = MutableStateFlow<List<TinderCardEntity>>(emptyList())
	val entities = _entities.asStateFlow()

	fun loadEntities() {
		_entities.value = TinderCardEntity.mocks
	}
}
