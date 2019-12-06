package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.ViewpointsAndPerspectivesDocumentationTemplate;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

import java.io.File;

/**
 * An empty software architecture document using the "Viewpoints and Perspectives" template.
 *
 * See https://structurizr.com/share/36371/documentation for the live version.
 */
public class ViewpointsAndPerspectivesDocumentationExample {

    private static final long WORKSPACE_ID = 36371;
    private static final String API_KEY = "key";
    private static final String API_SECRET = "secret";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Documentation - Viewpoints and Perspectives", "An empty software architecture document using the Viewpoints and Perspectives template.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.PERSON).shape(Shape.Person);

        ViewpointsAndPerspectivesDocumentationTemplate template = new ViewpointsAndPerspectivesDocumentationTemplate(workspace);

        // this is the Markdown version
        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/viewpointsandperspectives/markdown");
        template.addIntroductionSection(softwareSystem, new File(documentationRoot, "01-introduction.md"));
        template.addGlossarySection(softwareSystem, new File(documentationRoot, "02-glossary.md"));
        template.addSystemStakeholdersAndRequirementsSection(softwareSystem, new File(documentationRoot, "03-system-stakeholders-and-requirements.md"));
        template.addArchitecturalForcesSection(softwareSystem, new File(documentationRoot, "04-architectural-forces.md"));
        template.addArchitecturalViewsSection(softwareSystem, new File(documentationRoot, "05-architectural-views"));
        template.addSystemQualitiesSection(softwareSystem, new File(documentationRoot, "06-system-qualities.md"));
        template.addAppendicesSection(softwareSystem, new File(documentationRoot, "07-appendices.md"));

        // this is the AsciiDoc version
//        File documentationRoot = new File("./structurizr-examples/src/com/structurizr/example/documentation/viewpointsandperspectives/asciidoc");
//        template.addIntroductionSection(softwareSystem, new File(documentationRoot, "01-introduction.adoc"));
//        template.addGlossarySection(softwareSystem, new File(documentationRoot, "02-glossary.adoc"));
//        template.addSystemStakeholdersAndRequirementsSection(softwareSystem, new File(documentationRoot, "03-system-stakeholders-and-requirements.adoc"));
//        template.addArchitecturalForcesSection(softwareSystem, new File(documentationRoot, "04-architectural-forces.adoc"));
//        template.addArchitecturalViewsSection(softwareSystem, new File(documentationRoot, "05-architectural-views"));
//        template.addSystemQualitiesSection(softwareSystem, new File(documentationRoot, "06-system-qualities.adoc"));
//        template.addAppendicesSection(softwareSystem, new File(documentationRoot, "07-appendices.adoc"));

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}