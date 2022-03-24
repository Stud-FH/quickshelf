package ch.bookoflies.qsserver.persistent;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "longText")
public class LongText {

    private static final int LENGTH = 255;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, length = LENGTH)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    private LongText tail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getFullText() {
        return tail == null? text : text + tail.getFullText();
    }

    public void setText(String text) {
        if (text.length() > LENGTH) {
            this.text = text.substring(0, LENGTH);
            tail = new LongText();
            tail.setText(text.substring(LENGTH));
        } else {
            this.text = text;
        }
    }

    public LongText getTail() {
        return tail;
    }

    public void setTail(LongText tail) {
        this.tail = tail;
    }

    public Stream<LongText> stream() {
        return Stream.iterate(this, lt -> lt.tail != null, lt -> lt.tail);
    }
}
