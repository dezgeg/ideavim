package org.jetbrains.plugins.ideavim.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.maddyhome.idea.vim.VimPlugin;
import com.maddyhome.idea.vim.group.MotionGroup;
import org.jetbrains.plugins.ideavim.VimTestCase;

import static com.maddyhome.idea.vim.command.CommandState.Mode.COMMAND;
import static com.maddyhome.idea.vim.command.CommandState.Mode.VISUAL;
import static com.maddyhome.idea.vim.helper.StringHelper.parseKeys;
import static com.maddyhome.idea.vim.helper.StringHelper.stringToKeys;

/**
 * @author vlan
 */
public class MotionActionTest extends VimTestCase {

  // |v_w|
  public void testWordForwardMotion() {
    typeTextInFile(parseKeys("vw"),
                   "f<caret>oobar baz\n");
    assertSelection("oobar b");
  }

  // |v_w|
  public void testWordForwardMotionOnEmptyLineFollowedByEmptyLine() {
    typeTextInFile(parseKeys("w"), "foo\n" +
                                   "<caret>\n" +
                                   "\n" +
                                   "bar\n");
    myFixture.checkResult("foo\n" +
                          "\n" +
                          "<caret>\n" +
                          "bar\n");
  }

  // |v_w|
  public void testTwoWordsForwardMotionOnEmptyLine() {
    typeTextInFile(parseKeys("2w"), "foo\n" +
                                   "<caret>\n" +
                                   "\n" +
                                   "bar\n");
    myFixture.checkResult("foo\n" +
                          "\n" +
                          "\n" +
                          "<caret>bar\n");
  }

  // |v_e|
  public void testWordEndForwardMotion() {
    typeTextInFile(parseKeys("ve"),
                   "f<caret>oobar baz\n");
    assertSelection("oobar");
  }

  // |v_e|
  public void testWordEndForwardMotionOnEmptyLineFollowedByEmptyLine() {
    typeTextInFile(parseKeys("e"), "foo\n" +
                                   "<caret>\n" +
                                   "\n" +
                                   "bar\n");
    myFixture.checkResult("foo\n" +
                          "\n" +
                          "\n" +
                          "ba<caret>r\n");
  }

  // |v_e|
  public void testWordEndForwardMotionOnEmptyLineFollowedByTwoEmptyLines() {
    typeTextInFile(parseKeys("e"), "foo\n" +
                                   "<caret>\n" +
                                   "\n" +
                                   "\n" +
                                   "bar\n");
    myFixture.checkResult("foo\n" +
                          "\n" +
                          "\n" +
                          "\n" +
                          "ba<caret>r\n");
  }

  // |v_e|
  public void testTwoWordEndsForwardMotionOnEmptyLineFollowedByTwoEmptyLines() {
    typeTextInFile(parseKeys("2e"), "foo\n" +
                                    "<caret>\n" +
                                    "\n" +
                                    "\n" +
                                    "bar\n" +
                                    "baz\n");
    myFixture.checkResult("foo\n" +
                          "\n" +
                          "\n" +
                          "\n" +
                          "bar\n" +
                          "ba<caret>z\n");
  }

  // |v_e|
  public void testTwoWordEndsForwardMotionOnEmptyLine() {
    typeTextInFile(parseKeys("2e"), "foo\n" +
                                    "<caret>\n" +
                                    "\n" +
                                    "bar\n" +
                                    "baz\n");
    myFixture.checkResult("foo\n" +
                          "\n" +
                          "\n" +
                          "bar\n" +
                          "ba<caret>z\n");
  }

  // VIM-198 |v_iw|
  public void testVisualMotionInnerWordNewLineAtEOF() {
    typeTextInFile(parseKeys("viw"),
                   "one tw<caret>o\n");
    assertSelection("two");
  }

  // |v_iW|
  public void testVisualMotionInnerBigWord() {
    typeTextInFile(parseKeys("viW"),
                   "one tw<caret>o.three four\n");
    assertSelection("two.three");
  }

  // |v_iw|
  public void testVisualMotionInnerOnEmptyFile() {
    typeTextInFile(parseKeys("viw"), "");
    assertSelection(null);
  }

  // |v_iW|
  public void testVisualMotionInnerBigWordToRight() {
    typeTextInFile(parseKeys("vliW"),
                   "one tw<caret>o.three four\n");
    assertSelection("o.three");
  }

  // |v_iW|
  public void testVisualMotionInnerTwoBigWordsToRight() {
    typeTextInFile(parseKeys("vl2iW"),
                   "one tw<caret>o.three four  five\n");
    assertSelection("o.three ");
  }

