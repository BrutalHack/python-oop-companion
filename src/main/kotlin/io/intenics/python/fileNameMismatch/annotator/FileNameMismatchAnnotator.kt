package io.intenics.python.fileNameMismatch.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyFile
import io.intenics.python.fileNameMismatch.services.FileNameMismatchService

class FileNameMismatchAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is PyFile) {
            return
        }
        val service = FileNameMismatchService(element)
        if (service.isMismatch) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "The file name does not match the class name."
            )
                .fileLevel()
                .create()
        }
    }
}