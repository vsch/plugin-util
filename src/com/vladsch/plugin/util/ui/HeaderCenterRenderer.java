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

package com.vladsch.plugin.util.ui;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class HeaderCenterRenderer implements TableCellRenderer {
    TableCellRenderer renderer;
    final int[] centerColumns;

    public HeaderCenterRenderer(JTable table, int... centerColumns) {
        renderer = table.getTableHeader().getDefaultRenderer();
        this.centerColumns = centerColumns;
    }

    boolean isCenteredColumn(int column) {
        for (int col : centerColumns) {
            if (col == column) return true;
        }
        return false;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        Component component = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        if (component instanceof JLabel && isCenteredColumn(col)) {
            JLabel oldLabel = (JLabel) component;
            JLabel label = new JLabel(oldLabel.getText());
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setForeground(oldLabel.getForeground());
            label.setBackground(oldLabel.getBackground());
            label.setFont(oldLabel.getFont());
            return label;
        }
        return component;
    }
}
