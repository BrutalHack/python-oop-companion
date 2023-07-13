package io.intenics.python.oopCompanion.settings

import com.intellij.codeInspection.ex.InspectionProfileModifiableModel
import com.intellij.openapi.project.ProjectManager
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import io.intenics.python.oopCompanion.abstractmethod.MissingAbstractMethodDecoratorInspection
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


class OopCompanionSettingsComponent {
    val panel: JPanel
    private val excludePathContainsTextArea = JBTextArea()
    private val excludePathGlobPatternsTextArea = JBTextArea()
    private val excludePathRegexTextArea = JBTextArea()
    private val interfaceSuffixTextField = JBTextField()
    private val interfacePrefixTextField = JBTextField()
    private val isClassNameMismatchEnabledCheckbox =
        JBCheckBox("Enable validation of file names containing classes")
    private val isInterfaceNamingConventionEnabledCheckBox =
        JBCheckBox("Enable validation of interface naming conventions")
    private val isAbstractMethodValidationEnabledCheckBox =
        JBCheckBox("Enable validation of abstract methods in ABCs (@abstractmethod)")

    init {
        panel = FormBuilder.createFormBuilder()
            .addComponent(isClassNameMismatchEnabledCheckbox, 1)
            .addComponent(isAbstractMethodValidationEnabledCheckBox, 1)
            .addComponent(isInterfaceNamingConventionEnabledCheckBox, 1)
            .addComponent(TitledSeparator("Exclude Paths for All Validations"))
            .addLabeledComponent(
                JBLabel("Exclude paths containing: (one per line)"),
                excludePathContainsTextArea,
                1,
                false
            )
            .addLabeledComponent(
                JBLabel("Exclude paths matching Glob Patterns: (one per line)"),
                excludePathGlobPatternsTextArea,
                1,
                false
            )
            .addLabeledComponent(
                JBLabel("Exclude paths matching Regex Patterns: (one per line)"),
                excludePathRegexTextArea,
                1,
                false
            )
            .addComponent(TitledSeparator("Interface Naming Conventions"))
            .addLabeledComponent(
                JBLabel("Interface prefix:"),
                interfacePrefixTextField,
                1,
                false
            )
            .addLabeledComponent(
                JBLabel("Interface suffix:"),
                interfaceSuffixTextField,
                1,
                false
            )
            .addComponentFillVertically(JPanel(), 0)
            .panel
        isAbstractMethodValidationEnabledCheckBox.addChangeListener {
            toggleInspection(
                isAbstractMethodValidationEnabledCheckBox.isSelected,
                MissingAbstractMethodDecoratorInspection.SHORT_NAME
            )
        }
    }

    private fun toggleInspection(isEnabled: Boolean, shortName: String) {
        val project = ProjectManager.getInstance().openProjects.first() // get current project
        val currentProfile = InspectionProjectProfileManager.getInstance(project).currentProfile
        val model = InspectionProfileModifiableModel(currentProfile)
        model.setToolEnabled(shortName, isEnabled)
        model.commit()
    }

    val preferredFocusedComponent: JComponent
        get() = excludePathContainsTextArea

    var isClassNameAnnotatorEnabled: Boolean
        get() = isClassNameMismatchEnabledCheckbox.isSelected
        set(newStatus) {
            isClassNameMismatchEnabledCheckbox.setSelected(newStatus)
        }
    var isAbstractMethodValidationEnabled: Boolean
        get() = isAbstractMethodValidationEnabledCheckBox.isSelected
        set(newStatus) {
            isAbstractMethodValidationEnabledCheckBox.setSelected(newStatus)
        }
    var isInterfaceNamingConventionEnabled: Boolean
        get() = isInterfaceNamingConventionEnabledCheckBox.isSelected
        set(newStatus) {
            isInterfaceNamingConventionEnabledCheckBox.setSelected(newStatus)
        }

    var interfacePrefix: String
        get() = interfacePrefixTextField.text
        set(newText) {
            interfacePrefixTextField.text = newText
        }
    var interfaceSuffix: String
        get() = interfaceSuffixTextField.text
        set(newText) {
            interfaceSuffixTextField.text = newText
        }

    var excludeContainPaths: String
        get() = excludePathContainsTextArea.text
        set(newText) {
            excludePathContainsTextArea.text = newText
        }
    var excludeRegexPatterns: String
        get() = excludePathRegexTextArea.text
        set(newText) {
            excludePathRegexTextArea.text = newText
        }
    var excludeGlobPatterns: String
        get() = excludePathGlobPatternsTextArea.text
        set(newText) {
            excludePathGlobPatternsTextArea.text = newText
        }
}