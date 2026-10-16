package b4e.ru.mtp.backend.ms.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {

    private String id;
    private String title;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CategoryTypeCode type;

}
