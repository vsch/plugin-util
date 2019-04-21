package com.vladsch.plugin.util.edit;

import com.intellij.codeInsight.editorActions.TextBlockTransferable;
import com.intellij.codeInsight.editorActions.TextBlockTransferableData;
import com.intellij.codeInsight.generation.CommentByBlockCommentHandler;
import com.intellij.lang.Commenter;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actions.EditorActionUtil;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.impl.AbstractFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import com.vladsch.flexmark.util.sequence.BasedSequenceImpl;
import com.vladsch.flexmark.util.sequence.Range;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JPasswordField;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.openapi.diagnostic.Logger.getInstance;
import static java.lang.Character.isJavaIdentifierPart;
import static java.lang.Character.isLetterOrDigit;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.isWhitespace;

@SuppressWarnings({ "SameParameterValue", "WeakerAccess" })
public class Helpers {
    private static final Logger LOG = getInstance("com.vladsch.MissingInActions");

    public static final int START_OF_WORD = 0x0001;
    public static final int END_OF_WORD = 0x0002;
    public static final int START_OF_TRAILING_BLANKS = 0x0004;
    public static final int END_OF_LEADING_BLANKS = 0x0008;
    public static final int START_OF_LINE = 0x0010;
    public static final int END_OF_LINE = 0x0020;
    public static final int MIA_IDENTIFIER = 0x0040;
    public static final int START_OF_FOLDING_REGION = 0x0080;
    public static final int END_OF_FOLDING_REGION = 0x0100;
    public static final int SINGLE_LINE = 0x0200;
    public static final int MULTI_CARET_SINGLE_LINE = 0x0400;
    public static final int IDE_WORD = 0x0800;
    public static final int SPACE_DELIMITED = 0x1000;
    public static final int MIA_WORD = 0x2000;

    public static final int WORD_SPACE_DELIMITED = 0;
    public static final int WORD_IDE = 1;
    public static final int WORD_MIA = 2;
    public static final int WORD_IDENTIFIER = 3;

