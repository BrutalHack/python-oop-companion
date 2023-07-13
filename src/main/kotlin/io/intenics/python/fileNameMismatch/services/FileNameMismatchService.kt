package io.intenics.python.fileNameMismatch.services

import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyClass

class FileNameMismatchService(file: PsiFile) {

    val isMismatch = doesNotMatchFirstTypeName(file)
    val firstClass = getFirstClass(file)
    val firstClassName = getClassName(firstClass!!)
    val expectedName = getExpectedFileName(firstClassName!!)

    private fun doesNotMatchFirstTypeName(psiFile: PsiFile): Boolean {
        val fileName = psiFile.name
        val firstClass: PyClass = getFirstClass(psiFile) ?: return false
        val className = getClassName(firstClass) ?: return false

        val expectedName = getExpectedFileName(className)
        return expectedName != fileName
    }

    private fun getFirstClass(element: PsiFile): PyClass? {
        val classes = element.children.filterIsInstance<PyClass>()
        return classes.firstOrNull()
    }

    private fun getClassName(pyClass: PyClass): String? {
        return pyClass.nameIdentifier?.text
    }

    private fun getExpectedFileName(className: String): String {
        return "${convertPascalCaseToSnakeCase(className)}.py"
    }

    private fun convertPascalCaseToSnakeCase(name: String): String {
        val regex = Regex("([a-z0-9])([A-Z])")
        return name.replace(regex, "$1_$2").lowercase()
    }

}
