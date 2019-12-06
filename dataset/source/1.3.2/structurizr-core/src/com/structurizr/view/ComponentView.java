package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Component view from the C4 model, showing the components within a given container.
 */
public final class ComponentView extends StaticView {

    private Container container;
    private String containerId;

    ComponentView() {
    }

    ComponentView(Container container, String key, String description) {
        super(container.getSoftwareSystem(), key, description);

        this.container = container;
    }

    /**
     * Gets the ID of the container associated with this view.
     *
     * @return the ID, as a String
     */
    public String getContainerId() {
        if (this.container != null) {
            return container.getId();
        } else {
            return this.containerId;
        }
    }

    void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * Gets the container associated with this view.
     *
     * @return  a Container object
     */
    @JsonIgnore
    public Container getContainer() {
        return container;
    }

    void setContainer(Container container) {
        this.container = container;
    }

    /**
     * Adds the specified software system, including relationships to/from that software system.
     * Please note that the parent software system of the container in scope cannot be added to this view.
     *
     * @param softwareSystem    the SoftwareSystem to add to this view
     */
    @Override
    public void add(@Nonnull SoftwareSystem softwareSystem) {
        add(softwareSystem, true);
    }

    /**
     * Adds the specified software system.
     * Please note that the parent software system of the container in scope cannot be added to this view.
     *
     * @param softwareSystem    the SoftwareSystem to add to this view
     * @param addRelationships  whether to add relationships to/from the component
     */
    @Override
    public void add(@Nonnull SoftwareSystem softwareSystem, boolean addRelationships) {
        if (softwareSystem != null && !softwareSystem.equals(getSoftwareSystem())) {
            addElement(softwareSystem, addRelationships);
        }
    }

    /**
     * Adds all other containers in the software system to this view.
     */
    public void addAllContainers() {
        getSoftwareSystem().getContainers().forEach(this::add);
    }

    /**
     * Adds an individual container to this view, including relationships to/from that container.
     *
     * @param container the Container to add
     */
    public void add(Container container) {
        add(container, true);
    }

    /**
     * Adds an individual container to this view.
     *
     * @param container         the Container to add
     * @param addRelationships  whether to add relationships to/from the container
     */
    public void add(Container container, boolean addRelationships) {
        if (container != null && !container.equals(getContainer())) {
            addElement(container, addRelationships);
        }
    }

    /**
     * Adds all components in the container to this view.
     */
    public void addAllComponents() {
        container.getComponents().forEach(this::add);
    }

    /**
     * Adds an individual component to this view, including relationships to/from that component.
     *
     * @param component the Component to add
     */
    public void add(Component component) {
        add(component, true);
    }

    /**
     * Adds an individual component to this view.
     *
     * @param component         the Component to add
     * @param addRelationships  whether to add relationships to/from the component
     */
    public void add(Component component, boolean addRelationships) {
        if (component != null) {
            if (!component.getContainer().equals(getContainer())) {
                throw new IllegalArgumentException("Only components belonging to " + container.getName() + " can be added to this view.");
            }

            addElement(component, addRelationships);
        }
    }

    /**
     * Removes an individual container from this view.
     *
     * @param container the Container to remove
     */
    public void remove(Container container) {
        removeElement(container);
    }

    /**
     * Removes an individual component from this view.
     *
     * @param component the Component to remove
     */
    public void remove(Component component) {
        removeElement(component);
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return  the name, as a String
     */
    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - " + getContainer().getName() + " - Components";
    }

    /**
     * Adds all people, software systems, sibling containers and components belonging to the container in scope.
     */
    @Override
    public void addAllElements() {
        addAllSoftwareSystems();
        addAllPeople();
        addAllContainers();
        addAllComponents();
    }

    /**
     * Adds all people, software systems, sibling containers and components that are directly connected to the specified element.
     *
     * @param element   an Element
     */
    @Override
    public void addNearestNeighbours(@Nonnull Element element) {
        super.addNearestNeighbours(element, SoftwareSystem.class);
        super.addNearestNeighbours(element, Person.class);
        super.addNearestNeighbours(element, Container.class);
        super.addNearestNeighbours(element, Component.class);
    }

    /**
     * <p>Adds all {@link Element}s external to the container (Person, SoftwareSystem or Container)
     * that have {@link Relationship}s <b>to</b> or <b>from</b> {@link Component}s in this view.</p>
     * <p>Not included are:</p>
     * <ul>
     * <li>References to and from the {@link Container} of this view (only references to and from the components are considered)</li>
     * <li>{@link Relationship}s between external {@link Element}s (i.e. elements that are not part of this container)</li>
     * </ul>
     * <p>Don't forget to add elements to your view prior to calling this method, e.g. by calling {@link #addAllComponents()}
     * or be selectively choosing certain components.</p>
     */
    public void addExternalDependencies() {
        final Set<Element> components = new HashSet<>();
        getElements().stream()
                .map(ElementView::getElement)
                .filter(e -> e instanceof Component)
                .forEach(components::add);

        // add relationships of all other elements to or from our inside components
        for (Relationship relationship : getContainer().getModel().getRelationships()) {
            if (components.contains(relationship.getSource())) {
                addExternalDependency(relationship.getDestination(), components);
            }
            if (components.contains(relationship.getDestination())) {
                addExternalDependency(relationship.getSource(), components);
            }
        }

        // remove all relationships between elements outside of this container
        getRelationships().stream()
                .map(RelationshipView::getRelationship)
                .filter(r -> !components.contains(r.getSource()) && !components.contains(r.getDestination()))
                .forEach(this::remove);
    }

    private void addExternalDependency(Element element, Set<Element> components) {
        if (element instanceof Component) {
            if (element.getParent().equals(getContainer())) {
                // the component is in the same container, so we'll ignore it since we're only interested in external dependencies
                return;
            } else {
                // the component is in a different container, so let's try to add that instead
                element = element.getParent();
            }
        }

        if (element instanceof Container) {
            if (element.getParent().equals(this.getContainer().getParent())) {
                // the container is in the same software system
                addElement(element, true);
                return;
            } else {
                // the container is in a different software system, so add that instead
                element = element.getParent();
            }
        }

        if (element instanceof SoftwareSystem || element instanceof Person) {
            addElement(element, true);
        }
    }

    @Override
    protected boolean canBeRemoved(Element element) {
        return true;
    }

}