package io.intenics.python.oopCompanion.interfaces

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyClass
import io.intenics.python.oopCompanion.settings.OopCompanionSettingsState

class InterfaceNamingConventionInspection : LocalInspectionTool() {
    override fun buildVisitor(
        holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession
    ): PsiElementVisitor {
        val prefix = OopCompanionSettingsState.instance.interfacePrefix
        val suffix = OopCompanionSettingsState.instance.interfaceSuffix
        return object : PyInspectionVisitor(holder, getContext(session)) {

            override fun visitPyClass(pyClass: PyClass) {
                if (pyClass.name == null) {
                    return
                }

                if (OopCompanionSettingsState.instance.isPathExcluded(pyClass.containingFile.virtualFile.path)) {
                    return
                }

                val baseClasses = pyClass.getSuperClasses(getContext(session)).map { it.name }
                if ("ABC" in baseClasses) {
                    if (pyClass.classAttributes.size > 0 || pyClass.instanceAttributes.size > 0) {
                        return
                    }
                    if (pyClass.methods.any { it.decoratorList?.findDecorator("abstractmethod") == null }
                    ) {
                        return
                    }
                    if (prefix.isNotEmpty() && !pyClass.name!!.startsWith(prefix)) {
                        val prefixDescription = "Interface name does not start with '$prefix'."
                        holder.registerProblem(
                            pyClass,
                            prefixDescription,
                            RenameClassQuickFix(prefix + pyClass.name.orEmpty())
                        )
                    }
                    if (suffix.isNotEmpty() && !pyClass.name!!.endsWith(suffix)) {
                        val suffixDescription = "Interface name does not end with '$suffix'."
                        holder.registerProblem(
                            pyClass,
                            suffixDescription,
                            RenameClassQuickFix(pyClass.name.orEmpty() + suffix)
                        )
                    }
                }
            }
        }
    }
}
