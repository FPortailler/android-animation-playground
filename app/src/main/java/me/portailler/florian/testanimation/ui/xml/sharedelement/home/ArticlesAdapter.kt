package me.portailler.florian.testanimation.ui.xml.sharedelement.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import me.portailler.florian.testanimation.ui.xml.sharedelement.ArticleEntity
import me.portailler.florian.testanimation.databinding.ArticleItemBinding
import me.portailler.florian.testanimation.ui.xml.utils.loadAndSetImage

class ArticlesAdapter(
	private val onArticleClick: (ArticleItemBinding, ArticleEntity) -> Unit
) : RecyclerView.Adapter<ArticlesAdapter.ArticleItem>() {

	private val data: MutableList<ArticleEntity> = mutableListOf()

	fun replaceAll(articles: List<ArticleEntity>) {
		data.clear()
		data.addAll(articles)
		notifyDataSetChanged()
	}

	override fun getItemCount(): Int = data.size

	override fun onBindViewHolder(holder: ArticleItem, position: Int) = data.getOrNull(position)?.let(holder::bind) ?: Unit

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleItem = ArticleItem(
		ArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
		onArticleClick
	)

	class ArticleItem(
		private val binding: ArticleItemBinding,
		private val onArticleClick: (ArticleItemBinding, ArticleEntity) -> Unit
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(article: ArticleEntity) {
			binding.articleImage.loadAndSetImage(article.imageUrl)
			binding.articleMeta.text = article.author
			binding.articleTitle.text = article.title
			binding.root.setOnClickListener { onArticleClick(binding, article) }
			binding.articleImage.transitionName = "#${article.id}"
		}

		fun getImageView(): ImageView = binding.articleImage
	}
}
