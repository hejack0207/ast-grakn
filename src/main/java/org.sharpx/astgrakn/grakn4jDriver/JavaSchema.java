package org.sharpx.astgrakn.grakn4jDriver;

import ai.grakn.GraknTx;
import ai.grakn.concept.*;

/**
 * Created by he on 5/11/18.
 */
public class JavaSchema {

    private JavaSchema(){

    }

    public static JavaSchema setup(GraknTx tx){
        return null;
    }

    public static JavaSchema load(GraknTx tx){
        JavaSchema schema = new JavaSchema();
        schema.name = tx.getAttributeType("name");
        schema.package_name = tx.getAttributeType("package_name");

        schema.importing_class = tx.getRole("importing_class");
        schema.imported_by_compileunit = tx.getRole("import_class");
        schema.import_class = tx.getRelationshipType("import_class");

        schema.including_compileunit = tx.getRole("including_compileunit");
        schema.included_by_package = tx.getRole("included_by_package");
        schema.include_compileunit = tx.getRelationshipType("include_compileunit");

        schema.containing_class = tx.getRole("containing_class");
        schema.contained_by_compileunit = tx.getRole("contained_by_compileunit");
        schema.contain_class = tx.getRelationshipType("contain_class");

        schema.clazz = tx.getEntityType("class");
        schema.packagee = tx.getEntityType("package");
        schema.compileunit = tx.getEntityType("compileunit");
        return schema;
    }

    public AttributeType name;
    public AttributeType package_name;

    public Role importing_class;
    public Role imported_by_compileunit;
    public RelationshipType import_class;

    public Role including_compileunit;
    public Role included_by_package;
    public RelationshipType include_compileunit;

    public Role containing_class;
    public Role contained_by_compileunit;
    public RelationshipType contain_class;

    public EntityType clazz;
    public EntityType packagee;
    public EntityType compileunit;
}