  // |v_iW|
  public void testVisualMotionInnerBigWordToRightWhenAlreadyOnAWord() {
    typeTextInFile(parseKeys("vlliW"),
                   "<caret>one two.three four\n");
    assertSelection("one ");
  }

  // |v_iW|
  public void testVisualMotionInnerTwoBigWordsToRightWhenAlreadyOnAWord() {
    typeTextInFile(parseKeys("vll2iW"),
                   "<caret>one two.three four\n");
    assertSelection("one two.three");
  }

  // |v_iW|
  public void testVisualMotionInnerBigWordToLeft() {
    typeTextInFile(parseKeys("vhiW"),
                   "one two.t<caret>hree four\n");
    assertSelection("two.th");
  }

  // |v_iW|
  public void testVisualMotionInnerTwoBigWordToLeft() {
    typeTextInFile(parseKeys("vh2iW"),
                   "zero  one two.t<caret>hree four\n");
    assertSelection(" two.th");
  }

  // |v_iW|
  public void testVisualMotionInnerBigWordToLeftWhenAlreadyOnAWord() {
    typeTextInFile(parseKeys("vhhhiW"),
                   "one two.three fou<caret>r\n");
    assertSelection(" four");
  }

  // |v_iW|
  public void testVisualMotionInnerTwoBigWordsToLeftWhenAlreadyOnAWord() {
    typeTextInFile(parseKeys("vhhh2iW"),
                   "<one two.three fou<caret>r\n");
    assertSelection("two.three four");
  }

  // |v_aW|
  public void testVisualMotionOuterBigWordToRight() {
    typeTextInFile(parseKeys("vlaW"),
                   "one tw<caret>o.three four\n");
    assertSelection("o.three ");
  }

  // |v_aW|
  public void testVisualMotionOuterTwoBigWordsToRight() {
    typeTextInFile(parseKeys("vl2aW"),
                   "one tw<caret>o.three four  five\n");
    assertSelection("o.three four  ");
  }

  // |v_aW|
  public void testVisualMotionOuterBigWordToRightWhenAlreadyOnAWord() {
    typeTextInFile(parseKeys("vllaW"),
                   "<caret>one two.three four\n");
    assertSelection("one two.three");
  }

  // |v_aW|
  public void testVisualMotionOuterTwoBigWordsToRightWhenAlreadyOnAWord() {
    typeTextInFile(parseKeys("vll2aW"),
                   "<caret>one two.three four five\n");
    assertSelection("one two.three four");
  }

  // |v_aW|
  public void testVisualMotionOuterBigWordToLeft() {
    typeTextInFile(parseKeys("vhaW"),
                   "one two.t<caret>hree four\n");
    assertSelection(" two.th");
  }

  // |v_aW|
  public void testVisualMotionOuterTwoBigWordToLeft() {
    typeTextInFile(parseKeys("vh2aW"),
                   "zero  one two.t<caret>hree four\n");
    assertSelection("  one two.th");
  }

  // |v_aW|
  public void testVisualMotionOuterBigWordToLeftWhenAlreadyOnAWord() {
    typeTextInFile(parseKeys("vhhhaW"),
                   "one two.three fou<caret>r\n");
    assertSelection("two.three four");
  }

  // |v_aW|
  public void testVisualMotionOuterTwoBigWordsToLeftWhenAlreadyOnAWord() {
    typeTextInFile(parseKeys("vhhh2aW"),
                   "one two.three four fiv<caret>e\n");
    assertSelection("two.three four five");
  }

  public void testEscapeInCommand() {
    typeTextInFile(parseKeys("f", "<Esc>", "<Esc>"),
                   "on<caret>e two\n" +
                   "three\n");
    assertPluginError(true);
    assertOffset(2);
    assertMode(COMMAND);
  }

  // |h| |l|
  public void testLeftRightMove() {
    typeTextInFile(parseKeys("14l", "2h"),
                   "on<caret>e two three four five six seven\n");
    assertOffset(14);
  }

  // |j| |k|
  public void testUpDownMove() {
    final Editor editor = typeTextInFile(parseKeys("2j", "k"),
                                         "one\n" +
                                         "tw<caret>o\n" +
                                         "three\n" +
                                         "four\n");
    final VisualPosition position = editor.getCaretModel().getVisualPosition();
    assertEquals(new VisualPosition(2, 2), position);
  }

  public void testDeleteDigitsInCount() {
    typeTextInFile(parseKeys("42<Delete>l"),
                   "on<caret>e two three four five six seven\n");
    assertOffset(6);
  }

  // |f|
  public void testForwardToTab() {
    typeTextInFile(parseKeys("f<Tab>"),
                   "on<caret>e two\tthree\nfour\n");
    assertOffset(7);
    assertMode(COMMAND);
  }