    public static int getWordType(int flags) {
        if ((flags & MIA_IDENTIFIER) != 0) return WORD_IDENTIFIER;
        if ((flags & IDE_WORD) != 0) return WORD_IDE;
        if ((flags & SPACE_DELIMITED) != 0) return WORD_SPACE_DELIMITED;
        return WORD_MIA;
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    public static int BOUNDARY_FLAGS = 0
            | START_OF_WORD
            | END_OF_WORD
            | START_OF_TRAILING_BLANKS
            | END_OF_LEADING_BLANKS
            | START_OF_LINE
            | END_OF_LINE
            | START_OF_FOLDING_REGION
            | END_OF_FOLDING_REGION
            | SINGLE_LINE;

    public static boolean isSet(int options, int flag) {
        return (options & flag) != 0;
    }

    public static void moveCaretToNextWordStartOrEnd(@NotNull Editor editor, boolean isWithSelection, boolean camel, int flags) {
        if (!isSet(flags, BOUNDARY_FLAGS)) return;

        Document document = editor.getDocument();
        SelectionModel selectionModel = editor.getSelectionModel();
        int selectionStart = selectionModel.getLeadSelectionOffset();
        CaretModel caretModel = editor.getCaretModel();
        LogicalPosition blockSelectionStart = caretModel.getLogicalPosition();
        boolean haveMultiCarets = caretModel.getCaretCount() > 1;

        boolean stopAtTrailingBlanks = isSet(flags, START_OF_TRAILING_BLANKS);
        boolean stopAtLeadingBlanks = isSet(flags, END_OF_LEADING_BLANKS);
        boolean stopAtStartOfLine = isSet(flags, START_OF_LINE);
        boolean stopAtStartOfWord = isSet(flags, START_OF_WORD);
        boolean stopAtEndOfWord = isSet(flags, END_OF_WORD);
        boolean stopAtStartOfFolding = isSet(flags, START_OF_FOLDING_REGION);
        boolean stopAtEndOfFolding = isSet(flags, END_OF_FOLDING_REGION);
        boolean stopAtEndOfLine = isSet(flags, END_OF_LINE);
        boolean strictIdentifier = isSet(flags, MIA_IDENTIFIER);
        boolean singleLine = isSet(flags, SINGLE_LINE) || isSet(flags, MULTI_CARET_SINGLE_LINE) && haveMultiCarets;

        int offset = caretModel.getOffset();
        if (offset == document.getTextLength()) {
            return;
        }

        int lineNumber = caretModel.getLogicalPosition().line;
        if (lineNumber >= document.getLineCount()) return;

        int stopAtLastNonBlank = 0;

        // have to stop at start of character if caret is not at or before first non-blank
        // only applies to start boundary condition
        int lineStartOffset = document.getLineStartOffset(lineNumber);
        if (stopAtTrailingBlanks || stopAtEndOfLine) {
            int lineEndOffset = document.getLineEndOffset(lineNumber);
            int trailingBlanks = countWhiteSpaceReversed(document.getCharsSequence(), lineStartOffset, lineEndOffset);
            if (stopAtTrailingBlanks && caretModel.getOffset() < lineEndOffset - trailingBlanks) {
                stopAtLastNonBlank = lineEndOffset - trailingBlanks;
            } else if (stopAtEndOfLine && (caretModel.getOffset() < lineEndOffset || singleLine)) {
                stopAtLastNonBlank = lineEndOffset;
            }
        }

        int maxLineNumber = stopAtLastNonBlank > 0 || lineNumber + 1 > document.getLineCount() ? lineNumber : lineNumber + 1;
        int maxOffset = stopAtLastNonBlank > 0 ? stopAtLastNonBlank :
                (stopAtStartOfLine && lineNumber < maxLineNumber ? document.getLineStartOffset(maxLineNumber) : stopAtEndOfLine ? document.getLineEndOffset(maxLineNumber) : document.getTextLength());

        int newOffset = offset + 1;
        if (newOffset > maxOffset) return;

        boolean done = false;
        FoldRegion currentFoldRegion = editor.getFoldingModel().getCollapsedRegionAtOffset(offset);
        if (currentFoldRegion != null) {
            newOffset = currentFoldRegion.getEndOffset();
            if (stopAtEndOfFolding) done = true;
        }

        int wordType = getWordType(flags);
        while (!done) {
            for (; newOffset < maxOffset; newOffset++) {
                if (stopAtStartOfWord && isWordTypeStart(wordType, editor, newOffset, camel)) {
                    done = true;
                    break;
                }
                if (stopAtEndOfWord && isWordTypeEnd(wordType, editor, newOffset, camel)) {
                    done = true;
                    break;
                }
            }
            if (newOffset >= maxOffset) break;

            FoldRegion foldRegion = editor.getFoldingModel().getCollapsedRegionAtOffset(newOffset);
            if (foldRegion != null) {
                if (stopAtStartOfFolding) {
                    newOffset = foldRegion.getStartOffset();
                    break;
                }
                newOffset = foldRegion.getEndOffset();
                if (stopAtEndOfFolding) break;
            }
        }

        if (editor instanceof EditorImpl) {
            int boundaryOffset = ((EditorImpl) editor).findNearestDirectionBoundary(offset, true);
            if (boundaryOffset >= 0) {
                newOffset = Math.min(boundaryOffset, newOffset);
            }
        }

        caretModel.moveToOffset(newOffset);
        EditorModificationUtil.scrollToCaret(editor);
        setupSelection(editor, isWithSelection, selectionStart, blockSelectionStart);
    }

    public static void moveCaretToPreviousWordStartOrEnd(@NotNull Editor editor, boolean isWithSelection, boolean camel, int flags) {
        if (!isSet(flags, BOUNDARY_FLAGS)) return;

        Document document = editor.getDocument();
        SelectionModel selectionModel = editor.getSelectionModel();
        int selectionStart = selectionModel.getLeadSelectionOffset();
        CaretModel caretModel = editor.getCaretModel();
        boolean haveMultiCarets = caretModel.getCaretCount() > 1;

        int offset = caretModel.getOffset();
        if (offset == 0) return;

        boolean stopAtTrailingBlanks = isSet(flags, START_OF_TRAILING_BLANKS);
        boolean stopAtLeadingBlanks = isSet(flags, END_OF_LEADING_BLANKS);
        boolean stopAtStartOfLine = isSet(flags, START_OF_LINE);
        boolean stopAtStartOfWord = isSet(flags, START_OF_WORD);
        boolean stopAtEndOfWord = isSet(flags, END_OF_WORD);
        boolean stopAtEndOfLine = isSet(flags, END_OF_LINE);
        boolean stopAtStartOfFolding = isSet(flags, START_OF_FOLDING_REGION);
        boolean stopAtEndOfFolding = isSet(flags, END_OF_FOLDING_REGION);
        boolean strictIdentifier = isSet(flags, MIA_IDENTIFIER);
        boolean singleLine = isSet(flags, SINGLE_LINE) || isSet(flags, MULTI_CARET_SINGLE_LINE) && haveMultiCarets;

        LogicalPosition position = caretModel.getLogicalPosition();
        int lineNumber = position.line;
        int stopAtIndent = 0;

        // have to stop at start of character if caret is not at or before first non-blank
        int lineStartOffset = document.getLineStartOffset(lineNumber);
        int lineEndOffset = document.getLineEndOffset(lineNumber);

        if (stopAtEndOfLine && position.column > editor.offsetToLogicalPosition(lineEndOffset).column) {
            stopAtIndent = lineEndOffset;
        }

        if (stopAtIndent == 0 && stopAtTrailingBlanks) {
            int trailingBlanks = countWhiteSpaceReversed(document.getCharsSequence(), lineStartOffset, lineEndOffset);
            if (offset > lineEndOffset - trailingBlanks) {
                stopAtIndent = lineEndOffset - trailingBlanks;
            }
        }

        if (stopAtIndent == 0 && (stopAtLeadingBlanks || stopAtStartOfLine)) {
            int firstNonBlank = countWhiteSpace(document.getCharsSequence(), lineStartOffset, document.getLineEndOffset(lineNumber));
            LogicalPosition firstNonBlankPosition = editor.offsetToLogicalPosition(lineStartOffset + firstNonBlank);
            if (stopAtLeadingBlanks && position.column > firstNonBlankPosition.column) {
                stopAtIndent = lineStartOffset + firstNonBlank;
            } else if (stopAtStartOfLine && (position.column != 0 || singleLine)) {
                stopAtIndent = lineStartOffset;
            }
        }

        int minLineNumber = lineNumber == 0 || stopAtIndent > 0 ? lineNumber : lineNumber - 1;
        int minOffset = stopAtIndent > 0 ? stopAtIndent :
                (stopAtEndOfLine && lineNumber > minLineNumber ? document.getLineEndOffset(minLineNumber) : stopAtStartOfLine ? document.getLineStartOffset(minLineNumber) : 0);

        // if virtual spaces are enabled the caret can be after the end so we should pretend it is on the next char after the end
        int newOffset = stopAtIndent > offset - 1 ? stopAtIndent : offset - 1;
        if (newOffset < minOffset) return;

        boolean done = false;
        FoldRegion currentFoldRegion = editor.getFoldingModel().getCollapsedRegionAtOffset(offset - 1);
        if (currentFoldRegion != null) {
            newOffset = currentFoldRegion.getStartOffset();
            if (stopAtStartOfFolding) done = true;
        }

        int wordType = getWordType(flags);
        while (!done) {
            for (; newOffset > minOffset; newOffset--) {
                if (stopAtEndOfWord && isWordTypeEnd(wordType, editor, newOffset, camel)) {
                    done = true;
                    break;
                }
                if (stopAtStartOfWord && isWordTypeStart(wordType, editor, newOffset, camel)) {
                    done = true;
                    break;
                }
            }
            if (newOffset <= minOffset) break;

            FoldRegion foldRegion = editor.getFoldingModel().getCollapsedRegionAtOffset(newOffset);
            if (foldRegion != null) {
                if (stopAtEndOfFolding) {
                    newOffset = foldRegion.getEndOffset();
                    break;
                }
                newOffset = foldRegion.getStartOffset();
                if (stopAtStartOfFolding) break;
            }
        }

        if (editor instanceof EditorImpl) {
            int boundaryOffset = ((EditorImpl) editor).findNearestDirectionBoundary(offset, false);
            if (boundaryOffset >= 0) {
                newOffset = Math.max(boundaryOffset, newOffset);
            }
            caretModel.moveToLogicalPosition(editor.offsetToLogicalPosition(newOffset).leanForward(true));
        } else {
            editor.getCaretModel().moveToOffset(newOffset);
        }

        EditorModificationUtil.scrollToCaret(editor);
        setupSelection(editor, isWithSelection, selectionStart, position);
    }

    public static boolean isWordTypeStart(int wordType, @NotNull Editor editor, int offset, boolean isCamel) {
        switch (wordType) {
            case WORD_SPACE_DELIMITED:
                return isWhitespaceEnd(editor.getDocument().getCharsSequence(), offset, isCamel);
            case WORD_IDE:
                return EditorActionUtil.isWordOrLexemeStart(editor, offset, isCamel);
            case WORD_MIA:
                return isWordStart(editor.getDocument().getCharsSequence(), offset, isCamel);
            case WORD_IDENTIFIER:
                return isIdentifierStart(editor.getDocument().getCharsSequence(), offset, isCamel);
        }
        return false;
    }

    public static boolean isWordTypeEnd(int wordType, @NotNull Editor editor, int offset, boolean isCamel) {
        switch (wordType) {
            case WORD_SPACE_DELIMITED:
                return isWhitespaceStart(editor.getDocument().getCharsSequence(), offset);
            case WORD_IDE:
                return EditorActionUtil.isWordOrLexemeEnd(editor, offset, isCamel);
            case WORD_MIA:
                return isWordEnd(editor.getDocument().getCharsSequence(), offset, isCamel);
            case WORD_IDENTIFIER:
                return isIdentifierEnd(editor.getDocument().getCharsSequence(), offset, isCamel);
        }
        return false;
    }

    public static boolean isWordTypeStart(int wordType, @NotNull CharSequence charSequence, int offset, boolean isCamel) {
        switch (wordType) {
            case WORD_SPACE_DELIMITED:
                return isWhitespaceEnd(charSequence, offset, isCamel);
            case WORD_IDE:
                throw new IllegalArgumentException("wordType: WORD_IDE is only supported with editor parameter based isWordTypeStart function");
            case WORD_MIA:
                return isWordStart(charSequence, offset, isCamel);
            case WORD_IDENTIFIER:
                return isIdentifierStart(charSequence, offset, isCamel);
        }
        return false;
    }

    public static boolean isWordTypeEnd(int wordType, @NotNull CharSequence charSequence, int offset, boolean isCamel) {
        switch (wordType) {
            case WORD_SPACE_DELIMITED:
                return isWhitespaceStart(charSequence, offset);
            case WORD_IDE:
                throw new IllegalArgumentException("wordType: WORD_IDE is only supported with editor parameter based isWordTypeEnd function");
            case WORD_MIA:
                return isWordEnd(charSequence, offset, isCamel);
            case WORD_IDENTIFIER:
                return isIdentifierEnd(charSequence, offset, isCamel);
        }
        return false;
    }

    public static boolean isWordType(int wordType, @NotNull CharSequence charSequence, int offset) {
        switch (wordType) {
            case WORD_SPACE_DELIMITED:
                return offset >= 0 && offset < charSequence.length() && isWhitespace(charSequence.charAt(offset));
            case WORD_IDE:
            case WORD_MIA:
            case WORD_IDENTIFIER:
                return isIdentifier(charSequence, offset);
        }
        return false;
    }

    public static boolean isWordStart(@NotNull Editor editor, int offset, boolean isCamel) {
        CharSequence chars = editor.getDocument().getCharsSequence();
        return isWordStart(chars, offset, isCamel);
    }

    public static boolean isWordEnd(@NotNull Editor editor, int offset, boolean isCamel) {
        CharSequence chars = editor.getDocument().getCharsSequence();
        return isWordEnd(chars, offset, isCamel);
    }

    public static boolean isIdentifierStart(@NotNull Editor editor, int offset, boolean isCamel) {
        CharSequence chars = editor.getDocument().getCharsSequence();
        return isIdentifierStart(chars, offset, isCamel);
    }

    public static boolean isIdentifierEnd(@NotNull Editor editor, int offset, boolean isCamel) {
        CharSequence chars = editor.getDocument().getCharsSequence();
        return isIdentifierEnd(chars, offset, isCamel);
    }

    public static boolean isWhitespaceStart(@NotNull CharSequence text, int offset) {
        char prev = offset > 0 ? text.charAt(offset - 1) : 0;
        char current = offset < text.length() ? text.charAt(offset) : 0;

        return (!Character.isWhitespace(prev) && Character.isWhitespace(current));
    }

    public static boolean isWhitespaceMiddle(@NotNull CharSequence text, int offset) {
        char prev = offset > 0 ? text.charAt(offset - 1) : 0;
        char current = offset < text.length() ? text.charAt(offset) : 0;

        return (Character.isWhitespace(prev) && Character.isWhitespace(current));
    }

    public static boolean isWhitespaceEnd(@NotNull CharSequence text, int offset, boolean isCamel) {
        char prev = offset > 0 ? text.charAt(offset - 1) : 0;
        char current = offset < text.length() ? text.charAt(offset) : 0;

        return (Character.isWhitespace(prev) && !Character.isWhitespace(current));
    }

    public static boolean isIdentifierPart(char c) {
        // add $ since PHP and JavaScript take these as part of identifier
        return c == '$' || isJavaIdentifierPart(c);
    }

    public static boolean isIdentifier(@NotNull CharSequence text, int offset) {
        return offset >= 0 && offset < text.length() && isIdentifierPart(text.charAt(offset));
    }

    public static boolean isWordStart(@NotNull CharSequence text, int offset, boolean isCamel) {
        char prev = offset > 0 ? text.charAt(offset - 1) : 0;
        char current = offset < text.length() ? text.charAt(offset) : 0;

        final boolean firstIsIdentifierPart = prev != 0 && Character.isJavaIdentifierPart(prev);
        final boolean secondIsIdentifierPart = current != 0 && Character.isJavaIdentifierPart(current);
        if (!firstIsIdentifierPart && secondIsIdentifierPart) {
            return true;
        }

        if (isCamel && firstIsIdentifierPart && secondIsIdentifierPart && isHumpBoundWord(text, offset, true)) {
            return true;
        }

        return (Character.isWhitespace(prev) || firstIsIdentifierPart) &&
                !Character.isWhitespace(current) && !secondIsIdentifierPart;
    }

    public static boolean isWordEnd(@NotNull CharSequence text, int offset, boolean isCamel) {
        char prev = offset > 0 ? text.charAt(offset - 1) : 0;
        char current = offset < text.length() ? text.charAt(offset) : 0;
        char next = offset + 1 < text.length() ? text.charAt(offset + 1) : 0;

        final boolean firstIsIdentifierPart = prev != 0 && Character.isJavaIdentifierPart(prev);
        final boolean secondIsIdentifierPart = current != 0 && Character.isJavaIdentifierPart(current);
        if (firstIsIdentifierPart && !secondIsIdentifierPart) {
            return true;
        }

        if (isCamel) {
            if (firstIsIdentifierPart
                    && (Character.isLowerCase(prev) && isUpperCase(current)
                    || prev != '_' && current == '_'
                    || isUpperCase(prev) && isUpperCase(current) && Character.isLowerCase(next))) {
                return true;
            }
        }

        return !Character.isWhitespace(prev) && !firstIsIdentifierPart &&
                (Character.isWhitespace(current) || secondIsIdentifierPart);
    }

    public static boolean isIdentifierStart(@NotNull CharSequence text, int offset, boolean isCamel) {
        char prev = offset > 0 ? text.charAt(offset - 1) : 0;
        char current = offset < text.length() ? text.charAt(offset) : 0;

        final boolean prevIsIdentifierPart = prev != 0 && Character.isJavaIdentifierPart(prev);
        final boolean currentIsIdentifierPart = current != 0 && Character.isJavaIdentifierPart(current);

        //noinspection SimplifiableIfStatement
        if (!prevIsIdentifierPart && currentIsIdentifierPart) return true;

        return isCamel && prevIsIdentifierPart && currentIsIdentifierPart && isHumpBoundIdentifier(text, offset, true);
    }

    public static boolean isIdentifierEnd(@NotNull CharSequence text, int offset, boolean isCamel) {
        char prev = offset > 0 ? text.charAt(offset - 1) : 0;
        char current = offset < text.length() ? text.charAt(offset) : 0;
        char next = offset + 1 < text.length() ? text.charAt(offset + 1) : 0;

        final boolean prevIsIdentifierPart = prev != 0 && Character.isJavaIdentifierPart(prev);
        final boolean currentIsIdentifierPart = current != 0 && Character.isJavaIdentifierPart(current);

        //noinspection SimplifiableIfStatement
        if (prevIsIdentifierPart && !currentIsIdentifierPart) return true;

        return isCamel && prevIsIdentifierPart
                && (Character.isLowerCase(prev) && isUpperCase(current)
                || prev != '_' && current == '_'
                || isUpperCase(prev) && isUpperCase(current) && Character.isLowerCase(next));
    }

/*
    public static boolean isHumpBoundStart(@NotNull CharSequence editorText, int offset) {
        return isHumpBoundIdentifier(editorText, offset, true);
    }

    public static boolean isHumpBoundEnd(@NotNull CharSequence editorText, int offset) {
        return isHumpBoundIdentifier(editorText, offset, false);
    }
*/

    public static boolean isHumpBoundWord(@NotNull CharSequence editorText, int offset, boolean start) {
        if (offset <= 0) return start;
        else if (offset >= editorText.length()) return !start;

        final char prevChar = offset > 0 ? editorText.charAt(offset - 1) : 0;
        final char curChar = editorText.charAt(offset);
        final char nextChar = offset + 1 < editorText.length() ? editorText.charAt(offset + 1) : 0; // 0x00 is not lowercase.

        return isLowerCaseOrDigit(prevChar) && isUpperCase(curChar) ||
                start && prevChar == '_' && curChar != '_' ||
                !start && prevChar != '_' && curChar == '_' ||
                start && prevChar == '$' && isLetterOrDigit(curChar) ||
                !start && isLetterOrDigit(prevChar) && curChar == '$' ||
                isUpperCase(prevChar) && isUpperCase(curChar) && Character.isLowerCase(nextChar);
    }

    /*
    public static boolean isHumpBoundEnd(@NotNull CharSequence editorText, int offset, boolean start) {
        if (offset <= 0 || offset >= editorText.length()) return false;
        final char prevChar = editorText.charAt(offset - 1);
        final char curChar = editorText.charAt(offset);
        final char nextChar = offset + 1 < editorText.length() ? editorText.charAt(offset + 1) : 0; // 0x00 is not lowercase.

        return isLowerCaseOrDigit(prevChar) && Character.isUpperCase(curChar) ||
                !start && prevChar != '_' && curChar == '_' ||
                !start && Character.isLetterOrDigit(prevChar) && curChar == '$';
    }
*/

    public static boolean isHumpBoundIdentifier(@NotNull CharSequence editorText, int offset, boolean start) {
        if (offset <= 0) return start;
        else if (offset >= editorText.length()) return !start;
        final char prevChar = editorText.charAt(offset - 1);
        final char curChar = editorText.charAt(offset);

        return isLowerCaseOrDigit(prevChar) && isUpperCase(curChar) ||
                start && prevChar == '_' && curChar != '_' ||
                start && prevChar == '$' && isLetterOrDigit(curChar) ||
                !start && prevChar != '_' && curChar == '_' ||
                !start && isLetterOrDigit(prevChar) && curChar == '$';
    }

    public static boolean isSnakeCaseBound(@NotNull CharSequence editorText, int offset, boolean start) {
        if (offset <= 0) return start;
        else if (offset >= editorText.length()) return !start;

        final char prevChar = editorText.charAt(offset - 1);
        final char curChar = editorText.charAt(offset);

        return start ? prevChar == '_' && curChar != '_' && isLetterOrDigit(curChar) : prevChar != '_' && curChar == '_' && isLetterOrDigit(prevChar);
    }

    public static boolean isLowerCaseOrDigit(char c) {
        return Character.isLowerCase(c) || Character.isDigit(c);
    }

    private static void setupSelection(@NotNull Editor editor, boolean isWithSelection, int selectionStart, @NotNull LogicalPosition blockSelectionStart) {
        SelectionModel selectionModel = editor.getSelectionModel();
        CaretModel caretModel = editor.getCaretModel();
        if (isWithSelection) {
            if (editor.isColumnMode() && !caretModel.supportsMultipleCarets()) {
                selectionModel.setBlockSelection(blockSelectionStart, caretModel.getLogicalPosition());
            } else {
                selectionModel.setSelection(selectionStart, caretModel.getVisualPosition(), caretModel.getOffset());
            }
        } else {
            selectionModel.removeSelection();
        }

        EditorActionUtil.selectNonexpandableFold(editor);
    }

    public static int countWhiteSpace(CharSequence chars, int start, int end) {
        int pos = start;
        int length = chars.length();
        if (end > length) end = length;
        while (pos < end) {
            char c = chars.charAt(pos);
            if (c != ' ' && c != '\t') break;
            pos++;
        }
        return pos - start;
    }

    public static int countWhiteSpaceReversed(CharSequence chars, int start, int end) {
        int length = chars.length();
        if (end > length) end = length;
        int pos = end - 1;
        while (pos >= start) {
            char c = chars.charAt(pos);
            if (c != ' ' && c != '\t') break;
            pos--;
        }
        return end - pos - 1;
    }

    public static void scrollToCaret(Editor editor) {
        EditorModificationUtil.scrollToCaret(editor);
    }

    public static void scrollToSelection(Editor editor) {
        ScrollingModel scrollingModel = editor.getScrollingModel();
        SelectionModel selectionModel = editor.getSelectionModel();
        scrollingModel.scrollTo(editor.offsetToLogicalPosition(selectionModel.getSelectionEnd()), ScrollType.MAKE_VISIBLE);
        scrollingModel.scrollTo(editor.offsetToLogicalPosition(selectionModel.getSelectionStart()), ScrollType.MAKE_VISIBLE);
    }

    public static void restoreState(@Nullable Caret newCaret, CaretState caretState, boolean alwaysSetSelection) {
        if (newCaret != null) {
            if (caretState.getCaretPosition() != null) {
                newCaret.moveToLogicalPosition(caretState.getCaretPosition());
            }

            if (caretState.getSelectionStart() != null && caretState.getSelectionEnd() != null) {
                newCaret.setSelection(
                        newCaret.getEditor().logicalPositionToOffset(caretState.getSelectionStart()),
                        newCaret.getEditor().logicalPositionToOffset(caretState.getSelectionEnd())
                );
            } else if (alwaysSetSelection) {
                newCaret.setSelection(newCaret.getOffset(), newCaret.getOffset());
            }
        }
    }

    @Nullable
    public static Range getCaretRange(@NotNull Caret caret, boolean backwards, boolean lineMode, boolean singleLine) {
        @NotNull Editor editor = caret.getEditor();
        Document document = editor.getDocument();
        int caretOffset = caret.getOffset();
        Range range = null;

        int start;
        int end;

        if (caret.hasSelection()) {
            if (backwards) {
                end = caret.getOffset();
                start = end;
            } else {
                start = caret.getOffset();
                end = start;
            }

            // expand range to start/end of selection
            if (start > caret.getSelectionStart()) {
                start = caret.getSelectionStart();
            }

            if (end < caret.getSelectionEnd()) {
                end = caret.getSelectionEnd();
            }
        } else {
            if (backwards) {
                start = 0;
                end = caret.getOffset();
            } else {
                start = caret.getOffset();
                end = document.getTextLength();
            }
        }

        if (lineMode && (!singleLine || !caret.hasSelection())) {
            // expand range to start/end of line but not if caret has selection and is singleLine search spawning
            int startOffset = document.getLineStartOffset(document.getLineNumber(start));
            int endOffset = document.getLineEndOffset(document.getLineNumber(end));

            if (start > startOffset) {
                start = startOffset;
            }
            if (end < endOffset) {
                end = endOffset;
            }
        }

        if (singleLine) {
            // truncate to the caret line
            int lineNumber = document.getLineNumber(caretOffset);
            int startOffset = document.getLineStartOffset(lineNumber);
            int endOffset = document.getLineEndOffset(lineNumber);

            if (start < startOffset) {
                start = startOffset;
            }
            if (end > endOffset) {
                end = endOffset;
            }
        }

        if (start > end) {
            int tmp = 0;
        } else {
            range = new Range(start, end);
        }
        return range;
    }

    public static Map<Caret, Range> limitCaretRange(boolean backwards, Map<Caret, Range> rangeMap, boolean wantEmptyRanges) {
        HashMap<Caret, Range> map = new HashMap<>(rangeMap.size());
        Caret[] carets = rangeMap.keySet().toArray(new Caret[rangeMap.size()]);
        Range prevRange = null;

        Arrays.sort(carets, new Comparator<Caret>() {
            @Override
            public int compare(final Caret o1, final Caret o2) {
                return o1.getLogicalPosition().compareTo(o2.getLogicalPosition());
            }
        });

        if (!backwards) {
            for (int i = carets.length; i-- > 0; ) {
                Caret caret = carets[i];
                Range range = rangeMap.get(caret);
                if (range == null) continue;

                if (prevRange != null) {
                    if (prevRange.getStart() < range.getEnd()) {
                        // truncate end
                        range = range.withEnd(prevRange.getStart());
                    }
                }

                if (range.getSpan() > 0 || wantEmptyRanges && range.getSpan() == 0) {
                    map.put(caret, range);
                    prevRange = range;
                }
            }
        } else {
            for (int i = 0; i < carets.length; i++) {
                Caret caret = carets[i];
                Range range = rangeMap.get(caret);
                if (range == null) continue;

                if (prevRange != null) {
                    if (prevRange.getEnd() > range.getStart()) {
                        // truncate start
                        range = range.withStart(prevRange.getEnd());
                    }
                }

                if (range.getSpan() > 0 || wantEmptyRanges && range.getSpan() == 0) {
                    map.put(caret, range);
                    prevRange = range;
                }
            }
        }

        return map;
    }

    public static int getNextWordStartAtOffset(CharSequence charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        // move back on line to start of word
        int newOffset = offset;
        int length = charSequence.length();
        do {
            if (isWordTypeStart(wordType, charSequence, newOffset, isCamel)) {
                return newOffset;
            }
            if (stopIfNonWord && !isWordType(wordType, charSequence, newOffset)) break;
            newOffset++;
        } while (newOffset < length);

        return offset;
    }

    public static int getPreviousWordStartAtOffset(CharSequence charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        // move back on line to start of word
        int newOffset = offset;
        do {
            if (isWordTypeStart(wordType, charSequence, newOffset, isCamel)) {
                return newOffset;
            }
            if (stopIfNonWord && !isWordTypeEnd(wordType, charSequence, newOffset, false) && !isWordType(wordType, charSequence, newOffset)) break;
            newOffset--;
        } while (newOffset >= 0);

        return offset;
    }

    public static int getPreviousWordEndAtOffset(CharSequence charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        // move back on line to start of word
        int newOffset = offset;
        do {
            if (isWordTypeEnd(wordType, charSequence, newOffset, isCamel)) {
                return newOffset;
            }
            if (stopIfNonWord && !isWordType(wordType, charSequence, newOffset)) break;
            newOffset--;
        } while (newOffset >= 0);

        return offset;
    }

    public static int getNextWordEndAtOffset(CharSequence charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        // move back on line to start of word
        int newOffset = offset;
        int length = charSequence.length();
        do {
            if (isWordTypeEnd(wordType, charSequence, newOffset, isCamel)) {
                return newOffset;
            }
            if (stopIfNonWord && !isWordType(wordType, charSequence, newOffset)) break;
            newOffset++;
        } while (newOffset <= length);

        return offset;
    }

    public static int getWordStartAtOffset(CharSequence charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        if (wordType != WORD_SPACE_DELIMITED && !isIdentifier(charSequence, offset) && !isWordEnd(
                charSequence,
                offset,
                false
        ) || wordType == WORD_SPACE_DELIMITED && isWhitespaceMiddle(charSequence, offset)) {
            // go forward
            return offset;//getNextWordStartAtOffset(charSequence, offset, wordType, isCamel);
        } else {
            // go backwards
            return getPreviousWordStartAtOffset(charSequence, offset, wordType, isCamel, stopIfNonWord);
        }
    }

    public static int getWordEndAtOffset(CharSequence charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        if (wordType != WORD_SPACE_DELIMITED && !isIdentifier(charSequence, offset) && !isWordStart(
                charSequence,
                offset,
                false
        ) || wordType == WORD_SPACE_DELIMITED && isWhitespaceMiddle(charSequence, offset)) {
            // go backwards
            return offset; //getPreviousWordEndAtOffset(charSequence, offset, wordType, isCamel);
        } else {
            // go forward
            return getNextWordEndAtOffset(charSequence, offset, wordType, isCamel, stopIfNonWord);
        }
    }

    public static TextRange getWordRangeAtOffsets(CharSequence charSequence, int start, int end, int wordType, boolean isCamel, boolean stopIfNonWord) {
        if (start < 0) start = 0;
        if (end > charSequence.length()) end = charSequence.length();
        if (start > end) start = end;
        if (end < start) end = start;

        int startOffset = getWordStartAtOffset(charSequence, start, wordType, isCamel, stopIfNonWord);
        int endOffset = getWordEndAtOffset(charSequence, Math.max(startOffset, end), wordType, isCamel, stopIfNonWord);

        // trim to word
        while (startOffset < endOffset && !isWordType(wordType, charSequence, startOffset)) startOffset++;
        while (startOffset < endOffset && !isWordType(wordType, charSequence, endOffset - 1)) endOffset--;
        if (stopIfNonWord) {
            if (startOffset > end) startOffset = end;
            if (endOffset < start) endOffset = start;
        }

        return startOffset > endOffset ? new TextRange(start, end) : new TextRange(startOffset, endOffset);
    }

    public static String getWordAtOffsets(String charSequence, int start, int end, int wordType, boolean isCamel, boolean stopIfNonWord) {
        return getWordRangeAtOffsets(charSequence, start, end, wordType, isCamel, stopIfNonWord).substring(charSequence);
    }

    public static CharSequence getWordAtOffsets(CharSequence charSequence, int start, int end, int wordType, boolean isCamel, boolean stopIfNonWord) {
        return getWordRangeAtOffsets(charSequence, start, end, wordType, isCamel, stopIfNonWord).subSequence(charSequence);
    }

    public static BasedSequence getWordAtOffsets(BasedSequence charSequence, int start, int end, int wordType, boolean isCamel, boolean stopIfNonWord) {
        return (BasedSequence) getWordRangeAtOffsets(charSequence, start, end, wordType, isCamel, stopIfNonWord).subSequence(charSequence);
    }

    public static String getWordAtOffset(String charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        return getWordAtOffsets(charSequence, offset, offset, wordType, isCamel, stopIfNonWord);
    }

    public static CharSequence getWordAtOffset(CharSequence charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        return getWordAtOffsets(charSequence, offset, offset, wordType, isCamel, stopIfNonWord);
    }

    public static BasedSequence getWordAtOffset(BasedSequence charSequence, int offset, int wordType, boolean isCamel, boolean stopIfNonWord) {
        return getWordAtOffsets(charSequence, offset, offset, wordType, isCamel, stopIfNonWord);
    }

    public static boolean isPasswordEditor(@Nullable Editor editor) {
        return editor != null && editor.getContentComponent() instanceof JPasswordField;
    }

/*
    @NotNull
    public static String getFormatterOnTag(@NotNull Project project) {
        return CodeStyleSettingsManager.getSettings(project).FORMATTER_ON_TAG;
    }

    @NotNull
    public static String getFormatterOffTag(@NotNull Project project) {
        return CodeStyleSettingsManager.getSettings(project).FORMATTER_OFF_TAG;
    }

    public static boolean getFormatterTagsEnabled(@NotNull Project project) {
        return CodeStyleSettingsManager.getSettings(project).FORMATTER_TAGS_ENABLED;
    }

    public static boolean getFormatterRegExEnabled(@NotNull Project project) {
        return CodeStyleSettingsManager.getSettings(project).FORMATTER_TAGS_ACCEPT_REGEXP;
    }

    @Nullable
    public static Pattern getFormatterOnPattern(@NotNull Project project) {
        return CodeStyleSettingsManager.getSettings(project).getFormatterOnPattern();
    }

    @Nullable
    public static Pattern getFormatterOffPattern(@NotNull Project project) {
        return CodeStyleSettingsManager.getSettings(project).getFormatterOffPattern();
    }
*/

    public static int getStartOfLineOffset(@NotNull CharSequence charSequence, int offset) {
        return BasedSequenceImpl.of(charSequence).startOfLine(offset);
    }

    public static int getEndOfLineOffset(@NotNull CharSequence charSequence, int offset) {
        return BasedSequenceImpl.of(charSequence).endOfLine(offset);
    }

    @NotNull
    public static ItemTextRange<Language> findLanguageRangeFromElement(final PsiElement elt) {
        if (!(elt instanceof PsiFile) && elt.getFirstChild() == null) { //is leaf
            final PsiElement parent = elt.getParent();
            if (parent != null) {
                return new ItemTextRange<>(parent.getLanguage(), parent.getNode().getTextRange());
            }
        }

        return new ItemTextRange<>(elt.getLanguage(), elt.getNode().getTextRange());
    }

    @NotNull
    public static ItemTextRange<Language> getLanguageRangeAtOffset(@NotNull PsiFile file, int offset) {
        final PsiElement elt = file.findElementAt(offset);
        if (elt == null) return new ItemTextRange<Language>(file.getLanguage(), 0, file.getTextLength());
        if (elt instanceof PsiWhiteSpace) {
            TextRange textRange = elt.getTextRange();
            if (!textRange.contains(offset)) {
                LOG.error("PSI corrupted: in file " + file + " (" + file.getViewProvider().getVirtualFile() + ") offset=" + offset + " returned element " + elt + " with text range " + textRange);
            }
            final int decremented = textRange.getStartOffset() - 1;
            if (decremented >= 0) {
                return getLanguageRangeAtOffset(file, decremented);
            }
        }
        return findLanguageRangeFromElement(elt);
    }

    @Nullable
    public static ItemTextRange<Commenter> getCommenterRange(final @NotNull Editor editor, final @Nullable PsiFile file, final int startOffset, final int endOffset) {
        if (file != null) {
            final FileType fileType = file.getFileType();
            if (fileType instanceof AbstractFileType) {
                final Commenter commenter = ((AbstractFileType) fileType).getCommenter();
                if (commenter != null) {
                    return new ItemTextRange<>(commenter, 0, file.getTextLength());
                }
            } else {
                BasedSequence charSequence = BasedSequenceImpl.of(editor.getDocument().getCharsSequence());
                int lineStartOffset = charSequence.startOfLine(startOffset);
                int lineEndOffset = charSequence.endOfLine(endOffset);
                lineStartOffset += charSequence.countLeading((String) BasedSequence.WHITESPACE_NO_EOL_CHARS, lineStartOffset, lineEndOffset);
                lineEndOffset -= charSequence.countTrailing(BasedSequence.WHITESPACE_NO_EOL_CHARS, lineStartOffset, lineEndOffset);
                final ItemTextRange<Language> lineStartLanguage = getLanguageRangeAtOffset(file, lineStartOffset);
                final ItemTextRange<Language> lineEndLanguage = getLanguageRangeAtOffset(file, lineEndOffset);
                Commenter commenter = CommentByBlockCommentHandler.getCommenter(file, editor, lineStartLanguage.getItem(), lineEndLanguage.getItem());
                if (commenter != null) {
                    return new ItemTextRange<>(commenter, lineStartLanguage.getStartOffset(), lineEndLanguage.getEndOffset());
                }
            }
        }
        return null;
    }

    private static String splitCaretText(String text, StringBuilder sb, String sep, List<Integer> starts, List<Integer> ends) {
        int lastPos = 0;
        int length = text.length();
        while (lastPos < length) {
            int pos = text.indexOf('\n', lastPos);
            int endPos = pos == -1 ? length : pos;
            sb.append(sep);
            sep = "\n";
            starts.add(sb.length());
            sb.append(text.substring(lastPos, endPos));
            ends.add(sb.length());
            lastPos = endPos + 1;
        }
        return sep;
    }

    public static Transferable splitCaretTransferable(String... texts) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> starts = new ArrayList<>();
        ArrayList<Integer> ends = new ArrayList<>();
        String sep = "";
        for (String text : texts) {
            sep = splitCaretText(text, sb, sep, starts, ends);
        }

        final List<TextBlockTransferableData> transferableData = new ArrayList<>();

        int i = starts.size();
        int[] startOffsets = new int[i];
        while (i-- > 0) startOffsets[i] = starts.get(i);

        i = ends.size();
        int[] endOffsets = new int[i];
        while (i-- > 0) endOffsets[i] = ends.get(i);

        transferableData.add(new CaretStateTransferableData(startOffsets, endOffsets));
        final Transferable transferable = new TextBlockTransferable(sb.toString(), transferableData, null);
        return transferable;
    }

