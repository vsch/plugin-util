/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.plugin.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.vladsch.flexmark.util.Pair;
import com.vladsch.flexmark.util.Utils;
import com.vladsch.flexmark.util.format.TableFormatOptions;
import com.vladsch.flexmark.util.sequence.CharPredicate;
import com.vladsch.flexmark.util.sequence.SequenceUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    final public static String DUMMY_IDENTIFIER = TableFormatOptions.INTELLIJ_DUMMY_IDENTIFIER;
    final public static char DUMMY_IDENTIFIER_CHAR = TableFormatOptions.INTELLIJ_DUMMY_IDENTIFIER_CHAR;

    final public static char CARET_CHAR = '⦙';
    final public static char START_CHAR = '⟦';
    final public static char END_CHAR = '⟧';
    final public static char NBSP_CHAR = SequenceUtils.NBSP;
    final public static char NULL_CHAR = SequenceUtils.NUL;

    final public static String CARET_STRING = Character.toString(CARET_CHAR);
    final public static String END_STRING = Character.toString(END_CHAR);
    final public static String START_STRING = Character.toString(START_CHAR);
    final public static String TEST_CARET_STRING = "∣";    // caret positions for when caret markup result is desired
    final public static String TEST_END_STRING = "⦘";      // caret positions for when caret markup result is desired
    final public static String TEST_START_STRING = "⦗";    // caret positions for when caret markup result is desired
    final public static String NBSP_STRING = Character.toString(SequenceUtils.NBSP);
    final public static String NULL_STRING = Character.toString(SequenceUtils.NUL);
    final public static String CARET_MARKUP = START_STRING + CARET_STRING + END_STRING;
    final public static CharPredicate CARET_MARKUP_SET = CharPredicate.anyOf(TestUtils.CARET_MARKUP);

    public static String replaceCaretMarkers(@NotNull CharSequence input) {
        return input.toString()
                .replace(CARET_STRING, "<caret>")
                .replace(START_STRING, "<selection>")
                .replace(END_STRING, "</selection>");
    }

    @NotNull
    public static List<Integer> getCaretOffsets(@NotNull CharSequence content) {
        List<Integer> offsets = new ArrayList<>();
        int iMax = content.length();
        // add in descending order
        for (int i = iMax; i-- > 0; ) {
            if (content.charAt(i) == CARET_CHAR) {
                offsets.add(i + 1);
            }
        }
        return offsets;
    }

    @NotNull
    public static String getEditorTextWithCaretMarkup(@NotNull Editor editor, boolean withTestCaretMarkup, @Nullable Logger LOG) {
        CharSequence fileText = editor.getDocument().getCharsSequence();
        int length = fileText.length();

        if (LOG != null && LOG.isDebugEnabled()) LOG.debug(String.format("Got '%s' mod: %d", Utils.escapeJavaString(fileText), editor.getDocument().getModificationStamp()));

        List<Caret> carets = editor.getCaretModel().getAllCarets();
        ArrayList<Pair<Integer, Character>> inserts = new ArrayList<>();

        for (Caret caret : carets) {
            if (LOG != null && LOG.isDebugEnabled()) LOG.debug(String.format("  caret at %d", caret.getOffset()));

//            // NOTE: Math.min() is only need if document content is reverted to original after mods by actions in test mode only and if document sequence has not been accessed after document creation
//            inserts.add(Pair.of(Math.min(length, caret.getOffset()), 'c'));
            inserts.add(Pair.of(caret.getOffset(), 'c'));

            if (caret.hasSelection()) {
                inserts.add(Pair.of(caret.getSelectionStart(), 's'));
                inserts.add(Pair.of(caret.getSelectionEnd(), 'S'));
            }
        }

        inserts.sort((o1, o2) -> {
            int offsetCompare = o1.getFirst().compareTo(o1.getFirst());
            if (offsetCompare == 0) {
                // put carets ahead of selection start and after selection end
                return o1.getSecond().compareTo(o2.getSecond());
            }
            return offsetCompare;
        });

        int iMax = inserts.size();
        int lastPos = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = iMax; i-- > 0; ) {
            Pair<Integer, Character> insert = inserts.get(i);
            int offset = insert.getFirst();
            char c = insert.getSecond();
            String text = withTestCaretMarkup ? c == 'c' ? TEST_CARET_STRING : c == 's' ? TEST_START_STRING : c == 'S' ? TEST_END_STRING : null : c == 'c' ? CARET_STRING : c == 's' ? START_STRING : c == 'S' ? END_STRING : null;
            assert (text != null);

            if (lastPos < offset) {
                sb.append(fileText.subSequence(lastPos, offset));
            }
            sb.append(text);
            lastPos = offset;
        }

        if (lastPos < length) sb.append(fileText.subSequence(lastPos, length));

        return sb.toString();
    }
}