  public void testIllegalCharArgument() {
    typeTextInFile(parseKeys("f<Insert>"),
                   "on<caret>e two three four five six seven\n");
    assertOffset(2);
    assertMode(COMMAND);
  }

  // |F| |i_CTRL-K|
  public void testBackToDigraph() {
    typeTextInFile(parseKeys("F<C-K>O:"),
                   "Hallo, Öster<caret>reich!\n");
    assertOffset(7);
    assertMode(COMMAND);
  }

  // VIM-771 |t| |;|
  public void testTillCharRightTwice() {
    typeTextInFile(parseKeys("t:", "t:"),
                   "<caret> 1:a 2:b 3:c \n");
    myFixture.checkResult(" <caret>1:a 2:b 3:c \n");
  }

  // VIM-771 |t| |;|
  public void testTillCharRightRepeated() {
    typeTextInFile(parseKeys("t:;"),
                   "<caret> 1:a 2:b 3:c \n");
    myFixture.checkResult(" 1:a <caret>2:b 3:c \n");
  }

  // VIM-771 |t| |;|
  public void testTillCharRightRepeatedWithCount2() {
    typeTextInFile(parseKeys("t:2;"),
                   "<caret> 1:a 2:b 3:c \n");
    myFixture.checkResult(" 1:a <caret>2:b 3:c \n");
  }

  // VIM-771 |t| |;|
  public void testTillCharRightRepeatedWithCountHigherThan2() {
    typeTextInFile(parseKeys("t:3;"), "<caret> 1:a 2:b 3:c \n");
    myFixture.checkResult(" 1:a 2:b <caret>3:c \n");
  }

  // VIM-771 |t| |,|
  public void testTillCharRightReverseRepeated() {
    typeTextInFile(parseKeys("t:,,"),
                   " 1:a 2:b<caret> 3:c \n");
    myFixture.checkResult(" 1:<caret>a 2:b 3:c \n");
  }

  // VIM-771 |t| |,|
  public void testTillCharRightReverseRepeatedWithCount2() {
    typeTextInFile(parseKeys("t:,2,"),
                   " 1:a 2:b<caret> 3:c \n");
    myFixture.checkResult(" 1:<caret>a 2:b 3:c \n");
  }

  // VIM-771 |t| |,|
  public void testTillCharRightReverseRepeatedWithCountHigherThan3() {
    typeTextInFile(parseKeys("t:,3,"),
                   " 0:_ 1:a 2:b<caret> 3:c \n");
    myFixture.checkResult(" 0:<caret>_ 1:a 2:b 3:c \n");
  }

  // VIM-771 |T| |;|
  public void testTillCharLeftTwice() {
    typeTextInFile(parseKeys("T:", "T:"),
                   " 1:a 2:b 3:c <caret> \n");
    myFixture.checkResult(" 1:a 2:b 3:<caret>c  \n");
  }

  // VIM-771 |T| |;|
  public void testTillCharLeftRepeated() {
    typeTextInFile(parseKeys("T:;"),
                   " 1:a 2:b 3:c <caret> \n");
    myFixture.checkResult(" 1:a 2:<caret>b 3:c  \n");
  }

  // VIM-771 |T| |;|
  public void testTillCharLeftRepeatedWithCount2() {
    typeTextInFile(parseKeys("T:2;"),
    " 1:a 2:b 3:c <caret> \n");
    myFixture.checkResult(" 1:a 2:<caret>b 3:c  \n");
  }

  // VIM-771 |T| |;|
  public void testTillCharLeftRepeatedWithCountHigherThan2() {
    typeTextInFile(parseKeys("T:3;"),
    " 1:a 2:b 3:c <caret> \n");
    myFixture.checkResult(" 1:<caret>a 2:b 3:c  \n");
  }

  // VIM-771 |T| |,|
  public void testTillCharLeftReverseRepeated() {
    typeTextInFile(parseKeys("T:,"),
    " 1:a <caret>2:b 3:c \n");
    myFixture.checkResult(" 1:a <caret>2:b 3:c \n");
  }

  // VIM-771 |T| |,|
  public void testTillCharLeftReverseRepeatedWithCount2() {
    typeTextInFile(parseKeys("T:2,"),
    " 1:a <caret>2:b 3:c \n");
    myFixture.checkResult(" 1:a 2:b <caret>3:c \n");
  }

  // VIM-771 |T| |,|
  public void testTillCharLeftReverseRepeatedWithCountHigherThan3() {
    typeTextInFile(parseKeys("T:3,"),
    " 1:a <caret>2:b 3:c 4:d \n");
    myFixture.checkResult(" 1:a 2:b 3:c <caret>4:d \n");
  }

