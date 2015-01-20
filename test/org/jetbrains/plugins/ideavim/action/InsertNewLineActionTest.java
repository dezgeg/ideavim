package org.jetbrains.plugins.ideavim.action;

import org.jetbrains.plugins.ideavim.VimTestCase;

import static com.maddyhome.idea.vim.helper.StringHelper.parseKeys;

public class InsertNewLineActionTest extends VimTestCase {
  public void testInsertBeforeFold() {
    //doTestJava(parseKeys("zcO// asd"),
    //       "public class Foo {\n" +
    //       "    void bar() {\n" +
    //       "        return<caret>;\n" +
    //       "    } /* end bar */\n" +
    //       "}\n",
    //       "public class Foo {\n" +
    //       "    // asd\n" +
    //       "    void bar() {\n" +
    //       "        return;\n" +
    //       "    } /* end bar */\n" +
    //       "}\n"
    //);
  }

  public void testInsertAfterFold() {
    //doTestJava(parseKeys("zco// asd"),
    //           "public class Foo {\n" +
    //           "    void bar() {\n" +
    //           "        return<caret>;\n" +
    //           "    } /* end bar */\n" +
    //           "}\n",
    //           "public class Foo {\n" +
    //           "    void bar() {\n" +
    //           "        return;\n" +
    //           "    } /* end bar */\n" +
    //           "    // asd\n" +
    //           "}\n"
    //);
  }

  public void wip_testDeleteLineInFold() {
    doTestJava(parseKeys("zcdd"),
               "public class Foo {\n" +
               "    void bar() {\n" +
               "        return<caret>;\n" +
               "    } /* end bar */\n" +
               "}\n",
               "public class Foo {\n" +
               "}\n"
    );
  }
}
