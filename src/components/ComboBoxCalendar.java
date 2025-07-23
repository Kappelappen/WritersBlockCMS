package components;

import java.awt.*;

import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import windows.MainWindow;

public class ComboBoxCalendar extends JComboBox<String> {
    
    private final boolean state;
    private final MainWindow mWindow;
    private final int width;
    private final int height;
    
    private String[] weekdays = null;
    
    public ComboBoxCalendar(boolean state, 
        MainWindow mWindow,int width, 
        int height) {
        
        super();
      
        this.state = state;
        this.mWindow = mWindow;
        this.width = width;
        this.height = height;
                
        this.configJComboBox();

    }
    
    private void configJComboBox() {
                
        this.setEnabled(state);
        this.setFocusable(false);
        this.setOpaque(false);
        this.setRenderer(new CalendarComboRenderer());
        this.updateUI();
              
    }
    
    public void setCurrentDate(String item) {
     
        super.setSelectedItem(item);
     
    }
 
    public String getDateValue() {
     
        Object obj = super.getSelectedItem();
     
        String item = (obj != null) ? 
            getItemAt(0).toString().trim() : null;     
     
        return item;
     
    }
    
    @Override
    public Dimension getMinimumSize() {
       
        return new Dimension(width,height);
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        
        return new Dimension(width,height);
        
    }
    
    @Override
    public Dimension getMaximumSize() {
        
        return new Dimension(width,height);
        
    }
        
    @Override
    public void updateUI() {
    
        ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
    
        if (cui instanceof MetalComboBoxUI) {
            
            setUI(new MetalDateComboBoxUI(state));
        
        }
    }

     protected class MetalDateComboBoxUI extends MetalComboBoxUI {
    	 
        private final boolean state;

        public MetalDateComboBoxUI(boolean state) {
            this.state = state;
        }

        @Override
        protected ComboPopup createPopup() {
            return new DatePopup(state, ComboBoxCalendar.this);
        }
    }    