  public void testTillCharRightRepeatedMotion() {
    VimPlugin.getMotion().setLastFTCmd(MotionGroup.LAST_t, '.');
    doTest(parseKeys("d;"), "b. <caret>_ .c", "b. .c");
  }

  public void testTillCharRightRepeatedReverseMotion() {
    VimPlugin.getMotion().setLastFTCmd(MotionGroup.LAST_t, '.');
    doTest(parseKeys("d,"), "b. <caret>_ .c", "b._ .c");
  }

  public void testTillCharLeftRepeatedMotion() {
    VimPlugin.getMotion().setLastFTCmd(MotionGroup.LAST_T, '.');
    doTest(parseKeys("d;"), "b. <caret>_ .c", "b._ .c");
  }

  public void testTillCharLeftRepeatedReverseMotion() {
    VimPlugin.getMotion().setLastFTCmd(MotionGroup.LAST_T, '.');
    doTest(parseKeys("d,"), "b. <caret>_ .c", "b. .c");
  }

  public void testForwardCharRepeatedMotion() {
    VimPlugin.getMotion().setLastFTCmd(MotionGroup.LAST_f, '.');
    doTest(parseKeys("d;"), "b. <caret>_ .c", "b. c");
  }

  public void testForwardCharRepeatedReverseMotion() {
    VimPlugin.getMotion().setLastFTCmd(MotionGroup.LAST_f, '.');
    doTest(parseKeys("d,"), "b. <caret>_ .c", "b_ .c");
  }

  public void testBackwardCharRepeatedMotion() {
    VimPlugin.getMotion().setLastFTCmd(MotionGroup.LAST_F, '.');
    doTest(parseKeys("d;"), "b. <caret>_ .c", "b_ .c");
  }

  public void testBackwardCharRepeatedReverseMotion() {
    VimPlugin.getMotion().setLastFTCmd(MotionGroup.LAST_F, '.');
    doTest(parseKeys("d,"), "b. <caret>_ .c", "b. c");
  }

  // VIM-326 |d| |v_ib|
  public void testDeleteInnerBlock() {
    typeTextInFile(parseKeys("di)"),
                   "foo(\"b<caret>ar\")\n");
    myFixture.checkResult("foo()\n");
  }

  // VIM-326 |d| |v_ib|
  public void testDeleteInnerBlockCaretBeforeString() {
    typeTextInFile(parseKeys("di)"),
                   "foo(<caret>\"bar\")\n");
    myFixture.checkResult("foo()\n");
  }

  // VIM-326 |c| |v_ib|
  public void testChangeInnerBlockCaretBeforeString() {
    typeTextInFile(parseKeys("ci)"),
                   "foo(<caret>\"bar\")\n");
    myFixture.checkResult("foo()\n");
  }

  // VIM-392 |c| |v_ib|
  public void testChangeInnerBlockCaretBeforeBlock() {
    typeTextInFile(parseKeys("ci)"),
                   "foo<caret>(bar)\n");
    myFixture.checkResult("foo()\n");
    assertOffset(4);
  }

  // |v_ib|
  public void testInnerBlockCrashWhenNoDelimiterFound() {
    typeTextInFile(parseKeys("di)"), "(x\n");
    myFixture.checkResult("(x\n");
  }

  // |v_ib|
  public void testInnerBlockCrashWhenEmptyFile() {
    typeTextInFile(parseKeys("di)"), "");
    myFixture.checkResult("");
  }

  // VIM-314 |d| |v_iB|
  public void testDeleteInnerCurlyBraceBlock() {
    typeTextInFile(parseKeys("di{"),
                   "{foo, b<caret>ar, baz}\n");
    myFixture.checkResult("{}\n");
  }

  // VIM-314 |d| |v_iB|
  public void testDeleteInnerCurlyBraceBlockCaretBeforeString() {
    typeTextInFile(parseKeys("di{"),
                   "{foo, <caret>\"bar\", baz}\n");
    myFixture.checkResult("{}\n");
  }

  // |d| |v_aB|
  public void testDeleteOuterCurlyBraceBlock() {
    typeTextInFile(parseKeys("da{"),
                   "x = {foo, b<caret>ar, baz};\n");
    myFixture.checkResult("x = ;\n");
  }

  // VIM-261 |c| |v_iB|
  public void testChangeInnerCurlyBraceBlockMultiLine() {
    typeTextInFile(parseKeys("ci{"),
                   "foo {\n" +
                   "    <caret>bar\n" +
                   "}\n");
    myFixture.checkResult("foo {\n" +
                          "\n" +
                          "}\n");
    assertOffset(6);
  }

