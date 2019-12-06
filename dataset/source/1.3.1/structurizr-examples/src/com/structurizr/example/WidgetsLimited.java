package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * This workspace contains a number of diagrams for a fictional reseller of widgets online.
 *
 * You can see the workspace online at https://structurizr.com/public/14471
 */
public class WidgetsLimited {

    private static final long WORKSPACE_ID = 14471;
    private static final String API_KEY = "";
    private static final String API_SECRET = "";

    private static final String EXTERNAL_TAG = "External";
    private static final String INTERNAL_TAG = "Internal";

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("Widgets Limited", "Sells widgets to customers online.");
        Model model = workspace.getModel();
        ViewSet views = workspace.getViews();
        Styles styles = views.getConfiguration().getStyles();

        model.setEnterprise(new Enterprise("Widgets Limited"));

        Person customer = model.addPerson(Location.External, "Customer", "A customer of Widgets Limited.");
        Person customerServiceUser = model.addPerson(Location.Internal, "Customer Service Agent", "Deals with customer enquiries.");
        SoftwareSystem ecommerceSystem = model.addSoftwareSystem(Location.Internal, "E-commerce System", "Allows customers to buy widgets online via the widgets.com website.");
        SoftwareSystem fulfilmentSystem = model.addSoftwareSystem(Location.Internal, "Fulfilment System", "Responsible for processing and shipping of customer orders.");
        SoftwareSystem taxamo = model.addSoftwareSystem(Location.External, "Taxamo", "Calculates local tax (for EU B2B customers) and acts as a front-end for Braintree Payments.");
        taxamo.setUrl("https://www.taxamo.com");
        SoftwareSystem braintreePayments = model.addSoftwareSystem(Location.External, "Braintree Payments", "Processes credit card payments on behalf of Widgets Limited.");
        braintreePayments.setUrl("https://www.braintreepayments.com");
        SoftwareSystem jerseyPost = model.addSoftwareSystem(Location.External, "Jersey Post", "Calculates worldwide shipping costs for packages.");

        model.getPeople().stream().filter(p -> p.getLocation() == Location.External).forEach(p -> p.addTags(EXTERNAL_TAG));
        model.getPeople().stream().filter(p -> p.getLocation() == Location.Internal).forEach(p -> p.addTags(INTERNAL_TAG));

        model.getSoftwareSystems().stream().filter(ss -> ss.getLocation() == Location.External).forEach(ss -> ss.addTags(EXTERNAL_TAG));
        model.getSoftwareSystems().stream().filter(ss -> ss.getLocation() == Location.Internal).forEach(ss -> ss.addTags(INTERNAL_TAG));

        customer.interactsWith(customerServiceUser, "Asks questions to", "Telephone");
        customerServiceUser.uses(ecommerceSystem, "Looks up order information using");
        customer.uses(ecommerceSystem, "Places orders for widgets using");
        ecommerceSystem.uses(fulfilmentSystem, "Sends order information to");
        fulfilmentSystem.uses(jerseyPost, "Gets shipping charges from");
        ecommerceSystem.uses(taxamo, "Delegates credit card processing to");
        taxamo.uses(braintreePayments, "Uses for credit card processing");

        SystemLandscapeView systemLandscapeView = views.createSystemLandscapeView("SystemLandscape", "The system landscape for Widgets Limited.");
        systemLandscapeView.addAllElements();

        SystemContextView ecommerceSystemContext = views.createSystemContextView(ecommerceSystem, "EcommerceSystemContext", "The system context diagram for the Widgets Limited e-commerce system.");
        ecommerceSystemContext.addNearestNeighbours(ecommerceSystem);
        ecommerceSystemContext.remove(customer.getEfferentRelationshipWith(customerServiceUser));

        SystemContextView fulfilmentSystemContext = views.createSystemContextView(fulfilmentSystem, "FulfilmentSystemContext", "The system context diagram for the Widgets Limited fulfilment system.");
        fulfilmentSystemContext.addNearestNeighbours(fulfilmentSystem);

        DynamicView dynamicView = views.createDynamicView("CustomerSupportCall", "A high-level overview of the customer support call process.");
        dynamicView.add(customer, customerServiceUser);
        dynamicView.add(customerServiceUser, ecommerceSystem);

        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        template.addSection("System Landscape", Format.Markdown, "Here is some information about the Widgets Limited system landscape... ![](embed:SystemLandscape)");
        template.addContextSection(ecommerceSystem, Format.Markdown, "This is the context section for the E-commerce System... ![](embed:EcommerceSystemContext)");
        template.addContextSection(fulfilmentSystem, Format.Markdown, "This is the context section for the Fulfilment System... ![](embed:FulfilmentSystemContext)");

        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).shape(Shape.RoundedBox);
        styles.addElementStyle(Tags.PERSON).shape(Shape.Person);

        styles.addElementStyle(Tags.ELEMENT).color("#ffffff");
        styles.addElementStyle(EXTERNAL_TAG).background("#EC5381").border(Border.Dashed);
        styles.addElementStyle(INTERNAL_TAG).background("#B60037");

        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}