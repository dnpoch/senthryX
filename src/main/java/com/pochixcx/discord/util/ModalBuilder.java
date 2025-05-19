package com.pochixcx.discord.util;

import java.util.List;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class ModalBuilder {

    public static Modal build(String customId, String title, List<TextInputSpec> inputs) {
        List<ActionRow> rows = inputs.stream()
                .map(spec -> ActionRow.of(TextInput.create(spec.customId(), spec.label(), TextInputStyle.SHORT)
                        .setPlaceholder(spec.placeholder())
                        .setMinLength(spec.minLength())
                        .setMaxLength(spec.maxLength())
                        .build()))
                .toList();

        return Modal.create(customId, title)
                .addComponents(rows)
                .build();
    }

}
