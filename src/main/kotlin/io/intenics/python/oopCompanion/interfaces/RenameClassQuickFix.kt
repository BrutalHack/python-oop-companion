package io.intenics.python.oopCompanion.interfaces

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.refactoring.RefactoringFactory
import com.jetbrains.python.psi.PyClass

class RenameClassQuickFix(private val newName: String) : LocalQuickFix {
    override fun getFamilyName(): String = "Rename class"

    override fun getName(): String = "Rename Class to $newName"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val pyClass = descriptor.psiElement as? PyClass ?: return
        ApplicationManager.getApplication().invokeLater {
            val refactoringFactory = RefactoringFactory.getInstance(project)
            val rename = refactoringFactory.createRename(pyClass, newName)
            rename.isPreviewUsages = true
            rename.run()
        }
    }
}
