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

	implementation(appLibs.core.ktx.get())
	implementation(appLibs.appcompat.get())
	implementation(appLibs.material.get())
	implementation(appLibs.constraintlayout.get())
	implementation(appLibs.fragment.ktx.get())
	implementation(appLibs.lorem.get())
	testImplementation(appLibs.junit.get())
	androidTestImplementation(appLibs.junit.get())
	androidTestImplementation(appLibs.androidTestEspresso.get())

	// Image
	implementation(appLibs.picasso.get())
	implementation(appLibs.picassoTransformations.get())
	implementation(appLibs.coil.get())
	implementation(appLibs.coilSvg.get())

	// Compose
	implementation(appLibs.composeBom.get())
	androidTestImplementation(appLibs.composeBom.get())
	// Material design
	implementation(appLibs.material3.get())
	implementation(appLibs.lifecycleRuntimeCompose.get())

	// Android Studio Preview support
	implementation(appLibs.activityCompose.get())
	implementation(appLibs.uiToolingPreview.get())
	debugImplementation(appLibs.uiTooling.get())
	implementation(appLibs.coilCompose.get())

	//lottie
	implementation(appLibs.lottie.get())
	implementation(appLibs.lottieCompose.get())

	// Voyager
	// Navigator
	implementation(appLibs.voyagerNavigator.get())

	// Squircle shape
	implementation(appLibs.squircleShape.get())

	// Leak canary
	debugImplementation(appLibs.leakcanary.get())
}