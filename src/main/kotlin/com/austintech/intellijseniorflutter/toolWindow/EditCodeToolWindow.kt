package com.austintech.intellijseniorflutter.toolWindow

import com.austintech.intellijseniorflutter.MyBundle
import com.austintech.intellijseniorflutter.uiComponents.SeniorJBTextArea
import com.austintech.intellijseniorflutter.listeners.SelectedFileListener
import com.austintech.intellijseniorflutter.services.SelectedFileService
import com.austintech.intellijseniorflutter.uiComponents.SeniorJBTextField
import com.austintech.intellijseniorflutter.uiComponents.TitledTextComponent
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import org.jdesktop.swingx.VerticalLayout
import java.awt.Toolkit
import javax.swing.BorderFactory
import javax.swing.JButton

class EditCodeToolWindow(toolWindow: ToolWindow) : SelectedFileListener {

    private val messageBusConnection = toolWindow.project.messageBus.connect()
    private val selectedFileService = toolWindow.project.service<SelectedFileService>()

    private lateinit var openFileNameLabel: JBLabel
    private lateinit var changesTextArea: SeniorJBTextArea
    private lateinit var importsTextField: SeniorJBTextField

    init {
        messageBusConnection.subscribe(SelectedFileService.FILE_SELECTED_TOPIC, this)
    }

    fun getContent() = JBPanel<JBPanel<*>>().apply {
        layout = VerticalLayout()
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        openFileNameLabel = JBLabel(getNameFromVirtualFile(selectedFileService.currentlySelectedFile))
        changesTextArea = SeniorJBTextArea(MyBundle.message("describe_the_changes_you_want_to_make")).apply {
            val screenHeight = Toolkit.getDefaultToolkit().screenSize.height
            val preferredHeight = screenHeight / 3
            preferredSize = preferredSize.apply { height = preferredHeight }
        }
        importsTextField = SeniorJBTextField(MyBundle.message("enter_class_names_to_provide_context"))

        val makeChangesButton = JButton(MyBundle.message("make_changes")).apply {
            addActionListener {
                println("Changes: ${changesTextArea.text}")
                println("Imports: ${importsTextField.text}")
            }
        }

        add(openFileNameLabel)
        add(TitledTextComponent(MyBundle.message("changes_colon"), changesTextArea))
        add(TitledTextComponent(MyBundle.message("imports_colon"), importsTextField))
        add(makeChangesButton)
    }

    private fun getNameFromVirtualFile(virtualFile: VirtualFile?): String =
        virtualFile?.name ?: MyBundle.getMessage("no_file_open")

    override fun onFileSelected(currentlySelectedFile: VirtualFile?) {
        openFileNameLabel.text = getNameFromVirtualFile(currentlySelectedFile)
    }
}

