//daadwerkelijk visiten van de verschillende klassen
public class ConcreteVisitor implements Visitor {

    @Override
    public void visit(BasicEncryption encryption) {
        encryption.execute();
    }

    @Override
    public void visit(BasicDecryption decryption) {
        decryption.execute();
    }
}
