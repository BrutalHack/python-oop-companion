package io.intenics.python.oopCompanion.interfaces

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyClass
import io.intenics.python.oopCompanion.settings.OopCompanionSettingsState

class InterfaceMissingAbcInspection : LocalInspectionTool() {

    companion object {
        const val SHORT_NAME = "InterfaceMissingAbc"
    }

    override fun getShortName(): String {
        return SHORT_NAME
    }

    override fun buildVisitor(
        holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession
    ): PsiElementVisitor {
        val settingsState = OopCompanionSettingsState.instance
        val prefix = settingsState.interfacePrefix
        val suffix = settingsState.interfaceSuffix
        return object : PyInspectionVisitor(holder, getContext(session)) {

            override fun visitPyClass(pyClass: PyClass) {
                if (pyClass.name == null) {
                    return
                }

                if (!settingsState.isInterfaceMissingAbcEnabled) {
                    return
                }

                if (settingsState.isPathExcluded(pyClass.containingFile.virtualFile.path)) {
                    return
                }

                if (prefix.isEmpty() && suffix.isEmpty()) {
                    return
                }

                if (prefix.isNotEmpty() && !pyClass.name!!.startsWith(prefix)) {
                    return
                }
                if (suffix.isNotEmpty() && !pyClass.name!!.endsWith(suffix)) {
                    return
                }

                val baseClasses = pyClass.getSuperClasses(getContext(session)).map { it.name }

                if ("ABC" !in baseClasses) {
                    val description = "Interface does not inherit from ABC."
                    holder.registerProblem(pyClass.nameIdentifier!!, description, AddSuperclassQuickFix("ABC"))
                }
            }
        }
    }
}
