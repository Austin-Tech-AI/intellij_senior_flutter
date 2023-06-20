package com.austintech.intellijseniorflutter.toolWindow

import com.austintech.intellijseniorflutter.MyBundle
import com.austintech.intellijseniorflutter.listeners.EditCodeListener
import com.austintech.intellijseniorflutter.listeners.SelectedFileListener
import com.austintech.intellijseniorflutter.services.EditCodeService
import com.austintech.intellijseniorflutter.services.EditCodeState
import com.austintech.intellijseniorflutter.services.SelectedFileService
import com.austintech.intellijseniorflutter.uiComponents.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import org.jdesktop.swingx.VerticalLayout
import java.awt.Toolkit
import javax.swing.BorderFactory

class EditCodeToolWindow(toolWindow: ToolWindow) : SelectedFileListener, EditCodeListener {

    private val messageBusConnection = toolWindow.project.messageBus.connect()
    private val selectedFileService = toolWindow.project.service<SelectedFileService>()
    private val project = toolWindow.project

    private var openFileNameLabel: JBLabel
    private val changesTextArea = SeniorJBTextArea(MyBundle.message("describe_the_changes_you_want_to_make")).apply {
        val screenHeight = Toolkit.getDefaultToolkit().screenSize.height
        val preferredHeight = screenHeight / 3
        preferredSize = preferredSize.apply { height = preferredHeight }
        wrapStyleWord = true

        lineWrap = true
    }
    private val importsTextField = SeniorJBTextField(MyBundle.message("enter_class_names_to_provide_context"))

    private val jbPanel = JBPanel<JBPanel<*>>().apply {
        layout = VerticalLayout()
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    }

    private val scrollPane = JBScrollPane(jbPanel)

    init {
        messageBusConnection.subscribe(SelectedFileService.FILE_SELECTED_TOPIC, this)
        messageBusConnection.subscribe(EditCodeService.EDIT_CODE_TOPIC, this)

        openFileNameLabel = JBLabel(getNameFromVirtualFile(selectedFileService.currentlySelectedFile))
        addMakeChangesComponents()
    }

    fun getContent() = scrollPane

    private fun getNameFromVirtualFile(virtualFile: VirtualFile?): String =
        virtualFile?.name ?: MyBundle.getMessage("no_file_open")

    override fun onFileSelected(currentlySelectedFile: VirtualFile?) {
        openFileNameLabel.text = getNameFromVirtualFile(currentlySelectedFile)
    }

    override fun onStateChanged(editCodeState: EditCodeState) {
        ApplicationManager.getApplication().invokeLater {
            when (editCodeState) {
                EditCodeState.AwaitingUserInput -> updateEntireUIBody { addMakeChangesComponents() }
                EditCodeState.Loading -> updateEntireUIBody { jbPanel.add(LoadingUI()) }
                is EditCodeState.Loaded -> updateEntireUIBody {
                    val editor = DartSourceCodeEditor(editCodeState.newCode)
                    jbPanel.add(editor)
                    jbPanel.add(SizedBox.hSmall())
                    jbPanel.add(
                        SaveChangesButton(
                            project,
                            editor.getDartCode(),
                            selectedFileService.currentlySelectedFile!!,
                            changesTextArea,
                            importsTextField
                        ),
                    )
                    jbPanel.add(CancelButton(project))
                }
            }
        }
    }

    private fun updateEntireUIBody(addCallback: () -> Unit) {
        jbPanel.removeAll()
        addCallback()
        jbPanel.revalidate()
        jbPanel.repaint()
    }

    private fun addMakeChangesComponents() {
        jbPanel.add(openFileNameLabel)
        jbPanel.add(TitledTextComponent(MyBundle.message("changes_colon"), changesTextArea))
        jbPanel.add(TitledTextComponent(MyBundle.message("imports_colon"), importsTextField))
        jbPanel.add(SizedBox.hSmall())
        jbPanel.add(RequestChangesButton(project, changesTextArea, importsTextField).button)
    }
}

