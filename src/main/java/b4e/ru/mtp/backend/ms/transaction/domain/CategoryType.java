package b4e.ru.mtp.backend.ms.transaction.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryType {

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CategoryTypeCode code;

}
