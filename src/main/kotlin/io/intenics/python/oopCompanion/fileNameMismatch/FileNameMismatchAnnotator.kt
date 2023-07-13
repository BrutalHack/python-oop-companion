package io.intenics.python.oopCompanion.fileNameMismatch

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyFile
import io.intenics.python.oopCompanion.settings.OopCompanionSettingsState
import io.intenics.python.oopCompanion.services.FileNameMismatchService

class FileNameMismatchAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is PyFile) {
            return
        }
        val settingsState = OopCompanionSettingsState.instance
        if (!settingsState.isClassNameAnnotatorEnabled) {
            return
        }
        if (settingsState.isPathExcluded(element.containingFile.virtualFile.path)) {
            return
        }
        if (isExcludedViaComment(element)){
            return
        }
        val service = FileNameMismatchService(element)
        if (service.isMismatch) {
            val quickfixes = RenameFileQuickFix.createQuickFixForEachClassName(service)
            val message = "The filename ${element.name} does not match any class name in file."
            val inspectionManager = InspectionManager.getInstance(element.getProject())
            val problem = inspectionManager.createProblemDescriptor(
                element.navigationElement, message, true, quickfixes, ProblemHighlightType.GENERIC_ERROR_OR_WARNING
            )
            val annotationBuilder = holder.newAnnotation(
                HighlightSeverity.ERROR, message
            )
            for (quickfix in quickfixes) {
                annotationBuilder.newLocalQuickFix(quickfix, problem).registerFix()
            }
            annotationBuilder.newLocalQuickFix(SuppressRenameViaCommentQuickFix(), problem).registerFix()
            annotationBuilder.fileLevel().create()
        }
    }

    private fun isExcludedViaComment(element: PyFile): Boolean {
        val comments = element.children.filterIsInstance<PsiComment>()
        for (comment in comments) {
            if (comment.text.contains("OopCompanion:suppressRename")) {
                return true
            }
        }
        return false
    }
}