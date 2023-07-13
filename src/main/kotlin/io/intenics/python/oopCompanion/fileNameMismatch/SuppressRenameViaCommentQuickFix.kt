package io.intenics.python.oopCompanion.fileNameMismatch

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyImportStatementBase
import org.jetbrains.annotations.NotNull

class SuppressRenameViaCommentQuickFix() : LocalQuickFix {

    @NotNull
    override fun getName(): String {
        return "Suppress via Comment"
    }

    @NotNull
    override fun getFamilyName(): String {
        return "Rename file to match any contained class name"
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val file = descriptor.psiElement.containingFile as? PyFile ?: return
        val documentManager = PsiDocumentManager.getInstance(project)
        val document = documentManager.getDocument(file) ?: return

        val importStatements = PsiTreeUtil.findChildrenOfType(file, PyImportStatementBase::class.java)
        val lastImportLine = importStatements
            .mapNotNull { document.getLineNumber(it.textRange.endOffset).takeIf { line -> line > -1 } }
            .maxOrNull()
        val afterImportsLine = (lastImportLine ?: 0) + 1

        document.insertString(document.getLineStartOffset(afterImportsLine), "\n\n# OopCompanion:suppressRename\n")
        documentManager.commitDocument(document)
    }
}