  // VIM-275 |d| |v_ib|
  public void testDeleteInnerParensBlockBeforeOpen() {
    typeTextInFile(parseKeys("di)"),
                   "foo<caret>(bar)\n");
    myFixture.checkResult("foo()\n");
    assertOffset(4);
  }

  // |d| |v_ib|
  public void testDeleteInnerParensBlockBeforeClose() {
    typeTextInFile(parseKeys("di)"),
                   "foo(bar<caret>)\n");
    myFixture.checkResult("foo()\n");
  }

  // |d| |v_ab|
  public void testDeleteOuterBlock() {
    typeTextInFile(parseKeys("da)"),
                   "foo(b<caret>ar, baz);\n");
    myFixture.checkResult("foo;\n");
  }



  // |d| |v_aw|
  public void testDeleteOuterWord() {
    typeTextInFile(parseKeys("daw"),
                   "one t<caret>wo three\n");
    myFixture.checkResult("one three\n");
  }

  // |d| |v_aW|
  public void testDeleteOuterBigWord() {
    typeTextInFile(parseKeys("daW"),
                   "one \"t<caret>wo\" three\n");
    myFixture.checkResult("one three\n");
  }

  // |d| |v_aW|
  public void testDeleteOuterBigWordOnLineWithNoNewline() {
    typeTextInFile(parseKeys("daW"),
                   "foo");
    myFixture.checkResult("");
  }

  // |d| |v_aW|
  public void testDeleteOuterBigWordOnLineWithNoNewlineOnLastCharOfWord() {
    typeTextInFile(parseKeys("daW"),
                   "fo<caret>o");
    myFixture.checkResult("");
  }

  // |d| |v_is|
  public void testDeleteInnerSentence() {
    typeTextInFile(parseKeys("dis"),
                   "Hello World! How a<caret>re you? Bye.\n");
    myFixture.checkResult("Hello World!  Bye.\n");
  }

  // |d| |v_as|
  public void testDeleteOuterSentence() {
    typeTextInFile(parseKeys("das"),
                   "Hello World! How a<caret>re you? Bye.\n");
    myFixture.checkResult("Hello World! Bye.\n");
  }

  // |v_is|
  public void testMotionInnerSentenceOnEmptyFile() {
    typeTextInFile(parseKeys("vis"), "");
    myFixture.checkResult("");
  }

  // |v_as|
  public void testSentenceMotionPastStartOfFile() {
    typeTextInFile(parseKeys("8("), "\n" +
                                    "P<caret>.\n");
  }

  // |d| |v_ip|
  public void testDeleteInnerParagraph() {
    typeTextInFile(parseKeys("dip"),
                   "Hello World!\n" +
                   "\n" +
                   "How a<caret>re you?\n" +
                   "Bye.\n" +
                   "\n" +
                   "Bye.\n");
    myFixture.checkResult("Hello World!\n" +
                          "\n" +
                          "\n" +
                          "Bye.\n");
  }

  // |d| |v_ip|
  public void testDeleteInnerParagraphOnEmptyFile() {
    typeTextInFile(parseKeys("dip"), "");
    myFixture.checkResult("");
  }

  // |d| |v_ap|
  public void testDeleteOuterParagraph() {
    typeTextInFile(parseKeys("dap"),
                   "Hello World!\n" +
                   "\n" +
                   "How a<caret>re you?\n" +
                   "Bye.\n" +
                   "\n" +
                   "Bye.\n");
    myFixture.checkResult("Hello World!\n" +
                          "\n" +
                          "Bye.\n");
  }

  // |d| |v_a]|
  public void testDeleteOuterBracketBlock() {
    typeTextInFile(parseKeys("da]"),
                   "foo = [\n" +
                   "    one,\n" +
                   "    t<caret>wo,\n" +
                   "    three\n" +
                   "];\n");
    myFixture.checkResult("foo = ;\n");
  }

  // |d| |v_i]|
  public void testDeleteInnerBracketBlock() {
    typeTextInFile(parseKeys("di]"),
                   "foo = [one, t<caret>wo];\n");
    myFixture.checkResult("foo = [];\n");
  }

  // |d| |v_i>|
  public void testDeleteInnerAngleBracketBlock() {
    typeTextInFile(parseKeys("di>"),
                   "Foo<Foo, B<caret>ar> bar\n");
    myFixture.checkResult("Foo<> bar\n");
  }

  // |d| |v_a>|
  public void testDeleteOuterAngleBracketBlock() {
    typeTextInFile(parseKeys("da>"),
                   "Foo<Foo, B<caret>ar> bar\n");
    myFixture.checkResult("Foo bar\n");
  }

