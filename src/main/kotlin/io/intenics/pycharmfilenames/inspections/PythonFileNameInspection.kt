package io.intenics.pycharmfilenames.inspections

import com.intellij.codeInsight.daemon.ProblemHighlightFilter
import com.intellij.codeInspection.*
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.PyFile
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull

class PythonFileNameInspection : LocalInspectionTool() {

    var log: Logger = Logger.getInstance(PythonFileNameInspection::class.java)

    @Nls
    @NotNull
    override fun getDisplayName(): String {
        log.info("Getting display name.")
        return "Class Name Inspection"
    }

    @NotNull
    override fun getGroupDisplayName(): String {
        log.info("Getting group display name.")
        return "Python"
    }

    override fun checkFile(
        file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean
    ): Array<out ProblemDescriptor>? {
        if (file.virtualFile.canonicalPath!!.contains("/venv/")) {
            return null
        }


        log.info("Checking file ${file.name}.")
        val classes = file.children.filterIsInstance<PyClass>()
        if (classes.isEmpty()) {
            log.info("No classes found in file ${file.name}.")
            return null
        }
        log.info("Found ${classes.size} classes in file ${file.name}.")
        val firstClass: PyClass = classes.firstOrNull() ?: return null
        // cast firstClass to PyClass
        val className = firstClass.nameIdentifier?.text
        val fileName = file.name
        if (className != null) {
            val expectedName = "${convertPascalCaseToSnakeCase(className)}.py"
            if (expectedName == fileName) {
                log.info("File name $fileName matches class name $className.")
                return null
            }
            val message = "The filename $fileName does not match the class $className."
            val problem = manager.createProblemDescriptor(
                firstClass,
                message,
                isOnTheFly,
                arrayOf(RenameFileQuickFix(fileName, className)),
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING
            )
            markFileWithProblem(file.virtualFile, problem)
            return arrayOf(problem)
        }
        return null
    }

    private fun markFileWithProblem(file: VirtualFile, problem: ProblemDescriptor) {
        ProblemHighlightFilter.EP_NAME.extensionList.forEach {
            log.info("Found extension ${it.javaClass.name}.")
        }
    }

    private class RenameFileQuickFix(private val currentName: String, private val className: String) : LocalQuickFix {
        var expectedName: String

        init {
            // convert PascalCase to snake_case including the first character
            expectedName = convertPascalCaseToSnakeCase(className)
        }

        @NotNull
        override fun getName(): String {
            return "Rename file to $expectedName"
        }

        @NotNull
        override fun getFamilyName(): String {
            return "Rename file to match the first class name"
        }

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val file = descriptor.psiElement.containingFile.virtualFile
            runWriteAction { file.rename(this, "$expectedName.py") }
        }
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

    companion object {
        fun convertPascalCaseToSnakeCase(name: String): String {
            val regex = Regex("([a-z0-9])([A-Z])")
            return name.replace(regex, "$1_$2").lowercase()
        }
    }
}
