package com.kubernetes_hosted.azure.entitys;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
@Getter
@Builder
@Jacksonized
public class Articles {
        private final String title;
        private final String content;
        private final String from;

}