    private static Transferable getTransferable(String text, int lengthOffset) {
        final List<TextBlockTransferableData> transferableData = new ArrayList<>();
        int[] startOffsets = new int[1];
        int[] endOffsets = new int[1];
        startOffsets[0] = 0;
        endOffsets[0] = text.length() + lengthOffset;

        transferableData.add(new CaretStateTransferableData(startOffsets, endOffsets));
        final Transferable transferable = new TextBlockTransferable(text, transferableData, null);
        return transferable;
    }

    public static Couple<Integer> duplicateLineOrSelectedBlockAtCaret(Editor editor, final Document document, @NotNull Caret caret, final boolean moveCaret) {
        if (caret.hasSelection()) {
            int start = caret.getSelectionStart();
            int end = caret.getSelectionEnd();
            String s = document.getCharsSequence().subSequence(start, end).toString();
            document.insertString(end, s);
            if (moveCaret) {
                // select newly copied lines and move there
                caret.moveToOffset(end + s.length());
                editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
                caret.removeSelection();
                caret.setSelection(end, end + s.length());
            }
            return Couple.of(end, end + s.length());
        } else {
            return duplicateLinesRange(editor, document, caret, caret.getOffset(), caret.getVisualPosition(), caret.getVisualPosition(), moveCaret);
        }
    }

    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static Couple<Integer> duplicateLinesRange(
            Editor editor,
            Document document,
            @Nullable Caret caret,
            int offset,
            VisualPosition rangeStart,
            VisualPosition rangeEnd,
            boolean moveCaret
    ) {
        Pair<LogicalPosition, LogicalPosition> lines = EditorUtil.calcSurroundingRange(editor, rangeStart, rangeEnd);

        LogicalPosition lineStart = lines.first;
        LogicalPosition nextLineStart = lines.second;
        int start = editor.logicalPositionToOffset(lineStart);
        int end = editor.logicalPositionToOffset(nextLineStart);
        if (end <= start) {
            return null;
        }
        String s = document.getCharsSequence().subSequence(start, end).toString();
        final int lineToCheck = nextLineStart.line - 1;

        int newOffset = end + offset - start;
        if (lineToCheck == document.getLineCount() /* empty document */
                || lineStart.line == document.getLineCount() - 1 /* last line*/
                || document.getLineSeparatorLength(lineToCheck) == 0) {
            s = "\n" + s;
            newOffset++;
        }
        document.insertString(end, s);

        if (moveCaret && caret != null) {
            caret.moveToOffset(newOffset);

            editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
        }
        return Couple.of(end, end + s.length());
    }

