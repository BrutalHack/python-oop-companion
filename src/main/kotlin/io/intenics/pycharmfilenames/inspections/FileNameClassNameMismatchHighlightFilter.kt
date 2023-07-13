package io.intenics.pycharmfilenames.inspections

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.Condition
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.python.PythonFileType

class FileNameClassNameMismatchHighlightFilter : Condition<VirtualFile> {
    override fun value(virtualFile: VirtualFile?): Boolean {
        println("Highlighter Checking file ${virtualFile?.name}.")
        if (virtualFile == null) {
            return false
        }
        val fileType: FileType = virtualFile.fileType;
        return fileType == PythonFileType.INSTANCE;
    }

    private fun convertPascalCaseToSnakeCase(name: String): String {
        val regex = Regex("([a-z])([A-Z])")
        return name.replace(regex, "$1_$2").lowercase()
    }
}