plugins {
	kotlin("jvm") version "1.7.10"
	id("maven-publish")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))
	testImplementation(kotlin("test-junit5"))
}

dependencies{
	implementation("org.jgrapht:jgrapht-core:1.5.1")
	implementation("com.google.guava:guava:31.1-jre")
	implementation("com.badlogicgames.gdx:gdx:1.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

// we need to specify following sourceSets because we store main and test not in default
//  location (which is module_path/src/main and module_path/src/test)
sourceSets.main {
	java.srcDirs("src/main")
}

sourceSets.test {
	java.srcDirs("src/test")
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
