
package coursework;



public class Label {

    private final String name;
    private final int offset;
    private final Segment seg;
    
    public int getOffset() {
        return offset;
    }
    
    public Segment getSegment() {
        return seg;
    }
    
    public String getName() {
        return name;
    }
    
    public Label(String n, int o, Segment s) {
        name = n;
        offset = o;
        seg = s;
    }
}
