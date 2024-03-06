plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "me.portailler.florian.testanimation"
	compileSdk = 34

	defaultConfig {
		applicationId = "me.portailler.florian.testanimation"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

	}


	buildTypes {

		debug {
			buildConfigField("String", "API_KEY", "\"sk-ekoO6460771c35723927\"")
			isMinifyEnabled = false
			isDebuggable = true
		}
		release {
			isMinifyEnabled = true
			isDebuggable = false
			buildConfigField("String", "API_KEY", "\"sk-ekoO6460771c35723927\"")
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}

	buildFeatures {
		viewBinding = true
		buildConfig = true
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.6"
	}
}

dependencies {

	implementation(appLibs.core.ktx)
	implementation(appLibs.appcompat)
	implementation(appLibs.material)
	implementation(appLibs.constraintlayout)
	implementation(appLibs.fragment.ktx)
	implementation(appLibs.lorem)
	testImplementation(appLibs.junit)
	androidTestImplementation(appLibs.junit)
	androidTestImplementation(appLibs.androidTestEspresso)

	// Image
	implementation(appLibs.picasso)
	implementation(appLibs.picassoTransformations)
	implementation(appLibs.coil)
	implementation(appLibs.coilSvg)

	// Compose
	implementation(appLibs.composeBom)
	androidTestImplementation(appLibs.composeBom)
	// Material design
	implementation(appLibs.material3)
	implementation(appLibs.lifecycleRuntimeCompose)

	// Android Studio Preview support
	implementation(appLibs.activityCompose)
	implementation(appLibs.uiToolingPreview)
	debugImplementation(appLibs.uiTooling)
	implementation(appLibs.coilCompose)

	//lottie
	implementation(appLibs.lottie)
	implementation(appLibs.lottieCompose)

	// Voyager
	// Navigator
	implementation(appLibs.voyagerNavigator)

	// Squircle shape
	implementation(appLibs.squircleShape)

	// Exo player
	implementation(appLibs.exoplayer)
	implementation(appLibs.exoplayer.ui)

	// Leak canary
	debugImplementation(appLibs.leakcanary)

}