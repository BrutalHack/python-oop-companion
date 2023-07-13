package io.intenics.python.oopCompanion.abstractMethod

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.jetbrains.python.codeInsight.imports.AddImportHelper
import com.jetbrains.python.psi.*
import io.intenics.python.oopCompanion.ImportHelper

class AddAbstractMethodDecoratorQuickFix : LocalQuickFix {
    companion object {
        const val DECORATOR_NAME = "abstractmethod"
    }

    override fun getFamilyName() = "Add @$DECORATOR_NAME decorator"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val pyFunction = descriptor.psiElement as? PyFunction ?: return
        val decoratorList = pyFunction.decoratorList
        val generator = PyElementGenerator.getInstance(project)
        val pyFile = pyFunction.containingFile as PyFile

        ImportHelper.addImportIfMissing(pyFile, "abc", DECORATOR_NAME)

        if (decoratorList != null) {
            val tempDecoratorList = generator.createDecoratorList("@$DECORATOR_NAME")
            val newDecorator = tempDecoratorList.decorators[0]
            val newLine = generator.createNewLine()
            decoratorList.addAfter(newLine, decoratorList.lastChild)
            decoratorList.addAfter(newDecorator, decoratorList.lastChild)
        } else {
            val newDecoratorList = generator.createDecoratorList("@$DECORATOR_NAME")
            pyFunction.addBefore(newDecoratorList, pyFunction.firstChild)
        }
    }
}