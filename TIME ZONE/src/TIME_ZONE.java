import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class TIME_ZONE {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        ClockPanel clockPanel = new ClockPanel();
        frame.setLayout(new BorderLayout());
        frame.add(clockPanel, BorderLayout.NORTH);

        ClockTable clockTable = new ClockTable();
        frame.add(clockTable, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}

class ClockPanel extends JPanel {
    private final Timer timer;
    private final SimpleDateFormat timeFormat;
    private String currentTime;

    public ClockPanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(500, 100));
        timeFormat = new SimpleDateFormat("hh:mm:ss a");
        currentTime = timeFormat.format(new Date());

        timer = new Timer(1000, e -> {
            currentTime = timeFormat.format(new Date()).toUpperCase(); // Ensure AM/PM is uppercase
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString(currentTime, getWidth() / 2 - 90, getHeight() / 2 + 18);
    }
}

class ClockTable extends JPanel {
    private final JTable table;
    private final DefaultTableModel model;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField searchField;
    private final JButton searchButton;

    public ClockTable() {
        model = new DefaultTableModel();
        model.addColumn("TIME ZONE");
        model.addColumn("TIME");

        String[] timeZones = TimeZone.getAvailableIDs();

        for (String timeZoneID : timeZones) {
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneID);
            String time = getFormattedTime(timeZone);
            model.addRow(new Object[]{timeZoneID, time});
        }

        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = searchField.getText().trim();
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0));
                }
            }
        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private String getFormattedTime(TimeZone timeZone) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        timeFormat.setTimeZone(timeZone);
        return timeFormat.format(new Date()).toUpperCase(); 
    }
}
