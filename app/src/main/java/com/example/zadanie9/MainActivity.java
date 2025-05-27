package com.example.zadanie9; // Pamiętaj o zmianie na nazwę Twojego pakietu

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView settingsListView;
    private TextView editingLabelTextView;
    private SeekBar valueSeekBar;
    private TextView seekBarValueTextView;

    // Dane dla opcji konfiguracyjnych
    private ArrayList<String> settingNames;
    private ArrayList<Integer> settingValues;
    private ArrayList<String> settingUnits;
    private ArrayList<String> displayItemsForListView; // Sformatowane napisy do ListView

    private ArrayAdapter<String> adapter;
    private int selectedItemPosition = -1; // -1 oznacza, że nic nie jest wybrane

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja komponentów UI
        settingsListView = findViewById(R.id.settingsListView);
        editingLabelTextView = findViewById(R.id.editingLabelTextView);
        valueSeekBar = findViewById(R.id.valueSeekBar);
        seekBarValueTextView = findViewById(R.id.seekBarValueTextView);

        // Inicjalizacja danych opcji konfiguracyjnych
        settingNames = new ArrayList<>();
        settingValues = new ArrayList<>();
        settingUnits = new ArrayList<>();

        // Dodawanie przykładowych danych
        settingNames.add("Jasność Ekranu");
        settingValues.add(60);
        settingUnits.add("%");

        settingNames.add("Głośność Dźwięków");
        settingValues.add(80);
        settingUnits.add("%");

        settingNames.add("Czas Automatycznej Blokady");
        settingValues.add(30);
        settingUnits.add("s");

        settingNames.add("Czułość Myszki");
        settingValues.add(50);
        settingUnits.add(""); // Bez jednostki

        // Przygotowanie sformatowanej listy do wyświetlenia w ListView
        displayItemsForListView = new ArrayList<>();
        updateDisplayItemsForListView(); // Wypełnia displayItemsForListView

        // Stworzenie ArrayAdaptera
        adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_setting, // Nasz niestandardowy layout dla elementu listy
                R.id.itemTextView,          // Identyfikator TextView w naszym niestandardowym layoucie
                displayItemsForListView     // Źródło danych
        );
        settingsListView.setAdapter(adapter);

        // Ustawienie początkowych stanów
        editingLabelTextView.setText("Wybierz opcję z listy powyżej");
        seekBarValueTextView.setText("Wartość: --");
        valueSeekBar.setEnabled(false); // Suwak nieaktywny na starcie

        // Obsługa kliknięć elementów ListView
        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemPosition = position; // Zapisz wybraną pozycję

                // Pobierz nazwę i wartość wybranej opcji
                String selectedName = settingNames.get(selectedItemPosition);
                int selectedValue = settingValues.get(selectedItemPosition);

                // Zaktualizuj etykietę edytowanej opcji
                editingLabelTextView.setText("Edytujesz: " + selectedName);

                // Ustaw wartość suwaka i etykietę wartości suwaka
                valueSeekBar.setProgress(selectedValue);
                seekBarValueTextView.setText("Wartość: " + selectedValue + settingUnits.get(selectedItemPosition));

                // Aktywuj suwak
                valueSeekBar.setEnabled(true);
            }
        });

        // Obsługa zmian na suwaku
        valueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                if (fromUser && selectedItemPosition != -1) {
                    // Aktualizuj tylko etykietę wartości suwaka w czasie rzeczywistym
                    String unit = settingUnits.get(selectedItemPosition);
                    seekBarValueTextView.setText("Wartość: " + progressValue + unit);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Opcjonalnie: można dodać Toast lub inną informację
                // Toast.makeText(MainActivity.this, "Rozpoczęto edycję", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (selectedItemPosition != -1) {
                    // Pobierz aktualną wartość z suwaka
                    int finalValue = seekBar.getProgress();

                    // Zaktualizuj wartość w liście settingValues
                    settingValues.set(selectedItemPosition, finalValue);

                    // Odśwież displayItemsForListView i powiadom adapter
                    updateDisplayItemsForListView();
                    adapter.notifyDataSetChanged(); // Poinformuj adapter o zmianie danych
                }
            }
        });
    }

    /*
     * Metoda pomocnicza do aktualizacji listy displayItemsForListView
     * na podstawie settingNames, settingValues i settingUnits.
     * Należy ją wywołać po każdej zmianie wartości w settingValues.
     */
    private void updateDisplayItemsForListView() {
        displayItemsForListView.clear(); // Wyczyść starą zawartość
        for (int i = 0; i < settingNames.size(); i++) {
            String name = settingNames.get(i);
            int value = settingValues.get(i);
            String unit = settingUnits.get(i);
            displayItemsForListView.add(name + ": " + value + unit);
        }
    }
}
