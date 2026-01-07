import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainGUI extends JFrame {
    private JTextField searchField;
    private JTextPane resultsPane;
    private JButton searchButton;
    private JLabel statusLabel;
    private List<JSONObject> data;
    private FrequencyTable frequencyTable;

    private final Color DARK_BG = new Color(18, 18, 18);
    private final Color CARD_BG = new Color(40, 40, 40);
    private final Color ACCENT_GREEN = new Color(30, 215, 96);
    private final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private final Color TEXT_SECONDARY = new Color(179, 179, 179);
    private final Color HOVER_BG = new Color(60, 60, 60);
    private final Color SEARCH_BG = new Color(50, 50, 50);

    public MainGUI() {
        initializeComponents();
        loadData();
        centerFrame();
    }

    private void initializeComponents() {
        setTitle("Berman Business Finder • Similarity Analyzer");
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set dark theme
        getContentPane().setBackground(DARK_BG);

        // Create main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createSearchPanel(), BorderLayout.CENTER);
        mainPanel.add(createResultsPanel(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Left side - Logo and title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(DARK_BG);

        JLabel logoLabel = new JLabel("🎯");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logoLabel.setForeground(ACCENT_GREEN);

        JLabel titleLabel = new JLabel("Berman Business Finder");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Similarity Analyzer");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        titlePanel.add(logoLabel);
        titlePanel.add(Box.createHorizontalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalStrut(5));
        titlePanel.add(subtitleLabel);

        // Right side - Stats
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statsPanel.setBackground(DARK_BG);

        JLabel statsLabel = new JLabel("Ready to analyze • Powered by Yelp Data");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsLabel.setForeground(TEXT_SECONDARY);

        statsPanel.add(statsLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(DARK_BG);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Search card
        JPanel searchCard = new RoundedPanel(20);
        searchCard.setBackground(CARD_BG);
        searchCard.setLayout(new BorderLayout());
        searchCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Search header
        JLabel searchHeader = new JLabel("Discover Similar Businesses");
        searchHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        searchHeader.setForeground(TEXT_PRIMARY);
        searchHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel searchSubtitle = new JLabel("Enter a business name to find similar establishments based on category analysis");
        searchSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchSubtitle.setForeground(TEXT_SECONDARY);
        searchSubtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Search input group
        JPanel inputGroup = new JPanel(new BorderLayout(15, 0));
        inputGroup.setBackground(CARD_BG);

        searchField = new JTextField();
        styleSearchField(searchField);

        searchButton = new JButton("Analyze Similarity");
        styleActionButton(searchButton);
        searchButton.addActionListener(this::handleSearch);

        searchField.addActionListener(this::handleSearch);

        inputGroup.add(searchField, BorderLayout.CENTER);
        inputGroup.add(searchButton, BorderLayout.EAST);

        // Features grid
        JPanel featuresPanel = createFeaturesGrid();

        searchCard.add(searchHeader, BorderLayout.NORTH);
        searchCard.add(searchSubtitle, BorderLayout.CENTER);
        searchCard.add(inputGroup, BorderLayout.SOUTH);

        searchPanel.add(searchCard, BorderLayout.CENTER);
        searchPanel.add(featuresPanel, BorderLayout.SOUTH);

        return searchPanel;
    }

    private JPanel createFeaturesGrid() {
        JPanel featuresPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        featuresPanel.setBackground(DARK_BG);
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        String[][] features = {
                {"🔍", "Smart Matching", "Advanced category analysis for accurate results"},
                {"📊", "Real-time Analysis", "Instant similarity scoring and ranking"},
                {"🎯", "Business Insights", "Detailed comparisons and metrics"}
        };

        for (String[] feature : features) {
            JPanel featureCard = new RoundedPanel(15);
            featureCard.setBackground(CARD_BG);
            featureCard.setLayout(new BorderLayout());
            featureCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            featureCard.setPreferredSize(new Dimension(200, 120));
            featureCard.setMinimumSize(new Dimension(150, 100));

            JLabel icon = new JLabel(feature[0]);
            icon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            icon.setForeground(ACCENT_GREEN);

            JLabel title = new JLabel(feature[1]);
            title.setFont(new Font("Segoe UI", Font.BOLD, 16));
            title.setForeground(TEXT_PRIMARY);
            title.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

            JLabel desc = new JLabel("<html><div style='text-align: left;'>" + feature[2] + "</div></html>");
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            desc.setForeground(TEXT_SECONDARY);

            featureCard.add(icon, BorderLayout.NORTH);
            featureCard.add(title, BorderLayout.CENTER);
            featureCard.add(desc, BorderLayout.SOUTH);

            featuresPanel.add(featureCard);
        }

        return featuresPanel;
    }

    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(DARK_BG);

        JLabel resultsHeader = new JLabel("Similarity Results");
        resultsHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        resultsHeader.setForeground(TEXT_PRIMARY);
        resultsHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        resultsPane = new JTextPane();
        resultsPane.setContentType("text/html");
        resultsPane.setEditable(false);
        resultsPane.setBackground(DARK_BG);
        resultsPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(resultsPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(DARK_BG);

        // Make it responsive
        scrollPane.setPreferredSize(new Dimension(1140, 400));
        scrollPane.setMinimumSize(new Dimension(600, 200));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Style the scrollbars
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setBackground(DARK_BG);

        JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
        horizontal.setBackground(DARK_BG);

        resultsPanel.add(resultsHeader, BorderLayout.NORTH);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        return resultsPanel;
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(30, 30, 30));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)));
        statusPanel.setPreferredSize(new Dimension(getWidth(), 30));

        statusLabel = new JLabel("Berman Business Finder • Portfolio Demo • Ready to analyze business similarities");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel dataLabel = new JLabel("Yelp Business Dataset • Advanced Analytics");
        dataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dataLabel.setForeground(TEXT_SECONDARY);
        dataLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dataLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(dataLabel, BorderLayout.EAST);

        return statusPanel;
    }

    private void styleSearchField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(SEARCH_BG);
        field.setCaretColor(ACCENT_GREEN);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setPreferredSize(new Dimension(400, 50));
        field.setMinimumSize(new Dimension(200, 50));
    }

    private void styleActionButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ACCENT_GREEN);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_GREEN.darker()),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_GREEN.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_GREEN);
            }
        });
    }

    // Rounded panel class for modern card design
    class RoundedPanel extends JPanel {
        private int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw rounded panel
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText("Berman Business Finder • Portfolio Demo • " + message);
    }

    private void handleSearch(ActionEvent e) {
        String userInput = searchField.getText().trim();
        updateStatus("Analyzing: " + userInput + "...");

        if (userInput.isEmpty()) {
            showMessage("Please enter a business name to analyze.", "Search Required");
            updateStatus("Please enter a business name");
            return;
        }

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private String searchedBusiness;
            private String categories;
            private List<JSONObject> similarBusinesses;

            @Override
            protected Void doInBackground() throws Exception {
                JSONObject targetBusiness = findBusinessByName(userInput);
                if (targetBusiness == null) {
                    searchedBusiness = userInput;
                    categories = "Business not found in database";
                    similarBusinesses = new ArrayList<>();
                    return null;
                }

                searchedBusiness = userInput;
                categories = (String) targetBusiness.get("categories");
                if (categories == null || categories.trim().isEmpty()) {
                    categories = "No categories available";
                    similarBusinesses = new ArrayList<>();
                    return null;
                }

                similarBusinesses = findSimilarBusinesses(targetBusiness, categories, 10);
                return null;
            }

            @Override
            protected void done() {
                displaySpotifyResults(searchedBusiness, categories, similarBusinesses);
                updateStatus("Analysis complete • Found " + similarBusinesses.size() + " similar businesses");
            }
        };

        worker.execute();
    }

    private void displaySpotifyResults(String searchedBusiness, String categories, List<JSONObject> similarBusinesses) {
        String html = createSpotifyHTMLResults(searchedBusiness, categories, similarBusinesses);
        resultsPane.setText(html);
        resultsPane.setCaretPosition(0);
    }

    private String createSpotifyHTMLResults(String searchedBusiness, String categories, List<JSONObject> similarBusinesses) {
        StringBuilder html = new StringBuilder();

        html.append("""
            <html>
            <head>
            <style>
                body { 
                    font-family: 'Segoe UI', Arial, sans-serif; 
                    background: #121212;
                    margin: 0; 
                    padding: 0;
                    color: #ffffff;
                }
                .container {
                    max-width: 100%;
                    margin: 0 auto;
                    padding-bottom: 20px;
                }
                .results-header {
                    background: linear-gradient(135deg, #1db95420, #191414);
                    padding: 25px;
                    border-radius: 12px;
                    margin-bottom: 25px;
                    border-left: 4px solid #1db954;
                }
                .business-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
                    gap: 15px;
                    margin-top: 20px;
                }
                .business-card {
                    background: #282828;
                    padding: 20px;
                    border-radius: 8px;
                    transition: all 0.3s ease;
                    border: 1px solid #404040;
                    min-height: 120px;
                }
                .business-card:hover {
                    background: #333333;
                    transform: translateY(-2px);
                    border-color: #1db954;
                }
                .card-header {
                    display: flex;
                    align-items: center;
                    margin-bottom: 15px;
                }
                .business-icon {
                    font-size: 24px;
                    margin-right: 12px;
                }
                .business-name {
                    font-size: 16px;
                    font-weight: bold;
                    color: #ffffff;
                    margin: 0;
                }
                .business-categories {
                    color: #b3b3b3;
                    font-size: 13px;
                    margin: 5px 0;
                    line-height: 1.4;
                }
                .business-details {
                    color: #b3b3b3;
                    font-size: 12px;
                    margin: 3px 0;
                }
                .similarity-badge {
                    margin-top: 8px;
                    font-size: 11px;
                    font-weight: bold;
                    color: #ffffff;
                }
                .progress-container {
                    background: #404040;
                    border-radius: 10px;
                    height: 8px;
                    margin-top: 5px;
                    overflow: hidden;
                }
                .progress-bar {
                    height: 100%;
                    border-radius: 10px;
                    transition: width 0.3s ease;
                }
                .progress-text {
                    font-size: 10px;
                    margin-top: 3px;
                    color: #b3b3b3;
                    text-align: center;
                }
                .no-results {
                    background: #282828;
                    padding: 50px;
                    border-radius: 12px;
                    text-align: center;
                    border: 2px dashed #404040;
                }
                .search-query {
                    color: #1db954;
                    font-weight: bold;
                }
                .stats-bar {
                    background: #282828;
                    padding: 15px;
                    border-radius: 8px;
                    margin-top: 20px;
                    text-align: center;
                    font-size: 13px;
                    color: #b3b3b3;
                    border: 1px solid #404040;
                }
                .feature-highlight {
                    color: #1db954;
                    font-weight: bold;
                }
                .new-search-prompt {
                    background: #282828;
                    padding: 15px;
                    border-radius: 8px;
                    margin-top: 15px;
                    text-align: center;
                    font-size: 13px;
                    color: #b3b3b3;
                    border: 1px dashed #404040;
                }
            </style>
            </head>
            <body>
            <div class='container'>
            """);

        // Header with search results
        html.append("<div class='results-header'>")
                .append("<div style='font-size: 24px; font-weight: bold; margin-bottom: 10px;'>")
                .append("🔍 Analysis Results")
                .append("</div>")
                .append("<div style='font-size: 16px; color: #b3b3b3; margin-bottom: 5px;'>")
                .append("Business: <span class='search-query'>").append(escapeHTML(searchedBusiness)).append("</span>")
                .append("</div>")
                .append("<div style='font-size: 14px; color: #b3b3b3;'>")
                .append("Categories: ").append(escapeHTML(categories))
                .append("</div>")
                .append("</div>");

        // Results Grid
        if (similarBusinesses.isEmpty()) {
            if (categories.equals("Business not found in database")) {
                html.append("<div class='no-results'>")
                        .append("<div style='font-size: 48px; margin-bottom: 20px;'>🔍</div>")
                        .append("<h3 style='color: #1db954; margin-bottom: 10px;'>Business Not Found</h3>")
                        .append("<p style='margin-bottom: 20px; color: #b3b3b3;'>We couldn't find '<span class='search-query'>").append(escapeHTML(searchedBusiness)).append("</span>' in our database.</p>")
                        .append("<div style='text-align: left; display: inline-block; color: #b3b3b3; font-size: 14px;'>")
                        .append("<div style='margin-bottom: 8px;'>💡 <strong>Suggestions:</strong></div>")
                        .append("<div style='margin-bottom: 4px;'>• Check the spelling of the business name</div>")
                        .append("<div style='margin-bottom: 4px;'>• Try a different business name</div>")
                        .append("<div>• Ensure the business exists in the Yelp dataset</div>")
                        .append("</div>")
                        .append("</div>");
            } else {
                html.append("""
                    <div class='no-results'>
                        <div style='font-size: 48px; margin-bottom: 20px;'>🎯</div>
                        <h3 style='color: #1db954; margin-bottom: 10px;'>No Similar Businesses Found</h3>
                        <p style='margin-bottom: 20px; color: #b3b3b3;'>We couldn't find any businesses with similar categories.</p>
                        <div style='text-align: left; display: inline-block; color: #b3b3b3; font-size: 14px;'>
                            <div style='margin-bottom: 8px;'>💡 <strong>Tips for better results:</strong></div>
                            <div style='margin-bottom: 4px;'>• Try businesses with more common categories</div>
                            <div style='margin-bottom: 4px;'>• Check the spelling of the business name</div>
                            <div>• Try broader search terms</div>
                        </div>
                    </div>
                    """);
            }
        } else {
            html.append("<div style='font-size: 18px; font-weight: bold; margin-bottom: 20px; color: #ffffff;'>")
                    .append("🎯 Found ").append(similarBusinesses.size()).append(" Similar Businesses")
                    .append("</div>");

            html.append("<div class='business-grid'>");

            String[] rankIcons = {"🥇", "🥈", "🥉", "💎", "⭐", "🔥", "🚀", "🎯", "🏆", "👑"};

            for (int i = 0; i < similarBusinesses.size(); i++) {
                JSONObject business = similarBusinesses.get(i);
                String rankIcon = i < rankIcons.length ? rankIcons[i] : "⭐";

                html.append("<div class='business-card'>")
                        .append("<div class='card-header'>")
                        .append("<div class='business-icon'>").append(rankIcon).append("</div>")
                        .append("<div>")
                        .append("<div class='business-name'>").append(escapeHTML((String) business.get("name"))).append("</div>");

                // Categories
                String businessCategories = (String) business.get("categories");
                if (businessCategories != null && !businessCategories.trim().isEmpty()) {
                    html.append("<div class='business-categories'>").append(escapeHTML(businessCategories)).append("</div>");
                }

                html.append("</div>")
                        .append("</div>"); // Close card-header

                // Details
                html.append("<div class='business-details'>");

                if (business.containsKey("city")) {
                    html.append("📍 ").append(escapeHTML((String) business.get("city")));
                    if (business.containsKey("state")) {
                        html.append(", ").append(escapeHTML((String) business.get("state")));
                    }
                    html.append("<br>");
                }

                if (business.containsKey("stars")) {
                    html.append("⭐ ").append(business.get("stars")).append("/5.0");
                    if (business.containsKey("review_count")) {
                        html.append(" • ").append(business.get("review_count")).append(" reviews");
                    }
                    html.append("<br>");
                }

                html.append("</div>");

                // Similarity score with progress bar
                String similarityPercentage = calculateSimilarityScore(categories, businessCategories);
                int percentage = Integer.parseInt(similarityPercentage);
                String progressColor = getProgressBarColor(percentage);
                String progressIcon = getProgressIcon(percentage);

                html.append("<div class='similarity-badge'>")
                        .append(progressIcon).append(" Category Match: ").append(percentage).append("%")
                        .append("<div class='progress-container'>")
                        .append("<div class='progress-bar' style='width: ").append(percentage).append("%; background: ").append(progressColor).append(";'></div>")
                        .append("</div>")
                        .append("<div class='progress-text'>")
                        .append(getMatchText(percentage))
                        .append("</div>")
                        .append("</div>")
                        .append("</div>"); // Close business-card
            }

            html.append("</div>"); // Close business-grid

            // Statistics
            html.append("<div class='stats-bar'>")
                    .append("<span class='feature-highlight'>📊 Analysis Complete</span> • ")
                    .append("Primary category: <strong>").append(escapeHTML(extractPrimaryCategory(categories))).append("</strong> • ")
                    .append("Matches found: <strong>").append(similarBusinesses.size()).append("</strong> • ")
                    .append("Data source: <strong>Yelp Business Dataset</strong>")
                    .append("</div>");
        }

        // New search prompt
        html.append("<div class='new-search-prompt'>")
                .append("💡 <strong>Ready for another search?</strong> Enter a new business name above to find more similarities!")
                .append("</div>");

        html.append("</div></body></html>");
        return html.toString();
    }

    private String escapeHTML(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String calculateSimilarityScore(String categories1, String categories2) {
        if (categories1 == null || categories2 == null) return "0";

        try {
            Set<String> cats1 = new HashSet<>();
            Set<String> cats2 = new HashSet<>();

            if (categories1 != null) {
                for (String cat : categories1.split(",")) {
                    String trimmed = cat.trim().toLowerCase();
                    if (!trimmed.isEmpty()) {
                        cats1.add(trimmed);
                    }
                }
            }

            if (categories2 != null) {
                for (String cat : categories2.split(",")) {
                    String trimmed = cat.trim().toLowerCase();
                    if (!trimmed.isEmpty()) {
                        cats2.add(trimmed);
                    }
                }
            }

            if (cats1.isEmpty() || cats2.isEmpty()) {
                return "0";
            }

            Set<String> common = new HashSet<>(cats1);
            common.retainAll(cats2);

            int totalUnique = cats1.size() + cats2.size();
            int percentage = (common.size() * 200) / totalUnique;

            return String.valueOf(percentage);
        } catch (Exception e) {
            return "0";
        }
    }

    private String getProgressBarColor(int percentage) {
        if (percentage >= 80) return "#1db954";      // Spotify green - excellent
        else if (percentage >= 60) return "#ffa500"; // Orange - good
        else if (percentage >= 40) return "#ff6b00"; // Dark orange - fair
        else return "#ff4444";                       // Red - poor
    }

    private String getProgressIcon(int percentage) {
        if (percentage >= 80) return "🔥";
        else if (percentage >= 60) return "✅";
        else if (percentage >= 40) return "⚠️";
        else return "📊";
    }

    private String getMatchText(int percentage) {
        if (percentage >= 80) return "Excellent match!";
        else if (percentage >= 60) return "Strong similarity";
        else if (percentage >= 40) return "Moderate match";
        else if (percentage >= 20) return "Weak similarity";
        else return "Minimal match";
    }

    private JSONObject findBusinessByName(String name) {
        for (JSONObject record : data) {
            String businessName = (String) record.get("name");
            if (businessName != null && businessName.equalsIgnoreCase(name)) {
                return record;
            }
        }
        return null;
    }

    private List<JSONObject> findSimilarBusinesses(JSONObject target, String targetCategories, int maxResults) {
        List<JSONObject> similar = new ArrayList<>();
        String targetName = (String) target.get("name");
        String primaryTargetCategory = extractPrimaryCategory(targetCategories);

        for (JSONObject business : data) {
            String businessName = (String) business.get("name");
            if (businessName == null || businessName.equals(targetName)) {
                continue;
            }

            String businessCategories = (String) business.get("categories");
            if (businessCategories != null && !businessCategories.trim().isEmpty()) {
                String primaryBusinessCategory = extractPrimaryCategory(businessCategories);
                if (primaryTargetCategory.equalsIgnoreCase(primaryBusinessCategory)) {
                    similar.add(business);
                    if (similar.size() >= maxResults) {
                        break;
                    }
                }
            }
        }
        return similar;
    }

    private String extractPrimaryCategory(String categories) {
        if (categories == null) return "Unknown";
        String[] categoryArray = categories.split(",");
        return categoryArray.length > 0 ? categoryArray[0].trim() : categories.trim();
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void centerFrame() {
        setLocationRelativeTo(null);
    }

    private void loadData() {
        updateStatus("Loading business data...");
        String filePath = "data/yelp_academic_dataset_business.json";
        int maxRecords = 25000; // Portfolio-optimized limit

        try {
            data = JsonReader.readJsonFile(filePath, maxRecords);
            frequencyTable = createFrequencyTable(data);
            updateStatus("Ready! Loaded " + data.size() + " businesses • Portfolio Demo");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            updateStatus("Error loading data - Using optimized sample dataset");
            createPortfolioSampleData();
        }
    }

    private void createPortfolioSampleData() {
        data = new ArrayList<>();
        JSONParser parser = new JSONParser();

        // Curated sample data that showcases different business types
        String[] sampleBusinesses = {
                // Restaurants
                "{\"name\": \"Joe's Pizza\", \"categories\": \"Pizza, Italian, Restaurants\", \"city\": \"New York\", \"state\": \"NY\", \"stars\": 4.5, \"review_count\": 1250}",
                "{\"name\": \"Mario's Pizzeria\", \"categories\": \"Pizza, Italian, Food\", \"city\": \"Brooklyn\", \"state\": \"NY\", \"stars\": 4.2, \"review_count\": 890}",
                "{\"name\": \"Sushi Palace\", \"categories\": \"Japanese, Sushi, Restaurants\", \"city\": \"Los Angeles\", \"state\": \"CA\", \"stars\": 4.8, \"review_count\": 2100}",
                "{\"name\": \"Tokyo Sushi\", \"categories\": \"Japanese, Sushi, Asian\", \"city\": \"San Francisco\", \"state\": \"CA\", \"stars\": 4.6, \"review_count\": 1560}",

                // Fast Food
                "{\"name\": \"Burger King\", \"categories\": \"Fast Food, Burgers, American\", \"city\": \"Chicago\", \"state\": \"IL\", \"stars\": 3.8, \"review_count\": 3200}",
                "{\"name\": \"McDonald's\", \"categories\": \"Fast Food, Burgers, Restaurants\", \"city\": \"Houston\", \"state\": \"TX\", \"stars\": 3.5, \"review_count\": 2800}",
                "{\"name\": \"Wendy's\", \"categories\": \"Fast Food, Burgers, American\", \"city\": \"Columbus\", \"state\": \"OH\", \"stars\": 3.9, \"review_count\": 1900}",

                // Coffee Shops
                "{\"name\": \"Starbucks Coffee\", \"categories\": \"Coffee, Cafes, Breakfast\", \"city\": \"Seattle\", \"state\": \"WA\", \"stars\": 4.1, \"review_count\": 4500}",
                "{\"name\": \"Blue Bottle Coffee\", \"categories\": \"Coffee, Cafes, Organic\", \"city\": \"Oakland\", \"state\": \"CA\", \"stars\": 4.4, \"review_count\": 2100}",
                "{\"name\": \"Dunkin Donuts\", \"categories\": \"Coffee, Donuts, Breakfast\", \"city\": \"Boston\", \"state\": \"MA\", \"stars\": 3.7, \"review_count\": 1800}",

                // Various categories to showcase algorithm
                "{\"name\": \"Best Buy\", \"categories\": \"Electronics, Shopping, Retail\", \"city\": \"Minneapolis\", \"state\": \"MN\", \"stars\": 3.2, \"review_count\": 4200}",
                "{\"name\": \"Home Depot\", \"categories\": \"Home Services, Shopping, Retail\", \"city\": \"Atlanta\", \"state\": \"GA\", \"stars\": 3.5, \"review_count\": 3800}",
                "{\"name\": \"LA Fitness\", \"categories\": \"Fitness, Gym, Health\", \"city\": \"Los Angeles\", \"state\": \"CA\", \"stars\": 3.8, \"review_count\": 1200}",
                "{\"name\": \"AMC Theaters\", \"categories\": \"Cinema, Entertainment, Movies\", \"city\": \"Kansas City\", \"state\": \"MO\", \"stars\": 3.6, \"review_count\": 2500}",
                "{\"name\": \"CVS Pharmacy\", \"categories\": \"Drugstores, Pharmacy, Convenience\", \"city\": \"Providence\", \"state\": \"RI\", \"stars\": 3.1, \"review_count\": 1600}"
        };

        for (String jsonStr : sampleBusinesses) {
            try {
                JSONObject obj = (JSONObject) parser.parse(jsonStr);
                data.add(obj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        frequencyTable = createFrequencyTable(data);
        showMessage("Using portfolio-optimized sample data with diverse business categories", "Portfolio Demo Mode");
    }

    private FrequencyTable createFrequencyTable(List<JSONObject> records) {
        FrequencyTable ft = new FrequencyTable();
        for (JSONObject record : records) {
            if (record.containsKey("categories") && record.get("categories") != null) {
                String categories = (String) record.get("categories");
                if (categories != null && !categories.trim().isEmpty()) {
                    String[] categoryList = categories.split(",");
                    for (String category : categoryList) {
                        String trimmed = category.trim();
                        if (!trimmed.isEmpty()) {
                            ft.add(trimmed);
                        }
                    }
                }
            }
        }
        return ft;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Warning: Could not set system look and feel");
            }

            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}