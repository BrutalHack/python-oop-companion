package io.intenics.python.oopCompanion.services

import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyClass

class FileNameMismatchService(private val psiFile: PsiFile) {

    val isMismatch: Boolean
        get() {
            if (classNames.isEmpty()) {
                return false
            }
            val fileName = psiFile.name
            val result = expectedNames.find { "$it.py" == fileName }
            return result == null
        }
    val expectedNames: List<String>
        get() {
            if (classNames.isEmpty()) {
                return listOf(psiFile.name)
            }
            return classNames.map { className -> convertPascalCaseToSnakeCase(className) }
        }
    private val classes: List<PyClass>
        get() {
            return psiFile.children.filterIsInstance<PyClass>()
        }
    private val classNames: List<String>
        get() {
            return classes.map { pyClass -> pyClass.nameIdentifier!!.text }
        }

    private fun convertPascalCaseToSnakeCase(name: String): String {
        val regex = Regex("([a-z0-9])([A-Z])")
        return name.replace(regex, "$1_$2").lowercase()
    }

}
