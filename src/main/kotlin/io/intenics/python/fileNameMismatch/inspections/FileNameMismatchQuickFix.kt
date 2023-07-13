package io.intenics.python.fileNameMismatch.inspections

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.refactoring.RefactoringFactory
import com.jetbrains.python.psi.PyFile
import io.intenics.python.fileNameMismatch.services.FileNameMismatchService
import org.jetbrains.annotations.NotNull

class RenameFileQuickFix(private val service: FileNameMismatchService) : LocalQuickFix {

    @NotNull
    override fun getName(): String {
        return "Rename file to ${service.expectedName}"
    }

    @NotNull
    override fun getFamilyName(): String {
        return "Rename file to match the first class name"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {

        val refactoringFactory = RefactoringFactory.getInstance(project)
        val pyFile = descriptor.psiElement.containingFile as PyFile
        val rename = refactoringFactory.createRename(pyFile, service.expectedName)
        rename.run()
    }
}
