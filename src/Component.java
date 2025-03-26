public interface Component {
    void execute();
    void accept(Visitor visitor);
}
