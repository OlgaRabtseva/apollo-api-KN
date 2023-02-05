package pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Books {
    private String isbn;
    private String title;
    private String subTitle;
    private String author;
    private String description;
}
