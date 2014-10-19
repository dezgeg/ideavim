package org.jetbrains.plugins.ideavim.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.maddyhome.idea.vim.KeyHandler;
import com.maddyhome.idea.vim.helper.EditorDataContext;
import com.maddyhome.idea.vim.helper.RunnableHelper;
import org.jetbrains.plugins.ideavim.VimTestCase;
import static com.maddyhome.idea.vim.helper.StringHelper.parseKeys;


import javax.swing.*;
import java.util.List;

public class InsertNewLineActionTest extends VimTestCase {
  public void testInsertAfterFold() {
    doTest(parseKeys("O"),
           "\n" +
           "/* I should be fold\n" +
           " * a little more text\n" +
           " * and final fold */\n" +
           "and some <caret>text after",
           "\n" +
           "/* I should be fold\n" +
           " * a little more text\n" +
           " * and final fold */\n" +
           "\n" +
           "and some text after"
    );
  }

  public void testInsertBeforeFold() {
    doTest(parseKeys("zco"),
           "\n" +
           "/* I should be fold<caret>\n" +
           " * a little more text\n" +
           " * and final fold */\n" +
           "and some text after",

           "\n" +
           "/* I should be fold\n" +
           " * a little more text\n" +
           " * and final fold */\n" +
           "\n" +
           "and some text after"
    );
  }
}