    @NotNull
    public static String getCorrespondingQuoteLike(final String openingText) {
        StringBuilder sb = new StringBuilder(openingText.length());
        int iMax = openingText.length();
        for (int i = iMax; i-- > 0; ) {
            sb.append(getCorrespondingQuoteLike(openingText.charAt(i)));
        }
        return sb.toString();
    }

    public static final String QUOTES = "'\"`«»";
    public static final String QUOTE_LIKE = "''\"\"``()[]{}||//\\\\<>«»";

    public static char getCorrespondingQuoteLike(final char openingChar) {
        int pos = QUOTE_LIKE.indexOf(openingChar);
        if (pos == -1) return openingChar;
        if (pos % 2 == 0) {
            // opening quote
            return QUOTE_LIKE.charAt(pos + 1);
        } else {
            // closing quote
            return QUOTE_LIKE.charAt(pos - 1);
        }
    }

    public static boolean isQuote(char c) {
        return QUOTES.indexOf(c) != -1;
    }

    public static boolean startsWithQuote(String text) {
        return !text.isEmpty() && isQuote(text.charAt(0));
    }

    public static boolean endsWithQuote(String text) {
        return !text.isEmpty() && isQuote(text.charAt(text.length() - 1));
    }

    /**
     * Replace the text of range1 (non-overlapping part with range 2) with text from range2 (non-overlapping part of range 2)
     *
     * @param document
     * @param range1
     * @param range2
     * @return Pair first is range1 (non-overlapping) text and second is range2 non-overlapping text
     */
    @Nullable
    public static Pair<String, String> getRangeText(final Document document, final Range range1, final Range range2) {
        if (!range1.doesContain(range2) && !range2.doesContain(range1)) {
            // can replace text
            Object[] pair = new Object[] { null };

            ApplicationManager.getApplication().runReadAction(() -> {
                Range effectiveRange1 = range1.exclude(range2);
                Range effectiveRange2 = range2.exclude(range1);
                CharSequence chars = document.getCharsSequence();
                String text2 = effectiveRange2.subSequence(chars).toString();
                String text1 = effectiveRange1.subSequence(chars).toString();
                pair[0] = Pair.create(text1, text2);
            });

            //noinspection unchecked
            return (Pair<String, String>) pair[0];
        }
        return null;
    }

