/*
 * Copyright 2020 Benedikt Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.britter.maven.plugin.development

import com.github.britter.maven.plugin.development.internal.DefaultMavenPluginMetadataExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class MavenPluginDevelopmentPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        pluginManager.apply(JavaBasePlugin::class)

        val extension = createExtension(project) as DefaultMavenPluginMetadataExtension

        tasks.register<GenerateMavenPluginDescriptorTask>("generateMavenPluginDescriptor") {
            pluginDescriptor.set(extension.pluginDescriptor)

            classesDirs.set(extension.sourceSet.map { it.output.classesDirs })
            outputDirectory.fileProvider(extension.sourceSet.map { it.output.resourcesDir!! })
            dependsOn(extension.sourceSet.map { it.output })
        }
    }

    private fun Project.createExtension(project: Project) =
            extensions.create(
                    MavenPluginMetadataExtension::class,
                    MavenPluginMetadataExtension.NAME,
                    DefaultMavenPluginMetadataExtension::class,
                    project)
}