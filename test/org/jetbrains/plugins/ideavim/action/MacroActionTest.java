package org.jetbrains.plugins.ideavim.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.util.ui.UIUtil;
import com.maddyhome.idea.vim.VimPlugin;
import com.maddyhome.idea.vim.command.CommandState;
import com.maddyhome.idea.vim.common.Register;
import com.maddyhome.idea.vim.group.RegisterGroup;
import org.jetbrains.plugins.ideavim.VimTestCase;

import javax.swing.*;
import java.util.List;

import static com.maddyhome.idea.vim.helper.StringHelper.parseKeys;

/**
 * @author vlan
 */
public class MacroActionTest extends VimTestCase {
  // |q|
  public void testRecordMacro() {
    typeTextInFile(parseKeys("qa", "3l", "q"), "on<caret>e two three\n");
    assertNotRecording();
    assertEquals("3l", getRegisterContents('a'));
  }

  public void testKeyMappingsAreReplacedDuringMacroRecording() {
    configureByText("foo bar\n");
    typeText(commandToKeys("map j d"));
    typeText(parseKeys("qa", "jj", "q"));
    assertNotRecording();
    assertEquals("dd", getRegisterContents('a'));
  }

  public void testKeyMappingsAreIgnoredDuringMacroPlayback() {
    configureByText("foo bar\nfoo bar\n");
    typeText(parseKeys("qa", "dw", "q", "j0", ":map w $<Enter>", "@a"));
    UIUtil.dispatchAllInvocationEvents(); // required when testing macros
    myFixture.checkResult("bar\nbar\n");
  }

  private void assertNotRecording() {
    Editor editor = myFixture.getEditor();
    final CommandState commandState = CommandState.getInstance(editor);
    assertFalse(commandState.isRecording());
  }

  private String getRegisterContents(char registerName) {
    final RegisterGroup registerGroup = VimPlugin.getRegister();
    final Register register = registerGroup.getRegister(registerName);
    assertNotNull(register);
    return register.getText();
  }
}
