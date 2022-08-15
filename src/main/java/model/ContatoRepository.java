package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ContatoRepository implements IRepository<Contato> {
    private static final String dataFile = "data/contatos.json";
    private List<Contato> cache;

    public ContatoRepository() throws DataStorageException {
        cache = new ArrayList<>();
        load();
        // TODO: load file
        if (cache.isEmpty()) {
            cache.addAll(TestUtil.randomDataset());
            save();
        }
    }

    @Override
    public long insert(Contato element) throws DataUpdateException {
        var newContato = new Contato(nextId(), 
            element.getNome(),
            element.getEmail(),
            element.getEndereco(),
            element.getTelefone()
        );
        cache.add(newContato);
        try {
            save();
            return newContato.getId();
        } catch (DataStorageException e) {
            throw new DataUpdateException("Failed to insert element", e);
        }
    }

    private long nextId() {
        var max = 0L;

        for (var item : cache) {
            if (item.getId() > max) {
                max = item.getId();
            }
        }

        return max + 1;
    }

    @Override
    public void remove(Contato element) throws DataUpdateException {
        try {
            cache.remove(getById(element.getId()));
            save();
        } catch (ElementNotFoundException e) {
            throw new DataUpdateException("Failed to remove non-existing element", e);
        } catch (DataStorageException e) {
            throw new DataUpdateException("Failed to update contacts file", e);
        }
        
    }

    @Override
    public void update(Contato element) throws DataUpdateException, DataStorageException {
        try {
            var oldContato = getById(element.getId());

            cache.remove(oldContato);
            cache.add(element);
            save();
        } catch (ElementNotFoundException e) {
            throw new DataUpdateException("Failed to update non-existing element", e);
        }
    }

    @Override
    public Contato getById(long id) throws ElementNotFoundException {
        for (var contato : cache) {
            if (contato.getId() == id) {
                return contato;
            }
        }
        throw new ElementNotFoundException("Cannot find element with id " + id);
    }

    @Override
    public List<Contato> getAll() {
        return cache;
    }

    public List<Contato> getAllSortedByName() {
        var contatos = new ArrayList<Contato>(cache);

        contatos.sort(Comparator.comparing(Contato::getNome));

        return contatos;
    }

    private void save() throws DataStorageException {
        try {
            Files.writeString(Path.of(dataFile), new Gson().toJson(cache));
        } catch (IOException e) {
            throw new DataStorageException("Failed to save contacts to file", e);
        }
    }
    
    private void load() throws DataStorageException {
        var path = Path.of(dataFile);

        cache.clear();

        try {
            if (Files.exists(path)) {
                var jsonStr = Files.readString(path);
                List<Contato> obj = new Gson().fromJson(jsonStr, 
                    new TypeToken<List<Contato>>(){}.getType());

                cache.addAll(obj);
            } else {
                save();
            }
        } catch(IOException e) {
            throw new DataStorageException("Failed to load contacts from file", e);
        } 
        
    }
}
