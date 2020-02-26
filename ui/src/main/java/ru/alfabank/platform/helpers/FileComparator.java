package ru.alfabank.platform.helpers;

import org.apache.commons.io.*;
import org.apache.commons.text.diff.*;

import java.io.*;

public class FileComparator {

  /**
   * File comparator.
   * @param actual actual
   * @param expected expected
   * @throws IOException IOException
   */
  public static String compare(final File actual, final File expected) throws IOException {
    LineIterator act = FileUtils.lineIterator(actual,"utf-8");
    LineIterator exp = FileUtils.lineIterator(expected,"utf-8");
    FileCommandsVisitor fileCommandsVisitor = new FileCommandsVisitor();
    while (act.hasNext() || exp.hasNext()) {
      String left = (act.hasNext() ? act.nextLine() : "") + "\n";
      String right = (exp.hasNext() ? exp.nextLine() : "") + "\n";
      StringsComparator comparator = new StringsComparator(left, right);
      if (comparator.getScript().getLCSLength()
          > (Integer.max(left.length(), right.length()) * 0.4)) {
        comparator.getScript().visit(fileCommandsVisitor);
      } else {
        StringsComparator leftComparator = new StringsComparator(left, "\n");
        leftComparator.getScript().visit(fileCommandsVisitor);
        StringsComparator rightComparator = new StringsComparator("\n", right);
        rightComparator.getScript().visit(fileCommandsVisitor);
      }
    }
    return fileCommandsVisitor.generateHtml();
  }

  /*
   * Store comparison & generate HTML.
   */
  private static class FileCommandsVisitor implements CommandVisitor<Character> {
    private static final String DELETION =
        "<span style=\"background-color: #FB504B\">${text}</span>";
    private static final String INSERTION =
        "<span style=\"background-color: #45EA85\">${text}</span>";
    private String left = "";
    private String right = "";

    @Override
    public void visitKeepCommand(Character c) {
      String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
      left = left + toAppend;
      right = right + toAppend;
    }

    @Override
    public void visitInsertCommand(Character c) {
      String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
      right = right + INSERTION.replace("${text}", "" + toAppend);
    }

    @Override
    public void visitDeleteCommand(Character c) {
      String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
      left = left + DELETION.replace("${text}", "" + toAppend);
    }

    public String generateHtml() throws IOException {
      String template = FileUtils.readFileToString(
          new File("src/main/resources/difftemplate.html"), "utf-8");
      String out1 = template.replace("${left}", left);
      return out1.replace("${right}", right);
    }
  }
}