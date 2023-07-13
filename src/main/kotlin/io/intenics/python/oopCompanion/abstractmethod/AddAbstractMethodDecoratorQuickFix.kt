package io.intenics.python.oopCompanion.abstractmethod

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiParserFacade
import com.jetbrains.python.psi.*

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

        if (!hasImportStatement(pyFunction)) {
            val abcImportStatement = getAbcImportStatement(pyFunction)
            if (abcImportStatement == null) {
                val newImportStatement =
                    generator.createFromImportStatement(
                        LanguageLevel.forElement(pyFunction),
                        "abc",
                        DECORATOR_NAME,
                        null
                    )
                pyFile.addBefore(newImportStatement, pyFile.statements[0])
            } else {
                val abstractMethodImportElement =
                    generator.createImportElement(LanguageLevel.forElement(pyFunction), DECORATOR_NAME, null)
                val importElements = abcImportStatement.importElements
                abcImportStatement.addBefore(abstractMethodImportElement, importElements.lastOrNull()?.nextSibling)
            }
        }

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

    private fun hasImportStatement(pyFunction: PyFunction): Boolean {
        val pyFile = pyFunction.containingFile as PyFile
        val imports = pyFile.fromImports
        for (importStatement in imports) {
            for (importElement in importStatement.importElements) {
                if (importElement.importedQName == null) {
                    continue
                }
                if (importElement.importedQName!!.matches(DECORATOR_NAME)) {
                    return true
                }
            }
        }
        return imports.any { it.importSourceQName?.lastComponent == DECORATOR_NAME }
    }

    private fun getAbcImportStatement(pyFunction: PyFunction): PyFromImportStatement? {
        val pyFile = pyFunction.containingFile as PyFile
        val imports = pyFile.fromImports
        for (importStatement in imports) {
            if (importStatement.importSourceQName == null) {
                continue
            }
            if (importStatement.importSourceQName!!.matches("abc")) {
                return importStatement
            }
        }
        return null
    }
}