//initialiseren van de visitor pattern
public interface Visitor {
    void visit(BasicEncryption basicEncryption);

    void visit(BasicDecryption basicDecryption);
}
