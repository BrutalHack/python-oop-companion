package io.intenics.python.oopCompanion

import com.jetbrains.python.codeInsight.imports.AddImportHelper
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyFromImportStatement

class ImportHelper {

    companion object {

        fun addImportIfMissing(pyFile: PyFile, importSourceName: String, importElement: String) {
            if (!hasImportStatement(pyFile, importSourceName, importElement)) {
                val existingImportStatement = getImportStatement(pyFile, importSourceName)
                if (existingImportStatement == null) {
                    AddImportHelper.addFromImportStatement(
                        pyFile, importSourceName,
                        importElement, null, null, null, null
                    )
                } else {
                    AddImportHelper.addNameToFromImportStatement(
                        existingImportStatement,
                        importElement, null
                    )
                }
            }
        }

        fun hasImportStatement(pyFile: PyFile, matchImportSourceName: String, matchImportElement: String): Boolean {
            val imports = pyFile.fromImports
            for (importStatement in imports) {
                if (!importStatement.importSourceQName!!.matches(matchImportSourceName)) {
                    continue
                }
                for (importElement in importStatement.importElements) {
                    if (importElement.importedQName == null) {
                        continue
                    }
                    if (importElement.importedQName!!.matches(matchImportElement)) {
                        return true
                    }
                }
            }
            return false;
        }

        fun getImportStatement(pyFile: PyFile, matchImportSource: String): PyFromImportStatement? {
            val imports = pyFile.fromImports
            for (importStatement in imports) {
                if (importStatement.importSourceQName == null) {
                    continue
                }
                if (importStatement.importSourceQName!!.matches(matchImportSource)) {
                    return importStatement
                }
            }
            return null
        }
    }
}
