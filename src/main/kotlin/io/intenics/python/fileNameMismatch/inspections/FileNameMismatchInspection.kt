package io.intenics.python.fileNameMismatch.inspections

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.PyFile
import io.intenics.python.fileNameMismatch.services.FileNameMismatchService
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull

class FileNameMismatchInspection : LocalInspectionTool() {


    @Nls
    @NotNull
    override fun getDisplayName(): String {
        return "Sync Python File Names to Class Names"
    }

    @NotNull
    override fun getGroupDisplayName(): String {
        return "Python"
    }

    override fun checkFile(
        file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean
    ): Array<out ProblemDescriptor>? {
        val canonicalPath = file.virtualFile.canonicalPath!!
        if (canonicalPath.contains("/venv/") || canonicalPath.contains("/.venv/")
        ) {
            return null
        }

        val service = FileNameMismatchService(file)
        val firstClass = service.firstClass ?: return null
        val firstClassName = service.firstClassName ?: return null
        if (service.isMismatch) {
            val message = "The filename ${file.name} does not match the class $firstClassName."
            val problem = manager.createProblemDescriptor(
                firstClass.nameIdentifier!!,
                message,
                isOnTheFly,
                arrayOf(RenameFileQuickFix(service)),
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING
            )
            return arrayOf(problem)
        }
        return null
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PyElementVisitor() {
            override fun visitPyFile(node: PyFile) {
                super.visitPyFile(node)
                val problems = checkFile(node, holder.manager, isOnTheFly)
                if (problems != null) {
                    for (problem in problems) {
                        holder.registerProblem(problem)
                    }
                }
            }
        }
    }

}
