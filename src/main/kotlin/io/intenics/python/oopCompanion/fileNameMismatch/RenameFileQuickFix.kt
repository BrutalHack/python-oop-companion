package io.intenics.python.oopCompanion.fileNameMismatch

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.refactoring.RefactoringFactory
import com.jetbrains.python.psi.PyFile
import io.intenics.python.oopCompanion.services.FileNameMismatchService
import org.jetbrains.annotations.NotNull

class RenameFileQuickFix(private var expectedName: String) : LocalQuickFix {

    init {
        this.expectedName = "$expectedName.py"
    }

    @NotNull
    override fun getName(): String {
        return "Rename file to $expectedName"
    }

    @NotNull
    override fun getFamilyName(): String {
        return "Rename file to match any contained class name"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        ApplicationManager.getApplication().invokeLater {
            val refactoringFactory = RefactoringFactory.getInstance(project)
            val pyFile = descriptor.psiElement.containingFile as PyFile
            val rename = refactoringFactory.createRename(pyFile, expectedName)
            rename.isPreviewUsages = true
            rename.run()
        }
    }

    companion object {
        fun createQuickFixForEachClassName(service: FileNameMismatchService): Array<RenameFileQuickFix> {

            val quickFixes = mutableListOf<RenameFileQuickFix>()
            for (className in service.expectedNames) {
                quickFixes.add(RenameFileQuickFix(className))
            }
            return quickFixes.toTypedArray()
        }
    }

}
