package me.portailler.florian.testanimation.ui.xml.sharedelement

import com.thedeanda.lorem.LoremIpsum

object MockArticleSource {

	private val lorem = LoremIpsum.getInstance()

	private val articles: List<ArticleEntity> by lazy {
		buildList {
			repeat(300) {
				val article = ArticleEntity(
					id = it,
					title = lorem.getTitle(10).toString(),
					imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$it.png",
					author = lorem.firstName + lorem.lastName,
					content = lorem.getParagraphs(3, 5)
				)
				this.add(article)
			}
		}
	}

	fun load() = articles
}
