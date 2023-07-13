// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.sdk.settings

import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class OopCompanionSettingsComponent {
    val panel: JPanel
    private val excludePathContainsTextArea = JBTextArea()
    private val excludePathGlobPatternsTextArea = JBTextArea()
    private val excludePathRegexTextArea = JBTextArea()
    private val isClassNameMismatchEnabledCheckbox =
        JBCheckBox("Show error when file name doesn't match containing classes")

    init {
        panel = FormBuilder.createFormBuilder()
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
            .addComponent(TitledSeparator("Feature Toggles"))
            .addComponent(isClassNameMismatchEnabledCheckbox, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    val preferredFocusedComponent: JComponent
        get() = excludePathContainsTextArea

    var isClassNameAnnotatorEnabled: Boolean
        get() = isClassNameMismatchEnabledCheckbox.isSelected
        set(newStatus) {
            isClassNameMismatchEnabledCheckbox.setSelected(newStatus)
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