  // VIM-132 |d| |v_i"|
  public void testDeleteInnerDoubleQuoteString() {
    typeTextInFile(parseKeys("di\""),
                   "foo = \"bar b<caret>az\";\n");
    myFixture.checkResult("foo = \"\";\n");
  }

  // VIM-132 |d| |v_a"|
  public void testDeleteOuterDoubleQuoteString() {
    typeTextInFile(parseKeys("da\""),
                   "foo = \"bar b<caret>az\";\n");
    myFixture.checkResult("foo = ;\n");
  }

  // VIM-132 |d| |v_i"|
  public void testDeleteDoubleQuotedStringStart() {
    typeTextInFile(parseKeys("di\""),
                   "foo = [\"one\", <caret>\"two\", \"three\"];\n");
    myFixture.checkResult("foo = [\"one\", \"\", \"three\"];\n");
  }

  // VIM-132 |d| |v_i"|
  public void testDeleteDoubleQuotedStringEnd() {
    typeTextInFile(parseKeys("di\""),
                   "foo = [\"one\", \"two<caret>\", \"three\"];\n");
    myFixture.checkResult("foo = [\"one\", \"\", \"three\"];\n");
  }

  // VIM-132 |d| |v_i"|
  public void testDeleteDoubleQuotedStringWithEscapes() {
    typeTextInFile(parseKeys("di\""),
                   "foo = \"fo\\\"o b<caret>ar\";\n");
    myFixture.checkResult("foo = \"\";\n");
  }

  // VIM-132 |d| |v_i"|
  public void testDeleteDoubleQuotedStringBefore() {
    typeTextInFile(parseKeys("di\""),
                   "f<caret>oo = [\"one\", \"two\", \"three\"];\n");
    myFixture.checkResult("foo = [\"\", \"two\", \"three\"];\n");
  }

  // VIM-132 |v_i"|
  public void testInnerDoubleQuotedStringSelection() {
    typeTextInFile(parseKeys("vi\""),
                   "foo = [\"o<caret>ne\", \"two\"];\n");
    assertSelection("one");
  }

  // |c| |v_i"|
  public void testChangeEmptyQuotedString() {
    typeTextInFile(parseKeys("ci\""),
                   "foo = \"<caret>\";\n");
    myFixture.checkResult("foo = \"\";\n");
  }

  // VIM-132 |d| |v_i'|
  public void testDeleteInnerSingleQuoteString() {
    typeTextInFile(parseKeys("di'"),
                   "foo = 'bar b<caret>az';\n");
    myFixture.checkResult("foo = '';\n");
  }

  // VIM-132 |d| |v_i`|
  public void testDeleteInnerBackQuoteString() {
    typeTextInFile(parseKeys("di`"),
                   "foo = `bar b<caret>az`;\n");
    myFixture.checkResult("foo = ``;\n");
  }

  // VIM-132 |d| |v_a'|
  public void testDeleteOuterSingleQuoteString() {
    typeTextInFile(parseKeys("da'"),
                   "foo = 'bar b<caret>az';\n");
    myFixture.checkResult("foo = ;\n");
  }

  // VIM-132 |d| |v_a`|
  public void testDeleteOuterBackQuoteString() {
    typeTextInFile(parseKeys("da`"),
                   "foo = `bar b<caret>az`;\n");
    myFixture.checkResult("foo = ;\n");
  }

  // |v_i`|
  public void testQuoteMotionOnEmptyFile() {
    typeTextInFile(parseKeys("vi`"), "");
    myFixture.checkResult("");
  }

  // |%|
  public void testPercentMatchSimple() {
    typeTextInFile(parseKeys("%"),
                   "foo(b<caret>ar)\n");
    assertOffset(3);
  }

  // |%|
  public void testPercentMatchMultiLine() {
    typeTextInFile(parseKeys("%"),
                   "foo(bar,\n" +
                   "    baz,\n" +
                   "    <caret>quux)\n");
    assertOffset(3);
  }

  // |%|
  public void testPercentMatchParensInString() {
    typeTextInFile(parseKeys("%"),
                   "foo(bar, \"foo(bar\", <caret>baz)\n");
    assertOffset(3);
  }

  // |%|
  public void testPercentMatchXmlCommentStart() {
    configureByXmlText("<caret><!-- foo -->");
    typeText(parseKeys("%"));
    assertOffset(11);
  }

  // |%|
  public void testPercentDoesntMatchPartialXmlComment() {
    configureByXmlText("<!<caret>-- ");
    typeText(parseKeys("%"));
    assertOffset(2);
  }

