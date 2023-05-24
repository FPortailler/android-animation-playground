package me.portailler.florian.testanimation

import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

/**
 * Determine how to process the image to load.
 */
fun ImageView.loadAndSetImage(
	url: String?,
	scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP,
	cornerRadius: Int? = null,
	gravity: Int? = null,
	onImageReady: (() -> Unit)? = null
) {

	when {
		url?.endsWith(".svg") == true -> loadAndSetSvgFromUrl(url)
		else -> loadAndSetImageFromUrl(
			url,
			placeholderDrawableId = R.drawable.ic_launcher_foreground,
			scaleType = scaleType,
			cornerRadius = cornerRadius,
			gravity = gravity,
			onImageReady = onImageReady
		)
	}
}

/**
 * Load standard image with Picasso.
 */
fun ImageView.loadAndSetImageFromUrl(
	url: String?,
	placeholderDrawableId: Int,
	scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP,
	cornerRadius: Int? = null,
	gravity: Int? = null,
	onImageReady: (() -> Unit)?,
) {
	if (url?.isBlank() != false) {
		setImageResource(placeholderDrawableId)
		return
	}
	var picassoBuilder = Picasso.get().load(url)

	picassoBuilder = when (scaleType) {
		ImageView.ScaleType.CENTER,
		ImageView.ScaleType.CENTER_CROP -> picassoBuilder.fit().centerCrop(gravity ?: Gravity.CENTER)

		ImageView.ScaleType.CENTER_INSIDE -> picassoBuilder.fit().centerInside()
		ImageView.ScaleType.FIT_END,
		ImageView.ScaleType.FIT_START,
		ImageView.ScaleType.FIT_XY,
		ImageView.ScaleType.MATRIX,
		ImageView.ScaleType.FIT_CENTER -> picassoBuilder
	}
	if (cornerRadius != null) picassoBuilder = picassoBuilder.transform(RoundedCornersTransformation(cornerRadius, 0))
	picassoBuilder = picassoBuilder.placeholder(R.color.mockUpBackground)
	picassoBuilder = picassoBuilder.error(placeholderDrawableId)

	picassoBuilder.into(this, object : Callback {
		override fun onSuccess() {
			onImageReady?.invoke()
		}

		override fun onError(e: Exception?) {
			Log.w("Load image", "Failed to load image with url: $url", e)
			setScaleType(ImageView.ScaleType.CENTER_CROP)
			setBackgroundColor(ContextCompat.getColor(context, R.color.mockUpBackground))
			onImageReady?.invoke()
		}
	})
}

/**
 * Load svg image with Coil.
 */
fun ImageView.loadAndSetSvgFromUrl(url: String) {
	val imageLoader = ImageLoader.Builder(context)
		.components {
			add(SvgDecoder.Factory())
		}
		.build()
	val request = ImageRequest.Builder(context)
		.data(Uri.decode(url))
		.target(this)
		.build()
	imageLoader.enqueue(request)
}