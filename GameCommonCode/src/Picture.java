import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;


public class Picture implements Serializable{

    private String name;
    private byte [] stream;


    public byte [] getStream() {
        return stream;
    }

    public void setStream(byte [] stream) {
        this.stream = stream;
    }


    public Picture(String name, byte [] stream) {
        this.name = name;
        this.stream = stream;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "name='" + name + '\'' +
                ", stream=" + Arrays.toString(stream) +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