  // |%|
  public void testPercentMatchXmlCommentEnd() {
    configureByXmlText("<!-- foo --<caret>>");
    typeText(parseKeys("%"));
    assertOffset(0);
  }

  // |%|
  public void BROKEN_testPercentMatchJavaCommentStart() {
    configureByJavaText("/<caret>* foo */");
    typeText(parseKeys("%"));
    assertOffset(8);
  }

  // |%|
  public void BROKEN_testPercentDoesntMatchPartialJavaComment() {
    configureByJavaText("<caret>/* ");
    typeText(parseKeys("%"));
    assertOffset(0);
  }

  // |%|
  public void BROKEN_testPercentMatchJavaCommentEnd() {
    configureByJavaText("/* foo <caret>*/");
    typeText(parseKeys("%"));
    assertOffset(0);
  }

  // |%|
  public void BROKEN_testPercentMatchJavaDocCommentStart() {
    configureByJavaText("/*<caret>* foo */");
    typeText(parseKeys("%"));
    assertOffset(9);
  }

  // |%|
  public void BROKEN_testPercentMatchJavaDocCommentEnd() {
    configureByJavaText("/** foo *<caret>/");
    typeText(parseKeys("%"));
    assertOffset(0);
  }

  // |%|
  public void testPercentDoesntMatchAfterCommentStart() {
    configureByJavaText("/*<caret> foo */");
    typeText(parseKeys("%"));
    assertOffset(2);
  }

  // |%|
  public void testPercentDoesntMatchBeforeCommentEnd() {
    configureByJavaText("/* foo <caret> */");
    typeText(parseKeys("%"));
    assertOffset(7);
  }

  // |[(|
  public void testUnmatchedOpenParenthesis() {
    typeTextInFile(parseKeys("[("),
                   "foo(bar, foo(bar, <caret>baz\n" +
                   "bar(foo)\n");
    assertOffset(12);
  }

  // |[{|
  public void testUnmatchedOpenBracketMultiLine() {
    typeTextInFile(parseKeys("[{"),
                   "foo {\n" +
                   "    bar,\n" +
                   "    b<caret>az\n");
    assertOffset(4);
  }

  // |])|
  public void testUnmatchedCloseParenthesisMultiLine() {
    typeTextInFile(parseKeys("])"),
                   "foo(bar, <caret>baz,\n" +
                   "   quux)\n");
    assertOffset(21);
  }

  // |]}|
  public void testUnmatchedCloseBracket() {
    typeTextInFile(parseKeys("]}"),
                   "{bar, <caret>baz}\n");
    assertOffset(9);
  }

  // VIM-331 |w|
  public void testNonAsciiLettersInWord() {
    typeTextInFile(parseKeys("w"),
                   "Če<caret>ská republika");
    assertOffset(6);
  }