    class DatePopup implements ComboPopup, MouseMotionListener,
    	MouseListener, KeyListener, PopupMenuListener {
    
        private final boolean state;
        protected ComboBoxCalendar comboBox;
        private Properties props;
        protected int year;
        protected int month;
        protected int date;
        protected Color background;
        protected Color foreground;
        protected Color selectedBackground;
        protected Color selectedForeground;
        protected SimpleDateFormat dateFormat;
        protected SimpleDateFormat monthFormat;
        protected Calendar calendar;
        protected boolean shouldHide;
        protected JList calendarList;
        protected int yearField;
        protected int monthField;
        protected JLabel previousYearLabel;
        protected JLabel previousMonthLabel;
        protected JLabel monthLabel;
        protected JLabel nextMonthLabel;
        protected JLabel nextYearLabel;
        protected JSeparator separator;
        protected BorderLayout borderLayout;
        protected GridLayout gridLayout;
        protected GridBagLayout gbl;
        protected GridBagConstraints gbc;
        protected JPanel headerPanel;
        protected JPanel dayPanel = null;
        protected JPanel mainPanel;
        public JPopupMenu calendarPopup;
        
        public DatePopup(boolean state, 
            ComboBoxCalendar comboBox) {
            
            this.state = state;
            this.comboBox = comboBox;
            
            this.props = new Properties();

            this.year = Calendar.YEAR;
            this.month = Calendar.MONTH;
            this.date = Calendar.DATE;
                        
            background = UIManager.getColor("ComboBox.background");
            foreground = UIManager.getColor("ComboBox.foreground");
            selectedBackground = UIManager.getColor("ComboBox.selectionBackground");
            selectedForeground = UIManager.getColor("ComboBox.selectionForeground");
            
            this.dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            this.monthFormat = new SimpleDateFormat("MMM,yyyy");
            
            this.calendar = Calendar.getInstance();
            this.monthField = Calendar.MONTH;
            this.yearField = Calendar.YEAR;
            
            this.shouldHide = true;
            
            this.calendarList = new JList();
            this.calendarList.setBorder(null);
            this.calendarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
            this.previousYearLabel = createNavigationLabel("&#60;&#60;",yearField,-1);
            this.previousMonthLabel = createNavigationLabel("&#60;",monthField,-1);
            this.monthLabel = new JLabel();
            this.nextMonthLabel = createNavigationLabel("&#62;",monthField,1);
            this.nextYearLabel = createNavigationLabel("&#62;&#62;",yearField,1);
            
            this.separator = new JSeparator(JSeparator.HORIZONTAL);
            
            this.borderLayout = new BorderLayout();
            this.gridLayout = new GridLayout(0,7,2,0);
            this.gbl = new GridBagLayout();
            this.gbc = new GridBagConstraints();
            
            this.headerPanel = new JPanel(gbl);
            this.mainPanel = new JPanel(gbl);
            
            this.monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.calendarPopup = createJPopupMenu();
                
            this.populateCalendarPopup();            
            this.populateHeaderPanel();
                        
        }
        
        public void loadProperties(Properties props) {

            try {

            String path = "main.properties";
            InputStream input = getClass().getClassLoader().getResourceAsStream(path);
            props.load(input);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void populateComboBox() {
            
            comboBox.removeAllItems();
            comboBox.repaint();
            comboBox.revalidate();
                        
            int currentYear = this.calendar.get(year);
            int currentMonth = this.calendar.get(month) + 1;
            int currentDate = this.calendar.get(date);
            
            String dateValue = currentYear + "-" + currentMonth + "-" + currentDate;
            
            try {
            
            Date dateObj = dateFormat.parse(dateValue);
            Date today = new Date();
            String itemValue = dateFormat.format(dateObj);
            String monthValue = monthFormat.format(today);
            
            this.monthLabel.setText(monthValue);
                        
            comboBox.addItem(itemValue);
            comboBox.requestFocus();
                                        
            } catch (ParseException ex) {
                
                System.err.println(ex.getMessage());
                
            }            
        }
        
        private void populateCalendarPopup() {
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = GridBagConstraints.BOTH;
            gbc.weighty = GridBagConstraints.NONE;
            gbc.insets = new Insets(0,3,0,3);
            gbl.setConstraints(headerPanel, gbc);
            this.calendarPopup.add(headerPanel,gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = GridBagConstraints.BOTH;
            gbc.weighty = GridBagConstraints.BOTH;
            gbc.insets = new Insets(0,0,0,0);
            gbl.setConstraints(mainPanel, gbc);
            this.calendarPopup.add(mainPanel,gbc);
            
            //this.calendarPopup.add(BorderLayout.NORTH, headerPanel);
            //this.calendarPopup.add(BorderLayout.CENTER, mainPanel);
            
        }
        
        private void populateHeaderPanel() {
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.weightx = GridBagConstraints.BOTH;
            gbc.weighty = GridBagConstraints.NONE;
            gbc.insets = new Insets(3,6,3,3);
            gbl.setConstraints(previousYearLabel, gbc);
            this.headerPanel.add(previousYearLabel,gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.weightx = GridBagConstraints.NONE;
            gbc.weighty = GridBagConstraints.NONE;
            gbc.insets = new Insets(3,0,3,0);
            gbl.setConstraints(previousMonthLabel, gbc);
            this.headerPanel.add(previousMonthLabel,gbc);
            
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = GridBagConstraints.BOTH;
            gbc.weighty = GridBagConstraints.NONE;
            gbc.insets = new Insets(3,3,3,3);
            gbl.setConstraints(monthLabel, gbc);
            this.headerPanel.add(monthLabel,gbc);
            
            gbc.gridx = 3;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = GridBagConstraints.NONE;
            gbc.weighty = GridBagConstraints.NONE;
            gbc.insets = new Insets(3,0,3,3);
            gbl.setConstraints(nextMonthLabel, gbc);
            this.headerPanel.add(nextMonthLabel,gbc);
            
            gbc.gridx = 4;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.weightx = GridBagConstraints.BOTH;
            gbc.weighty = GridBagConstraints.NONE;
            gbc.insets = new Insets(3,0,0,6);
            gbl.setConstraints(nextYearLabel, gbc);
            this.headerPanel.add(nextYearLabel,gbc);
            
        }
        
        public String getProperty(String name) {

            loadProperties(props);
            String resource = props.getProperty(name);

            return resource;

        }
        
        private JPopupMenu createJPopupMenu() {
                        
            Dimension dim = new Dimension(200,200);
            JPopupMenu menu = new JPopupMenu();
            menu.setLayout(gbl);
            menu.setPreferredSize(dim);
                        
            return menu;
            
        }
        
        private JLabel createNavigationLabel(final String value, 
            final int field, final int amount) {
            
            final Dimension dim = new Dimension(25,20);
            final Border selectedBorder = new EtchedBorder();
            final Border unselectedBorder = 
                    new EmptyBorder(selectedBorder.getBorderInsets(new JLabel()));
            
            Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            Cursor mainCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            Font font = new Font("Verdana", Font.PLAIN, 12);
            String text = "<html>" + value + "</html>";
            JLabel label = new JLabel(text);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(false);
            label.setFont(font);
            label.setPreferredSize(dim);
            
            label.addMouseListener(new MouseAdapter() {
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    
                    calendar.add(field, amount);
                    buildCalendar();
                    
                                                
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
            
                    label.setCursor(handCursor);
                    label.setBorder(selectedBorder);
                    
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    
                    label.setCursor(mainCursor);
                    label.setBorder(unselectedBorder);
                    
                }                
            });
            
            return label;
            
        }
        
        protected void togglePopup() {
            
            if (isVisible() || shouldHide ) {
                
                hide();
            
            } else {
                
                show();
            }
                
            shouldHide = false;
                        
        }
        
        @Override
        public void show() {
            
            if (!state) {
                
                ComboBoxCalendar.this.setFocusable(false);
                
            }            
            
            if (ComboBoxCalendar.this.isEnabled() 
                && !calendarPopup.isShowing()) {
            
            int height = comboBox.getHeight();
            calendarPopup.pack();
            calendarPopup.show(comboBox, 0, height);
            
            comboBox.removeAllItems();
            calendar.setTime(new Date());
            buildCalendar();
        
            }
        }
        
        private void buildCalendar() {
                        
            String text = monthFormat.format(calendar.getTime());
            monthLabel.setText(text);
                        
            if (dayPanel != null) {
                            
                 this.dayPanel.revalidate();
                 this.dayPanel.removeAll();
                 
                 this.mainPanel.removeAll();
                 this.mainPanel.revalidate();
                 
            }
            
            this.dayPanel = new JPanel(gridLayout);
            
            Calendar setupCalendar = (Calendar) calendar.clone();
            int first = setupCalendar.getFirstDayOfWeek();
            setupCalendar.set(Calendar.DAY_OF_WEEK, first);
            
            String monday = getProperty("MON");
            String tuesday = getProperty("TUE");
            String wednesday = getProperty("WED");
            String thursday = getProperty("THU");
            String friday = getProperty("FRI");
            String saturday = getProperty("SAT");
            String sunday = getProperty("SUN");
            
            String[] weekdays = { monday,tuesday,wednesday,
                thursday,friday,saturday,sunday
            };            
                        
            for (int i = 0; i < 7; i++) {
                
                JLabel label = createDayLabel();
                int dayInt = setupCalendar.get(Calendar.DAY_OF_WEEK);
                
                if (dayInt == Calendar.SUNDAY) {
            
                    label.setText(weekdays[i]);
                    
                }
                
                else if (dayInt == Calendar.MONDAY) {
                    
                    label.setText(weekdays[i]);
                    
                }
                
                else if (dayInt == Calendar.TUESDAY) {
            
                    label.setText(weekdays[i]);
                    
                }
                
                else if (dayInt == Calendar.WEDNESDAY) {
                    
                    label.setText(weekdays[i]);
                    
                }
                
                else if (dayInt == Calendar.THURSDAY) {
            
                    label.setText(weekdays[i]);
                    
                }
                
                else if (dayInt == Calendar.FRIDAY) {
            
                    label.setText(weekdays[i]);
                    
                }
                
                else if (dayInt == Calendar.SATURDAY) {
            
                    label.setText(weekdays[i]);
                    
                }
                                                
                this.dayPanel.add(label);                           
                setupCalendar.roll(Calendar.DAY_OF_WEEK, false);
                
            }
            
            this.separator.setBorder(BorderFactory.createEmptyBorder());
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = GridBagConstraints.NONE;
            gbc.weighty = GridBagConstraints.NONE;
            gbl.setConstraints(separator, gbc);
            this.mainPanel.add(separator,gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = GridBagConstraints.BOTH;
            gbc.weighty = GridBagConstraints.BOTH;
            gbc.insets = new Insets(0,0,0,0);
            gbl.setConstraints(dayPanel, gbc);
            this.mainPanel.add(dayPanel,gbc);
                                                            
            setupCalendar = (Calendar) calendar.clone();
            setupCalendar.set(Calendar.DAY_OF_MONTH, 1);

            // Hämta den veckodag som den första dagen i månaden infaller på
            
            int start = setupCalendar.get(Calendar.DAY_OF_WEEK) - 
            setupCalendar.getFirstDayOfWeek();
            
            if (start < 0) {
            
                start += 7;
            
            }

            // Fyll i tomma platser fram till den första dagen i månaden
            for (int i = 0; i < start; i++) {
            
                this.dayPanel.add(new JLabel(""));
            
            }

            // Hämta det maximala antalet dagar i månaden
            int maximum = setupCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            // Lägg till alla dagar i månaden
            for (int i = 1; i <= maximum; i++) {
            
                final int currentDay = i;
                final JLabel calendarLabel = createCalendarLabel(currentDay);

                // Sätt dagen till aktuellt datum för denna iteration
                setupCalendar.set(Calendar.DAY_OF_MONTH, currentDay);

                Date currentDate = setupCalendar.getTime();
                String item = dateFormat.format(currentDate);

                // Lägg till det formaterade datumet i JComboBox
                comboBox.setName(item);

                // Lägg till dagen i dagpanelen
                this.dayPanel.add(calendarLabel);
            }

            // Uppdatera JComboBox och gör den redo att använda
            comboBox.repaint();
            comboBox.revalidate();            
        }
        
        private JLabel createCalendarLabel(int num) {
            
            Font font = new Font("Verdana", Font.BOLD, 13);
            String text = String.valueOf(num);
            JLabel label = new JLabel();
            
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder());
            label.setFont(font);
            label.setBackground(background);
            label.setForeground(foreground);
            label.setVerticalTextPosition(SwingConstants.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setText(text);
                        
            label.addMouseListener(new MouseAdapter() {
            
                @Override
                public void mouseReleased(MouseEvent e) {
                    
                    calendar.set(Calendar.DAY_OF_MONTH, num);
                    comboBox.setSelectedItem(dateFormat.format(calendar.getTime()));
                    populateComboBox();
                    hide();
                    
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    
                    label.setBackground(selectedBackground);
                    label.setForeground(selectedForeground);
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                    label.setBackground(background);
                    label.setForeground(foreground);
                    
                }
                
            });
            
            return label;
        }
        
        private JLabel createDayLabel() {
            
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setOpaque(false);        
            label.setBorder(null);
            return label;
            
        }
    
        @Override
        public void hide() {
        
            this.calendarPopup.setVisible(false);
        
        }
    
        @Override
        public JList getList() {
            return calendarList;
        }
    
        @Override
        public MouseListener getMouseListener() {
            return this;
        }
    
        @Override
        public MouseMotionListener getMouseMotionListener() {
            return this;
        }
    
        @Override
        public KeyListener getKeyListener() {
            return this;
        }
    
        @Override
        public boolean isVisible() {
        
            return calendarPopup.isVisible();
        
        }
    
        @Override
        public void uninstallingUI() {  
        
        }

        @Override
        public void mousePressed( MouseEvent e ) {}

        @Override
        public void mouseReleased( MouseEvent e ) {
            
            this.shouldHide = false;
            togglePopup();
                                    
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            
            show();
            buildCalendar();
                                    
        }
    
        @Override
        public void mouseEntered(MouseEvent e) {
    
        }

        @Override
        public void mouseExited(MouseEvent e) {

            this.shouldHide = true;
            
        }

        @Override
        public void mouseDragged(MouseEvent e) {}
        
        @Override
        public void mouseMoved(MouseEvent e) {} 
        
        @Override
        public void keyPressed(KeyEvent e) {}
        
        @Override
        public void keyTyped(KeyEvent e) {}
        
        @Override
        public void keyReleased( KeyEvent e ) {}
        
        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            
            this.shouldHide = false;
        
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        
        }
    
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                   
        }    
    }
    
    protected class CalendarComboRenderer extends DefaultListCellRenderer
        implements ListCellRenderer<Object> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Object> list,
            Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
            // Skapa en ny JLabel för varje cell (viktigt för att undvika störningar)
            
            EmptyBorder emptyBorder = new EmptyBorder(6,5,6,5);
            
            JLabel label = new JLabel();
            
            label.setOpaque(true);
            label.setBorder(emptyBorder);       
            
            if (value != null) {
            
                // Sätt texten med HTML för eventuell styling
                String text = value.toString();
                label.setText(text);
            
            }

            // Hantera bakgrund och textfärg beroende på om cellen är vald
            if (isSelected) {
                
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
                label.setOpaque(true); // Viktigt för att bakgrunden ska synas
            
            } else {
            
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
                label.setOpaque(true); // Behövs även här
            }

            return label; // Returnera JLabel som representerar cellen
    
        }    
    }
}
