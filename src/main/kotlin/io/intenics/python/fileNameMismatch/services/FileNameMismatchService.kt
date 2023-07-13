package io.intenics.python.fileNameMismatch.services

import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyClass

class FileNameMismatchService(private val psiFile: PsiFile) {

    val isMismatch: Boolean
        get() {
            val fileName = psiFile.name
            return expectedName != fileName
        }
    val firstClass: PyClass?
        get() {
            val classes = psiFile.children.filterIsInstance<PyClass>()
            return classes.firstOrNull()
        }
    val firstClassName: String?
        get() {
            if (firstClass == null) {
                return null
            }
            return firstClass?.nameIdentifier?.text
        }
    val expectedName: String
        get() {
            if (firstClassName == null) {
                return psiFile.name
            }
            return "${convertPascalCaseToSnakeCase(firstClassName!!)}.py"
        }

    private fun convertPascalCaseToSnakeCase(name: String): String {
        val regex = Regex("([a-z0-9])([A-Z])")
        return name.replace(regex, "$1_$2").lowercase()
    }

}