    public static class ByLineType {
        public int blank;
        public int comment;
        public int code;
        public int total;

        public ByLineType() {
            blank = 0;
            comment = 0;
            code = 0;
            total = 0;
        }

        public ByLineType(int blank, int comment, int code, int total) {
            this.blank = blank;
            this.comment = comment;
            this.code = code;
            this.total = total;
        }
    }

    public static class EachLineCarets {
        public int carets;
        public int caretSelections;

        public EachLineCarets() {
            carets = 0;
            caretSelections = 0;
        }
    }

    public static DataContext simpleDataContext(@Nullable Project project) {
        return simpleDataContext(project, null, null);
    }

    public static DataContext simpleDataContext(@Nullable Project project, @Nullable VirtualFile virtualFile) {
        return simpleDataContext(project, virtualFile, null);
    }

    public static DataContext simpleDataContext(@Nullable Project project, @Nullable VirtualFile virtualFile, @Nullable Editor editor) {
        HashMap<String, Object> dataMap = new HashMap<>();
        if (project != null) {
            dataMap.put(CommonDataKeys.PROJECT.getName(), project);
            if (project.getBasePath() != null) {
                VirtualFile baseDir = VirtualFileManager.getInstance().findFileByUrl("file://" + project.getBasePath());
                dataMap.put(PlatformDataKeys.PROJECT_FILE_DIRECTORY.getName(), baseDir);
            }
        }

        if (virtualFile != null) {
            dataMap.put(CommonDataKeys.VIRTUAL_FILE.getName(), virtualFile);
        }

        if (editor != null) dataMap.put(CommonDataKeys.EDITOR.getName(), editor);

        return SimpleDataContext.getSimpleContext(dataMap, null);
    }
}
