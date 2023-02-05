package pojos;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddBookToCollectionRequest {
    private String userId;
    private List<BooksForCollectionRequest> collectionOfIsbns;
}
