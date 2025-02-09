package tools;

import entity.VehicleEntity;
import enumerations.PowerSource;
import enumerations.TransmissionType;
import enumerations.VehicleType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AdvancedSearchBar extends JPanel {
    private JTextField searchField;
    private JComboBox<String> typeFilter;
    private JComboBox<String> powerSourceFilter;
    private JComboBox<String> transmissionFilter;
    private JComboBox<String> priceFilter;
    private List<VehicleEntity> allVehicles;
    private Consumer<List<VehicleEntity>> updateResultsCallback;

    public AdvancedSearchBar(List<VehicleEntity> vehicles, Consumer<List<VehicleEntity>> updateResultsCallback) {
        this.allVehicles = vehicles;
        this.updateResultsCallback = updateResultsCallback;

        setLayout(new FlowLayout());

        // Barre de recherche
        searchField = new JTextField(20);
        add(new JLabel("🔍 Recherche:"));
        add(searchField);

        // Filtres
        typeFilter = createComboBox("Tous", VehicleType.values());
        add(new JLabel("🚗 Type:"));
        add(typeFilter);

        powerSourceFilter = createComboBox("Tous", PowerSource.values());
        add(new JLabel("🔋 Énergie:"));
        add(powerSourceFilter);

        transmissionFilter = createComboBox("Tous", TransmissionType.values());
        add(new JLabel("⚙️ Transmission:"));
        add(transmissionFilter);

        priceFilter = new JComboBox<>(new String[]{"Tous", "< 20,000 €", "20,000 € - 50,000 €", "> 50,000 €"});
        add(new JLabel("💰 Prix:"));
        add(priceFilter);

        // 🔥 Ajout des listeners
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { performSearch(); }
            @Override public void removeUpdate(DocumentEvent e) { performSearch(); }
            @Override public void changedUpdate(DocumentEvent e) { performSearch(); }
        });

        typeFilter.addActionListener(e -> performSearch());
        powerSourceFilter.addActionListener(e -> performSearch());
        transmissionFilter.addActionListener(e -> performSearch());
        priceFilter.addActionListener(e -> performSearch());
    }

    /**
     * 🎯 Effectue la recherche avec les filtres et met à jour l'interface.
     */
    private void performSearch() {
        String query = searchField.getText().trim();
        String selectedType = (String) typeFilter.getSelectedItem();
        String selectedPower = (String) powerSourceFilter.getSelectedItem();
        String selectedTransmission = (String) transmissionFilter.getSelectedItem();
        String selectedPrice = (String) priceFilter.getSelectedItem();

        List<VehicleEntity> filteredVehicles = allVehicles.stream()
                .filter(v -> matchesSearchQuery(v, query))
                .filter(v -> matchesFilter(v.getVehicleType() != null ? v.getVehicleType().toString() : "Tous", selectedType))
                .filter(v -> matchesFilter(v.getVehiclePowerSource() != null ? v.getVehiclePowerSource().toString() : "Tous", selectedPower))
                .filter(v -> matchesFilter(v.getTransmissionType() != null ? v.getTransmissionType().toString() : "Tous", selectedTransmission))
                .filter(v -> matchesPriceFilter(v, selectedPrice))
                .collect(Collectors.toList());

        updateResultsCallback.accept(filteredVehicles);
    }

    private boolean matchesSearchQuery(VehicleEntity vehicle, String query) {
        if (query.isEmpty()) return true;
        String data = (vehicle.getModel().getModelName() + " " +
                (vehicle.getVehicleType() != null ? vehicle.getVehicleType().toString() : ""))
                .toLowerCase();
        return data.contains(query.toLowerCase());
    }

    private boolean matchesFilter(String value, String selectedValue) {
        return selectedValue.equals("Tous") || value.equalsIgnoreCase(selectedValue);
    }

    private boolean matchesPriceFilter(VehicleEntity vehicle, String selectedPrice) {
        double price = vehicle.getPrice().doubleValue();
        return switch (selectedPrice) {
            case "< 20,000 €" -> price < 20000;
            case "20,000 € - 50,000 €" -> price >= 20000 && price <= 50000;
            case "> 50,000 €" -> price > 50000;
            default -> true;
        };
    }

    private <T extends Enum<T>> JComboBox<String> createComboBox(String defaultOption, T[] values) {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem(defaultOption);
        for (T value : values) {
            comboBox.addItem(value.toString());
        }
        return comboBox;
    }
}
