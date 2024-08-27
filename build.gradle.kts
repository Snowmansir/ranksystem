import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask.JarUrl

plugins {
    `java`
    alias(libs.plugins.lombok)
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperyml)
    alias(libs.plugins.minecraftserver)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.miopowered.eu/snapshots")
    maven("https://ci.ender.zone/plugin/repository/everything/")
    maven("https://jitpack.io")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(libs.paper)
    library(libs.guice)
    library(libs.configlib)
    library(libs.bundles.cloud)
    library(libs.bstats)
    implementation(libs.bundles.ormlite)
    implementation(libs.invui)

    compileOnly(libs.placeholderapi)
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    shadowJar {
        fun reloc(pkg: String, name: String) {
            relocate(pkg, "${project.group}.libs.$name")
        }
        archiveClassifier.set("")
        reloc("xyz.xenondevs.invui", "invui")
        reloc("xyz.xenondevs.inventoryaccess", "inventoryaccess")
        reloc("com.j256.ormlite", "ormlite")

        dependencies {
            exclude("META-INF/NOTICE")
            exclude("META-INF/maven/**")
            exclude("META-INF/versions/**")
            exclude("META-INF/**.kotlin_module")
        }
//        minimize()
    }
    build {
        dependsOn(shadowJar)
    }
}

task<LaunchMinecraftServerTask>("testPlugin") {
    dependsOn("build")

    val buildDir = layout.buildDirectory.asFile.get();

    doFirst {
        copy {
            from(buildDir.resolve("libs/${project.name}-${project.version}.jar"))
            into(project.rootProject.file("run/plugins"))
        }
    }
    serverDirectory.set(project.rootProject.file("run").absolutePath)
    jarUrl.set(JarUrl.Paper("1.20.4"))
    agreeEula.set(true)
}

paper {
    main = "$group.RanksPlugin"
    loader = "$group.RanksPluginLoader"
    apiVersion = "1.20"
    author = "Emmanuel Lampe | rexlManu <mail@emmanuel-lampe.de>"
    generateLibrariesJson = true
    serverDependencies {
        register("PlaceholderAPI") {
            required = false
        }
    }
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.10.1")
        }
    }
}