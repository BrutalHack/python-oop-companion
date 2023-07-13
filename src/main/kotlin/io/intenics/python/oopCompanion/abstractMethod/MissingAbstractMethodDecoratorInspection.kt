package io.intenics.python.oopCompanion.abstractMethod

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.python.inspections.PyInspection
import com.jetbrains.python.inspections.PyInspectionVisitor
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.psi.PyFunction
import io.intenics.python.oopCompanion.settings.OopCompanionSettingsState

class MissingAbstractMethodDecoratorInspection : PyInspection() {

    companion object {
        const val SHORT_NAME: String = "MissingAbstractMethodDecorator"
    }

    override fun getDisplayName() = "Empty method in ABC class should be abstract"

    override fun getGroupDisplayName() = "Empty method in ABC class should be abstract"

    override fun getShortName(): String {
        return SHORT_NAME
    }

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {

        return object : PyInspectionVisitor(holder, getContext(session)) {

            override fun visitPyFunction(node: PyFunction) {
                super.visitPyFunction(node)

                val pyClass = node.containingClass ?: return
                if (OopCompanionSettingsState.instance.isPathExcluded(pyClass.containingFile.virtualFile.path)) {
                    return;
                }

                if (pyClass.isABCClass() && node.isMethodEmpty() && !node.hasAbstractMethodDecorator()) {
                    holder.registerProblem(
                        node,
                        "Method in ABC class should have body",
                        ProblemHighlightType.WARNING,
                        AddAbstractMethodDecoratorQuickFix()
                    )
                }
            }
        }
    }

    private fun PyClass.isABCClass(): Boolean {
        val superClassExpressions = superClassExpressions
        return superClassExpressions.any { it.text == "ABC" || it.text == "abc.ABC" }
    }

    private fun PyFunction.isMethodEmpty(): Boolean {
        // remove all entries with contain "pass" or "return"
        val statements = statementList.statements ?: return true

        val isEmptyBody = statements
            .all {
                it.text.equals("pass") || it.text.equals("return") || it.text.equals("...")
            }
        return isEmptyBody
    }

    private fun PyFunction.hasAbstractMethodDecorator(): Boolean {
        val decoratorList = decoratorList
        return decoratorList?.text?.contains("@abstractmethod") ?: false
    }
}