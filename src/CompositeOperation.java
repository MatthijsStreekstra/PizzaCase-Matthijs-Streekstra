import java.util.ArrayList;
import java.util.List;

// klasse die ervoor zorgt dat er verschillende components aan de lijst kunnen worden toegevoegd om deze tegelijkertijd uit te voeren
public class CompositeOperation implements Component {
    private List<Component> components = new ArrayList<>();
    //voeg de component toe aan de lijst van components
    public void addComponent(Component component) {
        components.add(component);
    }
    //execute de components die in de lijst staan
    @Override
    public void execute() {
        for (Component component : components) {
            component.execute();
        }
    }

    @Override
    public void accept(Visitor visitor) {
        for (Component component : components) {
            component.accept(visitor);
        }
    }
}
