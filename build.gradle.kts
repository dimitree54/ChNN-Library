plugins {
	id("maven-publish")
	kotlin("jvm") version "1.6.0"
	id("org.jetbrains.dokka") version "1.5.0"
}

repositories {
	mavenCentral()
}

@Suppress("GradlePackageUpdate")
dependencies {
	implementation(kotlin("stdlib"))
	testImplementation(kotlin("test-junit5"))
}

dependencies{
	implementation("org.apache.commons:commons-math3:3.6.1")
	implementation("com.google.guava:guava:31.0.1-jre")
}

// we need to specify following sourceSets because we store main and test not in default
//  location (which is module_path/src/main and module_path/src/test)
sourceSets.main {
	java.srcDirs("src/main")
}

sourceSets.test {
	java.srcDirs("src/test")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
	useJUnitPlatform()
	maxParallelForks = 8
}

publishing {
	publications {
		create<MavenPublication>("default") {
			from(components["java"])
			// Include any other artifacts here, like javadocs
		}
	}

	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/dimitree54/chnn-library")
			credentials {
				username = System.getenv("GITHUB_ACTOR")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}
}

tasks.dokkaHtml.configure {
	dokkaSourceSets {
		configureEach {
			includes.from("Module.md")
		}
	}
}
