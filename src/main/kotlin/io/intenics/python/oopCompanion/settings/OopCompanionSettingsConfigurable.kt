package io.intenics.python.oopCompanion.settings

import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

class OopCompanionSettingsConfigurable : SearchableConfigurable {

    private var component: OopCompanionSettingsComponent? = null
    private var state = OopCompanionSettingsState.instance

    override fun getId() = "preferences.python.oop.companion"

    override fun getDisplayName(): String {
        return "Python Oop Companion"
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return component!!.preferredFocusedComponent
    }

    override fun createComponent(): JComponent {
        component = OopCompanionSettingsComponent()
        return component!!.panel
    }

    override fun isModified(): Boolean =
        state.isClassNameAnnotatorEnabled != component!!.isClassNameAnnotatorEnabled ||
                state.isAbstractMethodValidationEnabled != component!!.isAbstractMethodValidationEnabled ||
                state.isInterfaceNamingConventionEnabled != component!!.isInterfaceNamingConventionEnabled ||
                state.excludeContainPaths != component!!.excludeContainPaths ||
                state.excludeRegexPatterns != component!!.excludeRegexPatterns ||
                state.excludeGlobPatterns != component!!.excludeGlobPatterns ||
                state.interfacePrefix != component!!.interfacePrefix ||
                state.interfaceSuffix != component!!.interfaceSuffix

    override fun apply() {
        state.isClassNameAnnotatorEnabled = component!!.isClassNameAnnotatorEnabled
        state.isAbstractMethodValidationEnabled = component!!.isAbstractMethodValidationEnabled
        state.isInterfaceNamingConventionEnabled = component!!.isInterfaceNamingConventionEnabled

        state.excludeContainPaths = component!!.excludeContainPaths
        state.excludeRegexPatterns = component!!.excludeRegexPatterns
        state.excludeGlobPatterns = component!!.excludeGlobPatterns

        state.interfacePrefix = component!!.interfacePrefix
        state.interfaceSuffix = component!!.interfaceSuffix
    }

    override fun reset() {
        component!!.isClassNameAnnotatorEnabled = state.isClassNameAnnotatorEnabled
        component!!.isAbstractMethodValidationEnabled = state.isAbstractMethodValidationEnabled
        component!!.isInterfaceNamingConventionEnabled = state.isInterfaceNamingConventionEnabled

        component!!.excludeContainPaths = state.excludeContainPaths
        component!!.excludeRegexPatterns = state.excludeRegexPatterns
        component!!.excludeGlobPatterns = state.excludeGlobPatterns

        component!!.interfacePrefix = state.interfacePrefix
        component!!.interfaceSuffix = state.interfaceSuffix

    }

}