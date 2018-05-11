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

    public static JavaSchema fetch(GraknTx tx){
        return null;
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
