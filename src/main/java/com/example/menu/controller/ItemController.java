package com.example.menu.controller;


import com.example.menu.model.Item;
import com.example.menu.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


// ✨ 👇 Quickly enable CORS ✨
@CrossOrigin(origins = "https://dashboard.whatabyte.app")



@RestController
@RequestMapping("api/menu/items")
    public class ItemController {
        private final ItemService service;

        public ItemController(ItemService service) {
            this.service = service;
        }

    //New! GET controller methods

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
            List<Item> items = service.findAll();
            return ResponseEntity.ok().body(items);
            }

@GetMapping("/{id}")
    //ResponseEntity is a helper class to fully describe the response, including the status code, headers, and body.
    // It makes it easy to set appropriate values without trying to remember what a value should be.
    // For example, you don't need to know what the status code not found is. Similarly, it prohibits you from adding a body if you set the status to no content.
    public ResponseEntity<Item> find(@PathVariable("id") Long id) {
        Optional<Item> item = service.find(id);

    //ResponseEntity.of() within the findAll method. This is a shortcut for creating a ResponseEntity with either a valid body                                                      // and the 200 OK status, or no body and a 404 Not Found status.
        return ResponseEntity.of(item);
    }

    // New!POST definition
@PostMapping
    //The POST /api/menu/items request should have a body which Spring will deserialize in an Item instance
    // and provide it as a method argument since it's annotated with   @RequestBody.
    public ResponseEntity<Item> create(@RequestBody Item item) {
        Item created = service.create(item);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    //New! PUT definition
@PutMapping("/{id}")
    public ResponseEntity<Item> update(
            @PathVariable("id") Long id,
            @RequestBody Item updatedItem) {

        Optional<Item> updated = service.update(id, updatedItem);

        return updated
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> {
                    Item created = service.create(updatedItem);
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(created.getId())
                            .toUri();
                    return ResponseEntity.created(location).body(created);
                });
    }

    //New! 👇 DELETE definition
@DeleteMapping("/{id}")
    public ResponseEntity<Item> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


