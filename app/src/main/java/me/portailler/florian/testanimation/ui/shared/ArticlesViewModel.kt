package me.portailler.florian.testanimation.ui.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.portailler.florian.testanimation.ArticleEntity
import me.portailler.florian.testanimation.MockArticleSource

class ArticlesViewModel(application: Application) : AndroidViewModel(application) {

	private val _articles: MutableStateFlow<List<ArticleEntity>> = MutableStateFlow(emptyList())
	val articles = _articles.asStateFlow()


	private val _currentArticle: MutableStateFlow<ArticleEntity?> = MutableStateFlow(null)
	val currentArticle = _currentArticle.asStateFlow()

	fun loadAllArticles() = _articles.update { MockArticleSource.load() }


	fun setCurrentArticle(articleEntity: ArticleEntity) {
		_currentArticle.update { articleEntity }
	}

	fun closeCurrentArticle() = _currentArticle.update { null }

}