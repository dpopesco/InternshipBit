package qaa.be.dummyapi.requests;

import qaa.be.core.exceptions.ConversionJsonToModelException;

public interface APIContract<T, K> {
    T getItem(String itemId) throws ConversionJsonToModelException;

    K getItems() throws ConversionJsonToModelException;

    T createItem(T item) throws ConversionJsonToModelException;
    T createItemWithoutBody() throws ConversionJsonToModelException;

    T updateItem(String itemId, T updatedItem) throws ConversionJsonToModelException;

    T deleteItem(String itemId) throws ConversionJsonToModelException;
}
