package com.example.menu.services;

import com.example.menu.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//Dependency injection, also known as inversion of control (IoC), is one of the core components of the framework.
// It helps to instantiate, assemble, and manage simple Java objects known as beans.
// To find them, Spring looks for various annotations such as @EnableMapRepositories.
@Service
public class ItemService {
    private final CrudRepository<Item, Long> repository;

    //when a CrudRepository implementation is found within the KeyValue dependency,
    // Spring knows how to construct an ItemService instance via constructor-based dependency injection.
    // It looks at the constructor parameters, checks all of them are instantiated, then passes them in and creates it on your behalf.
    public ItemService(CrudRepository<Item, Long> repository) {
        this.repository = repository;
        //New Populate the in-memory store
        this.repository.saveAll(defaultItems());
    }

    //The power of CrudRepository comes with the functionality it provides out-of-the-box.
    // Methods such as findById, findAll, save, deleteById will help you implement the remaining CRUD functionality.
    // New List of items
    private static List<Item> defaultItems() {
        return List.of(
                new Item(1L, "Burger", 599L, "Tasty", "https://cdn.auth0.com/blog/whatabyte/burger-sm.png"),
                new Item(2L, "Pizza", 299L, "Cheesy", "https://cdn.auth0.com/blog/whatabyte/pizza-sm.png"),
                new Item(3L, "Tea", 199L, "Informative", "https://cdn.auth0.com/blog/whatabyte/tea-sm.png")
        );
    }

    public List<Item> findAll() {
        List<Item> list = new ArrayList<>();
        Iterable<Item> items = repository.findAll();
        items.forEach(list::add);
        return list;
    }

    public Optional<Item> find(Long id) {
        return repository.findById(id);
    }

    public Item create(Item item) {
        // To ensure the item ID remains unique,
        // use the current timestamp.
        Item copy = new Item(
                new Date().getTime(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getImage()
        );
        return repository.save(copy);
    }

    public Optional<Item> update(Long id, Item newItem) {
        // Only update an item if it can be found first.
        return repository.findById(id)
                .map(oldItem -> {
                    Item updated = oldItem.updateWith(newItem);
                    return repository.save(updated);
                });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
