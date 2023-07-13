package io.intenics.python.oopCompanion.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.xmlb.XmlSerializerUtil
import java.nio.file.FileSystems

@State(
    name = "io.intenics.python.oopCompanion",
    storages = [Storage("io.intenics.python.oopCompanion.components.OopCompanionSettings.xml")]
)
data class OopCompanionSettingsState(
    var excludeContainPaths: String = "",
    var excludeGlobPatterns: String = "",
    var excludeRegexPatterns: String = "",
    var isClassNameAnnotatorEnabled: Boolean = true,
    var isAbstractMethodValidationEnabled: Boolean = true,
    var isInterfaceNamingConventionEnabled: Boolean = true
) : PersistentStateComponent<OopCompanionSettingsState> {

    override fun getState(): OopCompanionSettingsState {
        return this
    }

    override fun loadState(state: OopCompanionSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: OopCompanionSettingsState
            get() = ApplicationManager.getApplication().getService(
                OopCompanionSettingsState::class.java
            )
        private val LOG = Logger.getInstance(OopCompanionSettingsState::class.java)
    }

    fun isPathExcluded(path: String): Boolean {
        if (!isClassNameAnnotatorEnabled) {
            return false
        }
        return isExcludedViaRegex(path) ||
                isExcludedViaGlobPattern(path) ||
                isExcludedViaContains(path)
    }

    private fun isExcludedViaContains(path: String): Boolean {
        if (excludeContainPaths.isEmpty()) {
            return false
        }
        for (excludeContainPath in excludeContainPaths.lines()) {
            if (excludeContainPath.isEmpty()) {
                continue
            }
            if (path.contains(excludeContainPath)) {
                LOG.warn("${path}contains$excludeContainPath")
                return true
            }
        }
        return false
    }

    private fun isExcludedViaGlobPattern(path: String): Boolean {
        if (excludeGlobPatterns.isEmpty()) {
            return false
        }
        for (globPattern in excludeGlobPatterns.lines()) {
            if (globPattern.isEmpty()) {
                continue
            }
            val pathMatcher = FileSystems.getDefault().getPathMatcher("glob:$globPattern")
            if (pathMatcher.matches(FileSystems.getDefault().getPath(path))) {
                LOG.warn("$path matches $globPattern")
                return true
            }
        }
        return false
    }

    private fun isExcludedViaRegex(path: String): Boolean {
        if (excludeRegexPatterns.isEmpty()) {
            return false
        }
        for (regex in excludeRegexPatterns.lines()) {
            if (regex.isEmpty()) {
                continue
            }
            if (path.matches(Regex(regex))) {
                LOG.warn("$path matches $regex")
                return true
            }
        }
        return false
    }

}