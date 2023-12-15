package me.portailler.florian.testanimation.ui.changeicon.state

import android.support.annotation.DrawableRes
import me.portailler.florian.testanimation.R

enum class IconState(@DrawableRes val icon: Int, val alias: String) {
	DEFAULT(R.drawable.ic_launcher_foreground, "me.portailler.florian.testanimation.ui.menu.MenuActivity_Default"),
	COCKTAIL(R.drawable.ic_cocktail, "me.portailler.florian.testanimation.ui.menu.MenuActivity_Cocktail"),
	ICE_CREAM(R.drawable.ic_ice_cream, "me.portailler.florian.testanimation.ui.menu.MenuActivity_IceCream"),
	WINE(R.drawable.ic_wine, "me.portailler.florian.testanimation.ui.menu.MenuActivity_Wine"),
	TEA(R.drawable.ic_tea, "me.portailler.florian.testanimation.ui.menu.MenuActivity_Tea"),
}