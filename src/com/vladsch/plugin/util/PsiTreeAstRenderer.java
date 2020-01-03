/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the copyright holder.
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

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class PsiTreeAstRenderer {
    public static String generateAst(BasedSequence fileChars, PsiElement element) {
        StringBuilder sb = new StringBuilder();
        generateAst(fileChars, sb, "", element);
        return sb.toString();
    }

    public static void generateAst(BasedSequence fileChars, StringBuilder out, String indent, PsiElement element) {
        // add any leaf psi elements
        ASTNode node = element.getNode();
        out.append(indent);
        Node.segmentSpanCharsToVisible(out, fileChars.subSequence(node.getStartOffset(), node.getStartOffset() + node.getTextLength()), element.getClass().getSimpleName());
        out.append("\n");

        PsiElement child = element.getFirstChild();
        String nextIndent = indent + "  ";

        while (child != null) {
            if (child instanceof LeafPsiElement) {
                node = child.getNode();
                out.append(nextIndent);
                Node.segmentSpanCharsToVisible(out, fileChars.subSequence(node.getStartOffset(), node.getStartOffset() + node.getTextLength()), node.getElementType().toString());
                out.append("\n");
            } else {
                generateAst(fileChars, out, nextIndent, child);
            }
            child = child.getNextSibling();
        }
    }
}
