package ru.practicum.shareit.handler;

import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
public class ErrorMessage {
    private String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

}