  // VIM-58 |w|
  public void testHiraganaToPunctuation() {
    typeTextInFile(parseKeys("w"),
                   "は<caret>はは!!!");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testHiraganaToFullWidthPunctuation() {
    typeTextInFile(parseKeys("w"),
                   "は<caret>はは！！！");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testKatakanaToHiragana() {
    typeTextInFile(parseKeys("w"),
                   "チ<caret>チチははは");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testKatakanaToHalfWidthKana() {
    typeTextInFile(parseKeys("w"),
                   "チ<caret>チチｳｳｳ");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testKatakanaToDigits() {
    typeTextInFile(parseKeys("w"),
                   "チ<caret>チチ123");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testKatakanaToLetters() {
    typeTextInFile(parseKeys("w"),
                   "チ<caret>チチ123");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testKatakanaToFullWidthLatin() {
    typeTextInFile(parseKeys("w"),
                   "チ<caret>チチＡＡＡ");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testKatakanaToFullWidthDigits() {
    typeTextInFile(parseKeys("w"),
                   "チ<caret>チチ３３３");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testHiraganaToKatakana() {
    typeTextInFile(parseKeys("w"),
                   "は<caret>ははチチチ");
    assertOffset(3);
  }

  // VIM-58 |w|
  public void testHalftWidthKanaToLetters() {
    typeTextInFile(parseKeys("w"),
                   "ｳｳｳAAA");
    assertOffset(3);
  }

  // |w|
  public void testEmptyLineIsWord() {
    typeTextInFile(parseKeys("w"),
                   "<caret>one\n" +
                   "\n" +
                   "two\n");
    assertOffset(4);
  }

  // |w|
  public void testNotEmptyLineIsNotWord() {
    typeTextInFile(parseKeys("w"),
                   "<caret>one\n" +
                   " \n" +
                   "two\n");
    assertOffset(6);
  }

  // VIM-312 |w|
  public void testLastWord() {
    typeTextInFile(parseKeys("w"),
                   "<caret>one\n");
    assertOffset(2);
  }

  // |b|
  public void testWordBackwardsAtFirstLineWithWhitespaceInFront() {
    typeTextInFile(parseKeys("b"),
                   "    <caret>x\n");
    assertOffset(0);
  }

  public void testRightToLastChar() {
    typeTextInFile(parseKeys("i<Right>"),
                   "on<caret>e\n");
    assertOffset(3);
  }

  public void testDownToLastEmptyLine() {
    typeTextInFile(parseKeys("j"),
                   "<caret>one\n" +
                   "\n");
    assertOffset(4);
  }

  // VIM-262 |c_CTRL-R|
  public void testSearchFromRegister() {
    VimPlugin.getRegister().setKeys('a', stringToKeys("two"));
    typeTextInFile(parseKeys("/", "<C-R>a", "<Enter>"),
                   "<caret>one\n" +
                   "two\n" +
                   "three\n");
    assertOffset(4);
  }

  // |v_gv|
  public void testSwapVisualSelections() {
    typeTextInFile(parseKeys("viw", "<Esc>", "0", "viw", "gv", "d"),
                   "foo <caret>bar\n");
    myFixture.checkResult("foo \n");
  }

  // |CTRL-V|
  public void testVisualBlockSelectionsDisplayedCorrectlyMovingRight() {
    typeTextInFile(parseKeys("<C-V>jl"),
                   "<caret>foo\n" +
                   "bar\n");
    myFixture.checkResult("<selection>fo</selection>o\n" +
                          "<selection>ba</selection>r\n");
  }

  // |CTRL-V|
  public void testVisualBlockSelectionsDisplayedCorrectlyMovingLeft() {
    typeTextInFile(parseKeys("<C-V>jh"),
                   "fo<caret>o\n" +
                   "bar\n");
    myFixture.checkResult("f<selection>oo</selection>\n" +
                          "b<selection>ar</selection>\n");
  }

  // |CTRL-V|
  public void testVisualBlockSelectionsDisplayedCorrectlyInDollarMode() {
    typeTextInFile(parseKeys("<C-V>jj$"),
                   "a<caret>b\n" +
                   "abc\n" +
                   "ab\n");
    myFixture.checkResult("a<selection>b</selection>\n" +
                          "a<selection>bc</selection>\n" +
                          "a<selection>b</selection>\n");
  }

  // |v_o|
  public void testSwapVisualSelectionEnds() {
    typeTextInFile(parseKeys("v", "l", "o", "l", "d"),
                   "<caret>foo\n");
    myFixture.checkResult("fo\n");
  }

  // VIM-646 |gv|
  public void testRestoreMultiLineSelectionAfterYank() {
    typeTextInFile(parseKeys("V", "j", "y", "G", "p", "gv", "d"),
                   "<caret>foo\n" +
                   "bar\n" +
                   "baz\n");
    myFixture.checkResult("baz\n" +
                          "foo\n" +
                          "bar\n");
  }

  // |v_>| |gv|
  public void testRestoreMultiLineSelectionAfterIndent() {
    typeTextInFile(parseKeys("V", "2j"),
                   "<caret>foo\n" +
                   "bar\n" +
                   "baz\n");
    assertSelection("foo\n" +
                    "bar\n" +
                    "baz");
    typeText(parseKeys(">"));
    assertMode(COMMAND);
    myFixture.checkResult("    foo\n" +
                          "    bar\n" +
                          "    baz\n");
    typeText(parseKeys("gv"));
    assertSelection("    foo\n" +
                    "    bar\n" +
                    "    baz");
    typeText(parseKeys(">"));
    assertMode(COMMAND);
    myFixture.checkResult("        foo\n" +
                          "        bar\n" +
                          "        baz\n");
    typeText(parseKeys("gv"));
    assertSelection("        foo\n" +
                    "        bar\n" +
                    "        baz");
  }

  public void testVisualLineSelectDown() {
    typeTextInFile(parseKeys("Vj"),
                   "foo\n" +
                   "<caret>bar\n" +
                   "baz\n" +
                   "quux\n");
    assertMode(VISUAL);
    assertSelection("bar\n" +
                    "baz");
    assertOffset(8);
  }

  // VIM-784
  public void testVisualLineSelectUp() {
    typeTextInFile(parseKeys("Vk"),
                   "foo\n" +
                   "bar\n" +
                   "<caret>baz\n" +
                   "quux\n");
    assertMode(VISUAL);
    assertSelection("bar\n" +
                    "baz");
    assertOffset(4);
  }
}
