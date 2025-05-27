package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Map;


@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUserItems(long userId) {
        return get("", userId);
    }


    public ResponseEntity<Object> getItemsByQuery(String query) {
        Map<String, Object> parameters = Map.of(
                "text", query
        );
        return get("/search?text={text}", parameters);
    }


    public ResponseEntity<Object> getItemById(long itemId) {
        return get("/" + itemId);
    }


    public ResponseEntity<Object> createItem(long userId, NewItemRequest newItemRequest) {
        return post("", userId, newItemRequest);
    }


    public ResponseEntity<Object> updateItem(long userId, UpdateItemRequest updateItemRequest, long itemId) {
        return patch("/" + itemId, userId, updateItemRequest);
    }


    public ResponseEntity<Object> createComment(long itemId, long userId, NewCommentRequest comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }

}
