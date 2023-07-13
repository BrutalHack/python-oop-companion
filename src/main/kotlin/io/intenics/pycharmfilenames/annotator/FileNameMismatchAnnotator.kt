package io.intenics.pycharmfilenames.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyClass
import io.intenics.pycharmfilenames.inspections.FileNameMismatchInspectionTool

class FileNameMismatchAnnotator : Annotator {
    var log: Logger = Logger.getInstance(FileNameMismatchInspectionTool::class.java)

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        if (element !is PsiFile) {
            return
        }
        println("Checking file ${element.name}.")
        val classes = element.children.filterIsInstance<PyClass>()
        if (classes.isEmpty()) {
            log.info("No classes found in file ${element.name}.")
            return
        }
        log.info("Found ${classes.size} classes in file ${element.name}.")
        val firstClass: PyClass = classes.firstOrNull() ?: return
        // cast firstClass to PyClass
        val className = firstClass.nameIdentifier?.text
        val fileName = element.name
        if (className != null) {
            val expectedName = "${FileNameMismatchInspectionTool.convertPascalCaseToSnakeCase(className)}.py"
            if (expectedName == fileName) {
                log.info("File name $fileName matches class name $className.")
                return
            }

            holder.newAnnotation(HighlightSeverity.ERROR, "The filename does not match the class name.").create()
        }

    }
}