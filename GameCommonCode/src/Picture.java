import java.io.InputStream;
import java.io.Serializable;



public class Picture implements Serializable{

    private String name;

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    private InputStream stream;

    public Picture(String name, InputStream stream) {
        this.name = name;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
