package io.intenics.python.oopCompanion.interfaces

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.jetbrains.python.codeInsight.imports.AddImportHelper
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.psi.PyElementGenerator
import com.jetbrains.python.psi.PyFile
import io.intenics.python.oopCompanion.ImportHelper

class AddSuperclassQuickFix(private val newSuperClass: String) : LocalQuickFix {
    override fun getName(): String = "Add $newSuperClass as superclass"
    override fun getFamilyName(): String = "Add superclass"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val pyClassNameIdentifier = descriptor.psiElement!!
        val pyClass = pyClassNameIdentifier.parent as PyClass
        val pyFile = pyClass.containingFile as PyFile

        ImportHelper.addImportIfMissing(pyFile, "abc", newSuperClass)

        val generator = PyElementGenerator.getInstance(project)
        val tempPyClass = generator.createFromText(
            LanguageLevel.forElement(pyClassNameIdentifier),
            PyClass::class.java,
            "class Temp(ABC): pass"
        )
        val comma = generator.createComma().psi
        val superClassExpression = tempPyClass.superClassExpressionList!!
        val existingSuperClassExpression = pyClass.superClassExpressionList

        if (existingSuperClassExpression != null) {
            val openingBracket = existingSuperClassExpression.findElementAt(0)!!
            existingSuperClassExpression.addAfter(
                comma,
                openingBracket
            )
            existingSuperClassExpression.addAfter(
                superClassExpression.arguments[0],
                openingBracket
            )
        } else {
            pyClass.addAfter(superClassExpression, pyClassNameIdentifier)
        }
    }
}