package org.jetbrains.plugins.ideavim;

import com.intellij.openapi.util.io.FileUtil;
import com.maddyhome.idea.vim.ui.ExEntryPanel;

import static com.maddyhome.idea.vim.helper.StringHelper.parseKeys;

import java.io.File;
import java.io.IOException;

/**
 * @author Tuomas Tynkkynen
 */
public class FuzzTest extends VimTestCase {
  public void testFuzz() throws IOException {
    if (!new File("/tmp/vimfuzz").exists()) return;

    File inputFile = new File("/tmp/vimfuzz/input.txt");
    for (int fileNumber = 1; fileNumber <= 99; fileNumber++) {
      File tempFile = new File(String.format("/tmp/vimfuzz/temp-%d.txt", fileNumber));
      FileUtil.copy(inputFile, tempFile);
      myFixture.configureByFile(tempFile.getAbsolutePath());

      String commandPath = String.format("/tmp/vimfuzz/commands-%05d.txt", fileNumber);
      System.out.println("Using test file: " + commandPath);
      int lineNumber = 1;
      for (String line : FileUtil.loadLines(new File(commandPath))) {
        //System.out.println(commandPath + ":" + lineNumber + ": " + line);
        typeText(parseKeys(line));
        assertFalse("Ex panel became active after line: " + line, ExEntryPanel.getInstance().isActive());

        File outputFile = new File(String.format("/tmp/vimfuzz/ideavim-%05d-%03d.txt", fileNumber, lineNumber));
        CharSequence content = myFixture.getEditor().getDocument().getCharsSequence();
        FileUtil.writeToFile(outputFile, content.toString());

        lineNumber++;
      }

    }
  }
}
