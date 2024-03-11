package me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.zIndex
import computation.ComputationUtils
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.computation.MathUtils
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.container.ContainerTransformSpec
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.container.MaterialContainer
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.ElementCall
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.ElementInfo
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.FitMode
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.ProgressThresholdsGroup
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.element.states.applyTo
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.Fullscreen
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.FullscreenLayoutId
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.SharedElementsTransitionState
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.TopLeft
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.root.TransitionDirection
import me.portailler.florian.testanimation.ui.compose.sharedelements.lib.transitions.FadeMode
import kotlin.math.roundToInt

@Composable
internal fun Placeholder(state: SharedElementsTransitionState) {
	with(LocalDensity.current) {
		val startInfo = state.startInfo as ElementInfo
		val direction = state.direction
		val spec = state.spec as? ContainerTransformSpec
		val start = state.startBounds
		val end = state.endBounds
		val fraction = state.fraction

		val surfaceModifier: Modifier
		var startContentModifier = Fullscreen
		val elements = mutableListOf<ElementCall>()

		var shape = startInfo.shape
		var color = startInfo.color
		var contentColor = startInfo.contentColor
		var border = startInfo.border
		var elevation = startInfo.elevation
		var startAlpha = 1f

		if (start == null) {
			surfaceModifier = Modifier.layoutId(FullscreenLayoutId)
		} else {
			val fitMode = if (spec == null || end == null) null else remember {
				val mode = spec.fitMode
				if (mode != FitMode.Auto) mode else ComputationUtils.calculateFitMode(direction == TransitionDirection.Enter, start, end)
			}

			val thresholds =
				if (spec == null || direction == null) ProgressThresholdsGroup.DefaultEnter else remember {
					spec.progressThresholdsGroupFor(direction, state.pathMotion!!)
				}

			val scaleFraction = thresholds.scale.applyTo(fraction)
			val scale = MathUtils.calculateScale(start, end, scaleFraction)
			val contentScale = if (fitMode == FitMode.Height) scale.scaleY else scale.scaleX
			val scaleMaskFraction = thresholds.scaleMask.applyTo(fraction)
			val (containerWidth, containerHeight) = if (end == null) start.size * contentScale else {
				if (fitMode == FitMode.Height) Size(
					width = androidx.compose.ui.util.lerp(
						start.width * contentScale,
						start.height * contentScale / end.height * end.width,
						scaleMaskFraction
					),
					height = start.height * contentScale
				) else Size(
					width = start.width * contentScale,
					height = androidx.compose.ui.util.lerp(
						start.height * contentScale,
						start.width * contentScale / end.width * end.height,
						scaleMaskFraction
					)
				)
			}

			val offset = MathUtils.calculateOffset(start, end, fraction, state.pathMotion, containerWidth).round()

			surfaceModifier = Modifier
				.size(
					containerWidth.toDp(),
					containerHeight.toDp()
				)
				.offset { offset }

			val endInfo = state.endInfo as? ElementInfo
			val fadeFraction = thresholds.fade.applyTo(fraction)
			if (end != null && endInfo != null) {
				val endAlpha = MathUtils.calculateAlpha(direction, state.spec?.fadeMode, fadeFraction, false)
				if (endAlpha > 0) {
					val endScale = MathUtils.calculateScale(end, start, 1 - scaleFraction).run {
						if (fitMode == FitMode.Height) scaleY else scaleX
					}
					val containerColor = spec?.endContainerColor ?: Color.Transparent
					val containerModifier = Modifier
						.fillMaxSize()
						.run {
							if (containerColor == Color.Transparent) this else
								background(containerColor.copy(alpha = containerColor.alpha * endAlpha))
						}
						.run {
							if (state.spec?.fadeMode != FadeMode.Out) zIndex(1f) else this
						}
					val contentModifier = Modifier
						.size(
							end.width.toDp(),
							end.height.toDp()
						)
						.run {
							if (fitMode == FitMode.Height) offset {
								IntOffset(
									((containerWidth - end.width * endScale) / 2).roundToInt(),
									0
								)
							} else this
						}
						.graphicsLayer {
							this.transformOrigin = TopLeft
							this.scaleX = endScale
							this.scaleY = endScale
							this.alpha = endAlpha
						}

					elements.add(
						ElementCall(
							endInfo.screenKey,
							containerModifier,
							true,
							contentModifier,
							state.endCompositionLocalContext!!,
							state.endPlaceholder!!
						)
					)
				}

				val shapeFraction = thresholds.shapeMask.applyTo(fraction)
				shape = ComputationUtils.lerp(startInfo.shape, endInfo.shape, shapeFraction)
				color = androidx.compose.ui.graphics.lerp(startInfo.color, endInfo.color, shapeFraction)
				contentColor = androidx.compose.ui.graphics.lerp(startInfo.contentColor, endInfo.contentColor, shapeFraction)
				border = (startInfo.border ?: endInfo.border)?.copy(
					width = androidx.compose.ui.unit.lerp(
						startInfo.border?.width ?: 0.dp,
						endInfo.border?.width ?: 0.dp,
						shapeFraction
					)
				)
				elevation = androidx.compose.ui.unit.lerp(startInfo.elevation, endInfo.elevation, shapeFraction)
			}

			startAlpha = MathUtils.calculateAlpha(direction, state.spec?.fadeMode, fadeFraction, true)
			if (startAlpha > 0) {
				startContentModifier = Modifier
					.size(
						start.width.toDp(),
						start.height.toDp()
					)
					.run {
						if (fitMode == FitMode.Height) offset {
							IntOffset(
								((containerWidth - start.width * contentScale) / 2).roundToInt(),
								0
							)
						} else this
					}
					.graphicsLayer {
						this.transformOrigin = TopLeft
						this.scaleX = contentScale
						this.scaleY = contentScale
						this.alpha = startAlpha
					}
			}
		}

		if (startAlpha > 0) {
			val containerColor = spec?.startContainerColor ?: Color.Transparent
			val containerModifier = Modifier
				.fillMaxSize()
				.run {
					if (containerColor == Color.Transparent) this else
						background(containerColor.copy(alpha = containerColor.alpha * startAlpha))
				}

			elements.add(
				ElementCall(
					startInfo.screenKey,
					containerModifier,
					start != null,
					startContentModifier,
					state.startCompositionLocalContext,
					state.startPlaceholder
				)
			)
		}

		MaterialContainer(
			modifier = surfaceModifier,
			shape = shape,
			backgroundColor = color,
			contentColor = contentColor,
			border = border,
			elevation = elevation
		) {
			Box {
				elements.forEach { call ->
					key(call.screenKey) {
						ElementContainer(
							modifier = call.containerModifier,
							relaxMaxSize = call.relaxMaxSize
						) {
							ElementContainer(modifier = call.contentModifier) {
								CompositionLocalProvider(
									call.compositionLocalContext,
									content = call.content
								)
							}
						}
					}
				}
			}
		}
	}
}