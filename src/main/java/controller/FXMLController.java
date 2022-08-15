package controller;
import java.util.Timer;
import java.util.TimerTask;

import dk.brics.automaton.State;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Contato;
import model.ContatoRepository;
import model.DataStorageException;
import model.DataUpdateException;

public class FXMLController {
    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonNewContact;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonInsert;

    @FXML
    private ListView<ContatoListItem> listViewContatos;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldEndereco;

    @FXML
    private TextField textFieldNome;

    @FXML
    private TextField textFieldTelefone;

    @FXML
    private VBox formPane;

    @FXML
    private Label labelStatus;


    private ContatoRepository repository;

    public void initialize() {
        try {
            repository = new ContatoRepository();
        } catch (DataStorageException e) {
            setStatusMessage(e);
        }
        listViewContatos.setItems(FXCollections.observableArrayList());
        populateList();
        bindEvents();
        refreshDetailPane();
    }

    private void bindEvents() {
        listViewContatos.setOnMouseClicked(ev -> refreshDetailPane());
        buttonUpdate.setOnMouseClicked(ev -> {
            var selectedItem = listViewContatos.getSelectionModel().getSelectedItem();

            try {
                repository.update(parseForm(selectedItem.getId()));
                setStatusMessage("Contato atualizado com sucesso");
            } catch (DataUpdateException | DataStorageException  e) {
                setStatusMessage(e);
            }
            populateList();
            selectItemById(selectedItem.getId());
            refreshDetailPane();
        });
        buttonDelete.setOnMouseClicked(ev -> {
            try {
                repository.remove(listViewContatos
                    .getSelectionModel()
                    .getSelectedItem()
                    .getContato());
                populateList();
                refreshDetailPane();
            } catch (DataUpdateException e) {
                setStatusMessage(e);
            }
        });
        buttonNewContact.setOnMouseClicked(ev -> showInsertForm());
        buttonInsert.setOnMouseClicked(ev -> {
            try {
                var id = repository.insert(parseForm(0));

                populateList();
                selectItemById(id);
                refreshDetailPane();
                setStatusMessage("Contato inserido com sucesso");
            } catch(DataUpdateException e) {
                setStatusMessage(e);
            }
            
        });
    }

    private void showInsertForm() {
        textFieldNome.setText("");
        textFieldEmail.setText("");
        textFieldEndereco.setText("");
        textFieldTelefone.setText("");
        buttonDelete.setVisible(false);
        formPane.setVisible(true);
        buttonUpdate.setVisible(false);
        buttonInsert.setVisible(true);
    }

    private void selectItemById(long id) {
        for (var item : listViewContatos.getItems()) {
            if (item.getId() == id) {
                listViewContatos.getSelectionModel().select(item);
            }
        }
    }

    private Contato parseForm(long id) {
        return new Contato(id, 
            textFieldNome.getText(),
            textFieldEmail.getText(),
            textFieldEndereco.getText(),
            textFieldTelefone.getText()
        );
    }

    private void refreshDetailPane() {
        var selectedItem = listViewContatos.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            formPane.setVisible(false);
        } else {
            textFieldNome.setText(selectedItem.getNome());
            textFieldEmail.setText(selectedItem.getEmail());
            textFieldEndereco.setText(selectedItem.getEndereco());
            textFieldTelefone.setText(selectedItem.getTelefone());
            formPane.setVisible(true);
            buttonDelete.setVisible(true);
            buttonUpdate.setVisible(true);
            buttonInsert.setVisible(false);
        }
    }

    private void populateList() {
        listViewContatos.getItems().clear();

        for (var contato : repository.getAllSortedByName()) {
            listViewContatos.getItems().add(new ContatoListItem(contato));
        }
    }

    private void setStatusMessage(Throwable e) {
        labelStatus.setVisible(true);
        labelStatus.setTextFill(Color.RED);
        labelStatus.setText(e.getMessage());
        e.printStackTrace();
        hideStatusMessage();
    }

    private void setStatusMessage(String message) {
        labelStatus.setVisible(true);
        labelStatus.setTextFill(Color.BLUE);
        labelStatus.setText(message);
        hideStatusMessage();
    }

    private void hideStatusMessage() {
        var timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                labelStatus.setVisible(false);
                timer.cancel();
            }
            
        }, 2500);
    }
}
