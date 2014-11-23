package org.jetbrains.plugins.ideavim.action;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.impl.CurrentEditorProvider;
import com.intellij.openapi.command.impl.UndoManagerImpl;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import com.maddyhome.idea.vim.KeyHandler;
import com.maddyhome.idea.vim.helper.EditorDataContext;
import com.maddyhome.idea.vim.helper.RunnableHelper;
import com.maddyhome.idea.vim.ui.ExEntryPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

import static com.maddyhome.idea.vim.helper.StringHelper.parseKeys;

/**
 * @author Tuomas Tynkkynen
 */
public class UndoActionTest extends LightPlatformCodeInsightTestCase {
  public void testUndoCommandWithCharArgument() {
    doTestUndo(parseKeys("rz"), "fooba<caret>r\n");
  }

  public void testUndoCommandWithCharArgumentAndCount() {
    doTestUndo(parseKeys("3rz"), "foo<caret>bar\n");
  }

  public void testUndoInsert() {
    doTestUndo(parseKeys("ix<Esc>"), "foo<caret>bar\n");
  }

  public void testUndoInsertWithBackspaces() {
    doTestUndo(parseKeys("ibar<Backspace>z<Esc>"), "foo<caret>bar\n");
  }

  public void testUndoInsertWithCursorMovements() {
    doTestUndo(parseKeys("ibar<Left>a<Esc>"), "foo<caret>bar\n");
  }

  public void testAppend() {
    doTestUndo(parseKeys("abar<Enter>baz<Esc>"), "fo<caret>o\n");
  }

  private void doTestUndo(List<KeyStroke> keys, String text) {
    try {
      configureFromFileText("a.txt", text);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    init();
    typeText(keys);
    typeText(parseKeys("u"));
    //undo();
    checkResult(text);
  }

  private void checkResult(final String text) {
    checkResultByText(text);
    //CommandProcessor.getInstance().runUndoTransparentAction(new Runnable() {
    //  @Override
    //  public void run() {
    //    checkResultByText(text);
    //  }
    //});
  }

  private CurrentEditorProvider mySavedCurrentEditorProvider;

  public void setUp() throws Exception {
    super.setUp();
    //mySavedCurrentEditorProvider = getUndoManager().getEditorProvider();
  }

  public void tearDown() throws Exception {
    //getUndoManager().setEditorProvider(mySavedCurrentEditorProvider);
    super.tearDown();
  }

  // disabling execution of tests in command
  @Override
  protected void runTest() throws Throwable {
    new WriteAction<Void>() {
      @Override
      protected void run(@NotNull Result<Void> result) throws Throwable {
        doRunTest();
      }
    }.execute();
  }

  private UndoManagerImpl getUndoManager() {
    return (UndoManagerImpl)UndoManager.getInstance(getProject());
  }

  private void init() {
    //getUndoManager().setEditorProvider(new CurrentEditorProvider() {
    //  @Override
    //  public FileEditor getCurrentEditor() {
    //    return getFileEditor();
    //  }
    //});
  }

  private TextEditor getFileEditor() {
    return TextEditorProvider.getInstance().getTextEditor(getEditor());
  }

  @NotNull
  protected Editor typeText(@NotNull final List<KeyStroke> keys) {
    final Editor editor = getEditor();
    final KeyHandler keyHandler = KeyHandler.getInstance();
    final EditorDataContext dataContext = new EditorDataContext(editor);
    final Project project = getProject();
    for (final KeyStroke key : keys) {
      CommandProcessor.getInstance().executeCommand(project, new Runnable() {
        @Override
        public void run() {
          final ExEntryPanel exEntryPanel = ExEntryPanel.getInstance();
          if (exEntryPanel.isActive()) {
            exEntryPanel.handleKey(key);
          }
          else {
            keyHandler.handleKey(editor, key, dataContext);
          }
        }
      }, "", key, editor.getDocument());
    }
    return editor;
  }

  private void undo() {
    getUndoManager().undo(getFileEditor());
  }
}
